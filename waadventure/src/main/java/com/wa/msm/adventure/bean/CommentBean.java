package com.wa.msm.adventure.bean;

import lombok.Data;

@Data
public class CommentBean {
    private Long id;
    private String content;
    private Boolean reported;
    private Long adventureId;
    private Long userId;
}
