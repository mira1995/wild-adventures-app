package com.wa.msm.adventure.web.controller;

import com.sun.deploy.util.StringUtils;
import com.wa.msm.adventure.bean.CategoryBean;
import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.proxy.MSCategoryProxy;
import com.wa.msm.adventure.repository.AdventureRepository;
import com.wa.msm.adventure.repository.SessionRepository;
import com.wa.msm.adventure.web.exception.AdventureNotFoundException;
import com.wa.msm.adventure.web.exception.AdventureNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
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
public class AdventureController implements HealthIndicator {

    private Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    AdventureRepository adventureRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    MSCategoryProxy msCategoryProxy;

    @Override
    public Health health() {
        List<Adventure> adventures = adventureRepository.findAll();

        if(adventures.isEmpty()){
            return Health.down().build();
        }

        return Health.up().build();
    }

    @GetMapping
    public List<Adventure> adventureList() {
        List<Adventure> adventures = new ArrayList<>(0);
        adventureRepository.findAll().iterator().forEachRemaining(adventures::add);
        if (adventures.isEmpty()) throw new AdventureNotFoundException("Il n'existe aucune aventures.");

        log.info("Récupération de la liste des aventures");

        return adventures;
    }

    @GetMapping(value = "/category/{categoryId}")
    public List<Adventure> adventureList(@PathVariable Long categoryId) {
        List<Adventure> adventures = new ArrayList<>(0);

        // Vérifier si la catégorie existe
        Optional<CategoryBean> category = msCategoryProxy.getCategory(categoryId);
        category.ifPresent(categoryBean ->
                categoryBean.getCategoryAdventures().forEach(categoryAdventureBean ->
                        adventureRepository.findById(categoryAdventureBean.getAdventureId()).ifPresent(adventures::add)));
        if (adventures.isEmpty()) throw new AdventureNotFoundException("Il n'existe aucune aventures.");
        log.info("Récupération de l'aventure lié à la catégorie d'id "+categoryId);
        return adventures;
    }

    @GetMapping(value = "/{id}")
    public Optional<Adventure> getAdventure(@PathVariable Long id) {
        Optional<Adventure> adventure = adventureRepository.findById(id);
        if (!adventure.isPresent()) throw new AdventureNotFoundException("Il n'existe aucune aventure pour id " + id + ".");
        return adventure;
    }

    @PostMapping(value = "/admin/{categoryId}")
    public ResponseEntity<Adventure> addAdventure(@RequestBody Adventure adventure, @PathVariable Long categoryId) {
        Adventure newAdventure = new Adventure();
        // Vérifier si la catégorie existe
        Optional<CategoryBean> category = msCategoryProxy.getCategory(categoryId);
        if (category.isPresent()) {
            validateAdventure(adventure);
            newAdventure = adventureRepository.save(adventure);
            // Pour le client
            /*List<CategoryAdventureBean> categoryAdventures = category.get().getCategoryAdventures();
            categoryAdventures.add(new CategoryAdventureBean(categoryId, newAdventure.getId()));
            category.get().setCategoryAdventures(categoryAdventures);
            msCategoryProxy.updateCategory(category.get());*/
        }
        log.info("Création d'une aventure");
        return new ResponseEntity<>(newAdventure, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/admin")
    public ResponseEntity<Adventure> updateAdventure(@RequestBody Adventure adventure) {
        if (adventure == null || !adventureRepository.findById(adventure.getId()).isPresent())
            throw new AdventureNotFoundException("L'aventure envoyée n'existe pas.");
        validateAdventure(adventure);
        return new ResponseEntity<>(adventureRepository.save(adventure), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/admin/{id}")
    public ResponseEntity<String> deleteAdventure(@PathVariable Long id) {
        Optional<Adventure> adventureToDelete = adventureRepository.findById(id);
        if (!adventureToDelete.isPresent()) throw new AdventureNotFoundException("L'aventure correspondante à l'id " + id + " n'existe pas.");
        else {
            // Pour le client
            // msCommentProxy.deleteCommentByAdventureId(adventureToDelete.get().getId());
            adventureRepository.deleteById(adventureToDelete.get().getId());
        }
        return new ResponseEntity<>("L'aventure pour id " + id + " a bien été supprimé.", HttpStatus.GONE);
    }

    private void validateAdventure(Adventure adventure) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Adventure>> constraintViolations = validator.validate(adventure);

        if(constraintViolations.size() > 0) {
            List<String> violationMessages = new ArrayList<>();
            constraintViolations.iterator().forEachRemaining(constraintViolation ->
                    violationMessages.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage()));

            throw new AdventureNotValidException("L'aventure n'est pas valide. " + StringUtils.join(violationMessages, " ; "));
        }
    }
}
