package com.danhcaonguyen.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity
public class Projects {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_projects", nullable = false)
    private int idProjects;
    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;
    @Basic
    @Column(name = "description", nullable = false, length = -1)
    private String description;
    @Basic
    @Column(name = "img_url", nullable = true, length = 225)
    private String imgUrl;
    @Basic
    @Column(name = "github_url", nullable = true, length = 225)
    private String githubUrl;
    @Basic
    @Column(name = "technologies", nullable = true, length = -1)
    private String technologies;
    @Basic
    @Column(name = "role", nullable = false, length = 45)
    private String role;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
