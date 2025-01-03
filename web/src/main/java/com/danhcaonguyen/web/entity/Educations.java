package com.danhcaonguyen.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity
public class Educations {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_educations", nullable = false)
    private int idEducations;
    @Basic
    @Column(name = "start", nullable = false)
    private int start;
    @Basic
    @Column(name = "end", nullable = true)
    private Integer end;
    @Basic
    @Column(name = "status", nullable = false, length = 45)
    private String status;
    @Basic
    @Column(name = "school_name", nullable = false, length = -1)
    private String schoolName;
    @Basic
    @Column(name = "gpa_4", nullable = true, precision = 0)
    private Double gpa4;
    @Basic
    @Column(name = "gpa_10", nullable = true, precision = 0)
    private Double gpa10;
    @Basic
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Educations that = (Educations) o;
        return idEducations == that.idEducations && start == that.start && userId == that.userId && Objects.equals(end, that.end) && Objects.equals(status, that.status) && Objects.equals(schoolName, that.schoolName) && Objects.equals(gpa4, that.gpa4) && Objects.equals(gpa10, that.gpa10);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEducations, start, end, status, schoolName, gpa4, gpa10, userId);
    }
}
