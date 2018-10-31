package com.wa.msm.order.web.controller;

import com.wa.msm.order.bean.AdventureBean;
import com.wa.msm.order.bean.SessionBean;
import com.wa.msm.order.bean.UserAccountBean;
import com.wa.msm.order.entity.Order;
import com.wa.msm.order.entity.OrderSession;
import com.wa.msm.order.proxy.MSAdventureProxy;
import com.wa.msm.order.proxy.MSUserAccountProxy;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private OrderController orderController;

    @MockBean
    MSAdventureProxy msAdventureProxy;

    @MockBean
    MSUserAccountProxy msUserAccountProxy;

    private JacksonTester<Order> jsonOrder;

    private JacksonTester<List<Order>> jsonOrderList;

    private Order order;

    private SessionBean sessionBean;

    private AdventureBean adventure;

    private UserAccountBean userAccount;

    private List<OrderSession> orderSessions = new ArrayList<>();


    @Before
    public void setUp(){
        sessionBean = new SessionBean();
        sessionBean.setPrice(10D);
        sessionBean.setAdventureId(1L);
        Calendar cal = Calendar.getInstance();
        cal.set(2018, Calendar.JANUARY, 31);
        sessionBean.setStartDate(cal.getTime());
        cal.set(2018, Calendar.FEBRUARY, 1);
        sessionBean.setEndDate(cal.getTime());


        order = new Order();
        cal.set(2018, Calendar.JANUARY, 2);
        order.setOrderDate(cal);
        order.setUserAccountId(1L);

        OrderSession orderSession= new OrderSession();
        orderSession.setSessionId(1L);
        orderSession.setOrderId(1L);
        orderSessions.add(orderSession);

        OrderSession orderSession2= new OrderSession();
        orderSession2.setSessionId(2L);
        orderSession2.setOrderId(1L);
        orderSessions.add(orderSession2);

        order.setOrderSessions(orderSessions);

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

    public void test1_addOrderTest(){

    }
}
