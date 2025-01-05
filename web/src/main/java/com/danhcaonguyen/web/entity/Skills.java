package com.danhcaonguyen.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Skills {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_skills", nullable = false)
    private int idSkills;

    @Basic
    @Column(name = "name", nullable = false, length = 225)
    private String name;

    @Basic
    @Column(name = "level", nullable = false, length = 45)
    private String level;

    @ManyToOne
    @JoinColumn(name = "skill_type_id", nullable = false)
    private SkillType skillType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
