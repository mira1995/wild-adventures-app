package com.wa.msm.adventure.web.controller;

import com.sun.deploy.util.StringUtils;
import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.entity.Session;
import com.wa.msm.adventure.repository.AdventureRepository;
import com.wa.msm.adventure.repository.SessionRepository;
import com.wa.msm.adventure.web.exception.AdventureNotFoundException;
import com.wa.msm.adventure.web.exception.SessionNotFoundException;
import com.wa.msm.adventure.web.exception.SessionNotValidException;
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
public class SessionController {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    AdventureRepository adventureRepository;

    @GetMapping(value = "/sessions/{adventureId}")
    public List<Session> sessionList(@PathVariable Long adventureId) {
        adventureNotFound(adventureId);
        List<Session> sessions = new ArrayList<>(0);
        sessionRepository.findAllByAdventureId(adventureId).forEach(sessions::add);
        if (sessions.isEmpty()) throw new SessionNotFoundException("Il n'existe aucune sessions pour cette aventure.");
        return sessions;
    }

    @PostMapping(value = "/sessions")
    public List<Session> getAllById (@RequestBody List<Long> sessionsIdList){
        if(sessionsIdList == null || sessionsIdList.isEmpty())throw new SessionNotFoundException("Aucun id de session transmis");
        List<Session> sessions = new ArrayList<>(0);
        sessionRepository.findAllByIdIn(sessionsIdList).forEach(sessions::add);
        if (sessions.isEmpty()) throw new SessionNotFoundException("Il n'existe aucune sessions pour cette liste d'id.");
        return sessions;
    }

    @PostMapping(value = "/session")
    public ResponseEntity<Session> addSession(@RequestBody Session session) {
        adventureNotFound(session.getAdventureId());
        validateSession(session);
        return new ResponseEntity<>(sessionRepository.save(session), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/session")
    public ResponseEntity<Session> updateSession(@RequestBody Session session) {
        adventureNotFound(session.getAdventureId());
        if (session.getId() == null || !sessionRepository.findById(session.getId()).isPresent())
            throw new SessionNotFoundException("La session envoyée n'existe pas.");
        validateSession(session);
        return new ResponseEntity<>(sessionRepository.save(session), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/session/{id}")
    public ResponseEntity<String> deleteSession(@PathVariable Long id) {
        Optional<Session> sessionToDelete = sessionRepository.findById(id);
        if (!sessionToDelete.isPresent()) throw new SessionNotFoundException("La session correspondante à l'id " + id + " n'existe pas.");
        else sessionRepository.deleteById(sessionToDelete.get().getId());
        return new ResponseEntity<>("La session pour id " + id + " a bien été supprimé.", HttpStatus.GONE);
    }

    // Vérifier si l'aventure existe
    private void adventureNotFound(Long adventureId) {
        Optional<Adventure> adventure = adventureRepository.findById(adventureId);
        if (!adventure.isPresent())
            throw new AdventureNotFoundException("Il n'existe aucune aventure pour id " + adventureId + ".");
    }

    private void validateSession(Session session) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Session>> constraintViolations = validator.validate(session);

        if(constraintViolations.size() > 0) {
            List<String> violationMessages = new ArrayList<>();
            constraintViolations.iterator().forEachRemaining(constraintViolation ->
                    violationMessages.add(constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage()));

            throw new SessionNotValidException("La session n'est pas valide. " + StringUtils.join(violationMessages, " ; "));
        }
    }
}
