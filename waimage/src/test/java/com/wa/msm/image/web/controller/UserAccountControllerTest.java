package com.wa.msm.image.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.image.entity.Image;
import com.wa.msm.image.entity.ImageType;
import com.wa.msm.image.entity.UserAccountImage;
import com.wa.msm.image.repository.ImageRepository;
import com.wa.msm.image.repository.ImageTypeRepository;
import com.wa.msm.image.repository.UserAccountImageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserAccountControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private UserAccountImageController userAccountImageController;

    @Autowired
    private UserAccountImageRepository userAccountImageRepository;

    @Autowired
    protected ImageTypeRepository imageTypeRepository;

    JacksonTester<UserAccountImage> jsonUserAccountImage;

    UserAccountImage imagePersisted;

    static ImageType imageTypeAdv;
    static ImageType imageTypeCat;
    static ImageType imageTypeUsr;

    @BeforeEach
    @Transactional
    public void setUp(){

        imageTypeAdv = new ImageType();
        imageTypeAdv.setName("adventure");
        imageTypeAdv.setCode("ADV");
        imageTypeRepository.save(imageTypeAdv);

        imageTypeCat = new ImageType();
        imageTypeCat.setName("category");
        imageTypeCat.setCode("CAT");
        imageTypeRepository.save(imageTypeCat);

        imageTypeUsr = new ImageType();
        imageTypeUsr.setName("user");
        imageTypeUsr.setCode("USR");
        imageTypeRepository.save(imageTypeUsr);

        imagePersisted = new UserAccountImage();
        imagePersisted.setAlt("test");
        imagePersisted.setDescription("test description");
        imagePersisted.setUri("/test/test.jpeg");
        imagePersisted.setType(imageTypeUsr);

        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(userAccountImageController)
                .build();
    }

    @AfterEach
    @Transactional
    public void afterTest(){
        userAccountImageRepository.delete(imagePersisted);
    }

    @Test
    public void test1_create(){
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/image/user").accept(MediaType.APPLICATION_JSON).content(jsonUserAccountImage.write(imagePersisted).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(jsonUserAccountImage.write(imagePersisted).getJson().substring(11), response.getContentAsString().substring(8));

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test2_update(){
        UserAccountImage image = persistJddImage();
        image.setUri("/hello/world.jpg");
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/image/user").accept(MediaType.APPLICATION_JSON).content(jsonUserAccountImage.write(image).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(response.getContentAsString(), jsonUserAccountImage.write(image).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test3_findById(){
        UserAccountImage image = persistJddImage();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/image/user/"+image.getId()).accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(result.getResponse().getStatus() , HttpStatus.OK.value());
            Assertions.assertEquals(result.getResponse().getContentAsString(), jsonUserAccountImage.write(image).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test4_delete(){
        UserAccountImage image = persistJddImage();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/image/user/"+image.getId()).accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.GONE.value(),response.getStatus());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    public UserAccountImage persistJddImage(){
        userAccountImageRepository.save(imagePersisted);
        return imagePersisted;
    }
}
