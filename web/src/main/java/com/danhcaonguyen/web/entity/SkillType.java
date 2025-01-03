package com.danhcaonguyen.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillType skillType = (SkillType) o;
        return idSkillType == skillType.idSkillType && Objects.equals(type, skillType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSkillType, type);
    }
}
