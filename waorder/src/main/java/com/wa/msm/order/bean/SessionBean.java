package com.wa.msm.order.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor
public class SessionBean {

    private Long id;

    private AdventureBean adventure;

    private Long adventureId;

    private Date startDate;

    private Date endDate;

    private Double price;
}
