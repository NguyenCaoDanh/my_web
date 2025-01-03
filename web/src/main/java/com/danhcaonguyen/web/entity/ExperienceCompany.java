package com.danhcaonguyen.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "experience_company", schema = "my_website", catalog = "")
public class ExperienceCompany {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_experience_company", nullable = false)
    private int idExperienceCompany;
    @Basic
    @Column(name = "company_name", nullable = false, length = 225)
    private String companyName;
    @Basic
    @Column(name = "mst", nullable = false, length = 45)
    private String mst;
    @Basic
    @Column(name = "company_url", nullable = false, length = 225)
    private String companyUrl;
    @Basic
    @Column(name = "company_img", nullable = false, length = 225)
    private String companyImg;
    @Basic
    @Column(name = "role", nullable = false, length = 45)
    private String role;
    @Basic
    @Column(name = "description", nullable = false, length = -1)
    private String description;
    @Basic
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExperienceCompany that = (ExperienceCompany) o;
        return idExperienceCompany == that.idExperienceCompany && userId == that.userId && Objects.equals(companyName, that.companyName) && Objects.equals(mst, that.mst) && Objects.equals(companyUrl, that.companyUrl) && Objects.equals(companyImg, that.companyImg) && Objects.equals(role, that.role) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idExperienceCompany, companyName, mst, companyUrl, companyImg, role, description, userId);
    }
}
