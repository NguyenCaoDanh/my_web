package com.danhcaonguyen.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Activities {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_activities", nullable = false)
    private int idActivities;

    @Basic
    @Column(name = "title", nullable = false, length = 225)
    private String title;

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // This field maps to the User entity
}
