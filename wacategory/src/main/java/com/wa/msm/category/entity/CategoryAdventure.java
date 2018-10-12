package com.wa.msm.category.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity @IdClass(CategoryAdventureKey.class)
@Data @AllArgsConstructor @NoArgsConstructor
public class CategoryAdventure implements Serializable {
    @JsonBackReference
    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Category category;

    @Id @Column(name = "category_id")
    private Long categoryId;

    @Id @Column(name = "adventure_id")
    private Long adventureId;
}
