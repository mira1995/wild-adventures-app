package com.wa.msm.image.repository;

import com.wa.msm.image.entity.CategoryImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryImageRepository extends JpaRepository<CategoryImage, Long> {
}
