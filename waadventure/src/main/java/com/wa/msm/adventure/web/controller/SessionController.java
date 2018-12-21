package com.wa.msm.adventure.web.controller;

import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.entity.Session;
import com.wa.msm.adventure.repository.AdventureRepository;
import com.wa.msm.adventure.repository.SessionRepository;
import com.wa.msm.adventure.web.exception.AdventureNotFoundException;
import com.wa.msm.adventure.web.exception.SessionNotFoundException;
import com.wa.msm.adventure.web.exception.SessionNotValidException;
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

@Api(description = "API pour les opérations CRUD sur les sessions")
@RestController
@RequestMapping(value = "/sessions")
public class SessionController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    AdventureRepository adventureRepository;

    @ApiOperation(value = "Récupère la liste des sessions d'une aventure, s'il y en existe au moins une.")
    @GetMapping(value = "/{adventureId}")
    public List<Session> sessionList(@PathVariable Long adventureId) {
        log.info("Tentative de récupération de la liste des sessions");

        adventureNotFound(adventureId);
        List<Session> sessions = new ArrayList<>(0);
        sessionRepository.findAllByAdventureId(adventureId).forEach(sessions::add);
        if (sessions.isEmpty()) {
            String message = "Il n'existe aucune sessions pour cette aventure";
            log.error(message);
            throw new SessionNotFoundException(message);
        }

        log.info("Liste des sessions récupérée");
        return sessions;
    }

    @ApiOperation(value = "Récupère la liste des sessions selon une liste d'ID, s'il y en existe au moins une.")
    @PostMapping
    public List<Session> getAllById(@RequestBody List<Long> sessionsIdList){
        log.info("Tentative de récupération de la liste des sessions selon la liste d'ID");

        if(sessionsIdList == null || sessionsIdList.isEmpty()) {
            String message = "Aucun id de session transmis";
            log.error(message);
            throw new SessionNotFoundException(message);
        }

        List<Session> sessions = new ArrayList<>(0);
        sessionRepository.findAllByIdIn(sessionsIdList).forEach(sessions::add);
        if (sessions.isEmpty()) {
            String message = "Il n'existe aucune sessions pour cette liste d'id";
            log.error(message);
            throw new SessionNotFoundException(message);
        }

        log.info("Récupération réussie des sessions liées à la liste d'ID");
        return sessions;
    }

    @ApiOperation(value = "Récupère une session selon son ID.")
    @GetMapping(value= "/single/{sessionId}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long sessionId){
        log.info("Tentative de récupération de la session");

        if(sessionId == null){
            String message = "Aucun id de session transmis";
            log.error(message);
            throw new SessionNotFoundException(message);
        }
        Optional <Session> session = sessionRepository.findById(sessionId);
        if(!session.isPresent()){
            String message = "Aucune session de trouver pour cet id : " + sessionId;
            log.error(message);
            throw new SessionNotFoundException(message);
        }

        log.info("Session récupérée");
        return new ResponseEntity<>(session.get(), HttpStatus.OK);
    }

    @ApiOperation(value = "Avec ce truc, tu peux ajouter une session si t'es admin. Cool hein ?")
    @PostMapping(value = "/admin")
    public ResponseEntity<Session> addSession(@RequestBody Session session) {
        log.info("Tentative de création de la session");

        adventureNotFound(session.getAdventureId());
        validateSession(session);

        log.info("Session créée");
        return new ResponseEntity<>(sessionRepository.save(session), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Si tu veux mettre à jour une session en étant admin, c'est ici.")
    @PatchMapping(value = "/admin")
    public ResponseEntity<Session> updateSession(@RequestBody Session session) {
        log.info("Tentative de mise à jour de la session");

        adventureNotFound(session.getAdventureId());
        if (session.getId() == null || !sessionRepository.findById(session.getId()).isPresent()) {
            String message = "La session envoyée n'existe pas";
            log.error(message);
            throw new SessionNotFoundException(message);
        }
        validateSession(session);

        log.info("Session mise à jour");
        return new ResponseEntity<>(sessionRepository.save(session), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Si tu veux supprimer l'existence d'une session, c'est à tes risques et périls. En tant qu'admin tu le sais, non ?")
    @DeleteMapping(value = "/admin/{id}")
    public ResponseEntity<String> deleteSession(@PathVariable Long id) {
        log.info("Tentative de suppression de la session");

        Optional<Session> sessionToDelete = sessionRepository.findById(id);
        if (!sessionToDelete.isPresent()) {
            String message = "La session correspondante à l'id " + id + " n'existe pas";
            log.error(message);
            throw new SessionNotFoundException(message);
        }
        else sessionRepository.deleteById(sessionToDelete.get().getId());

        String message = "La session pour id " + id + " a bien été supprimé";
        log.info(message);
        return new ResponseEntity<>(message, HttpStatus.GONE);
    }

    private void adventureNotFound(Long adventureId) {
        Optional<Adventure> adventure = adventureRepository.findById(adventureId);
        if (!adventure.isPresent()) {
            String message = "Il n'existe aucune aventure pour id " + adventureId + ".";
            log.error(message);
            throw new AdventureNotFoundException(message);
        }
    }

    private void validateSession(Session session) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Session>> constraintViolations = validator.validate(session);

        if(constraintViolations.size() > 0) {
            List<String> violationMessages = new ArrayList<>();
            constraintViolations.iterator().forEachRemaining(constraintViolation ->
                    violationMessages.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage()));

            String message = "La session n'est pas valide. " + StringUtils.join(violationMessages, " ; ");
            log.error(message);
            throw new SessionNotValidException(message);
        }

        log.info("La session est valide");
    }
}
