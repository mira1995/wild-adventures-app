package com.wa.msm.order.web.controller;

import com.wa.msm.order.entity.Order;
import com.wa.msm.order.proxy.MSUserAccountProxy;
import com.wa.msm.order.repository.OrderRepository;
import com.wa.msm.order.repository.OrderSessionRepository;
import com.wa.msm.order.web.exception.OrderValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderSessionRepository orderSessionRepository;

    @Autowired
    private MSUserAccountProxy msUserAccountProxy;

    @PostMapping(value = "/order")
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        if(order == null) throw new OrderValidationException("La commande fournie est nulle");
        if(order.getId()!=null) throw new OrderValidationException("La commande fournie est déjà  à l'état persistant");
        if(order.getOrderSessions().size()==0) throw new OrderValidationException(("La commande fournie n'est reliée à aucune session d'aventure"));
        //TODO Vérifier que les sessions liées à la commande existent

        //Vérification que l'utilisateur fournie existe
        if(!msUserAccountProxy.getUserById(order.getUserAccountId()).isPresent()) throw new OrderValidationException(("L'utilisateur lié à la commande n'existe pas"));

        order = orderRepository.save(order);

        return new ResponseEntity<Order>(order, HttpStatus.CREATED);
    }
}
