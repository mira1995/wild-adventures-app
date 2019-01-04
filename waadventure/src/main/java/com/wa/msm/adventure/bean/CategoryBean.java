package com.wa.msm.adventure.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryBean {
    private Long id;
    private String title;
    private String description;
    private List<CategoryAdventureBean> categoryAdventures = new ArrayList<>(0);
}
