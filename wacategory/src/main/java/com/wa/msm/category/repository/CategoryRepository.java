package com.wa.msm.category.repository;

import com.wa.msm.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Iterable<Category> findAllByIdIn(List<Long> categoriesIdList);
    Iterable<Category> findTop5ByOrderByIdDesc();
}
