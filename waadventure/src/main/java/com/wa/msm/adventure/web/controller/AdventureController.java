package com.wa.msm.adventure.web.controller;

import com.wa.msm.adventure.bean.CategoryBean;
import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.proxy.MSCategoryProxy;
import com.wa.msm.adventure.repository.AdventureRepository;
import com.wa.msm.adventure.repository.SessionRepository;
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

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    MSCategoryProxy msCategoryProxy;

    @GetMapping(value = "/adventures")
    public List<Adventure> adventureList() {
        List<Adventure> adventures = new ArrayList<>(0);
        adventureRepository.findAll().iterator().forEachRemaining(adventures::add);
        if (adventures.isEmpty()) throw new AdventureNotFoundException("Il n'existe aucune aventures.");
        return adventures;
    }

    @GetMapping(value = "/adventures/{categoryId}")
    public List<Adventure> adventureList(@PathVariable Long categoryId) {
        List<Adventure> adventures = new ArrayList<>(0);

        // Vérifier si la catégorie existe
        Optional<CategoryBean> category = msCategoryProxy.getCategory(categoryId);
        category.ifPresent(categoryBean ->
                categoryBean.getCategoryAdventures().forEach(categoryAdventureBean ->
                        adventureRepository.findById(categoryAdventureBean.getAdventureId()).ifPresent(adventures::add)));
        if (adventures.isEmpty()) throw new AdventureNotFoundException("Il n'existe aucune aventures.");
        return adventures;
    }

    @GetMapping(value = "/adventure/{id}")
    public Optional<Adventure> getAdventure(@PathVariable Long id) {
        Optional<Adventure> adventure = adventureRepository.findById(id);
        if (!adventure.isPresent()) throw new AdventureNotFoundException("Il n'existe aucune aventure pour id " + id + ".");
        return adventure;
    }

    @PostMapping(value = "/adventure/{categoryId}")
    public Adventure addAdventure(@RequestBody Adventure adventure, @PathVariable Long categoryId) {
        // TODO : ResponseEntity
        Adventure newAdventure = new Adventure();
        // Vérifier si la catégorie existe
        Optional<CategoryBean> category = msCategoryProxy.getCategory(categoryId);
        if (category.isPresent()) {
            newAdventure = adventureRepository.save(adventure);
            // Pour le client
            /*List<CategoryAdventureBean> categoryAdventures = category.get().getCategoryAdventures();
            categoryAdventures.add(new CategoryAdventureBean(categoryId, newAdventure.getId()));
            category.get().setCategoryAdventures(categoryAdventures);
            msCategoryProxy.updateCategory(category.get());*/
        }
        return newAdventure;
    }

    @PatchMapping(value = "/adventure")
    public Adventure updateAdventure(@RequestBody Adventure adventure) {
        if (adventure == null || !adventureRepository.findById(adventure.getId()).isPresent())
            throw new AdventureNotFoundException("L'aventure envoyée n'existe pas.");
        return adventureRepository.save(adventure);
    }

    @DeleteMapping(value = "/adventure/{id}")
    public String deleteAdventure(@PathVariable Long id) {
        Optional<Adventure> adventureToDelete = adventureRepository.findById(id);
        if (!adventureToDelete.isPresent()) throw new AdventureNotFoundException("L'aventure correspondante à l'id " + id + " n'existe pas.");
        else {
            // Pour le client
            // msCommentProxy.deleteCommentByAdventureId(adventureToDelete.get().getId());
            adventureRepository.deleteById(adventureToDelete.get().getId());
        }
        return "L'aventure pour id " + id + " a bien été supprimé.";
    }
}
