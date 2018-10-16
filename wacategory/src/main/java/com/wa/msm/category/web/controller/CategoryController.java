package com.wa.msm.category.web.controller;

import com.wa.msm.category.entity.Category;
import com.wa.msm.category.proxy.MSAdventureProxy;
import com.wa.msm.category.repository.CategoryAdventureRepository;
import com.wa.msm.category.repository.CategoryRepository;
import com.wa.msm.category.web.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryAdventureRepository categoryAdventureRepository;

    @Autowired
    MSAdventureProxy msAdventureProxy;

    @GetMapping(value = "/categories")
    public List<Category> categoryList() {
        List<Category> categories = new ArrayList<>(0);
        categoryRepository.findAll().iterator().forEachRemaining(categories::add);
        if (categories.isEmpty()) throw new CategoryNotFoundException("Il n'existe aucune catégories.");
        return categories;
    }

    @GetMapping(value = "/category/{id}")
    public Optional<Category> getCategory(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (!category.isPresent()) throw new CategoryNotFoundException("Il n'existe aucune catégorie pour id " + id + ".");
        return category;
    }

    @PostMapping(value = "/category")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/category")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
        if (category.getId() == null || !categoryRepository.findById(category.getId()).isPresent())
            throw new CategoryNotFoundException("La catégorie envoyée n'existe pas.");
        else {
            // Vérifier que l'aventure existe
            if (!category.getCategoryAdventures().isEmpty())
                category.getCategoryAdventures().forEach(categoryAdventure -> msAdventureProxy.getAdventure(categoryAdventure.getAdventureId()));
        }
        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/category/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        Optional<Category> categoryToDelete = categoryRepository.findById(id);
        if (!categoryToDelete.isPresent()) throw new CategoryNotFoundException("La catégorie correspondante à l'id " + id + " n'existe pas.");
        else categoryRepository.deleteById(categoryToDelete.get().getId());
        return new ResponseEntity<>("La catégorie pour id " + id + " a bien été supprimé.", HttpStatus.GONE);
    }
}
