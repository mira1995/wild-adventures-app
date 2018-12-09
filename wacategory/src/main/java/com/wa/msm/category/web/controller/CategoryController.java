package com.wa.msm.category.web.controller;

import com.sun.deploy.util.StringUtils;
import com.wa.msm.category.entity.Category;
import com.wa.msm.category.entity.CategoryAdventure;
import com.wa.msm.category.proxy.MSAdventureProxy;
import com.wa.msm.category.repository.CategoryAdventureRepository;
import com.wa.msm.category.repository.CategoryRepository;
import com.wa.msm.category.web.exception.CategoryNotFoundException;
import com.wa.msm.category.web.exception.CategoryNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class CategoryController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryAdventureRepository categoryAdventureRepository;

    @Autowired
    MSAdventureProxy msAdventureProxy;

    @GetMapping
    public List<Category> categoryList() {
        log.info("Début de la méthode : categoryList()");
        List<Category> categories = new ArrayList<>(0);
        categoryRepository.findAll().iterator().forEachRemaining(categories::add);
        if (categories.isEmpty()) throw new CategoryNotFoundException("Il n'existe aucune catégories.");
        log.info("Récupération de toutes les catégories");
        return categories;
    }

    @GetMapping(value = "/{id}")
    public Optional<Category> getCategory(@PathVariable Long id) {
        log.info("Début de la méthode : getCategory()");
        Optional<Category> category = categoryRepository.findById(id);
        if (!category.isPresent()) throw new CategoryNotFoundException("Il n'existe aucune catégorie pour id " + id + ".");
        log.info("Récupération de toutes les catégories");
        return category;
    }

    @GetMapping(value = "/lastFiveCategories")
    public List<Category> getLastFiveCategories() {
        log.info("Début de la méthode : getCategory()");
        List<Category> categories = new ArrayList<>(0);
        categoryRepository.findTop5ByOrderByIdDesc().iterator().forEachRemaining(categories::add);
        if (categories.isEmpty()) throw new CategoryNotFoundException("Il n'existe aucune catégorie ");
        log.info("Récupération de toutes les catégories");
        return categories;
    }

    @GetMapping(value = "/adventure/{adventureId}")
    public List<Category> categoryListByAdventure(@PathVariable Long adventureId){
        log.info("Début de la méthode : categoryListByAdventure()");
        List<Category> categories = new ArrayList<>(0);
        List<CategoryAdventure> categoriesAdventure = new ArrayList<>(0);
        List <Long> categoriesId = new ArrayList<>(0);

        categoryAdventureRepository.findAllByAdventureId(adventureId).iterator().forEachRemaining(categoriesAdventure::add);
        if (categoriesAdventure.isEmpty()) throw new CategoryNotFoundException("Il n'existe aucune catégories.");
        categoriesAdventure.iterator().forEachRemaining(categoryAdventure -> categoriesId.add(categoryAdventure.getCategoryId()));
        categoryRepository.findAllByIdIn(categoriesId).iterator().forEachRemaining(categories::add);
        log.info("Récupération de toutes les catégories correspondant à l'aventure d'id "+adventureId);
        return categories;
    }

    @PostMapping(value = "/admin")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        log.info("Début de la méthode : addCategory()");
        validateCategory(category);
        log.info("Création d'une catégorie");
        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/admin")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
        log.info("Début de la méthode : updateCategory()");
        if (category.getId() == null || !categoryRepository.findById(category.getId()).isPresent())
            throw new CategoryNotFoundException("La catégorie envoyée n'existe pas.");
        else {
            // Vérifier que l'aventure existe
            if (!category.getCategoryAdventures().isEmpty())
                category.getCategoryAdventures().forEach(categoryAdventure -> msAdventureProxy.getAdventure(categoryAdventure.getAdventureId()));
        }
        validateCategory(category);
        log.info("Mise à jour de la catégorie d'id : "+category.getId());
        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/admin/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        log.info("Début de la méthode : deleteCategory()");
        Optional<Category> categoryToDelete = categoryRepository.findById(id);
        if (!categoryToDelete.isPresent()) throw new CategoryNotFoundException("La catégorie correspondante à l'id " + id + " n'existe pas.");
        else categoryRepository.deleteById(categoryToDelete.get().getId());
        log.info("Suppression de la catégorie d'id : "+id);
        return new ResponseEntity<>("La catégorie pour id " + id + " a bien été supprimé.", HttpStatus.GONE);
    }

    private void validateCategory(Category category) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);

        if(constraintViolations.size() > 0) {
            List<String> violationMessages = new ArrayList<>();
            constraintViolations.iterator().forEachRemaining(constraintViolation ->
                    violationMessages.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage()));

            throw new CategoryNotValidException("La catégorie n'est pas valide. " + StringUtils.join(violationMessages, " ; "));
        }
    }
}
