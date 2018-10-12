package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.AdventureImage;
import com.wa.msm.image.entity.AdventureImageKey;
import com.wa.msm.image.entity.Image;
import com.wa.msm.image.repository.AdventureImageRepository;
import com.wa.msm.image.web.exception.ImageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AdventureImageController extends AbstractImageDependencyController<AdventureImage, AdventureImageKey> {

    @Autowired
    private AdventureImageRepository adventureImageRepository;

    /*@Override
    @PostMapping(value = "/image/adventure/find")
    public Optional<AdventureImage> findById(@RequestBody AdventureImageKey imageId) {
        Optional<AdventureImage> adventureImage = adventureImageRepository.findById(new AdventureImageKey());
        if(!adventureImage.isPresent()) throw new ImageNotFoundException("L'image d'id "+ imageId +" n'existe pas.");
        return adventureImage;
    }*/

    @Override
    @PostMapping(value = "/image/adventure")
    public AdventureImage create(@RequestBody AdventureImage entity) {
        validateImageDependency(entity);
        return adventureImageRepository.save(entity);
    }

    /*@Override
    @PatchMapping(value = "/image/adventure")
    public AdventureImage update(AdventureImage entity) {
        Optional<AdventureImage> adventureImage = adventureImageRepository.findById(new AdventureImageKey(entity.getImageId(), entity.getAdventureId()));
        if(!adventureImage.isPresent()) throw new ImageNotFoundException("L'image d'id "+ entity.getImageId() +" n'existe pas.");
        validateImageDependency(entity);
        return adventureImageRepository.save(entity);
    }*/

    @Override
    @DeleteMapping(value = "/image/adventure")
    public String delete(@RequestBody AdventureImageKey imageId) {
        Optional<AdventureImage> adventureImage = adventureImageRepository.findById(imageId);
        if(!adventureImage.isPresent()) throw new ImageNotFoundException("L'image d'id "+ imageId +" n'existe pas.");
        else adventureImageRepository.deleteById(imageId);
        return "L'image d'id : "+imageId+" a bien été supprimée";
    }

    @Override
    @PostMapping(value = "/image/adventure/exist")
    public Boolean imageExist(@RequestBody AdventureImageKey imageId) {
        Optional<AdventureImage> adventureImage = adventureImageRepository.findById(new AdventureImageKey());
        return adventureImage.isPresent();
    }

    @GetMapping(value = "/image/adventure/{adventureId}")
    public List<Image> findImagesByAdventure(@PathVariable Long adventureId){
        List<Long> imagesId = new ArrayList<>();
        List<Image> images= null;
        //TODO Vérifier que l'aventure existe
        List <AdventureImage> adventureImages = adventureImageRepository.findAdventureImagesByAdventureId(adventureId);
        if(adventureImages.size()>0){
            adventureImages.forEach(itemAdventureImage -> imagesId.add(itemAdventureImage.getImageId()));
            images = getImageRepository().findByIdIn(imagesId);
        }
        return images;
    }
}
