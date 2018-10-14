package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.Image;
import com.wa.msm.image.web.exception.ImageNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ImageController extends AbstractImageController<Image, Long> {
    @Override
    @PostMapping(value = "/image")
    public Image create(@RequestBody Image image){
        validateImage(image);
        return getImageRepository().save(image);
    }

    @Override
    @PatchMapping(value = "/image")
    public Image update(@RequestBody Image image){
        if(image == null || image.getId() == null) {throw new ImageNotFoundException("L'image fournie est nulle");
        }else{
            Optional<Image> dbImage = getImageRepository().findById(image.getId());
            if(!dbImage.isPresent()) throw new ImageNotFoundException("L'image fournie n'existe pas en Base");
        }
        validateImage(image);
        return getImageRepository().save(image);
    }

    @Override
    @DeleteMapping(value = "/image/{imageId}")
    public String delete(@PathVariable Long imageId){
        Optional<Image> dbImage = getImageRepository().findById(imageId);
        if(!dbImage.isPresent()) throw new ImageNotFoundException("L'image fournie n'existe pas en Base");
        else {
            getImageRepository().deleteById(imageId);
        }
        return "L'image d'id : "+imageId+" a bien été supprimée";
    }

    @Override
    @GetMapping(value = "/image/{imageId}")
    public Optional<Image> findById(@PathVariable Long imageId){
        Optional<Image> image = getImageRepository().findById(imageId);
        if(!image.isPresent()) throw new ImageNotFoundException("L'image d'id "+ imageId +" n'existe pas.");
        return image;
    }

    @Override
    @GetMapping(value = "/image/exist/{imageId}")
    public Boolean imageExist(@PathVariable Long imageId){
        Optional<Image> userAccount = getImageRepository().findById(imageId);
        return userAccount.isPresent();
    }
}
