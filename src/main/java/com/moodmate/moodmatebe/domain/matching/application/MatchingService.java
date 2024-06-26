package com.moodmate.moodmatebe.domain.matching.application;

import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.matching.algorithm.GaleShapley;
import com.moodmate.moodmatebe.domain.matching.domain.Man;
import com.moodmate.moodmatebe.domain.matching.domain.Person;
import com.moodmate.moodmatebe.domain.matching.domain.WhoMeet;
import com.moodmate.moodmatebe.domain.matching.domain.Woman;
import com.moodmate.moodmatebe.domain.matching.repository.WhoMeetRepository;
import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.exception.UserNotFoundException;
import com.moodmate.moodmatebe.domain.user.repository.PreferRepository;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final PreferRepository preferRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final WhoMeetRepository whoMeetRepository;
    private final EntityManager entityManager;

    private List<Man> men = new ArrayList<>();
    private List<Woman> women = new ArrayList<>();

    @Transactional
    public void match() {
        List<Prefer> activeMatchTrue = getActiveMatchPreferences();
        Collections.shuffle(activeMatchTrue);

        // 매칭 작업 시작: 사용자 상태 업데이트
        activeMatchTrue.forEach(prefer -> {
            User user = prefer.getUser();
            user.setMatchInProgress(true);
            userRepository.save(user);
        });
        entityManager.flush();
        entityManager.clear();

        try {
            for (Prefer prefer : activeMatchTrue) {
                addPersonToGroup(prefer);
            }
            grouping();
        } finally {
            // 매칭 작업 완료: 사용자 상태 원래대로 되돌림
            activeMatchTrue.forEach(prefer -> {
                User user = prefer.getUser();
                user.setMatchInProgress(false);
                userRepository.save(user);
            });
            entityManager.flush();
            entityManager.clear();
        }
    }
    private List<Prefer> getActiveMatchPreferences() {
        List<Prefer> activeMatchTrue = preferRepository.findByUserMatchActiveAndGenderTrue(Gender.MALE);
        activeMatchTrue.addAll(preferRepository.findByUserMatchActiveAndGenderTrue(Gender.FEMALE));
        return activeMatchTrue;
    }

    private void addPersonToGroup(Prefer prefer) {
        Person person = Person.createPerson(prefer.getUser(), prefer);
        if (person.getUserProfile().gender() == Gender.MALE) {
            men.add(Man.createMan(prefer.getUser(), prefer));
        } else if (person.getUserProfile().gender() == Gender.FEMALE) {
            women.add(Woman.createWoman(prefer.getUser(), prefer));
        }
    }

    @Transactional
    public void grouping() {
        Map<String, Map<String, Man>> menGroups = groupByMood(convertListToMap(men));
        Map<String, Map<String, Woman>> womenGroups = groupByMood(convertListToMap(women));

        for (String mood : menGroups.keySet()) {
            Map<String, Man> menGroup = menGroups.get(mood);
            Map<String, Woman> womenGroup = womenGroups.get(mood);

            // null일 시, 다음 그룹으로 넘어가도록 함
            if (menGroup == null || womenGroup == null || menGroup.isEmpty() || womenGroup.isEmpty()) {
                log.info("{} 그룹에 유효한 매칭 대상이 없습니다.", mood);
                continue;
            }

            matchGroupsByMood(mood, menGroup, womenGroup);
        }
    }

    private void matchGroupsByMood(String mood, Map<String, Man> menGroup, Map<String, Woman> womenGroup) {
        logGroupStart(mood);
        logGroupMembers(mood, menGroup, womenGroup);

        preparePreferences(menGroup, womenGroup);
        preparePreferences(womenGroup, menGroup);

        new GaleShapley(menGroup, womenGroup, roomRepository, userRepository, whoMeetRepository);
    }

    private void logGroupStart(String mood) {
        log.info("====================================");
        log.info("{} 그룹 매칭을 시작합니다.", mood);
        log.info("====================================");
    }

    private void logGroupMembers(String mood, Map<String, Man> menGroup, Map<String, Woman> womenGroup) {
        log.debug("{} 그룹 남자:", mood);
        for (Man man : menGroup.values()) {
            log.info(man.getName());
        }

        log.debug("{} 그룹 여자:", mood);
        for (Woman woman : womenGroup.values()) {
            log.info(woman.getName());
        }
    }

    private <T extends Person, U extends Person> void preparePreferences(Map<String, T> group, Map<String, U> otherGroup) {
        for (T person : group.values()) {
            List<String> preferences = createPreferenceList(person, otherGroup);
            person.setPreferences(preferences);

            log.info("{}의 선호도 목록 : {}", person.getName(), preferences);
        }
    }

    private <T extends Person, U extends Person> List<String> createPreferenceList(T person, Map<String, U> otherGroup) {
        List<String> mainPreferences = new ArrayList<>();
        List<String> secondaryPreferences = new ArrayList<>();
        List<String> thirdPreferences = new ArrayList<>();
        Set<Long> metPeopleSet = getMetPeopleSet(person);
        List<String> metPeopleList = new ArrayList<>();

        for (U otherPerson : otherGroup.values()) {
            Long otherPersonId = otherPerson.getUserProfile().userId();
            if (metPeopleSet.contains(otherPersonId)) {
                metPeopleList.add(otherPerson.getName());
                continue;
            }

            if (isSameDepartment(person, otherPerson)) {
                thirdPreferences.add(otherPerson.getName());
            } else if (isWithinPreferredAgeRange(person, otherPerson)) {
                mainPreferences.add(otherPerson.getName());
            } else {
                secondaryPreferences.add(otherPerson.getName());
            }
        }

        mainPreferences.addAll(secondaryPreferences);
        mainPreferences.addAll(thirdPreferences);
        mainPreferences.addAll(metPeopleList);

        return mainPreferences;
    }

    private <T extends Person, U extends Person> boolean isSameDepartment(T person, U otherPerson) {
        return !person.getUserPreferences().departmentPossible() &&
                person.getUserProfile().department().equals(otherPerson.getUserProfile().department());
    }

    private <T extends Person, U extends Person> boolean isWithinPreferredAgeRange(T person, U otherPerson) {
        return otherPerson.getUserProfile().birthYear() >= person.getUserPreferences().yearMin() &&
                otherPerson.getUserProfile().birthYear() <= person.getUserPreferences().yearMax();
    }

    private <T extends Person> Set<Long> getMetPeopleSet(T person) {
        Set<Long> metPeopleSet = new HashSet<>();
        User user = userRepository.findById(person.getUserProfile().userId())
                .orElseThrow(UserNotFoundException::new);

        List<WhoMeet> metPeople;
        if (person instanceof Man) {
            metPeople = whoMeetRepository.findByMetUser2(user);
            for (WhoMeet met : metPeople) {
                metPeopleSet.add(met.getMetUser1().getUserId());
            }
        } else {
            metPeople = whoMeetRepository.findByMetUser1(user);
            for (WhoMeet met : metPeople) {
                metPeopleSet.add(met.getMetUser2().getUserId());
            }
        }

        return metPeopleSet;
    }

    private <T extends Person> Map<String, T> convertListToMap(List<T> list) {
        Map<String, T> map = new LinkedHashMap<>();
        for (T person : list) {
            map.put(String.valueOf(person.getUserProfile().userId()), person);  // user_id를 키로 사용
        }
        return map;
    }

    private static <T extends Person> Map<String, Map<String, T>> groupByMood(Map<String, T> persons) {
        Map<String, Map<String, T>> groups = new LinkedHashMap<>();
        for (T person : persons.values()) {
            if (!groups.containsKey(person.getMood())) {
                groups.put(person.getMood(), new LinkedHashMap<>());
            }
            groups.get(person.getMood()).put(person.getName(), person);
        }
        return groups;
    }
}