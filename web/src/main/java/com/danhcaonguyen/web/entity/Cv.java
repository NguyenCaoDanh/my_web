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
    @Basic
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cv cv = (Cv) o;
        return idCv == cv.idCv && userId == cv.userId && Objects.equals(cvName, cv.cvName) && Objects.equals(link, cv.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCv, cvName, link, userId);
    }
}
