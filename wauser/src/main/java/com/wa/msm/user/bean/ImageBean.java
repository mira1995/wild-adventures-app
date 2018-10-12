package com.wa.msm.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ImageBean {


    private Long id;

    private String alt;

    private String description;

    private String uri;

    private ImageTypeBean type;
}
