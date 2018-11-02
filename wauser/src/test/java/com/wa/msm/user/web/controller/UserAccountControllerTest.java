package com.wa.msm.user.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.user.AbstractUserAccountTest;
import com.wa.msm.user.bean.ImageTypeBean;
import com.wa.msm.user.bean.UserAccountImageBean;
import com.wa.msm.user.entity.UserAccount;
import com.wa.msm.user.proxy.MSImageProxy;
import com.wa.msm.user.repository.UserAccountRepository;
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

import java.util.Calendar;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAccountControllerTest extends AbstractUserAccountTest {

    private MockMvc mockMvc;

    @MockBean
    private MSImageProxy msImageProxy;

    @Autowired
    @InjectMocks
    private UserAccountController userAccountController;

    private JacksonTester<UserAccount> jsonUserAccount;

    UserAccountImageBean profileImage;

    @Before
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
                msImageProxy.getUserAccountImageById(Mockito.anyLong())).thenReturn(Optional.ofNullable(profileImage));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user").accept(MediaType.APPLICATION_JSON).content(jsonUserAccount.write(userAccount).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.OK.value(),response.getStatus());
            userAccount.setId(1L);
            Assert.assertEquals(response.getContentAsString(), jsonUserAccount.write(userAccount).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }

    @Test
    public void test2_getUserByIdTest(){
        Mockito.when(
                msImageProxy.getUserAccountImageById(Mockito.anyLong())).thenReturn(Optional.ofNullable(profileImage));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/1").accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            userAccount.setId(1L);
            Assert.assertEquals(result.getResponse().getStatus() , HttpStatus.OK.value());
            Assert.assertEquals(result.getResponse().getContentAsString().substring(0,50), jsonUserAccount.write(userAccount).getJson().substring(0,50));
        }catch(Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    public void test3_updateUserAccount(){
        Mockito.when(
                msImageProxy.getUserAccountImageById(Mockito.anyLong())).thenReturn(Optional.ofNullable(profileImage));

        userAccount.setId(1L);
        Calendar cal = Calendar.getInstance();
        cal.set(2001, Calendar.JANUARY, 1);
        userAccount.setBirthDate(cal);
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/user").accept(MediaType.APPLICATION_JSON).content(jsonUserAccount.write(userAccount).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.OK.value(),response.getStatus());
            Assert.assertEquals(response.getContentAsString(), jsonUserAccount.write(userAccount).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }

    @Test
    public void test4_deleteUserAccount(){
        Mockito.when(
                msImageProxy.getUserAccountImageById(Mockito.anyLong())).thenReturn(Optional.ofNullable(profileImage));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/user/1").accept(MediaType.APPLICATION_JSON).content(jsonUserAccount.write(userAccount).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.OK.value(),response.getStatus());

        }catch (Exception e){
            e.printStackTrace();
            Assert.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }
}

