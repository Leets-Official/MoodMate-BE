package com.moodmate.moodmatebe.domain.matching.application;

import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.matching.domain.Man;
import com.moodmate.moodmatebe.domain.matching.domain.Person;
import com.moodmate.moodmatebe.domain.matching.domain.WhoMeet;
import com.moodmate.moodmatebe.domain.matching.domain.Woman;
import com.moodmate.moodmatebe.domain.matching.repository.WhoMeetRepository;
import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.repository.PreferRepository;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
public class MatchingService {

    private final PreferRepository preferRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final WhoMeetRepository whoMeetRepository;

    private List<Man> men = new ArrayList<>();
    private List<Woman> women = new ArrayList<>();

    @Autowired
    public MatchingService(PreferRepository personRepository, RoomRepository roomRepository, UserRepository userRepository, WhoMeetRepository whoMeetRepository) {
        this.preferRepository = personRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.whoMeetRepository = whoMeetRepository;
    }

    @Transactional
    public void match() {
        List<Prefer> activeMatchTrue = preferRepository.findByUserMatchActiveAndGenderTrue(Gender.MALE);
        activeMatchTrue.addAll(preferRepository.findByUserMatchActiveAndGenderTrue(Gender.FEMALE));

        Collections.shuffle(activeMatchTrue);

        for (Prefer prefer : activeMatchTrue) {
            Person person = new Person(prefer.getUser(), prefer);

            if (person.getGender() == Gender.MALE) {
                men.add(new Man(person.getUser(), person.getPrefer()));
            } else if (person.getGender() == Gender.FEMALE) {
                women.add(new Woman(person.getUser(), person.getPrefer()));
            }
        }
    }

    @Transactional
    public void grouping() {
        Map<String, Man> m = convertListToMap(men);
        Map<String, Woman> w = convertListToMap(women);

        Map<String, Map<String, Man>> menGroups = groupByMood(m);
        Map<String, Map<String, Woman>> womenGroups = groupByMood(w);

        for (String mood : menGroups.keySet()) {
            Map<String, Man> menGroup = menGroups.get(mood);
            Map<String, Woman> womenGroup = womenGroups.get(mood);

            log.info("====================================");
            log.info("{} 그룹 매칭을 시작합니다.", mood);
            log.info("====================================");

            log.debug("{} 그룹 남자:", mood);
            for (Man man : menGroup.values()) {
                log.info(man.getName());
            }

            log.debug("{} 그룹 여자:", mood);
            for (Woman woman : womenGroup.values()) {
                log.info(woman.getName());
            }

            for (Man man : menGroup.values()) {
                List<String> mainPreferences = new ArrayList<>();
                List<String> secondaryPreferences = new ArrayList<>();
                List<String> thirdPreferences = new ArrayList<>();
                Set<String> metFemalesSet = new HashSet<>();

                List<WhoMeet> metFemales = whoMeetRepository.findByMetUser2(man.getUser());
                for (WhoMeet met : metFemales) {
                    metFemalesSet.add(met.getMetUser1().getUserNickname());
                }

                for (Woman woman : womenGroup.values()) {
                    String womanName = woman.getName();
                    if (metFemalesSet.contains(womanName)) {
                        continue;
                    }

                    if (!man.isDontCareSameDepartment() && man.getDepartment().equals(woman.getDepartment())) {
                        thirdPreferences.add(womanName);
                    } else if (woman.getYear() >= man.getMinYear() && woman.getYear() <= man.getMaxYear()) {
                        mainPreferences.add(womanName);
                    } else {
                        secondaryPreferences.add(womanName);
                    }
                }

                mainPreferences.addAll(secondaryPreferences);
                mainPreferences.addAll(thirdPreferences);
                mainPreferences.addAll(metFemalesSet);

                man.setPreferences(mainPreferences);

                log.info("{}의 선호도 목록 : {}", man.getName(), mainPreferences);
            }

            for (Woman woman : womenGroup.values()) {
                List<String> mainPreferences = new ArrayList<>();
                List<String> secondaryPreferences = new ArrayList<>();
                List<String> thirdPreferences = new ArrayList<>();
                Set<String> metMalesSet = new HashSet<>();

                List<WhoMeet> metMales = whoMeetRepository.findByMetUser1(woman.getUser());
                for (WhoMeet met : metMales) {
                    metMalesSet.add(met.getMetUser2().getUserNickname());
                }

                for (Man man : menGroup.values()) {
                    String manName = man.getName();
                    if (metMalesSet.contains(manName)) {
                        continue;
                    }

                    if (!woman.isDontCareSameDepartment() && woman.getDepartment().equals(man.getDepartment())) {
                        thirdPreferences.add(man.getName());
                    }
                    else if (man.getYear() >= woman.getMinYear() && man.getYear() <= woman.getMaxYear()) {
                        mainPreferences.add(man.getName());
                    } else {
                        secondaryPreferences.add(man.getName());
                    }
                }

                mainPreferences.addAll(secondaryPreferences);
                mainPreferences.addAll(thirdPreferences);
                mainPreferences.addAll(metMalesSet);

                woman.setPreferences(mainPreferences);

                log.info("{}의 선호도 목록 : {}", woman.getName(), mainPreferences);
            }

            new GaleShapley(menGroup, womenGroup, roomRepository, userRepository, whoMeetRepository);
        }
    }

    private <T extends Person> Map<String, T> convertListToMap(List<T> list) {
        Map<String, T> map = new LinkedHashMap<>();
        for (T person : list) {
            map.put(String.valueOf(person.getUser().getUserId()), person);  // user_id를 키로 사용
        }
        return map;
    }

    private static <T extends Person> Map<String, Map<String, T>> groupByMood(Map<String, T> persons) {
        Map<String, Map<String, T>> groups = new HashMap<>();
        for (T person : persons.values()) {
            if (!groups.containsKey(person.getMood())) {
                groups.put(person.getMood(), new HashMap<>());
            }
            groups.get(person.getMood()).put(person.getName(), person);
        }
        return groups;
    }
}