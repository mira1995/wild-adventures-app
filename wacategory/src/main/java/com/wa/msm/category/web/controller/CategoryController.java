package com.wa.msm.category.web.controller;

import com.wa.msm.category.entity.Category;
import com.wa.msm.category.entity.CategoryAdventure;
import com.wa.msm.category.proxy.MSAdventureProxy;
import com.wa.msm.category.repository.CategoryAdventureRepository;
import com.wa.msm.category.repository.CategoryRepository;
import com.wa.msm.category.web.exception.CategoryNotFoundException;
import com.wa.msm.category.web.exception.CategoryNotValidException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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

@Api(description = "API pour les opérations CRUD sur les catégories")
@RestController
public class CategoryController {
    private Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryAdventureRepository categoryAdventureRepository;

    @Autowired
    MSAdventureProxy msAdventureProxy;

    @ApiOperation(value = "Récupère la liste des catégories, s'il y en existe au moins une.")
    @GetMapping(value = "/getAll")
    public List<Category> categoryList() {
        log.info("Tentative de récupération de la liste des catégories");

        List<Category> categories = new ArrayList<>(0);
        categoryRepository.findAll().iterator().forEachRemaining(categories::add);
        if (categories.isEmpty()) {
            String message = "Il n'existe aucune catégories.";
            log.error(message);
            throw new CategoryNotFoundException(message);
        }

        log.info("Liste des catégories récupérée");
        return categories;
    }

    @ApiOperation(value = "Récupère une catégorie selon son ID.")
    @GetMapping(value = "/getOne/{id}")
    public Optional<Category> getCategory(@PathVariable Long id) {
        log.info("Tentative de récupération de la catégorie");

        Optional<Category> category = categoryRepository.findById(id);
        if (!category.isPresent()) {
            String message = "Il n'existe aucune catégorie pour id " + id + ".";
            log.error(message);
            throw new CategoryNotFoundException(message);
        }

        log.info("Catégorie récupérée");
        return category;
    }

    @ApiOperation(value = "Récupère la liste des cinq dernières catégories , s'il en existe au moins une.")
    @GetMapping(value = "/lastFiveCategories")
    public List<Category> getLastFiveCategories() {
        log.info("Début de la méthode : getCategory()");
        List<Category> categories = new ArrayList<>(0);
        categoryRepository.findTop5ByOrderByIdDesc().iterator().forEachRemaining(categories::add);
        if (categories.isEmpty()) throw new CategoryNotFoundException("Il n'existe aucune catégorie ");
        log.info("Récupération de toutes les catégories");
        return categories;
    }

    @ApiOperation(value = "Récupère la liste des catégories d'une aventure, s'il y en existe au moins une.")
    @GetMapping(value = "/adventure/{adventureId}")
    public List<Category> categoryListByAdventure(@PathVariable Long adventureId) {
        log.info("Tentative de récupération de la liste des catégories");

        List<Category> categories = new ArrayList<>(0);
        List<CategoryAdventure> categoriesAdventure = new ArrayList<>(0);
        List <Long> categoriesId = new ArrayList<>(0);

        categoryAdventureRepository.findAllByAdventureId(adventureId).iterator().forEachRemaining(categoriesAdventure::add);
        if (categoriesAdventure.isEmpty()) {
            String message = "Il n'existe aucune catégories.";
            log.error(message);
            throw new CategoryNotFoundException(message);
        }
        categoriesAdventure.iterator().forEachRemaining(categoryAdventure -> categoriesId.add(categoryAdventure.getCategoryId()));
        categoryRepository.findAllByIdIn(categoriesId).iterator().forEachRemaining(categories::add);

        log.info("Récupération des catégories liées à l'aventure d'ID " + adventureId);
        return categories;
    }

    @ApiOperation(value = "Avec ce truc, tu peux ajouter une catégorie si t'es admin. Cool hein ?")
    @PostMapping(value = "/admin")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {

        log.info("Tentative de création de la catégorie");
        validateCategory(category);

        log.info("Catégorie créée");
        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Si tu veux mettre à jour une catégorie en étant admin, c'est ici.")
    @PatchMapping(value = "/admin")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {

        log.info("Tentative de mise à jour de la catégorie");

        if (category.getId() == null || !categoryRepository.findById(category.getId()).isPresent()) {
            String message = "La catégorie envoyée n'existe pas.";
            log.error(message);
            throw new CategoryNotFoundException(message);
        }
        else {
            log.info("Vérifier si la catégorie existe");
            if (!category.getCategoryAdventures().isEmpty())
                category.getCategoryAdventures().forEach(categoryAdventure -> msAdventureProxy.getAdventure(categoryAdventure.getAdventureId()));
        }

        validateCategory(category);

        log.info("Catégorie mise à jour");
        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Si tu veux supprimer l'existence d'une catégorie, c'est à tes risques et périls. En tant qu'admin tu le sais, non ?")
    @DeleteMapping(value = "/admin/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        log.info("Tentative de suppression de la catégorie");

        Optional<Category> categoryToDelete = categoryRepository.findById(id);
        if (!categoryToDelete.isPresent()) {
            String message = "La catégorie correspondante à l'id " + id + " n'existe pas.";
            throw new CategoryNotFoundException(message);
        }
        else categoryRepository.deleteById(categoryToDelete.get().getId());

        String message = "La catégorie pour id " + id + " a bien été supprimé.";
        log.info(message);
        return new ResponseEntity<>(message, HttpStatus.GONE);
    }

    private void validateCategory(Category category) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);

        if(constraintViolations.size() > 0) {
            List<String> violationMessages = new ArrayList<>();
            constraintViolations.iterator().forEachRemaining(constraintViolation ->
                    violationMessages.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage()));

            String message = "La catégorie n'est pas valide. " + StringUtils.join(violationMessages, " ; ");
            log.error(message);
            throw new CategoryNotValidException(message);
        }

        log.info("La catégorie est valide");
    }
}
