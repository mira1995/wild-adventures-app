package com.wa.msm.comment.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.comment.bean.AdventureBean;
import com.wa.msm.comment.bean.UserAccountBean;
import com.wa.msm.comment.entity.Comment;
import com.wa.msm.comment.proxy.MSAdventureProxy;
import com.wa.msm.comment.proxy.MSUserAccountProxy;
import com.wa.msm.comment.repository.CommentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommentControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private CommentController commentController;

    @MockBean
    MSAdventureProxy msAdventureProxy;

    @MockBean
    MSUserAccountProxy msUserAccountProxy;

    private JacksonTester<Comment> jsonComment;

    private JacksonTester<List<Comment>> jsonCommentList;

    private Comment comment;

    private AdventureBean adventure;

    private UserAccountBean userAccount;

    @Autowired
    private CommentRepository commentRepository;

    @Before
    public void setUp(){
        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .build();


        comment = new Comment();
        comment.setAdventureId(1L);
        comment.setUserId(1L);
        comment.setReported(false);
        comment.setContent("Hello world");

        adventure = new AdventureBean();
        adventure.setId(1L);
        adventure.setStatus("NOT_PAID");
        adventure.setLocation("Paris");
        adventure.setTitle("Aventure Test");
        adventure.setDescription("Aventure de test");

        userAccount = new UserAccountBean();
        userAccount.setProfileImageId(1L);
        userAccount.setActive(true);
        userAccount.setAddress("1 rue du Maronnier");
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 0, 1);
        userAccount.setBirthDate(Calendar.getInstance());
        userAccount.setCity("Paris");
        userAccount.setCountry("France");
        userAccount.setEmail("test@gmail.com");
        userAccount.setFirstname("Toto");
        userAccount.setLastname("tata");
        userAccount.setPhoneNumber("0123456789");
        userAccount.setPassword("tititata");
        userAccount.setPseudo("test");
        userAccount.setPostalCode(75000);
    }


    @Test
    public void test1_addCommentTest(){
        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventure));

        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comment").accept(MediaType.APPLICATION_JSON).content(jsonComment.write(comment).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            comment.setId(1L);
            Assert.assertEquals(response.getContentAsString(), jsonComment.write(comment).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    public void test2_commentListTest(){
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/comments/1").accept(MediaType.APPLICATION_JSON);

        List<Comment> comments = new ArrayList<>();
        comment.setId(1L);
        comments.add(comment);
        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assert.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assert.assertEquals(result.getResponse().getContentAsString(), jsonCommentList.write(comments).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test3_updateCommentTest(){
        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventure));

        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));

        comment.setId(1L);
        comment.setReported(true);
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/comment").accept(MediaType.APPLICATION_JSON).content(jsonComment.write(comment).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assert.assertEquals(response.getContentAsString(), jsonComment.write(comment).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test4_deleteByIdTest(){
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/comment/1").accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.GONE.value(),response.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test5_deleteByAdventureId(){
        for(int i=0; i<=2; i++){
            commentRepository.save(comment);
        }

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/comment/adventure/1").accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.GONE.value(),response.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }
}
