package com.wa.msm.image.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(AdventureImageKey.class)
@Table(name = "image_adventure", schema = "waimage")
@Data @AllArgsConstructor @NoArgsConstructor
public class AdventureImage extends ImageDependency implements Serializable {

    @Id
    @Column(name = "image_id")
    private Long imageId;

    @Id
    @Column(name = "adventure_id")
    private Long adventureId;
}
