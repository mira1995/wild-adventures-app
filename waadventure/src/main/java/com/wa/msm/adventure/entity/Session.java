package com.wa.msm.adventure.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "session", schema = "waadventure")
@Data @AllArgsConstructor @NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(targetEntity = Adventure.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "adventure_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Adventure adventure;

    @Column(name = "adventure_id")
    private Long adventureId;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "price")
    private Double price;
}
