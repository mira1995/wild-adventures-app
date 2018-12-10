package com.wa.msm.adventure.web.controller;

import com.sun.deploy.util.StringUtils;
import com.wa.msm.adventure.bean.CategoryBean;
import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.proxy.MSCategoryProxy;
import com.wa.msm.adventure.repository.AdventureRepository;
import com.wa.msm.adventure.repository.SessionRepository;
import com.wa.msm.adventure.web.exception.AdventureNotFoundException;
import com.wa.msm.adventure.web.exception.AdventureNotValidException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api(description = "API pour les opérations CRUD sur les aventures")
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

    @ApiOperation(value = "Récupère la liste des aventures, s'il y en existe au moins une.")
    @GetMapping(value = "/getAll")
    public List<Adventure> adventureList() {
        log.info("Tentative de récupération de la liste des aventures");

        List<Adventure> adventures = new ArrayList<>(0);
        adventureRepository.findAll().iterator().forEachRemaining(adventures::add);
        if (adventures.isEmpty()) {
            String message = "Il n'existe aucune aventures.";
            log.error(message);
            throw new AdventureNotFoundException(message);
        }

        log.info("Liste des aventures récupérée");
        return adventures;
    }

    @ApiOperation(value = "Récupère la liste des aventures d'une catégorie, s'il y en existe au moins une.")
    @GetMapping(value = "/category/{categoryId}")
    public List<Adventure> adventureList(@PathVariable Long categoryId) {
        log.info("Tentative de récupération de la liste des aventures");

        List<Adventure> adventures = new ArrayList<>(0);

        log.info("Vérifier si la catégorie existe");
        Optional<CategoryBean> category = msCategoryProxy.getCategory(categoryId);
        category.ifPresent(categoryBean ->
                categoryBean.getCategoryAdventures().forEach(categoryAdventureBean ->
                        adventureRepository.findById(categoryAdventureBean.getAdventureId()).ifPresent(adventures::add)));
        if (adventures.isEmpty()) {
            String message = "Il n'existe aucune aventures.";
            log.error(message);
            throw new AdventureNotFoundException(message);
        }

        log.info("Récupération des aventures liées à la catégorie d'ID " + categoryId);
        return adventures;
    }

    @ApiOperation(value = "Récupère une aventure selon son ID.")
    @GetMapping(value = "/getOne/{id}")
    public Optional<Adventure> getAdventure(@PathVariable Long id) {
        log.info("Tentative de récupération de l'aventure");

        Optional<Adventure> adventure = adventureRepository.findById(id);
        if (!adventure.isPresent()) {
            String message = "Il n'existe aucune aventure pour id " + id;
            log.error(message);
            throw new AdventureNotFoundException(message);
        }

        log.info("Aventure récupérée");
        return adventure;
    }

    @ApiOperation(value = "Avec ce truc, tu peux ajouter une aventure si t'es admin. Cool hein ?")
    @PostMapping(value = "/admin/{categoryId}")
    public ResponseEntity<Adventure> addAdventure(@RequestBody Adventure adventure, @PathVariable Long categoryId) {
        log.info("Tentative de création de l'aventure");

        Adventure newAdventure = new Adventure();

        log.info("Vérifier si la catégorie existe");
        Optional<CategoryBean> category = msCategoryProxy.getCategory(categoryId);
        if (category.isPresent()) {
            validateAdventure(adventure);
            newAdventure = adventureRepository.save(adventure);
        }

        log.info("Aventure créée");
        return new ResponseEntity<>(newAdventure, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Si tu veux mettre à jour une aventure en étant admin, c'est ici.")
    @PatchMapping(value = "/admin")
    public ResponseEntity<Adventure> updateAdventure(@RequestBody Adventure adventure) {
        log.info("Tentative de mise à jour de l'aventure");

        if (adventure == null || !adventureRepository.findById(adventure.getId()).isPresent()) {
            String message = "L'aventure envoyée n'existe pas";
            log.error(message);
            throw new AdventureNotFoundException(message);
        }
        validateAdventure(adventure);

        log.info("Aventure mise à jour");
        return new ResponseEntity<>(adventureRepository.save(adventure), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Si tu veux supprimer l'existence d'une aventure, c'est à tes risques et périls. En tant qu'admin tu le sais, non ?")
    @DeleteMapping(value = "/admin/{id}")
    public ResponseEntity<String> deleteAdventure(@PathVariable Long id) {
        log.info("Tentative de suppression de l'aventure");

        Optional<Adventure> adventureToDelete = adventureRepository.findById(id);
        if (!adventureToDelete.isPresent()) {
            String message = "L'aventure correspondante à l'id " + id + " n'existe pas.";
            log.error(message);
            throw new AdventureNotFoundException(message);
        }
        else {
            adventureRepository.deleteById(adventureToDelete.get().getId());
        }

        String message = "L'aventure pour id \" + id + \" a bien été supprimé.";
        log.info(message);
        return new ResponseEntity<>(message, HttpStatus.GONE);
    }

    private void validateAdventure(Adventure adventure) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Adventure>> constraintViolations = validator.validate(adventure);

        if(constraintViolations.size() > 0) {
            List<String> violationMessages = new ArrayList<>();
            constraintViolations.iterator().forEachRemaining(constraintViolation ->
                    violationMessages.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage()));

            String message = "L'aventure n'est pas valide. " + StringUtils.join(violationMessages, " ; ");
            log.error(message);
            throw new AdventureNotValidException(message);
        }

        log.info("L'aventure est valide");
    }
}
