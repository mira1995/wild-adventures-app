package com.wa.msm.image.repository;

import com.wa.msm.image.entity.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageTypeRepository extends JpaRepository<ImageType, String> {
}
