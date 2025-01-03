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
    @Basic
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projects projects = (Projects) o;
        return idProjects == projects.idProjects && userId == projects.userId && Objects.equals(title, projects.title) && Objects.equals(description, projects.description) && Objects.equals(imgUrl, projects.imgUrl) && Objects.equals(githubUrl, projects.githubUrl) && Objects.equals(technologies, projects.technologies) && Objects.equals(role, projects.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProjects, title, description, imgUrl, githubUrl, technologies, role, userId);
    }
}
