package com.moodmate.moodmatebe.domain.matching.algorithm;

import com.moodmate.moodmatebe.domain.chat.domain.ChatRoom;
import com.moodmate.moodmatebe.domain.chat.repository.RoomRepository;
import com.moodmate.moodmatebe.domain.matching.domain.Man;
import com.moodmate.moodmatebe.domain.matching.domain.WhoMeet;
import com.moodmate.moodmatebe.domain.matching.domain.Woman;
import com.moodmate.moodmatebe.domain.matching.exception.ChatRoomSaveException;
import com.moodmate.moodmatebe.domain.matching.exception.MeetingRecordSaveException;
import com.moodmate.moodmatebe.domain.matching.repository.WhoMeetRepository;
import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
public class GaleShapley {
    private final int totalMatches;
    private int engagedCount;
    private final Map<String, Man> men;
    private final Map<String, Woman> women;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final WhoMeetRepository whoMeetRepository;

    public GaleShapley(Map<String, Man> men, Map<String, Woman> women,
                       RoomRepository roomRepository, UserRepository userRepository,
                       WhoMeetRepository whoMeetRepository) {
        this.totalMatches = Math.min(men.size(), women.size());
        this.men = new LinkedHashMap<>(men);
        this.women = new LinkedHashMap<>(women);
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.whoMeetRepository = whoMeetRepository;
        this.engagedCount = 0;

        initializePreferences();
        performMatching();
    }

    private void initializePreferences() {
        men.values().forEach(
                man -> man.setPreferences(new ArrayList<>(man.getPreferences()))
        );
        women.values().forEach(
                woman -> woman.setPreferences(new ArrayList<>(woman.getPreferences()))
        );
    }

    private void performMatching() {
        log.info("매칭 프로세스를 시작합니다.");

        menProposeToFirstChoice();
        womenChooseBestProposal();

        while (engagedCount < totalMatches) {
            Man free = null;
            for (Man man : men.values()) {
                if (!man.isEngaged()) {
                    free = man;
                    break;
                }
            }

            if (free != null) {
                processProposals(free);
            }
        }

        printCouples();
    }

    private void menProposeToFirstChoice() {
        log.debug("모든 남자가 첫 번째 선호도의 여성에게 고백합니다.");
        for (Man man : men.values()) {
            Woman woman = women.get(man.getPreferences().get(0));
            woman.getProposals().add(man);
            man.getProposed().add(woman);
            log.trace("{}이(가) {}에게 고백했습니다.", man.getName(), woman.getName());
        }
    }

    private void womenChooseBestProposal() {
        log.debug("각 여성이 고백한 남자 중 가장 선호하는 남자를 선택합니다.");
        for (Woman woman : women.values()) {
            if (!woman.getProposals().isEmpty()) {
                Man chosenMan = woman.getProposals().get(0);
                for (Man man : woman.getProposals()) {
                    if (prefersNewPartner(man, chosenMan, woman)) {
                        chosenMan = man;
                    }
                }
                engage(chosenMan, woman);
                engagedCount++;
                log.info("{}이(가) {}을(를) 선택했습니다.", woman.getName(), chosenMan.getName());

                woman.getProposals().remove(chosenMan);
                for (Man man : woman.getProposals()) {
                    disengage(man, woman);
                    man.getPreferences().remove(woman.getName());
                }
                woman.getProposals().clear();
            }
        }
    }

    private void processProposals(Man freeMan) {
        log.debug("남자 {}이(가) 매칭을 시도합니다.", freeMan.getName());

        for (String womanName : freeMan.getPreferences()) {
            Woman woman = women.get(womanName);
            if (!freeMan.isEngaged() && !freeMan.getProposed().contains(woman)) {
                propose(freeMan, woman);
            }
        }
    }

    private void propose(Man man, Woman woman) {
        log.debug("{}이(가) {}에게 고백합니다.", man.getName(), woman.getName());
        man.getProposed().add(woman);

        if (woman.getPartner() == null) {
            engage(man, woman);
            engagedCount++;
            log.info("{}은(는) {}과(와) 매칭되었습니다.", woman.getName(), man.getName());
        } else {
            Man currentPartner = men.get(woman.getPartner());
            log.debug("{}은(는) 이미 {}과(와) 매칭되어 있습니다.", woman.getName(), currentPartner.getName());

            if (prefersNewPartner(currentPartner, man, woman)) {
                engage(man, woman);
                disengage(currentPartner, woman); // 이전 파트너와 해제
                log.info("{}은(는) {}와(과) 이제 매칭되었습니다. 이전 파트너는 매칭 해제됩니다.(매칭해제)", woman.getName(), man.getName());
            } else {
                log.debug("{}은(는) 여전히 {}와(과) 매칭되어 있습니다.(새로운 남자 선호도부족)", woman.getName(), currentPartner.getName());
            }
        }
    }

    private void engage(Man man, Woman woman) {
        woman.setPartnerUserId(man.getUserProfile().userId());
        woman.setPartner(man.getName());
        man.setEngaged(true);
        log.info("{}은(는) {}과(와) 매칭되었습니다.", man.getName(), woman.getName());
    }

    private void disengage(Man currentPartner, Woman woman) {
        currentPartner.setEngaged(false);
        log.info("{}은(는) {}과(와) 매칭이 해제되었습니다.", currentPartner.getName(), woman.getName());
    }

    public boolean prefersNewPartner(Man currentPartner, Man newPartner, Woman woman) {
        return woman.getPreferences().indexOf(newPartner.getName()) < woman.getPreferences().indexOf(currentPartner.getName());
    }

    public void printCouples() {
        printMatchingResults();
        for (Woman woman : women.values()) {
            log.info("{} - {}", woman.getName(), woman.getPartner());

            if (woman.getUserId() != null && woman.getPartnerUserId() != null) {
                createChatRoomAndMeetingRecord(woman);
            }
        }

        log.info("총 매칭 수: {}", engagedCount);
    }

    // 채팅방과 만남 기록 생성
    private void createChatRoomAndMeetingRecord(Woman woman) {
        log.info("createChatRoomAndMeetingRecord 시작");

        Optional<User> womanUser = userRepository.findById(woman.getUserId());
        Optional<User> partnerUser = userRepository.findById(woman.getPartnerUserId());

        if (womanUser.isPresent() && partnerUser.isPresent()) {
            User womanUserObj = womanUser.get();
            User partnerUserObj = partnerUser.get();

            ChatRoom chatRoom = createChatRoom(womanUserObj, partnerUserObj);
            try {
                log.info("채팅방 저장 시도: {} <-> {}", womanUserObj.getUserId(), partnerUserObj.getUserId());
                roomRepository.save(chatRoom);
                log.info("채팅방 저장 성공: {} <-> {}", womanUserObj.getUserId(), partnerUserObj.getUserId());
            } catch (Exception e) {
                log.error("채팅방 저장 중 오류 발생: ", e);
                throw new ChatRoomSaveException();
            }

            WhoMeet whoMeet = createWhoMeetRecord(womanUserObj, partnerUserObj);
            try {
                log.info("만남 기록 저장 시도: {} <-> {}", womanUserObj.getUserId(), partnerUserObj.getUserId());
                whoMeetRepository.save(whoMeet);
                log.info("만남 기록 저장 성공: {} <-> {}", womanUserObj.getUserId(), partnerUserObj.getUserId());
            } catch (Exception e) {
                log.error("만남 기록 저장 중 오류 발생: ", e);
                throw new MeetingRecordSaveException();
            }
        } else {
            log.warn("유효한 사용자 정보가 없습니다. womanUser: {}, partnerUser: {}", womanUser, partnerUser);
        }

        log.info("createChatRoomAndMeetingRecord 종료");
    }

    // 매칭 결과 출력
    private void printMatchingResults() {
        log.info("========================================");
        log.info("=============== 매칭 결과 ==============");
        log.info("========================================");
    }

    // 채팅방 생성
    private ChatRoom createChatRoom(User user1, User user2) {
        return ChatRoom.builder()
                .user1(user1)
                .user2(user2)
                .roomActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 만남 기록 생성
    private WhoMeet createWhoMeetRecord(User user1, User user2) {
        return WhoMeet.builder()
                .metUser1(user1)
                .metUser2(user2)
                .createdAt(LocalDateTime.now())
                .build();
    }
}