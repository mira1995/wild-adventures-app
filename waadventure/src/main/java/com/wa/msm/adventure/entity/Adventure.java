package com.wa.msm.adventure.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adventure", schema = "waadventure")
@Data @AllArgsConstructor @NoArgsConstructor
public class Adventure {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 250)
    @Column(name = "title")
    private String title;

    @NotNull
    @Size(max = 1000)
    @Column(name = "description")
    private String description;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "status")
    private String status;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "location")
    private String location;

    @JsonManagedReference
    @OneToMany(mappedBy = "adventure", targetEntity = Session.class, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<Session> sessions = new ArrayList<>(0);
}
