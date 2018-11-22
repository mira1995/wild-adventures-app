package com.wa.msm.category.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "category", schema = "wacategory")
@Data @AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {
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

    @JsonManagedReference
    @OneToMany(mappedBy = "category", targetEntity = CategoryAdventure.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<CategoryAdventure> categoryAdventures = new ArrayList<>(0);
}
