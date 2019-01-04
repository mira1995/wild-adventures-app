package com.wa.msm.image.web.controller;

import com.wa.msm.image.entity.Image;
import com.wa.msm.image.entity.ImageType;
import com.wa.msm.image.repository.ImageRepository;
import com.wa.msm.image.repository.ImageTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class AbstractImageControllerTest {

    @Autowired
    protected ImageTypeRepository imageTypeRepository;

    @Autowired
    protected ImageRepository imageRepository;

    Image imagePersisted;

    static ImageType imageTypeAdv;
    static ImageType imageTypeCat;
    static ImageType imageTypeUsr;

    JacksonTester<Image> jsonImage;

    private void instantiateImageType(){
        imageTypeAdv = new ImageType();
        imageTypeAdv.setName("adventure");
        imageTypeAdv.setCode("ADV");

        imageTypeCat = new ImageType();
        imageTypeCat.setName("category");
        imageTypeCat.setCode("CAT");

        imageTypeUsr = new ImageType();
        imageTypeUsr.setName("user");
        imageTypeUsr.setCode("USR");

    }

    @BeforeEach
    @Transactional
    public void setUpDatas(){
        List<ImageType> imageTypeList =  imageTypeRepository.findAll();

        instantiateImageType();
        if(imageTypeList.isEmpty()){
            imageTypeRepository.save(imageTypeAdv);
            imageTypeRepository.save(imageTypeCat);
            imageTypeRepository.save(imageTypeUsr);
        }

        imagePersisted = new Image();
        imagePersisted.setAlt("test");
        imagePersisted.setDescription("test description");
        imagePersisted.setUri("/test/test.jpeg");

    }

    @AfterEach
    @Transactional
    public void afterTest(){
        imageRepository.delete(imagePersisted);
    }

    public Image persistJddImage(ImageType pImageType){
        imagePersisted.setType(pImageType);
        imageRepository.save(imagePersisted);
        return imagePersisted;
    }
}
