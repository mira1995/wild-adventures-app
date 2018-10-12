package com.wa.msm.image.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(CategoryImageKey.class)
@Table(name = "image_category", schema = "waimage")
@Data @AllArgsConstructor @NoArgsConstructor
public class CategoryImage extends ImageDependency implements Serializable {

    @Id
    @Column(name = "image_id")
    private Long imageId;

    @Id
    @Column(name = "category_id")
    private Long categoryId;
}
