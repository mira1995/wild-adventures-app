package com.wa.msm.adventure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.adventure.bean.CategoryAdventureBean;
import com.wa.msm.adventure.bean.CategoryBean;
import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.proxy.MSCategoryProxy;
import com.wa.msm.adventure.repository.AdventureRepository;
import org.junit.*;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdventureControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private MSCategoryProxy msCategoryProxy;

    @Autowired
    @InjectMocks
    private AdventureController adventureController;

    @Autowired
    private AdventureRepository adventureRepository;

    private JacksonTester<Adventure> jsonAdventure;

    private JacksonTester<List<Adventure>> jsonAdventureList;

    private Adventure adventurePersisted;

    private CategoryBean category;

    @Before
    @Transactional
    public void setUp(){


        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(adventureController)
                .build();

        adventurePersisted = new Adventure();
        adventurePersisted.setStatus("NOT_PAID");
        adventurePersisted.setLocation("Paris");
        adventurePersisted.setTitle("Aventure Test");
        adventurePersisted.setDescription("Aventure de test");

        adventureRepository.save(adventurePersisted);

        category = new CategoryBean();
        category.setId(1L);
        category.setTitle("Test");
        category.setDescription("Catégorie de test");

        List<CategoryAdventureBean> categoryAdventureBeans = new ArrayList<>();
        CategoryAdventureBean categoryAdventureBean = new CategoryAdventureBean();
        categoryAdventureBean.setAdventureId(adventurePersisted.getId());
        categoryAdventureBean.setCategoryId(category.getId());
        categoryAdventureBeans.add(categoryAdventureBean);
        category.setCategoryAdventures(categoryAdventureBeans);
    }

    void persistJDD(){
        adventureRepository.save(adventurePersisted);
    }



    @After
    @Transactional
    public void afterTest(){
        adventureRepository.delete(adventurePersisted);
    }



    @Test
    public void test1_addAdventureTest(){
        Mockito.when(
                msCategoryProxy.getCategory(Mockito.anyLong())).thenReturn(Optional.ofNullable(category));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/"+category.getId()).accept(MediaType.APPLICATION_JSON).content(jsonAdventure.write(adventurePersisted).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assert.assertEquals(jsonAdventure.write(adventurePersisted).getJson().substring(8), response.getContentAsString().substring(8));

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    public void test2_getAdventureTest(){
        persistJDD();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/"+adventurePersisted.getId()).accept(MediaType.APPLICATION_JSON);
        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assert.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assert.assertEquals(result.getResponse().getContentAsString(), jsonAdventure.write(adventurePersisted).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    public void test3_adventureListByCategoryTest(){
        persistJDD();
        Mockito.when(
                msCategoryProxy.getCategory(Mockito.anyLong())).thenReturn(Optional.ofNullable(category));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/category/"+category.getId()).accept(MediaType.APPLICATION_JSON);

        List<Adventure> adventures = new ArrayList<>();
        adventures.add(adventurePersisted);
        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assert.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assert.assertEquals(result.getResponse().getContentAsString(), jsonAdventureList.write(adventures).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    public void test4_adventureListTest(){
        persistJDD();
        Mockito.when(
                msCategoryProxy.getCategory(Mockito.anyLong())).thenReturn(Optional.ofNullable(category));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON);

        List<Adventure> adventures = new ArrayList<>();
        adventures.add(adventurePersisted);
        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assert.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assert.assertEquals(result.getResponse().getContentAsString(), jsonAdventureList.write(adventures).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test5_updateAdventureTest(){
        persistJDD();
        Mockito.when(
                msCategoryProxy.getCategory(Mockito.anyLong())).thenReturn(Optional.ofNullable(category));

        adventurePersisted.setDescription("Hello world");
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/admin").accept(MediaType.APPLICATION_JSON).content(jsonAdventure.write(adventurePersisted).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assert.assertEquals(response.getContentAsString(), jsonAdventure.write(adventurePersisted).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }


    @Test
    public void test6_deleteAdventureTest(){
        persistJDD();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/"+adventurePersisted.getId()).accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.GONE.value(),response.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }
}
