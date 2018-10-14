package com.wa.msm.image.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdventureImageKey implements Serializable {
    private Long imageId;
    private Long adventureId;
}
