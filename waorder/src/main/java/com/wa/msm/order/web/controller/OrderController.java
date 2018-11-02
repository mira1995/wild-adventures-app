package com.wa.msm.order.web.controller;

import com.wa.msm.order.bean.SessionBean;
import com.wa.msm.order.entity.Order;
import com.wa.msm.order.entity.OrderSession;
import com.wa.msm.order.proxy.MSAdventureProxy;
import com.wa.msm.order.proxy.MSUserAccountProxy;
import com.wa.msm.order.repository.OrderRepository;
import com.wa.msm.order.repository.OrderSessionRepository;
import com.wa.msm.order.util.enumeration.OrderStatusEnum;
import com.wa.msm.order.web.exception.OrderNotFoundException;
import com.wa.msm.order.web.exception.OrderValidationException;
import com.wa.msm.order.web.exception.SessionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderSessionRepository orderSessionRepository;

    @Autowired
    private MSUserAccountProxy msUserAccountProxy;

    @Autowired
    private MSAdventureProxy msAdventureProxy;

    @GetMapping(value = "/orders")
    public List<Order> getAllOrders(){
        List<Order> orders = orderRepository.findAll();
        if(orders == null || orders.isEmpty()) throw  new OrderNotFoundException("Aucune commande enregistrée");
        return orders;
    }

    @GetMapping(value = "/orders/{userId}")
    public List<Order> getAllOrdersByUser(@PathVariable Long userId){
        List<Order> orders = orderRepository.findByUserAccountId(userId);
        if(orders == null || orders.isEmpty()) throw  new OrderNotFoundException("Aucune commande enregistrée pour cet utilisateur");
        return orders;
    }

    @GetMapping(value = "/order/{orderId}")
    public Optional<Order> getOrder(@PathVariable Long orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if(!order.isPresent()) throw new OrderNotFoundException("La commande d'id : "+ orderId + "n'existe pas");
        return order;
    }

    @PostMapping(value = "/order")
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        if(order == null) throw new OrderValidationException("La commande fournie est nulle");
        if(order.getId()!=null) throw new OrderValidationException("La commande fournie est déjà  à l'état persistant");
        if(order.getOrderSessions().size()==0) throw new OrderValidationException(("La commande fournie n'est reliée à aucune session d'aventure"));
        validateSessions(order.getOrderSessions());
        //Vérification que l'utilisateur fournie existe
        if(!msUserAccountProxy.getUserById(order.getUserAccountId()).isPresent()) throw new OrderValidationException(("L'utilisateur lié à la commande n'existe pas"));

        order = orderRepository.save(order);

        final Long orderId = order.getId();
        if(!order.getOrderSessions().isEmpty()){
            validateSessions(order.getOrderSessions());

            //Si les sessions existent on sauvegarde les liens entre la commande et les sessions
            order.getOrderSessions().iterator().forEachRemaining(orderSession -> {
                orderSession.setOrderId(orderId);
                orderSessionRepository.save(orderSession);
            });
        }

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/order")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order){
        if(order == null) throw new OrderValidationException("La commande fournie est nulle");
        if(order.getId()==null || !orderRepository.findById(order.getId()).isPresent()) throw new OrderValidationException("La commande fournie n' a pas encore été enregistrée");
        validateSessions(order.getOrderSessions());
        /*if(!order.getOrderSessions().isEmpty()) */

            /*order.getOrderSessions().forEach(orderSession -> );*/

        return new ResponseEntity<>(orderRepository.save(order),HttpStatus.CREATED);
    }

    @PatchMapping(value = "/order/pay/{orderId}")
    public ResponseEntity<Order> payOrder(@PathVariable Long orderId){
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) throw new OrderNotFoundException("La commande d'id : "+ orderId + "n'existe pas");
        if(order.get().getIsPaid()) throw new OrderValidationException("La commande fournie est déjà payée");
        validateSessions(order.get().getOrderSessions());
        order.get().setIsPaid(true);
        order.get().setStatus(OrderStatusEnum.FINALIZED);
        return new ResponseEntity<>(orderRepository.save(order.get()), HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/order/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId){
        Optional<Order> orderToDelete = orderRepository.findById(orderId);
        if(!orderToDelete.isPresent()) throw new OrderNotFoundException("La commande fournie n'existe pas");
        else orderRepository.deleteById(orderId);
        return new ResponseEntity<>("La commande d'id " + orderId + " a bien été supprimé", HttpStatus.GONE);
    }

    private void validateSessions(List<OrderSession> orderSessions){
        if(orderSessions.isEmpty())throw new SessionNotFoundException("Aucune session à rechercher");
        List<Long> sessionIdsList = new ArrayList<>(0);
        orderSessions.forEach(orderSession -> sessionIdsList.add(orderSession.getSessionId()));
        List<SessionBean> sessionBeans = msAdventureProxy.getAllById(sessionIdsList);
        if (sessionBeans == null || sessionBeans.size()!= sessionIdsList.size()) throw new SessionNotFoundException("Certaines des sessions fournies n'existent pas");
    }


}
