package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.CategoryImage;
import com.wa.msm.image.entity.CategoryImageKey;
import com.wa.msm.image.entity.Image;
import com.wa.msm.image.proxy.MSCategoryProxy;
import com.wa.msm.image.repository.CategoryImageRepository;
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
public class CategoryImageController extends AbstractImageDependencyController<CategoryImage, CategoryImageKey> {

    @Autowired
    private CategoryImageRepository categoryImageRepository;

    @Autowired
    private MSCategoryProxy msCategoryProxy;

    @Override
    @PostMapping(value = "/image/category")
    ResponseEntity<CategoryImage> create(@RequestBody CategoryImage entity) {
        validateImageDependency(entity);
        validateCategory(entity.getCategoryId());
        return new ResponseEntity<>(categoryImageRepository.save(entity), HttpStatus.CREATED);
    }

    @Override
    @DeleteMapping(value = "/image/category")
    ResponseEntity<String> delete(@RequestBody CategoryImageKey imageId) {
        Optional<CategoryImage> categoryImage = categoryImageRepository.findById(imageId);
        if(!categoryImage.isPresent()) throw new ImageNotFoundException("L'image d'id "+ imageId +" n'existe pas.");
        else categoryImageRepository.deleteById(imageId);
        return new ResponseEntity<>("L'image d'id : "+imageId+" a bien été supprimée", HttpStatus.GONE);
    }

    @Override
    @PostMapping(value = "/image/category/exist")
    Boolean imageExist(@RequestBody CategoryImageKey imageId) {
        Optional<CategoryImage> categoryImage = categoryImageRepository.findById(imageId);
        return categoryImage.isPresent();
    }

    @GetMapping(value = "/image/category/{categoryId}")
    public List<Image> findImagesByCategory(@PathVariable Long categoryId){
        List<Long> imagesId = new ArrayList<>();
        List<Image> images= null;
        //TODO Vérifier que l'aventure existe
        List <CategoryImage> categoryImages = categoryImageRepository.findCategoryImagesByCategoryId(categoryId);
        if(categoryImages.size()>0){
            categoryImages.forEach(itemAdventureImage -> imagesId.add(itemAdventureImage.getImageId()));
            images = getImageRepository().findByIdIn(imagesId);
        }
        return images;
    }

    private void validateCategory(Long categoryId){
        if(!msCategoryProxy.getCategory(categoryId).isPresent()) throw new CategoryNotFoundException("La catégorie d'id "+ categoryId + " n'existe pas");
    }
}
