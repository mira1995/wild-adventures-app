package com.wa.msm.image.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "image_type", schema = "waimage")
@Data @AllArgsConstructor @NoArgsConstructor
public class ImageType {

    @Id
    @Column(name = "code", updatable = false, nullable = false)
    private String code;

    @Column(name = "name")
    private String name;
}
