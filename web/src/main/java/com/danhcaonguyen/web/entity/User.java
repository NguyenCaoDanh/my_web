package com.danhcaonguyen.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_user", nullable = false)
    private int idUser;

    @Basic
    @Column(name = "first_name", nullable = true, length = 45)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = true, length = 45)
    private String lastName;

    @Basic
    @Column(name = "middle_name", nullable = true, length = 45)
    private String middleName;

    @Basic
    @Column(name = "email", nullable = true, length = 100)
    private String email;

    @Basic
    @Column(name = "phone", nullable = true, length = 45)
    private String phone;

    @Basic
    @Column(name = "address", nullable = true, length = 225)
    private String address;

    @Basic
    @Column(name = "github", nullable = true, length = 45)
    private String github;

    @Basic
    @Column(name = "facebook", nullable = true, length = 45)
    private String facebook;

    @Basic
    @Column(name = "zalo", nullable = true, length = 45)
    private String zalo;

    @Basic
    @Column(name = "avatar", nullable = true, length = 225)
    private String avatar;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // Corrected 'mappedBy'
    private List<Activities> activities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Projects> projects;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Skills> skills;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> post;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cv> cv;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ExperienceCompany> experienceCompanies;
}
