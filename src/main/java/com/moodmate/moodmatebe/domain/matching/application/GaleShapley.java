package com.moodmate.moodmatebe.domain.matching.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.matching.domain.Man;
import com.moodmate.moodmatebe.domain.matching.domain.Woman;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

public class GaleShapley {
    private final int N; // 총 커플 수
    private int engagedCount;  // 현재 매칭된 커플 수
    private Map<String, Man> men;  // 모든 남자를 저장하는 맵
    private Map<String, Woman> women;  // 모든 여자를 저장하는 맵
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    // 생성자
    public GaleShapley(Map<String, Man> m, Map<String, Woman> w, RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        N = Math.min(m.size(), w.size());
        engagedCount = 0;
        men = m;
        women = w;
        for (Man man : men.values()) {
            man.setPreferences(new ArrayList<>(man.getPreferences()));
        }
        for (Woman woman : women.values()) {
            woman.setPreferences(new ArrayList<>(woman.getPreferences()));
        }
        calcMatches();  // 매칭을 계산하는 메소드 호출
    }

    // 매칭을 계산하는 메소드
    private void calcMatches() {
        System.out.println("매칭을 계산합니다.");
        firstPropose();  // 모든 남자가 첫 번째 선호도의 여성에게 고백
        firstAcceptOrReject();  // 각 여성이 고백한 남자 중 가장 선호하는 남자를 선택
        while (engagedCount < N) {  // 모든 커플이 매칭될 때까지 반복
            Man free = null;
            for (Man man : men.values()) {  // 매칭되지 않은 남자를 찾음
                if (!man.isEngaged()) {
                    free = man;
                    break;
                }
            }

            if (free != null) {  // 매칭되지 않은 남자가 있으면

                for (String w : free.getPreferences()) {  // 그 남자의 선호도 목록을 순회
                    Woman woman = women.get(w);  // 선호하는 여자를 찾음
                    if (!free.isEngaged() && !free.getProposed()
                            .contains(woman)) {  // 남자가 아직 매칭되지 않았고, 아직 고백하지 않은 여성에게만 고백
                        free.getProposed().add(woman);  // 남자가 고백한 여성을 기록

                        if (woman.getPartnerUserId() == null) {  // 여자가 아직 매칭되지 않았으면
                            woman.setPartnerUserId(free.getUser().getUserId()); // 여자와 남자를 매칭
                            woman.setPartner(free.getName());
                            free.setEngaged(true);  // 남자의 상태를 매칭됨으로 변경
                            engagedCount++;  // 매칭된 커플 수 증가
                        } else {  // 여자가 이미 매칭되어 있으면
                            Man currentPartner = men.get(woman.getPartnerUserId());  // 현재 파트너를 찾음

                            if (morePreference(currentPartner, free, woman)) {  // 여자가 새로운 남자를 더 선호하면
                                woman.setPartnerUserId(free.getUser().getUserId());// 여자와 새로운 남자를 매칭
                                woman.setPartner(free.getName());  // 여자와 새로운 남자를 매칭
                                free.setEngaged(true);  // 새로운 남자의 상태를 매칭됨으로 변경
                                currentPartner.setEngaged(false);  // 현재 파트너의 상태를 매칭되지 않음으로 변경
                            }
                        }
                    }
                }
            }
        }
        printCouples();  // 최종 매칭 결과 출력
    }

    // 모든 남자가 첫 번째 선호도의 여성에게 고백하는 메서드
    private void firstPropose() {
        for (Man man : men.values()) {
            Woman woman = women.get(man.getPreferences().get(0));
            woman.getProposals().add(man);
            man.getProposed().add(woman);  // 남자가 고백한 여성을 기록
        }
    }

    // 각 여성이 고백한 남자 중 가장 선호하는 남자를 선택하는 메소드
    private void firstAcceptOrReject() {
        for (Woman woman : women.values()) {
            if (!woman.getProposals().isEmpty()) {
                Man chosenMan = woman.getProposals().get(0);
                for (Man man : woman.getProposals()) {
                    if (woman.getPreferences().indexOf(man.getName()) < woman.getPreferences()
                            .indexOf(chosenMan.getName())) {
                        chosenMan = man;
                    }
                }
                woman.setPartnerUserId(chosenMan.getUser().getUserId());
                woman.setPartner(chosenMan.getName());
                chosenMan.setEngaged(true);
                engagedCount++;

                woman.getProposals().remove(chosenMan);
                for (Man man : woman.getProposals()) {
                    chosenMan.setEngaged(true);
                    man.getPreferences().remove(woman.getName());
                }
                woman.getProposals().clear();
            }
        }
    }

    // 여자가 새로운 남자를 더 선호하는지 확인하는 메소드
    public boolean morePreference(Man curPartner, Man newPartner, Woman w) {
        for (String m : w.getPreferences()) {  // 여자의 선호도 목록을 순회
            if (m.equals(newPartner.getName()))  // 새로운 남자가 먼저 나오면 true 반환
                return true;
            if (m.equals(curPartner.getName()))  // 현재 파트너가 먼저 나오면 false 반환
                return false;
        }
        return false;
    }

    public void printCouples() {
        System.out.println("최종결과 ");
        for (Woman w : women.values()) {
            System.out.println(w.getName() + " - " + w.getPartner());
        }
        for (Woman w : women.values()) {
            Optional<User> womenUser = userRepository.findById(w.getUserId());
            Optional<User> partnerUser = userRepository.findById(w.getPartnerUserId());
            ChatRoom chatRoom = ChatRoom.builder()
                    .user1(womenUser.get())
                    .user2(partnerUser.get())
                    .roomActive(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            roomRepository.save(chatRoom);
        }
    }
}

