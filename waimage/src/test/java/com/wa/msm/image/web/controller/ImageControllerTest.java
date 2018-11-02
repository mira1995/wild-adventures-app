package com.wa.msm.image.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.image.entity.Image;

import org.junit.jupiter.api.*;
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
public class ImageControllerTest extends AbstractImageControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ImageController imageController;

    @BeforeEach
    @Transactional
    public void setUp(){
        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .build();
    }

    @Test
    public void test1_create(){
        imagePersisted.setType(imageTypeAdv);
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/image").accept(MediaType.APPLICATION_JSON).content(jsonImage.write(imagePersisted).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(jsonImage.write(imagePersisted).getJson().substring(11), response.getContentAsString().substring(8));

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test2_update(){
        Image image = persistJddImage(imageTypeAdv);
        image.setUri("/hello/world.jpg");
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/image").accept(MediaType.APPLICATION_JSON).content(jsonImage.write(image).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(response.getContentAsString(), jsonImage.write(image).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test3_findById(){
        Image image = persistJddImage(imageTypeAdv);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/image/"+image.getId()).accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(result.getResponse().getStatus() , HttpStatus.OK.value());
            Assertions.assertEquals(result.getResponse().getContentAsString(), jsonImage.write(image).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test4_delete(){
        Image image = persistJddImage(imageTypeAdv);
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/image/"+image.getId()).accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.GONE.value(),response.getStatus());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }


}
