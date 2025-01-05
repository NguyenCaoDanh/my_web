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
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
