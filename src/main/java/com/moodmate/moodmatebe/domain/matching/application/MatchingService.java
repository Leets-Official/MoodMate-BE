package com.moodmate.moodmatebe.domain.matching.application;

import com.moodmate.moodmatebe.domain.matching.domain.Man;
import com.moodmate.moodmatebe.domain.matching.domain.Person;
import com.moodmate.moodmatebe.domain.matching.domain.Woman;
import com.moodmate.moodmatebe.domain.user.domain.Gender;
import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.repository.PreferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchingService {

    private final PreferRepository preferRepository;

    @Autowired
    public MatchingService(PreferRepository personRepository) {
        this.preferRepository = personRepository;
    }

    public void match() {
        // 활성화된 Prefer 객체들을 가져옴
        List<Prefer> activeMatchTrue = preferRepository.findByUserMatchActiveAndGenderTrue(Gender.MALE);
        activeMatchTrue.addAll(preferRepository.findByUserMatchActiveAndGenderTrue(Gender.FEMALE));

        List<Man> men = new ArrayList<>();
        List<Woman> women = new ArrayList<>();

        // Prefer 객체들을 Person 인스턴스로 변환하고 남성과 여성 리스트에 추가
        for (Prefer prefer : activeMatchTrue) {
            Person person = new Person(prefer.getUser(), prefer);

            if (person.getGender() == Gender.MALE) {
                men.add(new Man(person.getUser(), person.getPrefer()));
            } else if (person.getGender() == Gender.FEMALE) {
                women.add(new Woman(person.getUser(), person.getPrefer()));
            }
        }
    }

    public void grouping() {
        Map<String, Man> m = new LinkedHashMap<>();
        Map<String, Woman> w = new LinkedHashMap<>();

        Map<String, Map<String, Man>> menGroups = groupByMood(m);
        Map<String, Map<String, Woman>> womenGroups = groupByMood(w);

        // 각 그룹에 대해 Gale-Shapley 알고리즘 실행
        for (String mood : menGroups.keySet()) {
            Map<String, Man> menGroup = menGroups.get(mood);
            Map<String, Woman> womenGroup = womenGroups.get(mood);


            // 각 남자의 선호도 목록 생성
            for (Man man : menGroup.values()) {
                List<String> mainPreferences = new ArrayList<>();
                List<String> secondaryPreferences = new ArrayList<>();
                List<String> finalPreferences = new ArrayList<>();

                // 여자 그룹을 순회하면서 남자의 선호도를 결정
                for (Woman woman : womenGroup.values()) {
                    if (!man.isDontCareSameDepartment() && man.getDepartment().equals(woman.getDepartment())) {
                        finalPreferences.add(woman.getName());
                    } else if (woman.getYear() >= man.getMinYear() && woman.getYear() <= man.getMaxYear()) {
                        // 만약 여자의 나이가 남자의 나이 선호 범위 내에 있다면 첫 번째 선호도로 추가
                        mainPreferences.add(woman.getName());
                    } else {
                        // 그렇지 않으면 보조 선호도로 추가
                        secondaryPreferences.add(woman.getName());
                    }
                }

                // 보조 선호도를 전체 선호도 목록에 추가
                mainPreferences.addAll(secondaryPreferences);
                mainPreferences.addAll(finalPreferences);

                // 남자의 preferences 속성에 최종 선호도 목록 설정
                man.setPreferences(mainPreferences);
            }

            // 각 여자의 선호도 목록 생성
            for (Woman woman : womenGroup.values()) {
                List<String> mainPreferences = new ArrayList<>();
                List<String> secondaryPreferences = new ArrayList<>();
                List<String> finalPreferences = new ArrayList<>();

                // 남자 그룹을 순회하면서 여자의 선호도를 결정
                for (Man man : menGroup.values()) {
                    if (!woman.isDontCareSameDepartment() && woman.getDepartment().equals(man.getDepartment())) {
                        finalPreferences.add(man.getName());
                    }
                    else if (man.getYear() >= woman.getMinYear() && man.getYear() <= woman.getMaxYear()) {
                        // 만약 남자의 나이가 여자의 나이 선호 범위 내에 있다면 첫 번째 선호도로 추가
                        mainPreferences.add(man.getName());
                    } else {
                        // 그렇지 않으면 보조 선호도로 추가
                        secondaryPreferences.add(man.getName());
                    }
                }

                // 보조 선호도를 전체 선호도 목록에 추가
                mainPreferences.addAll(secondaryPreferences);
                mainPreferences.addAll(finalPreferences);

                // 여자의 preferences 속성에 최종 선호도 목록 설정
                woman.setPreferences(mainPreferences);
            }

            // Gale-Shapley 알고리즘 실행
            new GaleShapley(menGroup, womenGroup);
        }
    }

    // 무드에 따라 그룹화하는 메소드
    // 질문: 그럼 무드가 다른 사람 남 녀 둘이 있다면 매칭이 아예 안되나요?
    /*
        예를 들어서

        성별  무드
        남   A
        남   B
        남   C
        여   D

        이렇게 남으면 아예 저 사람들은 매칭이 안되어버리는건지, 아니면 후순위로 밀리는건지 궁금합니다
     */
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