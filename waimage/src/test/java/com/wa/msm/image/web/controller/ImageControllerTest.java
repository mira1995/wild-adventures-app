package com.wa.msm.image.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.image.entity.Image;
import com.wa.msm.image.entity.ImageType;
import com.wa.msm.image.repository.ImageTypeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImageControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ImageController imageController;

    @Autowired
    private ImageTypeRepository imageTypeRepository;

    private JacksonTester<Image> jsonImage;

    private Image image;

    @Before
    @Transactional
    public void setUpTest(){

        image = new Image();
        image.setAlt("test");
        image.setDescription("test description");

        image.setType(imageTypeRepository.getOne("ADV"));
        image.setUri("/toto/titi.jpeg");

        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .build();
    }

    @Test
    public void test1_create(){

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/image").accept(MediaType.APPLICATION_JSON).content(jsonImage.write(image).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.OK.value(),response.getStatus());
            image.setId(1L);
            Assert.assertEquals(response.getContentAsString(), jsonImage.write(image).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test2_update(){
        image.setId(1L);
        image.setUri("/hello/world.jpg");
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/image").accept(MediaType.APPLICATION_JSON).content(jsonImage.write(image).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.OK.value(),response.getStatus());
            Assert.assertEquals(response.getContentAsString(), jsonImage.write(image).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test3_findById(){
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/image/1").accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            image.setId(1L);
            Assert.assertEquals(result.getResponse().getStatus() , HttpStatus.OK.value());
            Assert.assertEquals(result.getResponse().getContentAsString(), jsonImage.write(image).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test4_delete(){
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/image/1").accept(MediaType.APPLICATION_JSON).content(jsonImage.write(image).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.OK.value(),response.getStatus());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }
}
