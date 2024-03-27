package com.moodmate.moodmatebe.domain.matching.domain;

import com.moodmate.moodmatebe.domain.user.domain.Prefer;
import com.moodmate.moodmatebe.domain.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Man extends Person {
    private boolean engaged;
    private List<Woman> proposed;

    public Man(User user, Prefer prefer) {
        super(user, prefer);
        this.engaged = false;
        this.proposed = new ArrayList<>();
    }
}