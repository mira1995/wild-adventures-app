package com.wa.msm.comment.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.comment.bean.AdventureBean;
import com.wa.msm.comment.entity.Comment;
import com.wa.msm.comment.proxy.MSAdventureProxy;
import com.wa.msm.comment.proxy.MSUserAccountProxy;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommentControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private CommentController commentController;

    @Mock
    MSAdventureProxy msAdventureProxy;

    @Mock
    MSUserAccountProxy msUserAccountProxy;

    private JacksonTester<Comment> jsonImage;

    private Comment comment;

    private AdventureBean adventure;

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
        adventure.setStatus("NOT_PAID");
        adventure.setLocation("Paris");
        adventure.setTitle("Aventure Test");
        adventure.setDescription("Aventure de test");
    }


    @Test
    public void addCommentTest(){
        Mockito.when(
                msAdventureProxy.getAdventure(Mockito.anyLong())).thenReturn(Optional.ofNullable(adventure));
    }
}
