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

    @BeforeEach
    void setUp(){
        persistJddOrder();

        orderDemand = new OrderDemand();

        OrderDemandSession orderDemandSession = new OrderDemandSession();
        orderDemandSession.setSessionId(sessionBean.getId());
        orderDemand.getOrderDemandSessions().add(orderDemandSession);

        OrderDemandSession orderDemandSession1 = new OrderDemandSession();
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
        if(orderDemand.getId() !=null && !isDeleted) orderDemandRepository.delete(orderDemand);
        if(orderDemand.getId() !=null && order.getId() !=null && !isDeleted) orderRepository.delete(order);
    }

    @Test
    void test1_createDemandTest(){
        Mockito.when(
                msAdventureProxy.getAllById(Mockito.anyList())).thenReturn(sessionBeanList);
        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/demand").accept(MediaType.APPLICATION_JSON).content(jsonDemand.write(orderDemand).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(jsonDemand.write(orderDemand).getJson().substring(11,81), response.getContentAsString().substring(8,78));

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
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/demand").accept(MediaType.APPLICATION_JSON).content(jsonDemand.write(orderDemand).getJson()).contentType(MediaType.APPLICATION_JSON) ;
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
    @Transactional
    void test3_validateUpdateDemand(){
        Mockito.when(
                msAdventureProxy.getAllById(Mockito.anyList())).thenReturn(sessionBeanList);
        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));
        orderDemand.setStatus(OrderStatusEnum.UPDATE_DEMAND);
        persistJddDemand();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/demand/validate/update").accept(MediaType.APPLICATION_JSON).content(jsonDemand.write(orderDemand).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(response.getContentAsString(), jsonDemand.write(orderDemand).getJson());

        }catch (Exception e){
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
