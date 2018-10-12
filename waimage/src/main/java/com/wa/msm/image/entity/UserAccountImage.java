package com.wa.msm.image.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "image_useraccount", schema = "waimage")
@Data @AllArgsConstructor
public class UserAccountImage extends Image {


}
