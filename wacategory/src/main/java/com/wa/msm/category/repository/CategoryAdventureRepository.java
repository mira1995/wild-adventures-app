package com.wa.msm.category.repository;

import com.wa.msm.category.entity.CategoryAdventure;
import com.wa.msm.category.entity.CategoryAdventureKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryAdventureRepository extends JpaRepository<CategoryAdventure, CategoryAdventureKey> {
}
