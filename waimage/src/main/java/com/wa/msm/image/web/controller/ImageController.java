package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.Image;
import com.wa.msm.image.web.exception.ImageNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(description = "API pour les opérations CRUD sur les images")
@RestController
@RequestMapping(value = "/api")
public class ImageController extends AbstractImageController<Image, Long> {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    @ApiOperation(value = "Créé une image (disponible à tous les utilisateurs)")
    @PostMapping(value = "/admin")
    public ResponseEntity<Image> create(@RequestBody Image image){
        log.info("Début méthode : create()");
        validateImage(image);
        log.info("Création d'une image");
        return new ResponseEntity<>(getImageRepository().save(image), HttpStatus.CREATED);
    }

    @Override
    @ApiOperation(value = "Met à jour une image (réservé aux admins)")
    @PatchMapping(value = "/admin")
    public ResponseEntity<Image> update(@RequestBody Image image){
        log.info("Début méthode : update()");
        if(image == null || image.getId() == null) {
            log.error(("L'image fournie est nulle"));
            throw new ImageNotFoundException("L'image fournie est nulle");
        }else{
            log.info("Vérification de l'existance de l'image fournie ");
            Optional<Image> dbImage = getImageRepository().findById(image.getId());
            if(!dbImage.isPresent()) {
                log.error(("Erreur : L'image d'id "+ image.getId() +" n'existe pas."));
                throw new ImageNotFoundException("L'image fournie n'existe pas en Base");
            }
        }

        validateImage(image);
        log.info("Image fournie valide, Mise à jour de l'image d'id : "+image.getId());
        return new ResponseEntity<>(getImageRepository().save(image), HttpStatus.CREATED);
    }

    @Override
    @ApiOperation(value = "Supprime une image (réservé aux admins)")
    @DeleteMapping(value = "/admin/{imageId}")
    public ResponseEntity<String> delete(@PathVariable Long imageId){
        log.info("Début méthode : delete()");
        Optional<Image> dbImage = getImageRepository().findById(imageId);
        if(!dbImage.isPresent()) {
            log.error(("Erreur : L'image d'id "+ imageId +" n'existe pas."));
            throw new ImageNotFoundException("L'image fournie n'existe pas en Base");
        }
        else {
            log.info("Id de l'image fourni valide, Suppression de l'image d'id : "+imageId);
            getImageRepository().deleteById(imageId);
        }
        return new ResponseEntity<>("L'image d'id : "+imageId+" a bien été supprimée", HttpStatus.GONE);
    }

    @Override
    @ApiOperation(value = "Récupère une image en fonction de son id (disponible à tous les utilisateurs)")
    @GetMapping(value = "/{imageId}")
    public Optional<Image> findById(@PathVariable Long imageId){
        log.info("Début méthode : findById()");
        Optional<Image> image = getImageRepository().findById(imageId);
        if(!image.isPresent()) {
            log.error(("Erreur : L'image d'id "+ imageId +" n'existe pas."));
            throw new ImageNotFoundException("L'image d'id "+ imageId +" n'existe pas.");
        }
        log.info("Récupération de l'image d'id :"+imageId);
        return image;
    }

    @Override
    @ApiOperation(value = "Vérifie l'existance d'une image par son id (disponible à tous les utilisateurs)")
    @GetMapping(value = "/exist/{imageId}")
    public Boolean imageExist(@PathVariable Long imageId){
        log.info("Début méthode : imageExist()");
        Optional<Image> userAccount = getImageRepository().findById(imageId);
        log.info("Vérification de l'existance de l'image d'id : "+imageId);
        return userAccount.isPresent();
    }
}
