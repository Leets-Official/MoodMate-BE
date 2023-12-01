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

    @Column(name = "prefer_year_min", nullable = false)
    private int preferYearMin;

    @Column(name = "prefer_year_max", nullable = false)
    private int preferYearMax;

}
