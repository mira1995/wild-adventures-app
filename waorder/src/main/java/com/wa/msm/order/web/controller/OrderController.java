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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Api(description = "API pour les opérations CRUD sur les commandes")
@RestController
@RequestMapping(value = "/api")
public class OrderController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderSessionRepository orderSessionRepository;

    @Autowired
    private MSUserAccountProxy msUserAccountProxy;

    @Autowired
    private MSAdventureProxy msAdventureProxy;

    @ApiOperation(value = "Récupère toutes les commandes enregistrées (restreint aux admins)")
    @GetMapping(value = "/admin")
    public List<Order> getAllOrders(){
        log.info("Début méthode : getAllOrders()");
        List<Order> orders = orderRepository.findAll();
        if(orders == null || orders.isEmpty()) {
            log.error("Aucune commande enregistrée");
            throw  new OrderNotFoundException("Aucune commande enregistrée");
        }
        log.info("Récupération de toutes les commandes");
        return orders;
    }

    @ApiOperation(value = "Récupère toutes les commandes d'un utlisateur (réservée aux utilisateurs)")
    @GetMapping(value = "/user/{userId}")
    public List<Order> getAllOrdersByUser(@PathVariable Long userId){
        log.info("Début méthode : getAllOrdersByUser()");
        List<Order> orders = orderRepository.findByUserAccountId(userId);
        if(orders == null || orders.isEmpty()) {
            log.error("Aucune commande enregistrée pour l'utilisateur d'id : "+userId);
            throw  new OrderNotFoundException("Aucune commande enregistrée pour cet utilisateur");
        }
        log.info("Récupération de toutes les commandes de l'utilisateur d'id : "+userId);
        return orders;
    }

    @ApiOperation(value = "Récupère une commande en fonction de son id")
    @GetMapping(value = "/{orderId}")
    public Optional<Order> getOrder(@PathVariable Long orderId){
        log.info("Début méthode : getOrder()");
        Optional<Order> order = orderRepository.findById(orderId);
        if(!order.isPresent()) {
            log.error("Aucune commande enregistrée pour l'utilisateur d'id : "+orderId);
            throw new OrderNotFoundException("La commande d'id : "+ orderId + "n'existe pas");
        }
        log.info("Récupération de la commande d'id : "+orderId);
        return order;
    }

    @ApiOperation(value = "Créé une commande avec les informations fournies par l'utilisateur")
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        log.info("Début méthode : createOrder()");
        if(order == null) {
            log.error("La commande fournie est nulle");
            throw new OrderValidationException("La commande fournie est nulle");
        }
        if(order.getId()!=null){
            log.error("La commande fournie est déjà  à l'état persistant");
            throw new OrderValidationException("La commande fournie est déjà  à l'état persistant");
        }
        if(order.getOrderSessions().size()==0) {
            log.error("La commande fournie n'est reliée à aucune session d'aventure");
            throw new OrderValidationException(("La commande fournie n'est reliée à aucune session d'aventure"));
        }
        validateSessions(order.getOrderSessions());
        //Vérification que l'utilisateur fournie existe
        if(!msUserAccountProxy.getUserById(order.getUserAccountId()).isPresent()) {
            log.error("L'utilisateur lié à la commande n'existe pas");
            throw new OrderValidationException(("L'utilisateur lié à la commande n'existe pas"));
        }

        log.info("Création de la commande");
        order = orderRepository.save(order);

        final Long orderId = order.getId();
        if(!order.getOrderSessions().isEmpty()){
            validateSessions(order.getOrderSessions());

            //Si les sessions existent on sauvegarde les liens entre la commande et les sessions
            log.info("Enregistrement des sessions liés à la commande");
            order.getOrderSessions().iterator().forEachRemaining(orderSession -> {
                orderSession.setOrderId(orderId);
                orderSessionRepository.save(orderSession);
            });
        }

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Met à jour une commande avec les informations fournies par l'utilisateur (réservé aux admins)")
    @PatchMapping(value = "/admin")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order){
        log.info("Début méthode : updateOrder()");
        if(order == null) {
            log.error("La commande fournie est nulle");
            throw new OrderValidationException("La commande fournie est nulle");
        }
        if(order.getId()==null || !orderRepository.findById(order.getId()).isPresent()) {
            log.error("La commande fournie n' a pas encore été enregistrée");
            throw new OrderValidationException("La commande fournie n' a pas encore été enregistrée");
        }
        validateSessions(order.getOrderSessions());
        /*if(!order.getOrderSessions().isEmpty()) */

            /*order.getOrderSessions().forEach(orderSession -> );*/
        log.info("Mise à jour de la commande");
        return new ResponseEntity<>(orderRepository.save(order),HttpStatus.CREATED);
    }

    @ApiOperation(value = "Passe le statut de la commande dont l'id est fournie à PAYE")
    @PatchMapping(value = "/pay/{orderId}")
    public ResponseEntity<Order> payOrder(@PathVariable Long orderId){
        log.info("Début méthode : payOrder()");
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            log.error("La commande d'id : "+ orderId + "n'existe pas");
            throw new OrderNotFoundException("La commande d'id : "+ orderId + "n'existe pas");
        }
        if(order.get().getIsPaid()) {
            log.error("La commande fournie est déjà payée");
            throw new OrderValidationException("La commande fournie est déjà payée");
        }
        validateSessions(order.get().getOrderSessions());
        order.get().setIsPaid(true);
        order.get().setStatus(OrderStatusEnum.FINALIZED);
        log.info("Changement du statut de la commande d'id : "+orderId+" à Payé");
        return new ResponseEntity<>(orderRepository.save(order.get()), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Supprime une commande dont l'id est fournie en paramètre (réservé aux admins)")
    @DeleteMapping(value = "/admin/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId){
        log.info("Début méthode : deleteOrder()");
        Optional<Order> orderToDelete = orderRepository.findById(orderId);
        if(!orderToDelete.isPresent()) {
            log.error("La commande fournie n'existe pas");
            throw new OrderNotFoundException("La commande fournie n'existe pas");
        }
        else {
            log.info("Suppression de la commande d'id : "+orderId);
            orderRepository.deleteById(orderId);
        }
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
