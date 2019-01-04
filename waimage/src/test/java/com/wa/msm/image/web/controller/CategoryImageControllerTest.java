package com.wa.msm.image.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.image.bean.AdventureBean;
import com.wa.msm.image.bean.CategoryBean;
import com.wa.msm.image.entity.*;
import com.wa.msm.image.proxy.MSAdventureProxy;
import com.wa.msm.image.proxy.MSCategoryProxy;
import com.wa.msm.image.repository.AdventureImageRepository;
import com.wa.msm.image.repository.CategoryImageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CategoryImageControllerTest extends AbstractImageControllerTest {
    //Utilitaries
    private MockMvc mockMvc;

    @MockBean
    private MSCategoryProxy msCategoryProxy;

    @Autowired
    @InjectMocks
    private CategoryImageController categoryImageController;

    @Autowired
    private CategoryImageRepository categoryImageRepository;

    //attributes
    private CategoryBean category;
    private Image image;
    private CategoryImage categoryImage;
    private CategoryImageKey categoryImageKey;
    private JacksonTester <Boolean> jsonBoolean;
    private JacksonTester <CategoryImage> jsonCategoryImage;
    private JacksonTester <CategoryImageKey> jsonCategoryImageKey;
    private JacksonTester <List<Image>> jsonListImage;
    private static final String urlPrefix = "/api/categories";

    @BeforeEach
    @Transactional
    public void setUp(){
        category = new CategoryBean();
        category.setId(1L);
        category.setTitle("Aventure Test");
        category.setDescription("Aventure de test");

        image = persistJddImage(imageTypeCat);

        categoryImage = new CategoryImage();
        categoryImage.setCategoryId(category.getId());
        categoryImage.setImageId(image.getId());

        categoryImageKey = new CategoryImageKey();
        categoryImageKey.setCategoryId(category.getId());
        categoryImageKey.setImageId(image.getId());

        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(categoryImageController)
                .build();
    }

    @Test
    void test1_createTest(){
        Mockito.when(
                msCategoryProxy.getCategory(Mockito.anyLong())).thenReturn(Optional.ofNullable(category));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post(urlPrefix+"/admin").accept(MediaType.APPLICATION_JSON).content(jsonCategoryImage.write(categoryImage).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(jsonCategoryImage.write(categoryImage).getJson(), response.getContentAsString());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    void test2_deleteTest(){
        Mockito.when(
                msCategoryProxy.getCategory(Mockito.anyLong())).thenReturn(Optional.ofNullable(category));
        persistJddCategoryImage();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(urlPrefix+"/admin").accept(MediaType.APPLICATION_JSON).content(jsonCategoryImageKey.write(categoryImageKey).getJson()).contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.GONE.value(),response.getStatus());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }

    @Test
    void test3_imageExistTest(){
        Mockito.when(
                msCategoryProxy.getCategory(Mockito.anyLong())).thenReturn(Optional.ofNullable(category));
        persistJddCategoryImage();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post(urlPrefix+"/exist").accept(MediaType.APPLICATION_JSON).content(jsonCategoryImageKey.write(categoryImageKey).getJson()).contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
            Assertions.assertEquals(jsonBoolean.write(true).getJson(), response.getContentAsString());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    void test4_findImagesByCategoryTest(){
        Mockito.when(
                msCategoryProxy.getCategory(Mockito.anyLong())).thenReturn(Optional.ofNullable(category));

        persistJddCategoryImage();
        List<Image> imageList = new ArrayList<>();
        imageList.add(image);
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.get(urlPrefix+"/"+category.getId()).accept(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
            Assertions.assertEquals(jsonListImage.write(imageList).getJson(), response.getContentAsString());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    private void persistJddCategoryImage(){
        categoryImage = categoryImageRepository.save(categoryImage);
    }
}
