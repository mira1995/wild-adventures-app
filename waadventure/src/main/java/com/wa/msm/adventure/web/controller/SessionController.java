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

    @GetMapping(value = "/sessions")
    public List<Session> sessionList() {
        // TODO : Vérifier si l'aventure existe
        List<Session> sessions = new ArrayList<>(0);
        sessionRepository.findAll().iterator().forEachRemaining(sessions::add);
        if (sessions.isEmpty()) throw new SessionNotFoundException("Il n'existe aucune sessions.");
        return sessions;
    }

    @PostMapping(value = "/session")
    public Session addSession(@RequestBody Session session) {
        // TODO : Vérifier si l'aventure existe
        Optional<Adventure> adventure = adventureRepository.findById(session.getAdventureId());
        if (!adventure.isPresent()) throw new AdventureNotFoundException("Il n'existe aucune aventure pour id " + session.getAdventureId() + ".");
        return sessionRepository.save(session);
    }

    @PatchMapping(value = "/session")
    public Session updateSession(@RequestBody Session session) {
        if (session == null) throw new SessionNotFoundException("La session envoyée n'existe pas.");
        return sessionRepository.save(session);
    }

    @DeleteMapping(value = "/session/{id}")
    public String deleteSession(@PathVariable Long id) {
        Optional<Session> sessionToDelete = sessionRepository.findById(id);
        if (!sessionToDelete.isPresent()) throw new SessionNotFoundException("La session correspondante à l'id " + id + " n'existe pas.");
        else sessionRepository.deleteById(sessionToDelete.get().getId());
        return "La session pour id " + id + " a bien été supprimé.";
    }
}
