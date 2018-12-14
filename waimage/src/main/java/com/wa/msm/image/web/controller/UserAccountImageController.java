package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.UserAccountImage;
import com.wa.msm.image.web.exception.ImageNotFoundException;
import com.wa.msm.image.repository.UserAccountImageRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(description = "API pour les opérations CRUD sur les images de profil")
@RestController
@RequestMapping(value = "/user")
public class UserAccountImageController extends AbstractImageController<UserAccountImage, Long> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserAccountImageRepository userAccountImageRepository;

    @Override
    @ApiOperation(value = "Créé une image de profil (réservée aux utilisateurs)")
    @PostMapping
    public ResponseEntity<UserAccountImage> create(@RequestBody UserAccountImage userAccountImage){
        log.info("Début méthode : create()");
        validateImage(userAccountImage);
        log.info("Création d'une image de profil");
        return new ResponseEntity<>(userAccountImageRepository.save(userAccountImage), HttpStatus.CREATED);
    }

    @Override
    @ApiOperation(value = "Met à jour une image de profil (réservée aux utilisateurs)")
    @PatchMapping
    public ResponseEntity<UserAccountImage> update(@RequestBody UserAccountImage userAccountImage){
        log.info("Début méthode : update()");
        if(userAccountImage == null || userAccountImage.getId() == null) {
            log.error("L'image fournie n'existe pas");
            throw new ImageNotFoundException("L'image fournie n'existe pas");
        }else{
            Optional<UserAccountImage> dbUserAccountImage = userAccountImageRepository.findById(userAccountImage.getId());
            if(!dbUserAccountImage.isPresent()) {
                log.error("L'image fournie n'existe pas en Base");
                throw new ImageNotFoundException("L'image fournie n'existe pas en Base");
            }
        }
        validateImage(userAccountImage);
        log.info("Mise à jour  de l'image de profil d'id : "+userAccountImage.getId());
        return new ResponseEntity<>(userAccountImageRepository.save(userAccountImage), HttpStatus.CREATED);
    }

    @Override
    @ApiOperation(value = "Supprime une image de profil (réservée aux utilisateurs)")
    @DeleteMapping(value = "/{imageId}")
    public ResponseEntity<String> delete(@PathVariable Long imageId){
        log.info("Début méthode : delete()");
        Optional<UserAccountImage> dbUserAccountImage = userAccountImageRepository.findById(imageId);
        if(!dbUserAccountImage.isPresent()) {
            log.error("L'image fournie n'existe pas en Base");
            throw new ImageNotFoundException("L'image fournie n'existe pas en Base");
        }
        else {
            log.info("Suppression de l'image de profil d'id : "+imageId);
            userAccountImageRepository.deleteById(imageId);
        }
        return new ResponseEntity<>("L'image d'id : "+imageId+" a bien été supprimée", HttpStatus.GONE);
    }

    @Override
    @ApiOperation(value = "Récupère une image de profil par son id (réservée aux utilisateurs)")
    @GetMapping(value = "/{imageId}")
    public Optional<UserAccountImage> findById(@PathVariable Long imageId){
        log.info("Début méthode : findById()");
        Optional<UserAccountImage> userAccount = userAccountImageRepository.findById(imageId);
        if(!userAccount.isPresent()) {
            log.error("L'image d'id "+ imageId +" n'existe pas.");
            throw new ImageNotFoundException("L'image d'id "+ imageId +" n'existe pas.");
        }
        log.info("Récupération de l'image de profil d'id : "+imageId);
        return userAccount;
    }

    @Override
    @ApiOperation(value = "Vérifie l'existance d'une image de profil par son id (réservée aux utilisateurs)")
    @GetMapping(value = "/exist/{imageId}")
    public Boolean imageExist(@PathVariable Long imageId){
        log.info("Début méthode : imageExist()");
        Optional<UserAccountImage> userAccount = userAccountImageRepository.findById(imageId);
        log.info("Vérification de l'existance de l'image de profil d'id : "+imageId);
        return userAccount.isPresent();
    }
}
