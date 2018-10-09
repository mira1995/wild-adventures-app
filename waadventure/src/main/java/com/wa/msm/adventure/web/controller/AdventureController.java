package com.wa.msm.adventure.web.controller;

import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.repository.AdventureRepository;
import com.wa.msm.adventure.web.exception.AdventureNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AdventureController {

    @Autowired
    AdventureRepository adventureRepository;

    @GetMapping(value = "/adventures")
    public List<Adventure> adventureList() {
        List<Adventure> adventures = new ArrayList<>(0);
        adventureRepository.findAll().iterator().forEachRemaining(adventures::add);
        if (adventures.isEmpty()) throw new AdventureNotFoundException("Il n'existe aucune aventures.");
        return adventures;
    }

    @GetMapping(value = "/adventure/{id}")
    public Optional<Adventure> getAdventure(@PathVariable Long id) {
        Optional<Adventure> adventure = adventureRepository.findById(id);
        if (!adventure.isPresent()) throw new AdventureNotFoundException("Il n'existe aucune aventure pour id " + id + ".");
        return adventure;
    }

    @PostMapping(value = "/adventure")
    public Adventure addAdventure(@RequestBody Adventure adventure) {
        return adventureRepository.save(adventure);
    }

    @PatchMapping(value = "/adventure")
    public Adventure updateAdventure(@RequestBody Adventure adventure) {
        if (adventure == null) throw new AdventureNotFoundException("L'aventure envoyée n'existe pas.");
        return adventureRepository.save(adventure);
    }

    @DeleteMapping(value = "/adventure/{id}")
    public String deleteAdventure(@PathVariable Long id) {
        Optional<Adventure> adventureToDelete = adventureRepository.findById(id);
        if (!adventureToDelete.isPresent()) throw new AdventureNotFoundException("L'aventure correspondante à l'id " + id + " n'existe pas.");
        else adventureRepository.deleteById(adventureToDelete.get().getId());
        return "L'aventure pour id " + id + " a bien été supprimé.";
    }
}
