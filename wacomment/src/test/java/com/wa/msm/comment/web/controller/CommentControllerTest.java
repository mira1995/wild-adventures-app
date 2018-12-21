package com.wa.msm.comment.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.comment.bean.AdventureBean;
import com.wa.msm.comment.bean.UserAccountBean;
import com.wa.msm.comment.entity.Comment;
import com.wa.msm.comment.proxy.MSAdventureProxy;
import com.wa.msm.comment.proxy.MSUserAccountProxy;
import com.wa.msm.comment.repository.CommentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
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

    @BeforeEach
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

    private void persistJdd(){
        commentRepository.save(comment);
    }

    @AfterEach
    public void afterTest(){
        commentRepository.delete(comment);
    }


    @Test
    public void test1_addCommentTest(){
        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventure));

        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/add").accept(MediaType.APPLICATION_JSON).content(jsonComment.write(comment).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(jsonComment.write(comment).getJson().substring(11),response.getContentAsString().substring(8));
            StringBuilder idString = new StringBuilder();
            idString.append(response.getContentAsString().charAt(6));
            comment.setId(Long.parseLong(idString.toString()));
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    public void test2_commentListTest(){
        persistJdd();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAll/"+adventure.getId()).accept(MediaType.APPLICATION_JSON);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
            Assertions.assertEquals(jsonCommentList.write(comments).getJson(),result.getResponse().getContentAsString());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test3_updateCommentTest(){
        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventure));

        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));
        persistJdd();
        comment.setReported(true);
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/update").accept(MediaType.APPLICATION_JSON).content(jsonComment.write(comment).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals( jsonComment.write(comment).getJson(),response.getContentAsString());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test4_deleteByIdTest(){
        persistJdd();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/delete/"+comment.getId()).accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.GONE.value(),response.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test5_deleteByAdventureId(){
        persistJdd();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/adventure/"+adventure.getId()).accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.GONE.value(),response.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }
}
