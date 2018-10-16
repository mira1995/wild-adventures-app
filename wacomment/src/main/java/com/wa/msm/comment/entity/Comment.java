package com.wa.msm.comment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment", schema = "wacomment")
@JsonIgnoreProperties(value = {"parentId"})
@Data @AllArgsConstructor @NoArgsConstructor
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "reported")
    private Boolean reported;

    @OneToMany(targetEntity = Comment.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private List<Comment> comments = new ArrayList<>(0);

    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;

    @Column(name = "adventure_id")
    private Long adventureId;

    @Column(name = "user_id")
    private Long userId;
}
