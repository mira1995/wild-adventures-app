package com.wa.msm.adventure.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adventure", schema = "waadventure")
@Data @AllArgsConstructor @NoArgsConstructor
public class Adventure {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "location")
    private String location;

    @JsonManagedReference
    @OneToMany(mappedBy = "adventure", targetEntity = Session.class, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<Session> sessions = new ArrayList<>(0);
}
