package com.moodmate.moodmatebe.domain.user.domain;

import com.moodmate.moodmatebe.global.shared.entity.BaseTimeEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ElementCollection
    @CollectionTable(name = "preferences", joinColumns = @JoinColumn(name = "prefer_id"))
    @Column(name = "preferences", nullable = false)
    private List<String> preferences;
}
