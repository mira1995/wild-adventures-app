package com.wa.msm.adventure.web.controller;

import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.entity.Session;
import com.wa.msm.adventure.repository.AdventureRepository;
import com.wa.msm.adventure.repository.SessionRepository;
import com.wa.msm.adventure.web.exception.AdventureNotFoundException;
import com.wa.msm.adventure.web.exception.SessionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Session addSession(@RequestBody Session session) {
        // TODO : ResponseEntity
        adventureNotFound(session.getAdventureId());
        return sessionRepository.save(session);
    }

    @PatchMapping(value = "/session")
    public Session updateSession(@RequestBody Session session) {
        adventureNotFound(session.getAdventureId());
        if (session.getId() == null || !sessionRepository.findById(session.getId()).isPresent())
            throw new SessionNotFoundException("La session envoyée n'existe pas.");
        return sessionRepository.save(session);
    }

    @DeleteMapping(value = "/session/{id}")
    public String deleteSession(@PathVariable Long id) {
        Optional<Session> sessionToDelete = sessionRepository.findById(id);
        if (!sessionToDelete.isPresent()) throw new SessionNotFoundException("La session correspondante à l'id " + id + " n'existe pas.");
        else sessionRepository.deleteById(sessionToDelete.get().getId());
        return "La session pour id " + id + " a bien été supprimé.";
    }

    // Vérifier si l'aventure existe
    private void adventureNotFound(Long adventureId) {
        Optional<Adventure> adventure = adventureRepository.findById(adventureId);
        if (!adventure.isPresent())
            throw new AdventureNotFoundException("Il n'existe aucune aventure pour id " + adventureId + ".");
    }
}
