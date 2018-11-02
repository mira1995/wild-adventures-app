package com.wa.msm.adventure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.entity.Session;
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

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SessionControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private MSCategoryProxy msCategoryProxy;

    @Autowired
    private AdventureRepository adventureRepository;

    @Autowired
    @InjectMocks
    private SessionController sessionController;

    private JacksonTester<Session> jsonSession;

    private JacksonTester<List<Session>> jsonSessionList;

    private JacksonTester<List<Long>> jsonLongList;

    private Session session;

    private Adventure adventure;

    public static boolean dbInit = false;


    @Before
    @Transactional
    public void setUp(){

        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(sessionController)
                .build();

        adventure = new Adventure();
        adventure.setStatus("NOT_PAID");
        adventure.setLocation("Paris");
        adventure.setTitle("Aventure Test");
        adventure.setDescription("Aventure de test");
        if(!dbInit){

            adventureRepository.save(adventure);
            dbInit = true;
        }
        adventure.setId(1L);

        session = new Session();
        session.setPrice(10.0D);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2018, 10, 11);
        session.setStartDate(cal.getTime());
        cal.set(2018, 10, 12);
        session.setEndDate(cal.getTime());
        session.setAdventure(adventure);
        session.setAdventureId(adventure.getId());
    }

    @Test
    public void test1_addSessionTest() {
        try {
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/session").accept(MediaType.APPLICATION_JSON).content(jsonSession.write(session).getJson()).contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
            session.setId(1L);
            Assert.assertEquals(response.getContentAsString(), jsonSession.write(session).getJson());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test2_updateSessionTest(){

        session.setId(1L);
        session.setPrice(150D);
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/session").accept(MediaType.APPLICATION_JSON).content(jsonSession.write(session).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assert.assertEquals(response.getContentAsString(), jsonSession.write(session).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    public void test3_sessionListTest(){
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sessions/1").accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            List<Session> sessions = new ArrayList<>();
            session.setId(1L);
            session.setPrice(150D);
            sessions.add(session);
            Assert.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assert.assertEquals(result.getResponse().getContentAsString(), jsonSessionList.write(sessions).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test4_getAllByIdTest(){

        List<Long> sessionsIdList = new ArrayList<>();
        sessionsIdList.add(1L);

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sessions").accept(MediaType.APPLICATION_JSON).content(jsonLongList.write(sessionsIdList).getJson()).contentType(MediaType.APPLICATION_JSON);

            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            List<Session> sessions = new ArrayList<>();
            session.setId(1L);
            session.setPrice(150D);
            sessions.add(session);
            Assert.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assert.assertEquals(result.getResponse().getContentAsString(), jsonSessionList.write(sessions).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test5_deleteSessionTest(){
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/session/1").accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.GONE.value(),response.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }
}
