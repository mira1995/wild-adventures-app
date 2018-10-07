package com.wa.msm.image.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "image_category", schema = "waimage")
@Data @AllArgsConstructor @NoArgsConstructor
public class CategoryImage extends Image {

    @Column(name = "category_id")
    private Long categoryId;
}
