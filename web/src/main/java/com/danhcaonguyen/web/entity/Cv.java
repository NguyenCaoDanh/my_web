package com.danhcaonguyen.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity
public class Cv {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_cv", nullable = false)
    private int idCv;
    @Basic
    @Column(name = "cv_name", nullable = false, length = 45)
    private String cvName;
    @Basic
    @Column(name = "link", nullable = false, length = -1)
    private String link;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
