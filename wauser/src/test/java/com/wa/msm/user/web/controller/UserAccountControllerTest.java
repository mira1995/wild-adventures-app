package com.wa.msm.user.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.user.AbstractUserAccountTest;
import com.wa.msm.user.bean.ImageTypeBean;
import com.wa.msm.user.bean.UserAccountImageBean;
import com.wa.msm.user.entity.UserAccount;
import com.wa.msm.user.proxy.MSImageProxy;
import com.wa.msm.user.repository.UserAccountRepository;
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

import java.util.Calendar;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserAccountControllerTest extends AbstractUserAccountTest {

    private MockMvc mockMvc;

    @MockBean
    private MSImageProxy msImageProxy;

    @Autowired
    @InjectMocks
    private UserAccountController userAccountController;

    private JacksonTester<UserAccount> jsonUserAccount;

    UserAccountImageBean profileImage;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    public void setUpTest(){
        ImageTypeBean imageTypeBean = new ImageTypeBean();
        imageTypeBean.setCode("USR");
        imageTypeBean.setName("user");

        profileImage = new UserAccountImageBean();
        profileImage.setAlt("test");
        profileImage.setDescription("test description");
        profileImage.setType(imageTypeBean);
        profileImage.setUri("/toto/titi.jpeg");
        profileImage.setId(1L);

        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(userAccountController)
                .build();
    }


    @Test
    public void test1_createUserAccountTest(){
        Mockito.when(
                msImageProxy.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(profileImage));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/").accept(MediaType.APPLICATION_JSON).content(jsonUserAccount.write(userAccount).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(jsonUserAccount.write(userAccount).getJson().substring(11), response.getContentAsString().substring(8));
            StringBuilder idString = new StringBuilder();
            idString.append(response.getContentAsString().charAt(6));
            userAccount.setId(Long.parseLong(idString.toString()));
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }

    @Test
    public void test2_getUserByIdTest(){
        persistJdd();
        Mockito.when(
                msImageProxy.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(profileImage));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/"+userAccount.getId()).accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(result.getResponse().getStatus() , HttpStatus.OK.value());
            Assertions.assertEquals(result.getResponse().getContentAsString().substring(0,50), jsonUserAccount.write(userAccount).getJson().substring(0,50));
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test3_updateUserAccount(){
        persistJdd();
        Mockito.when(
                msImageProxy.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(profileImage));
        ;
        Calendar cal = Calendar.getInstance();
        cal.set(2001, Calendar.JANUARY, 1);
        userAccount.setBirthDate(cal);
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/").accept(MediaType.APPLICATION_JSON).content(jsonUserAccount.write(userAccount).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(response.getContentAsString(), jsonUserAccount.write(userAccount).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }

    @Test
    public void test4_deleteUserAccount(){
        persistJdd();
        Mockito.when(
                msImageProxy.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(profileImage));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/"+userAccount.getId()).accept(MediaType.APPLICATION_JSON).content(jsonUserAccount.write(userAccount).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.GONE.value(),response.getStatus());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test5_getUserAccountByEmailTest(){
        persistJdd();
        Mockito.when(
                msImageProxy.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(profileImage));
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/email").accept(MediaType.APPLICATION_JSON).content(userAccount.getEmail()).contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(result.getResponse().getStatus() , HttpStatus.OK.value());
            Assertions.assertEquals(result.getResponse().getContentAsString().substring(0,50), jsonUserAccount.write(userAccount).getJson().substring(0,50));
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

}

