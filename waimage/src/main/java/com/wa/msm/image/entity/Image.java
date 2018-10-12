package com.wa.msm.image.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;

@Entity
@Table(name = "image", schema = "waimage")
@Inheritance(strategy = InheritanceType.JOINED)
@Data @AllArgsConstructor @NoArgsConstructor
public class Image {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alt")
    private String alt;

    @Column(name = "description")
    private String description;

    @Column(name = "uri")
    private String uri;

    @ManyToOne
    @JoinColumn(name = "type_code", referencedColumnName = "code")
    private ImageType type;

}
