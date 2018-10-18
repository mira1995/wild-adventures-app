package com.wa.msm.comment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment", schema = "wacomment")
@JsonIgnoreProperties(value = {"parentId"})
@Data @AllArgsConstructor @NoArgsConstructor
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 1000)
    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "reported")
    private Boolean reported;

    @OneToMany(targetEntity = Comment.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private List<Comment> comments = new ArrayList<>(0);

    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;

    @NotNull
    @Column(name = "adventure_id")
    private Long adventureId;

    @NotNull
    @Column(name = "user_id")
    private Long userId;
}
