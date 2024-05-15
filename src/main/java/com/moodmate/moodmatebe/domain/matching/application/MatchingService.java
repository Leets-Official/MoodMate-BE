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
import com.moodmate.moodmatebe.domain.user.repository.PreferRepository;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
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

    private List<Man> men = new ArrayList<>();
    private List<Woman> women = new ArrayList<>();

    @Transactional
    public void match() {
        List<Prefer> activeMatchTrue = getActiveMatchPreferences();
        Collections.shuffle(activeMatchTrue);

        for (Prefer prefer : activeMatchTrue) {
            addPersonToGroup(prefer);
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
            matchGroupsByMood(mood, menGroups.get(mood), womenGroups.get(mood));
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
        Set<String> metPeopleSet = getMetPeopleSet(person);

        for (U otherPerson : otherGroup.values()) {
            String otherPersonName = otherPerson.getName();
            if (metPeopleSet.contains(otherPersonName)) continue;

            if (isSameDepartment(person, otherPerson)) {
                thirdPreferences.add(otherPersonName);
            } else if (isWithinPreferredAgeRange(person, otherPerson)) {
                mainPreferences.add(otherPersonName);
            } else {
                secondaryPreferences.add(otherPersonName);
            }
        }

        mainPreferences.addAll(secondaryPreferences);
        mainPreferences.addAll(thirdPreferences);
        mainPreferences.addAll(metPeopleSet);

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

    private <T extends Person> Set<String> getMetPeopleSet(T person) {
        Set<String> metPeopleSet = new HashSet<>();
        User user = userRepository.findById(person.getUserProfile().userId())
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 유저가 없음"));

        List<WhoMeet> metPeople;
        if (person instanceof Man) {
            metPeople = whoMeetRepository.findByMetUser2(user);
            for (WhoMeet met : metPeople) {
                metPeopleSet.add(met.getMetUser1().getUserNickname());
            }
        } else {
            metPeople = whoMeetRepository.findByMetUser1(user);
            for (WhoMeet met : metPeople) {
                metPeopleSet.add(met.getMetUser2().getUserNickname());
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