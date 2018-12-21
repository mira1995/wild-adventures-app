package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.CategoryImage;
import com.wa.msm.image.entity.CategoryImageKey;
import com.wa.msm.image.entity.Image;
import com.wa.msm.image.proxy.MSCategoryProxy;
import com.wa.msm.image.repository.CategoryImageRepository;
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

@Api(description = "API pour les opérations CRUD sur les images liés à des catégories")
@RestController
@RequestMapping(value = "/api/categories")
public class CategoryImageController extends AbstractImageDependencyController<CategoryImage, CategoryImageKey> {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CategoryImageRepository categoryImageRepository;

    @Autowired
    private MSCategoryProxy msCategoryProxy;

    @Override
    @ApiOperation(value = "Créé le lien d'une image avec une catégorie (réservée aux admins)")
    @PostMapping(value = "/admin")
    ResponseEntity<CategoryImage> create(@RequestBody CategoryImage entity) {
        log.info("Début méthode : create()");
        validateImageDependency(entity);
        validateCategory(entity.getCategoryId());
        log.info("Création du lien entre la catégorie d'id : "+entity.getCategoryId() +" et l'image d'id : "+entity.getImageId());
        return new ResponseEntity<>(categoryImageRepository.save(entity), HttpStatus.CREATED);
    }

    @Override
    @ApiOperation(value = "Supprime le lien d'une image avec une catégorie (réservée aux admins)")
    @DeleteMapping(value = "/admin")
    ResponseEntity<String> delete(@RequestBody CategoryImageKey imageId) {
        log.info("Début méthode : create()");
        Optional<CategoryImage> categoryImage = categoryImageRepository.findById(imageId);
        if(!categoryImage.isPresent()) {
            log.error("Le lieb entre image d'id "+ imageId.getImageId() +" et la catégorie d'id : "+imageId.getCategoryId()+" n'existe pas.");
            throw new ImageNotFoundException("L'image d'id "+ imageId +" n'existe pas.");
        }
        else {
            log.info("Suppression du lien entre la catégorie d'id : "+imageId.getCategoryId() +" et l'image d'id : "+imageId.getImageId());
            categoryImageRepository.deleteById(imageId);
        }

        return new ResponseEntity<>("L'image d'id : "+imageId+" a bien été supprimée", HttpStatus.GONE);
    }

    @Override
    @ApiOperation(value = "Vérifie l'existance du lien d'une image avec une catégorie (disponible à tous les utilisateurs)")
    @PostMapping(value = "/exist")
    Boolean imageExist(@RequestBody CategoryImageKey imageId) {
        log.info("Début méthode : imageExist()");
        Optional<CategoryImage> categoryImage = categoryImageRepository.findById(imageId);
        log.info("Vérification de l'existance du lien entre l'aventure d'id : "+imageId.getCategoryId() +" et l'image d'id : "+imageId.getImageId());
        return categoryImage.isPresent();
    }

    @ApiOperation(value = "Récupère les images liées à une catégorie (disponible à tous les utilisateurs)")
    @GetMapping(value = "/{categoryId}")
    public List<Image> findImagesByCategory(@PathVariable Long categoryId){
        log.info("Début méthode : findImagesByCategory()");
        List<Long> imagesId = new ArrayList<>();
        List<Image> images= null;
        validateCategory(categoryId);
        List <CategoryImage> categoryImages = categoryImageRepository.findCategoryImagesByCategoryId(categoryId);
        if(categoryImages.size()>0){

            categoryImages.forEach(itemAdventureImage -> imagesId.add(itemAdventureImage.getImageId()));
            images = getImageRepository().findByIdIn(imagesId);
        }else{
            log.error("Aucun image trouvé pour la catégorie d'id : "+categoryId);
        }
        log.info("Récupération des images liés à la catégorie d'id : "+categoryId);
        return images;
    }

    private void validateCategory(Long categoryId){
        if(!msCategoryProxy.getCategory(categoryId).isPresent()) throw new CategoryNotFoundException("La catégorie d'id "+ categoryId + " n'existe pas");
    }
}
