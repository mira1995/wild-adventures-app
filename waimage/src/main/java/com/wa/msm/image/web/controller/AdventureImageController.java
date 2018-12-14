package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.AdventureImage;
import com.wa.msm.image.entity.AdventureImageKey;
import com.wa.msm.image.entity.Image;
import com.wa.msm.image.proxy.MSAdventureProxy;
import com.wa.msm.image.repository.AdventureImageRepository;
import com.wa.msm.image.web.exception.AdventureNotFoundException;
import com.wa.msm.image.web.exception.CategoryNotFoundException;
import com.wa.msm.image.web.exception.ImageNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Api(description = "API pour les opérations CRUD sur les images liés à des aventures")
@RestController
@RequestMapping(value = "/adventure")
public class AdventureImageController extends AbstractImageDependencyController<AdventureImage, AdventureImageKey> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

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
    @ApiOperation(value = "Créé le lien d'une image avec une aventure (réservé aux admins)")
    @PostMapping(value = "/admin")
    public ResponseEntity<AdventureImage> create(@RequestBody AdventureImage entity) {
        log.info("Début méthode : create()");
        validateImageDependency(entity);
        validateAdventure(entity.getAdventureId());
        log.info("Création du lien entre l'aventure d'id : "+entity.getAdventureId() +" et l'image d'id : "+entity.getImageId());
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
    @ApiOperation(value = "Supprime le lien d'une image avec une aventure (réservé aux admins)")
    @DeleteMapping(value = "/admin")
    public ResponseEntity<String> delete(@RequestBody AdventureImageKey imageId) {
        log.info("Début méthode : delete()");
        Optional<AdventureImage> adventureImage = adventureImageRepository.findById(imageId);
        if(!adventureImage.isPresent()) {
            log.error("L'image d'id "+ imageId +" n'existe pas.");
            throw new ImageNotFoundException("L'image d'id "+ imageId +" n'existe pas.");
        }
        else {
            log.info("Suppression du lien entre l'aventure d'id : "+imageId.getAdventureId() +" et l'image d'id : "+imageId.getImageId());
            adventureImageRepository.deleteById(imageId);
        }
        return new ResponseEntity<>("L'image d'id : "+imageId+" a bien été supprimée", HttpStatus.GONE);
    }

    @Override
    @ApiOperation(value = "Vérifie l'existance d'un lien entre une image et une aventure")
    @PostMapping(value = "/exist")
    public Boolean imageExist(@RequestBody AdventureImageKey imageId) {
        log.info("Début méthode : imageExist()");
        Optional<AdventureImage> adventureImage = adventureImageRepository.findById(imageId);
        log.info("Vérification de l'existance du lien entre l'aventure d'id : "+imageId.getAdventureId() +" et l'image d'id : "+imageId.getImageId());
        return adventureImage.isPresent();
    }

    @ApiOperation(value = "Récupère  les images liées à une aventure")
    @GetMapping(value = "/{adventureId}")
    public List<Image> findImagesByAdventure(@PathVariable Long adventureId){
        log.info("Début méthode : findImagesByAdventure()");
        List<Long> imagesId = new ArrayList<>();
        List<Image> images= null;
        validateAdventure(adventureId);
        List <AdventureImage> adventureImages = adventureImageRepository.findAdventureImagesByAdventureId(adventureId);
        if(adventureImages.size()>0){

            adventureImages.forEach(itemAdventureImage -> imagesId.add(itemAdventureImage.getImageId()));
            images = getImageRepository().findByIdIn(imagesId);
        }else {
            log.error("Aucune image ne correspond à l'aventure d'id : "+adventureId);
        }
        log.info("Récupération des images correspondant à l'aventure d'id : "+adventureId);
        return images;
    }

    private void validateAdventure(Long adventureId){
        if(!msAdventureProxy.getAdventure(adventureId).isPresent()) throw new AdventureNotFoundException("L'aventure d'id "+ adventureId + " n'existe pas");
    }
}
