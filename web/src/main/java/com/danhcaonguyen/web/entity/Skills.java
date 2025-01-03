package com.danhcaonguyen.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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
    @Basic
    @Column(name = "skill_type_id", nullable = false)
    private int skillTypeId;
    @Basic
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skills skills = (Skills) o;
        return idSkills == skills.idSkills && skillTypeId == skills.skillTypeId && userId == skills.userId && Objects.equals(name, skills.name) && Objects.equals(level, skills.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSkills, name, level, skillTypeId, userId);
    }
}
