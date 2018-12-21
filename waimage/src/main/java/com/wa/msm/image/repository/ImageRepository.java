package com.wa.msm.image.repository;

import com.wa.msm.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Integer countByUri(String uri);

    List<Image> findByIdIn(List<Long> idList);

    Image findTopByOrderByIdDesc();
}
