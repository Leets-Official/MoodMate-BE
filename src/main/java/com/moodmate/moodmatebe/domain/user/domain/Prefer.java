package com.moodmate.moodmatebe.domain.user.domain;

import com.moodmate.moodmatebe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "prefer")
public class Prefer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prefer_id")
    private Long preferId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "prefer_department_possible", nullable = false)
    private boolean preferDepartmentPossible;

    @Column(name = "prefer_mood", nullable = false)
    private String preferMood;

    @Column(name = "prefer_age_min", nullable = false)
    private int preferAgeMin;

    @Column(name = "prefer_age_max", nullable = false)
    private int preferAgeMax;

    // TODO: 동훈아 이거 없애도 되는지 확인해줘. 아래 칼럼은 Person에 들어가야 할 것 같은데??
//    @ElementCollection
//    @CollectionTable(name = "preferences", joinColumns = @JoinColumn(name = "prefer_id"))
//    @Column(name = "preferences", nullable = false)
//    private List<String> preferences;
}
