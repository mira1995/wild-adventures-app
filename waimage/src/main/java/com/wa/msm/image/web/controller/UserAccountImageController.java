package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.UserAccountImage;
import com.wa.msm.image.web.exception.ImageNotFoundException;
import com.wa.msm.image.repository.UserAccountImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/user")
public class UserAccountImageController extends AbstractImageController<UserAccountImage, Long> {

    @Autowired
    private UserAccountImageRepository userAccountImageRepository;

    @Override
    @PostMapping
    public ResponseEntity<UserAccountImage> create(@RequestBody UserAccountImage userAccountImage){
        validateImage(userAccountImage);
        return new ResponseEntity<>(userAccountImageRepository.save(userAccountImage), HttpStatus.CREATED);
    }

    @Override
    @PatchMapping
    public ResponseEntity<UserAccountImage> update(@RequestBody UserAccountImage userAccountImage){
        if(userAccountImage == null || userAccountImage.getId() == null) {throw new ImageNotFoundException("l'image fournie n'existe pas");
        }else{
            Optional<UserAccountImage> dbUserAccountImage = userAccountImageRepository.findById(userAccountImage.getId());
            if(!dbUserAccountImage.isPresent()) throw new ImageNotFoundException("L'image fournie n'existe pas en Base");
        }
        validateImage(userAccountImage);
        return new ResponseEntity<>(userAccountImageRepository.save(userAccountImage), HttpStatus.CREATED);
    }

    @Override
    @DeleteMapping(value = "/{imageId}")
    public ResponseEntity<String> delete(@PathVariable Long imageId){
        Optional<UserAccountImage> dbUserAccountImage = userAccountImageRepository.findById(imageId);
        if(!dbUserAccountImage.isPresent()) throw new ImageNotFoundException("L'image fournie n'existe pas en Base");
        else userAccountImageRepository.deleteById(imageId);
        return new ResponseEntity<>("L'image d'id : "+imageId+" a bien été supprimée", HttpStatus.GONE);
    }

    @Override
    @GetMapping(value = "/{imageId}")
    public Optional<UserAccountImage> findById(@PathVariable Long imageId){
        Optional<UserAccountImage> userAccount = userAccountImageRepository.findById(imageId);
        if(!userAccount.isPresent()) throw new ImageNotFoundException("L'image d'id "+ imageId +" n'existe pas.");
        return userAccount;
    }

    @Override
    @GetMapping(value = "/exist/{imageId}")
    public Boolean imageExist(@PathVariable Long imageId){
        Optional<UserAccountImage> userAccount = userAccountImageRepository.findById(imageId);
        return userAccount.isPresent();
    }
}
