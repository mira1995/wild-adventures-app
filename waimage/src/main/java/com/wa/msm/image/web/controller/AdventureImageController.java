package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.AdventureImage;
import com.wa.msm.image.entity.AdventureImageKey;
import com.wa.msm.image.entity.Image;
import com.wa.msm.image.proxy.MSAdventureProxy;
import com.wa.msm.image.repository.AdventureImageRepository;
import com.wa.msm.image.web.exception.AdventureNotFoundException;
import com.wa.msm.image.web.exception.CategoryNotFoundException;
import com.wa.msm.image.web.exception.ImageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/adventure")
public class AdventureImageController extends AbstractImageDependencyController<AdventureImage, AdventureImageKey> {

    @Autowired
    private AdventureImageRepository adventureImageRepository;

    @Autowired
    private MSAdventureProxy msAdventureProxy;

    /*@Override
    @PostMapping(value = "/find")
    public Optional<AdventureImage> findById(@RequestBody AdventureImageKey imageId) {
        Optional<AdventureImage> adventureImage = adventureImageRepository.findById(new AdventureImageKey());
        if(!adventureImage.isPresent()) throw new ImageNotFoundException("L'image d'id "+ imageId +" n'existe pas.");
        return adventureImage;
    }*/

    @Override
    @PostMapping(value = "/admin")
    public ResponseEntity<AdventureImage> create(@RequestBody AdventureImage entity) {
        validateImageDependency(entity);
        validateAdventure(entity.getAdventureId());
        return new ResponseEntity<>(adventureImageRepository.save(entity), HttpStatus.CREATED);
    }

    /*@Override
    @PatchMapping(value = "/admin")
    public AdventureImage update(AdventureImage entity) {
        Optional<AdventureImage> adventureImage = adventureImageRepository.findById(new AdventureImageKey(entity.getImageId(), entity.getAdventureId()));
        if(!adventureImage.isPresent()) throw new ImageNotFoundException("L'image d'id "+ entity.getImageId() +" n'existe pas.");
        validateImageDependency(entity);
        return adventureImageRepository.save(entity);
    }*/

    @Override
    @DeleteMapping(value = "/admin")
    public ResponseEntity<String> delete(@RequestBody AdventureImageKey imageId) {
        Optional<AdventureImage> adventureImage = adventureImageRepository.findById(imageId);
        if(!adventureImage.isPresent()) throw new ImageNotFoundException("L'image d'id "+ imageId +" n'existe pas.");
        else adventureImageRepository.deleteById(imageId);
        return new ResponseEntity<>("L'image d'id : "+imageId+" a bien été supprimée", HttpStatus.GONE);
    }

    @Override
    @PostMapping(value = "/exist")
    public Boolean imageExist(@RequestBody AdventureImageKey imageId) {
        Optional<AdventureImage> adventureImage = adventureImageRepository.findById(imageId);
        return adventureImage.isPresent();
    }

    @GetMapping(value = "/{adventureId}")
    public List<Image> findImagesByAdventure(@PathVariable Long adventureId){
        List<Long> imagesId = new ArrayList<>();
        List<Image> images= null;
        validateAdventure(adventureId);
        List <AdventureImage> adventureImages = adventureImageRepository.findAdventureImagesByAdventureId(adventureId);
        if(adventureImages.size()>0){
            adventureImages.forEach(itemAdventureImage -> imagesId.add(itemAdventureImage.getImageId()));
            images = getImageRepository().findByIdIn(imagesId);
        }
        return images;
    }

    private void validateAdventure(Long adventureId){
        if(!msAdventureProxy.getAdventure(adventureId).isPresent()) throw new AdventureNotFoundException("L'aventure d'id "+ adventureId + " n'existe pas");
    }
}
