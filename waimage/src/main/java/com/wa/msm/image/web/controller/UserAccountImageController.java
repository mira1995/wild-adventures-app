package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.UserAccountImage;
import com.wa.msm.image.web.exception.ImageNotFoundException;
import com.wa.msm.image.repository.UserAccountImageRepository;
import com.wa.msm.image.web.exception.ImageValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserAccountImageController {

    @Autowired
    private UserAccountImageRepository userAccountImageRepository;

    @PostMapping(value = "/image/user")
    public UserAccountImage createUserAccountImage(@RequestBody UserAccountImage userAccountImage){
        validateUserAccountImage(userAccountImage);
        return userAccountImageRepository.save(userAccountImage);
    }

    @PatchMapping(value = "/image/user")
    public UserAccountImage updateUserAccountImage(@RequestBody UserAccountImage userAccountImage){
        if(userAccountImage == null || userAccountImage.getId() == null) {throw new ImageNotFoundException("l'image fournie n'existe pas");
        }else{
            Optional<UserAccountImage> dbUserAccountImage = userAccountImageRepository.findById(userAccountImage.getId());
            if(!dbUserAccountImage.isPresent()) throw new ImageNotFoundException("L'image fournie n'existe pas en Base");
        }
        validateUserAccountImage(userAccountImage);
        return userAccountImageRepository.save(userAccountImage);
    }

    @DeleteMapping(value = "/image/user/{imageId}")
    public String deleteUserAccountImageById(@PathVariable Long imageId){
        Optional<UserAccountImage> dbUserAccountImage = userAccountImageRepository.findById(imageId);
        if(!dbUserAccountImage.isPresent()) throw new ImageNotFoundException("L'image fournie n'existe pas en Base");
        else userAccountImageRepository.deleteById(imageId);
        return "L'image d'id : "+imageId+" a bien été supprimée";
    }

    @GetMapping(value = "/image/user/{imageId}")
    public Optional<UserAccountImage> getUserAccountImageById(@PathVariable Long imageId){
        Optional<UserAccountImage> userAccount = userAccountImageRepository.findById(imageId);
        if(!userAccount.isPresent()) throw new ImageNotFoundException("L'utilisateur d'id "+ imageId +" n'existe pas.");
        return userAccount;
    }

    @GetMapping(value = "/image/user/exist/{imageId}")
    public Boolean userAccountImageExist(@PathVariable Long imageId){
        Optional<UserAccountImage> userAccount = userAccountImageRepository.findById(imageId);
        return userAccount.isPresent();
    }

    private void validateUserAccountImage(UserAccountImage userAccountImage){
        if(userAccountImage.getType() == null || !userAccountImage.getType().getCode().equals("USR")) throw new ImageValidationException("Le type de l'image est incorrect");
    }
}
