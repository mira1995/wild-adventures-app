package com.wa.msm.order.web.controller;

import com.wa.msm.order.bean.AdventureBean;
import com.wa.msm.order.bean.SessionBean;
import com.wa.msm.order.bean.UserAccountBean;
import com.wa.msm.order.entity.Order;
import com.wa.msm.order.entity.OrderSession;
import com.wa.msm.order.repository.OrderRepository;
import com.wa.msm.order.repository.OrderSessionRepository;
import com.wa.msm.order.util.enumeration.OrderStatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class AbstractOrderControllerTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderSessionRepository orderSessionRepository;

    JacksonTester<Order> jsonOrder;

    JacksonTester<List<Order>> jsonOrderList;

    Order order;

    SessionBean sessionBean;

    SessionBean sessionBean2;

    AdventureBean adventure;

    UserAccountBean userAccount;

    List<OrderSession> orderSessions = new ArrayList<>();

    List<SessionBean> sessionBeanList = new ArrayList<>();

    Boolean isDeleted = false;


    @BeforeEach
    void setUpAll(){
        sessionBean = new SessionBean();
        sessionBean.setId(1L);
        sessionBean.setPrice(10D);
        sessionBean.setAdventureId(1L);
        Calendar cal = Calendar.getInstance();
        cal.set(2018, Calendar.JANUARY, 31);
        sessionBean.setStartDate(cal.getTime());
        cal.set(2018, Calendar.FEBRUARY, 1);
        sessionBean.setEndDate(cal.getTime());
        sessionBeanList.add(sessionBean);

        sessionBean2 = new SessionBean();
        sessionBean2.setId(2L);
        sessionBean2.setPrice(50D);
        sessionBean2.setAdventureId(1L);
        cal.set(2018, Calendar.JANUARY, 31);
        sessionBean2.setStartDate(cal.getTime());
        cal.set(2018, Calendar.FEBRUARY, 1);
        sessionBean2.setEndDate(cal.getTime());
        sessionBeanList.add(sessionBean2);

        adventure = new AdventureBean();
        adventure.setId(1L);
        adventure.setStatus("NOT_PAID");
        adventure.setLocation("Paris");
        adventure.setTitle("Aventure Test");
        adventure.setDescription("Aventure de test");

        userAccount = new UserAccountBean();
        userAccount.setId(1L);
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

        order = new Order();
        cal.set(2018, Calendar.JANUARY, 2);
        order.setOrderDate(cal);
        order.setStatus(OrderStatusEnum.NOT_PAID);
        order.setIsPaid(false);
        order.setUserAccountId(1L);

        OrderSession orderSession= new OrderSession();
        orderSession.setNbOrder(1L);
        orderSession.setSessionId(sessionBean.getId());
        orderSessions.add(orderSession);

        OrderSession orderSession2= new OrderSession();
        orderSession2.setNbOrder(2L);
        orderSession2.setSessionId(sessionBean2.getId());
        orderSessions.add(orderSession2);
        order.setOrderSessions(orderSessions);

    }


    void persistJddOrder(){
        order = orderRepository.save(order);
        final Long orderId = order.getId();
        order.getOrderSessions().iterator().forEachRemaining(orderSession -> {
            orderSession.setOrderId(orderId);
            orderSessionRepository.save(orderSession);
        });
    }
}
