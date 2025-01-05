package com.danhcaonguyen.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "skill_type", schema = "my_website", catalog = "")
public class SkillType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_skill_type", nullable = false)
    private int idSkillType;

    @Basic
    @Column(name = "type", nullable = false, length = 45)
    private String type;

    @OneToMany(mappedBy = "skillType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skills> skills;
}
