package com.wa.msm.order.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.order.entity.OrderDemand;
import com.wa.msm.order.entity.OrderDemandSession;
import com.wa.msm.order.proxy.MSAdventureProxy;
import com.wa.msm.order.proxy.MSUserAccountProxy;

import com.wa.msm.order.repository.OrderDemandRepository;
import com.wa.msm.order.repository.OrderDemandSessionRepository;
import com.wa.msm.order.util.enumeration.OrderDemandEnum;
import com.wa.msm.order.util.enumeration.OrderStatusEnum;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DemandControllerTest extends AbstractOrderControllerTest{

    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private DemandController demandController;

    @MockBean
    MSAdventureProxy msAdventureProxy;

    @MockBean
    MSUserAccountProxy msUserAccountProxy;

    @Autowired
    private OrderDemandRepository orderDemandRepository;

    @Autowired
    private OrderDemandSessionRepository orderDemandSessionRepository;

    private OrderDemand orderDemand;

    private JacksonTester<OrderDemand> jsonDemand;

    private JacksonTester<List<OrderDemand>> jsonListDemand;

    private JacksonTester<OrderDemandEnum> jsonOrderDemandEnum;

    @BeforeEach
    void setUp(){
        persistJddOrder();

        orderDemand = new OrderDemand();

        OrderDemandSession orderDemandSession = new OrderDemandSession();
        orderDemandSession.setNbOrder(1L);
        orderDemandSession.setSessionId(sessionBean.getId());
        orderDemand.getOrderDemandSessions().add(orderDemandSession);

        OrderDemandSession orderDemandSession1 = new OrderDemandSession();
        orderDemandSession1.setNbOrder(2L);
        orderDemandSession1.setSessionId(sessionBean2.getId());
        orderDemand.getOrderDemandSessions().add(orderDemandSession1);

        Calendar cal = Calendar.getInstance();
        cal.set(2018, Calendar.JANUARY, 2);
        orderDemand.setOrderDate(cal);
        orderDemand.setStatus(OrderStatusEnum.NOT_PAID);
        orderDemand.setIsPaid(false);
        orderDemand.setUserAccountId(1L);
        orderDemand.setDemandStatus(OrderDemandEnum.OPENED_DEMAND);
        orderDemand.setDemandMessage("test");
        orderDemand.setAnswerMessage("test");
        orderDemand.setOrder(order);

        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(demandController)
                .build();
    }

    @AfterEach
    @Transactional
    public void afterTest(){
        if(!isDeleted)orderDemandRepository.delete(orderDemand); }

    @Test
    void test1_createDemandTest(){
        Mockito.when(
                msAdventureProxy.getAllById(Mockito.anyList())).thenReturn(sessionBeanList);
        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/demands").accept(MediaType.APPLICATION_JSON).content(jsonDemand.write(orderDemand).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(jsonDemand.write(orderDemand).getJson().substring(11,81), response.getContentAsString().substring(8,78));
            StringBuilder idString = new StringBuilder();
            idString.append(response.getContentAsString().charAt(6));
            orderDemand.setId(Long.parseLong(idString.toString()));
            orderDemand.getOrderDemandSessions().iterator().forEachRemaining(orderDemandSession -> orderDemandSession.setDemandId(Long.parseLong(idString.toString())));
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    void test2_updateOrderTest(){
        Mockito.when(
                msAdventureProxy.getAllById(Mockito.anyList())).thenReturn(sessionBeanList);
        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));

        persistJddDemand();
        orderDemand.setDemandMessage("Bonjour je voudrais modifier ma commande");
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/demands").accept(MediaType.APPLICATION_JSON).content(jsonDemand.write(orderDemand).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(response.getContentAsString(), jsonDemand.write(orderDemand).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    void test3_validateUpdateDemand(){
        Mockito.when(
                msAdventureProxy.getAllById(Mockito.anyList())).thenReturn(sessionBeanList);
        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));
        orderDemand.setStatus(OrderStatusEnum.UPDATE_DEMAND);
        persistJddDemand();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/demands/admin/validate/update").accept(MediaType.APPLICATION_JSON).content(jsonDemand.write(orderDemand).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            orderDemand.setDemandStatus(OrderDemandEnum.VALIDATED_DEMAND);
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(response.getContentAsString().substring(0,50), jsonDemand.write(orderDemand).getJson().substring(0,50));

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    void test4_validateDeleteDemand(){
        Mockito.when(
                msAdventureProxy.getAllById(Mockito.anyList())).thenReturn(sessionBeanList);
        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));
        orderDemand.setStatus(OrderStatusEnum.DELETE_DEMAND);
        persistJddDemand();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/demands/admin/validate/delete").accept(MediaType.APPLICATION_JSON).content(jsonDemand.write(orderDemand).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            orderDemand.setDemandStatus(OrderDemandEnum.VALIDATED_DEMAND);
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(response.getContentAsString().substring(0,50), jsonDemand.write(orderDemand).getJson().substring(0,50));

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    void test5_deleteDemand(){
        persistJddDemand();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/demands/"+orderDemand.getId()).accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.GONE.value(),response.getStatus());
            isDeleted = true;
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    void test6_getOrderDemand(){
        persistJddDemand();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/demands/admin/"+orderDemand.getId()).accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
            Assertions.assertEquals(result.getResponse().getContentAsString(), jsonDemand.write(orderDemand).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    void test7_getAllDemands(){
        persistJddDemand();
        List<OrderDemand> orderDemands = new ArrayList<>();
        orderDemands.add(orderDemand);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/demands/admin").accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
            Assertions.assertEquals(result.getResponse().getContentAsString(), jsonListDemand.write(orderDemands).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    void test8_getAllDemandsByUser(){
        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));

        persistJddDemand();
        List<OrderDemand> orderDemands = new ArrayList<>();
        orderDemands.add(orderDemand);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/demands/user/"+userAccount.getId()).accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
            Assertions.assertEquals(result.getResponse().getContentAsString(), jsonListDemand.write(orderDemands).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    void test9_getAllDemandsByStatus(){
        persistJddDemand();
        List<OrderDemand> orderDemands = new ArrayList<>();
        orderDemands.add(orderDemand);

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/demands/admin/status").accept(MediaType.APPLICATION_JSON).content(jsonOrderDemandEnum.write(orderDemand.getDemandStatus()).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
            Assertions.assertEquals(result.getResponse().getContentAsString(), jsonListDemand.write(orderDemands).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    private void persistJddDemand(){
        orderDemand = orderDemandRepository.save(orderDemand);
        final Long demandId = orderDemand.getId();
        orderDemand.getOrderDemandSessions().iterator().forEachRemaining(orderSession -> {
            orderSession.setDemandId(demandId);
            orderDemandSessionRepository.save(orderSession);
        });
    }
}
