package com.wa.msm.category.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor @NoArgsConstructor
public class CategoryAdventureKey implements Serializable {
    private Long categoryId;
    private Long adventureId;
}
