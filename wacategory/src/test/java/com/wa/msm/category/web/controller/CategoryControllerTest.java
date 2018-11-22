package com.wa.msm.category.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.category.bean.AdventureBean;
import com.wa.msm.category.entity.Category;
import com.wa.msm.category.proxy.MSAdventureProxy;
import com.wa.msm.category.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private MSAdventureProxy msAdventureProxy;

    @Autowired
    @InjectMocks
    private CategoryController categoryController;

    @Autowired
    private CategoryRepository categoryRepository;

    private JacksonTester<Category> jsonCategory;

    private JacksonTester<List<Category>> jsonCategoryList;

    private AdventureBean adventureBean;

    private Category category;


    @BeforeEach
    public void setUp(){
        adventureBean = new AdventureBean();
        adventureBean.setId(1L);
        adventureBean.setDescription("Une aventure de test");
        adventureBean.setLocation("Paris");
        adventureBean.setStatus("NOT_PAID");
        adventureBean.setTitle("Aventure de test");

        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .build();

        category = new Category();
        category.setTitle("Test");
        category.setDescription("Catégorie de test");

    }

    private void persistJdd(){
        categoryRepository.save(category);
    }

    @AfterEach
    public void afterTest(){
        categoryRepository.delete(category);
    }

    @Test
    public void test1_addCategoryTest(){

        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventureBean));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin").accept(MediaType.APPLICATION_JSON).content(jsonCategory.write(category).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(jsonCategory.write(category).getJson().substring(11), response.getContentAsString().substring(8));
            StringBuilder idString = new StringBuilder();
            idString.append(response.getContentAsString().charAt(6));
            category.setId(Long.parseLong(idString.toString()));
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }



    @Test
    @Transactional
    public void test2_getCategoryTest(){
        persistJdd();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/"+category.getId()).accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assertions.assertEquals(jsonCategory.write(category).getJson(), result.getResponse().getContentAsString());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }

    @Test
    @Transactional
    public void test3_categoryList(){
        persistJdd();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON);

        List<Category> categories = new ArrayList<>();
        categories.add(category);
        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assertions.assertEquals(result.getResponse().getContentAsString(), jsonCategoryList.write(categories).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }


    @Test
    public void test4_updateCategoryTest(){
        persistJdd();
        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventureBean));


        category.setDescription("Hello world");
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/admin").accept(MediaType.APPLICATION_JSON).content(jsonCategory.write(category).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(response.getContentAsString(), jsonCategory.write(category).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }

    @Test
    public void test5_deleteCategoryTest(){
        persistJdd();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/"+category.getId()).accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.GONE.value(),response.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

}
