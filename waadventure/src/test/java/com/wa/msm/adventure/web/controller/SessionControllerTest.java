package com.wa.msm.adventure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.adventure.entity.Adventure;
import com.wa.msm.adventure.entity.Session;
import com.wa.msm.adventure.proxy.MSCategoryProxy;
import com.wa.msm.adventure.repository.AdventureRepository;
import com.wa.msm.adventure.repository.SessionRepository;
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

    @Autowired
    private SessionRepository sessionRepository;

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

    private void persistJdd(){
        sessionRepository.save(session);
    }

    @After
    @Transactional
    public void afterTest(){
        sessionRepository.delete(session);
        System.out.print("test");
    }

    @Test
    public void test1_addSessionTest() {
        try {
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sessions/admin").accept(MediaType.APPLICATION_JSON).content(jsonSession.write(session).getJson()).contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
            Assert.assertEquals(jsonSession.write(session).getJson().substring(11), response.getContentAsString().substring(8));
            StringBuilder idString = new StringBuilder();
            idString.append(response.getContentAsString().charAt(6));
            session.setId(Long.parseLong(idString.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test2_updateSessionTest(){
        persistJdd();
        session.setPrice(150D);
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/sessions/admin").accept(MediaType.APPLICATION_JSON).content(jsonSession.write(session).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assert.assertEquals(jsonSession.write(session).getJson(), response.getContentAsString());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    public void test3_sessionListTest(){
        persistJdd();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sessions/"+adventure.getId()).accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            List<Session> sessions = new ArrayList<>();
            sessions.add(session);
            Assert.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assert.assertEquals(jsonSessionList.write(sessions).getJson(),result.getResponse().getContentAsString());
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test4_getAllByIdTest(){

        persistJdd();
        List<Long> sessionsIdList = new ArrayList<>();
        sessionsIdList.add(session.getId());

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sessions").accept(MediaType.APPLICATION_JSON).content(jsonLongList.write(sessionsIdList).getJson()).contentType(MediaType.APPLICATION_JSON);

            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            List<Session> sessions = new ArrayList<>();
            sessions.add(session);
            Assert.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assert.assertEquals(jsonSessionList.write(sessions).getJson(), result.getResponse().getContentAsString());
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test5_deleteSessionTest(){
        persistJdd();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/sessions/admin/"+session.getId()).accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.GONE.value(),response.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }
}
