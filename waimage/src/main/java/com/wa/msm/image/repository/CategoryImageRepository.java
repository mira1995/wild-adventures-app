package com.wa.msm.image.repository;

import com.wa.msm.image.entity.CategoryImage;
import com.wa.msm.image.entity.CategoryImageKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryImageRepository extends JpaRepository<CategoryImage, CategoryImageKey> {
    List<CategoryImage> findCategoryImagesByCategoryId(Long categoryId);
}
