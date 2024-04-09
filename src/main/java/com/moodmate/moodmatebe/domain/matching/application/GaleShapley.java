package com.moodmate.moodmatebe.domain.matching.application;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.matching.domain.Man;
import com.moodmate.moodmatebe.domain.matching.domain.WhoMeet;
import com.moodmate.moodmatebe.domain.matching.domain.Woman;
import com.moodmate.moodmatebe.domain.matching.repository.WhoMeetRepository;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class GaleShapley {
    private final int N;
    private int engagedCount;
    private Map<String, Man> men;
    private Map<String, Woman> women;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final WhoMeetRepository whoMeetRepository;

    public GaleShapley(Map<String, Man> m, Map<String, Woman> w, RoomRepository roomRepository, UserRepository userRepository, WhoMeetRepository whoMeetRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.whoMeetRepository = whoMeetRepository;
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
        calcMatches();
    }

    private void calcMatches() {
        log.info("매칭을 계산합니다.");
        firstPropose();
        firstAcceptOrReject();
        while (engagedCount < N) {
            Man free = null;
            for (Man man : men.values()) {
                if (!man.isEngaged()) {
                    free = man;
                    break;
                }
            }

            if (free != null) {
                log.debug("남자 {}이(가) 매칭을 시도합니다.", free.getName());

                for (String w : free.getPreferences()) {
                    Woman woman = women.get(w);
                    if (!free.isEngaged() && !free.getProposed().contains(woman)) {
                        log.debug("{}이(가) {}에게 고백합니다.", free.getName(), woman.getName());
                        free.getProposed().add(woman);

                        if (woman.getPartner() == null) {
                            woman.setPartnerUserId(free.getUser().getUserId());
                            woman.setPartner(free.getName());
                            free.setEngaged(true);
                            engagedCount++;
                            log.info("{}은(는) {}과(와) 매칭되었습니다.", woman.getName(), free.getName());
                        } else {
                            Man currentPartner = men.get(woman.getPartner());
                            log.debug("{}은(는) 이미 {}과(와) 매칭되어 있습니다.", woman.getName(), currentPartner.getName());

                            if (morePreference(currentPartner, free, woman)) {
                                woman.setPartnerUserId(free.getUser().getUserId());
                                woman.setPartner(free.getName());
                                free.setEngaged(true);
                                currentPartner.setEngaged(false);
                                log.info("{}은(는) {}와(과) 이제 매칭되었습니다. 이전 파트너는 매칭 해제됩니다.(매칭해제)", woman.getName(), free.getName());
                            } else {
                                log.debug("{}은(는) 여전히 {}와(과) 매칭되어 있습니다.(새로운 남자 선호도부족)", woman.getName(), currentPartner.getName());
                            }
                        }
                    }
                }
            }
        }
        printCouples();
    }

    private void firstPropose() {
        log.debug("모든 남자가 첫 번째 선호도의 여성에게 고백합니다.");
        for (Man man : men.values()) {
            Woman woman = women.get(man.getPreferences().get(0));
            woman.getProposals().add(man);
            man.getProposed().add(woman);
            log.trace("{}이(가) {}에게 고백했습니다.", man.getName(), woman.getName());
        }
    }

    private void firstAcceptOrReject() {
        log.debug("각 여성이 고백한 남자 중 가장 선호하는 남자를 선택합니다.");
        for (Woman woman : women.values()) {
            if (!woman.getProposals().isEmpty()) {
                Man chosenMan = woman.getProposals().get(0);
                for (Man man : woman.getProposals()) {
                    if (woman.getPreferences().indexOf(man.getName()) < woman.getPreferences().indexOf(chosenMan.getName())) {
                        chosenMan = man;
                    }
                }
                woman.setPartnerUserId(chosenMan.getUser().getUserId());
                woman.setPartner(chosenMan.getName());
                chosenMan.setEngaged(true);
                engagedCount++;
                log.info("{}이(가) {}을(를) 선택했습니다.", woman.getName(), chosenMan.getName());

                woman.getProposals().remove(chosenMan);
                for (Man man : woman.getProposals()) {
                    man.setEngaged(false);
                    man.getPreferences().remove(woman.getName());
                }
                woman.getProposals().clear();
            }
        }
    }

    public boolean morePreference(Man curPartner, Man newPartner, Woman w) {
        for (String m : w.getPreferences()) {
            if (m.equals(newPartner.getName()))
                return true;
            if (m.equals(curPartner.getName()))
                return false;
        }
        return false;
    }

    public void printCouples() {
        log.info("매칭 결과를 출력합니다.");
        System.out.println("========================================");
        System.out.println("=============== 매칭 결과 ===============");
        System.out.println("========================================");
        for (Woman w : women.values()) {
            log.info("{} - {}", w.getName(), w.getPartner());

            if (w.getUserId() != null && w.getPartnerUserId() != null) {
                Optional<User> womenUser = userRepository.findById(w.getUserId());
                Optional<User> partnerUser = userRepository.findById(w.getPartnerUserId());

                if (womenUser.isPresent() && partnerUser.isPresent()) {
                    ChatRoom chatRoom = ChatRoom.builder()
                            .user1(womenUser.get())
                            .user2(partnerUser.get())
                            .roomActive(true)
                            .createdAt(LocalDateTime.now())
                            .build();
                    roomRepository.save(chatRoom);

                    WhoMeet whoMeet = WhoMeet.builder()
                            .metUser1(womenUser.get())
                            .metUser2(partnerUser.get())
                            .createdAt(LocalDateTime.now())
                            .build();
                    whoMeetRepository.save(whoMeet);
                }
            }
        }
    }
}