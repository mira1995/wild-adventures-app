package com.wa.msm.order.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wa.msm.order.entity.Order;
import com.wa.msm.order.proxy.MSAdventureProxy;
import com.wa.msm.order.proxy.MSUserAccountProxy;

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
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderControllerTest extends AbstractOrderControllerTest{
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private OrderController orderController;

    @MockBean
    MSAdventureProxy msAdventureProxy;

    @MockBean
    MSUserAccountProxy msUserAccountProxy;

    @BeforeEach
    void setUp(){

        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .build();
    }

    @AfterEach
    @Transactional
    public void afterTest(){
        if(order.getId() !=null && !isDeleted) orderRepository.delete(order);
    }

    @Test
    void test1_createOrderTest(){
        Mockito.when(
                msAdventureProxy.getAllById(Mockito.anyList())).thenReturn(sessionBeanList);
        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));

        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/order").accept(MediaType.APPLICATION_JSON).content(jsonOrder.write(order).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(jsonOrder.write(order).getJson().substring(11,81), response.getContentAsString().substring(8,78));

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    void test2_updateOrderTest(){
        Mockito.when(
                msAdventureProxy.getAllById(Mockito.anyList())).thenReturn(sessionBeanList);
        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));

        persistJddOrder();
        order.setIsPaid(true);
        order.setStatus(OrderStatusEnum.FINALIZED);
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/order").accept(MediaType.APPLICATION_JSON).content(jsonOrder.write(order).getJson()).contentType(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(response.getContentAsString(), jsonOrder.write(order).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    void test3_payOrderTest(){
        Mockito.when(
                msAdventureProxy.getAllById(Mockito.anyList())).thenReturn(sessionBeanList);
        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));

        persistJddOrder();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/order/pay/"+order.getId()).accept(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            order.setIsPaid(true);
            order.setStatus(OrderStatusEnum.FINALIZED);
            Assertions.assertEquals(HttpStatus.CREATED.value(),response.getStatus());
            Assertions.assertEquals(response.getContentAsString(), jsonOrder.write(order).getJson());

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    void test4_deleteOrderTest(){
        persistJddOrder();
        try{
            RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/order/"+order.getId()).accept(MediaType.APPLICATION_JSON) ;
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assertions.assertEquals(HttpStatus.GONE.value(),response.getStatus());
            isDeleted=true;

        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }

    @Test
    @Transactional
    void test5_getAllOrdersTest(){

        persistJddOrder();
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orders").accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
            Assertions.assertEquals(result.getResponse().getContentAsString(), jsonOrderList.write(orders).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }

    @Test
    @Transactional
    void test6_getAllOrdersByUserTest(){

        Mockito.when(
                msUserAccountProxy.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userAccount));

        persistJddOrder();
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orders/"+userAccount.getId()).accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
            Assertions.assertEquals(result.getResponse().getContentAsString(), jsonOrderList.write(orders).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }

    }

    @Test
    @Transactional
    void test7_getOrderTest(){
        persistJddOrder();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/order/"+order.getId()).accept(MediaType.APPLICATION_JSON);

        try{
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
            Assertions.assertEquals(result.getResponse().getContentAsString(), jsonOrder.write(order).getJson());
        }catch(Exception e){
            e.printStackTrace();
            Assertions.fail("Erreur lors de l'envoi de la requête au controleur REST");
        }
    }
}
