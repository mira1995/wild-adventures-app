package com.wa.msm.category.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity @IdClass(CategoryAdventureKey.class)
@Data @AllArgsConstructor @NoArgsConstructor
public class CategoryAdventure implements Serializable {
    @Id @Column(name = "category_id")
    private Long categoryId;

    @Id @Column(name = "adventure_id")
    private Long adventureId;
}
