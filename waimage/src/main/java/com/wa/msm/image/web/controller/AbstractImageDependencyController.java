package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.AdventureImage;
import com.wa.msm.image.entity.Image;
import com.wa.msm.image.entity.ImageDependency;
import com.wa.msm.image.repository.ImageRepository;
import com.wa.msm.image.util.enumeration.ImageTypeEnum;
import com.wa.msm.image.web.exception.ImageValidationException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class AbstractImageDependencyController<T, ID> {

    @Autowired
    private ImageRepository imageRepository;

    public ImageRepository getImageRepository() {
        return imageRepository;
    }

    abstract T create(T entity);

    abstract String delete(ID imageId);

    abstract  Boolean imageExist(ID imageId);

    protected void  validateImageDependency(ImageDependency imageDependency){
        Optional<Image> image = imageRepository.findById(imageDependency.getImageId());
        if(!image.isPresent()) throw new ImageValidationException("L'image d'id : "+ imageDependency.getImageId() +" n'existe pas");
        if(imageDependency instanceof AdventureImage && !image.get().getType().getCode().equals(ImageTypeEnum.ADVENTURE.toString())) throw new ImageValidationException("Le type de l'image est incorrect");
    }
}
