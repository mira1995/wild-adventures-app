package com.wa.msm.image.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.image.bean.AdventureBean;
import com.wa.msm.image.entity.AdventureImage;
import com.wa.msm.image.entity.AdventureImageKey;
import com.wa.msm.image.entity.Image;
import com.wa.msm.image.proxy.MSAdventureProxy;
import com.wa.msm.image.repository.AdventureImageRepository;
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
public class AdventureImageControllerTest extends AbstractImageControllerTest {

    //Utilitaries
    private MockMvc mockMvc;

    @MockBean
    private MSAdventureProxy msAdventureProxy;

    @Autowired
    @InjectMocks
    private AdventureImageController adventureImageController;

    @Autowired
    private AdventureImageRepository adventureImageRepository;

    //attributes
    private AdventureBean adventure;
    private Image image;
    private AdventureImage adventureImage;
    private AdventureImageKey adventureImageKey;
    private JacksonTester <Boolean> jsonBoolean;
    private JacksonTester <AdventureImage> jsonAdventureImage;
    private JacksonTester <AdventureImageKey> jsonAdventureImageKey;
    private JacksonTester <List<Image>> jsonListImage;
    private static final String urlPrefix = "/api/adventures";

    @BeforeEach
    @Transactional
    public void setUp(){
        adventure = new AdventureBean();
        adventure.setId(1L);
        adventure.setStatus("NOT_PAID");
        adventure.setLocation("Paris");
        adventure.setTitle("Aventure Test");
        adventure.setDescription("Aventure de test");

        image = persistJddImage(imageTypeAdv);

        adventureImage = new AdventureImage();
        adventureImage.setAdventureId(adventure.getId());
        adventureImage.setImageId(image.getId());

        adventureImageKey = new AdventureImageKey();
        adventureImageKey.setAdventureId(adventure.getId());
        adventureImageKey.setImageId(image.getId());

        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(adventureImageController)
                .build();
    }

    @Test
    void test1_createTest(){
        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventure));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post(urlPrefix+"/admin").accept(MediaType.APPLICATION_JSON).content(jsonAdventureImage.write(adventureImage).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(jsonAdventureImage.write(adventureImage).getJson(), response.getContentAsString());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    void test2_deleteTest(){
        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventure));
        persistJddAdventureImage();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(urlPrefix+"/admin").accept(MediaType.APPLICATION_JSON).content(jsonAdventureImageKey.write(adventureImageKey).getJson()).contentType(MediaType.APPLICATION_JSON);
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
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventure));
        persistJddAdventureImage();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post(urlPrefix+"/exist").accept(MediaType.APPLICATION_JSON).content(jsonAdventureImageKey.write(adventureImageKey).getJson()).contentType(MediaType.APPLICATION_JSON);
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
    void test4_findImagesByAdventureTest(){
        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventure));

        persistJddAdventureImage();
        List<Image> imageList = new ArrayList<>();
        imageList.add(image);
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.get(urlPrefix+"/"+adventure.getId()).accept(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
            Assertions.assertEquals(jsonListImage.write(imageList).getJson(), response.getContentAsString());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    private void persistJddAdventureImage(){
        adventureImage = adventureImageRepository.save(adventureImage);
    }

}
