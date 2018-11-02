package com.wa.msm.category.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.category.bean.AdventureBean;
import com.wa.msm.category.entity.Category;
import com.wa.msm.category.proxy.MSAdventureProxy;
import com.wa.msm.category.repository.CategoryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private MSAdventureProxy msAdventureProxy;

    @Autowired
    @InjectMocks
    private CategoryController categoryController;

    private JacksonTester<Category> jsonCategory;

    private JacksonTester<List<Category>> jsonCategoryList;

    private AdventureBean adventureBean;

    private Category category;

    @Autowired
    CategoryRepository categoryRepository;

    @Before
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

    @Test
    public void test1_addCategoryTest(){

        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventureBean));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/category").accept(MediaType.APPLICATION_JSON).content(jsonCategory.write(category).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            category.setId(1L);
            Assert.assertEquals(response.getContentAsString(), jsonCategory.write(category).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }



    @Test
    @Transactional
    public void test2_getCategoryTest(){

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/category/1").accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            category.setId(1L);
            Assert.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assert.assertEquals(result.getResponse().getContentAsString(), jsonCategory.write(category).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }

    @Test
    @Transactional
    public void test3_categoryList(){

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/categories").accept(MediaType.APPLICATION_JSON);

        List<Category> categories = new ArrayList<>();
        category.setId(1L);
        categories.add(category);
        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assert.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assert.assertEquals(result.getResponse().getContentAsString(), jsonCategoryList.write(categories).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }


    @Test
    public void test4_updateCategoryTest(){
        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventureBean));


        category.setId(1L);
        category.setDescription("Hello world");
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/category").accept(MediaType.APPLICATION_JSON).content(jsonCategory.write(category).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assert.assertEquals(response.getContentAsString(), jsonCategory.write(category).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }

    @Test
    public void test5_deleteCategoryTest(){
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/category/1").accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.GONE.value(),response.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

}
