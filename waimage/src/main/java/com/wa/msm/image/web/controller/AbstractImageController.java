package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.*;
import com.wa.msm.image.repository.ImageRepository;
import com.wa.msm.image.util.enumeration.ImageTypeEnum;
import com.wa.msm.image.web.exception.ImageValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public abstract class AbstractImageController<T, ID> {

    @Autowired
    private ImageRepository imageRepository;

    protected ImageRepository getImageRepository() {
        return imageRepository;
    }

    abstract Optional<T> findById(ID imageId);

    abstract ResponseEntity<T> create(T entity);

    abstract ResponseEntity<T> update(T entity);

    abstract ResponseEntity<String> delete(ID imageId);

    abstract  Boolean imageExist(ID imageId);

    protected void validateImage(Image image){
        if(image.getType() == null ) throw new ImageValidationException("Le type de l'image est incorrect");
        if(image instanceof UserAccountImage && !image.getType().getCode().equals(ImageTypeEnum.USER.toString())) throw new ImageValidationException("Le type de l'image est incorrect");
        if(!(image instanceof UserAccountImage) && image.getType().getCode().equals(ImageTypeEnum.USER.toString())) throw new ImageValidationException("Le type de l'image est incorrect");
        if(imageRepository.countByUri(image.getUri())>0) throw new ImageValidationException("Le chemin sur le serveur de l'image n'est pas unique");
    }


}
