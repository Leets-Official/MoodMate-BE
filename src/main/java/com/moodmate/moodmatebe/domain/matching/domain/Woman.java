package com.moodmate.moodmatebe.domain.matching.domain;

import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Woman extends Person {
    private String partner;
    private Long partnerUserId;  // 현재 파트너
    private List<Man> proposals; // 여자가 남자에게 받은 고백 목록

    public Woman(User user, Prefer prefer) {
        super(user, prefer);
        this.partner = null;
        this.partnerUserId = null;
        this.proposals = new ArrayList<>();
    }
    public Long getUserId() {
        return this.getUser().getUserId();
    }
}