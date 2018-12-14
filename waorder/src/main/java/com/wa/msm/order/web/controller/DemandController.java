package com.wa.msm.order.web.controller;

import com.wa.msm.order.bean.SessionBean;
import com.wa.msm.order.entity.OrderDemand;
import com.wa.msm.order.entity.OrderDemandSession;
import com.wa.msm.order.entity.OrderSession;
import com.wa.msm.order.proxy.MSAdventureProxy;
import com.wa.msm.order.proxy.MSUserAccountProxy;
import com.wa.msm.order.repository.OrderDemandRepository;
import com.wa.msm.order.repository.OrderDemandSessionRepository;
import com.wa.msm.order.repository.OrderRepository;
import com.wa.msm.order.repository.OrderSessionRepository;
import com.wa.msm.order.util.enumeration.OrderDemandEnum;
import com.wa.msm.order.util.enumeration.OrderStatusEnum;
import com.wa.msm.order.web.exception.*;
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

@Api(description = "API pour les opérations CRUD sur les demandes liés aux commandes")
@RestController
@RequestMapping(value = "/api/demands")
public class DemandController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderDemandRepository orderDemandRepository;

    @Autowired
    private OrderDemandSessionRepository orderDemandSessionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderSessionRepository orderSessionRepository;

    @Autowired
    private MSUserAccountProxy msUserAccountProxy;

    @Autowired
    private MSAdventureProxy msAdventureProxy;

    @ApiOperation(value = "Récupère une demande par son id (restreint aux admins)")
    @GetMapping("/admin/{demandId}")
    public Optional<OrderDemand> getOrderDemand(@PathVariable Long demandId){
        log.info("Début méthode : getOrderDemand()");
        Optional<OrderDemand> orderDemand = null;
        try{
            orderDemand = orderDemandRepository.findById(demandId);
            if(!orderDemand.isPresent()) {
                log.error("La demande d'id : "+demandId+" n'a pas été trouvée");
                throw new OrderDemandNotFoundException("La demande n'a pas été trouvée");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("Récupération de la demande d'id : "+demandId);
        return orderDemand;
    }

    @ApiOperation(value = "Récupère toutes les demandes (restreint aux admins)")
    @GetMapping("/admin")
    public List<OrderDemand> getAllDemands(){
        log.info("Début méthode : getAllDemands()");
        List<OrderDemand> orderDemands = orderDemandRepository.findAll();
        if(orderDemands == null || orderDemands.isEmpty()) {
            log.error("Il n'existe actuellement aucune demande");
            throw new OrderDemandNotFoundException("Il n'existe actuellement aucune demande");
        }
        log.info("Récupération de toutes les demandes");
        return orderDemands;
    }

    @ApiOperation(value = "Récupère toutes les demandes d'un utilisateur grâce à son id")
    @GetMapping("/user/{userId}")
    public List<OrderDemand> getAllDemandsByUser(@PathVariable Long userId){
        log.info("Début méthode : getAllDemandsByUser()");
        if(!msUserAccountProxy.getUserById(userId).isPresent()) {
            log.error("L'utilisateur d'id "+userId+"n'existe pas");
            throw new UserAccountNotFoundException("L'utilisateur d'id "+userId+"n'existe pas");
        }
        List<OrderDemand> orderDemands = orderDemandRepository.findByUserAccountId(userId);
        if(orderDemands == null || orderDemands.isEmpty()) {
            log.error("Il n'existe actuellement aucune demande correspondant à cet utilisateur");
            throw new OrderDemandNotFoundException("Il n'existe actuellement aucune demande correspondant à cet utilisateur");
        }
        log.info("Récupération des demandes de l'utilisateur d'id : "+userId);
        return orderDemands;
    }

    @ApiOperation(value = "Récupère toutes les demandes en fonction du statut fournie en paramètre (restreint aux admins)")
    @PostMapping("/admin/status")
    public List<OrderDemand> getAllDemandsByStatus(@RequestBody OrderDemandEnum status){
        log.info("Début méthode : getAllDemandsByStatus()");
        List<OrderDemand> orderDemands = orderDemandRepository.findByDemandStatus(status);
        if(orderDemands == null  || orderDemands.isEmpty()) {
            log.error("Il n'existe actuellement aucune demande correspondant à ce statut : "+status.toString());
            throw new OrderDemandNotFoundException("Il n'existe actuellement aucune demande correspondant à ce statut");
        }
        log.info("Récupération de toutes les demandes ayant pour statut : "+status.toString());
        return orderDemands;
    }

    @ApiOperation(value = "Créé une demande avec les informations fournies par l'utilisateur")
    @PostMapping
    public ResponseEntity<OrderDemand> createDemand(@RequestBody OrderDemand orderDemand){
        log.info("Début méthode : getAllDemandsByStatus()");
        validateOrderDemand(orderDemand);
        if(orderDemand.getId()!= null) {
            log.error("La demande a déjà été enregistrée");
            throw new OrderDemandValidationException("La demande a déjà été enregistrée");
        }

        final OrderDemand orderDemandSaved = orderDemandRepository.save(orderDemand);
        validateSessions(orderDemand.getOrderDemandSessions());
        orderDemandSaved.getOrderDemandSessions().iterator().forEachRemaining(orderDemandSession -> {
            orderDemandSession.setDemandId(orderDemandSaved.getId());
            orderDemandSessionRepository.save(orderDemandSession);
        });
        orderRepository.save(orderDemand.getOrder());
        log.info("Création de la demande");
        return new ResponseEntity<>(orderDemand, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Met à jour une demande avec les informations fournies par l'utilisateur")
    @PatchMapping
    public ResponseEntity<OrderDemand> updateDemand(@RequestBody OrderDemand orderDemand){
        log.info("Début méthode : updateDemand()");
        validateOrderDemand(orderDemand);
        if(orderDemand.getId() == null || !orderDemandRepository.findById(orderDemand.getId()).isPresent()) {
            log.error("La demande fournie n'existe pas");
            throw new OrderDemandNotFoundException("La demande fournie n'existe pas");
        }
        validateSessions(orderDemand.getOrderDemandSessions());
        log.info("Mise à jour de la demande");
        return new ResponseEntity<>(orderDemandRepository.save(orderDemand), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Valide la demande de mise à jour de la commande (restreint aux admins)")
    @PatchMapping("/admin/validate/update")
    public ResponseEntity<OrderDemand> validateUpdateDemand(@RequestBody OrderDemand orderDemand){
        log.info("Début méthode : validateUpdateDemand()");
        validateOrderDemand(orderDemand);
        if(orderDemand.getId() == null || !orderDemandRepository.findById(orderDemand.getId()).isPresent()) {
            log.error("La demande fournie n'existe pas");
            throw new OrderDemandNotFoundException("La demande fournie n'existe pas");
        }
        validateSessions(orderDemand.getOrderDemandSessions());
        if(!orderDemand.getStatus().equals(OrderStatusEnum.UPDATE_DEMAND)) {
            log.error("Le statut de la demande est incorrect");
            throw new OrderDemandValidationException("Le statut de la demande est incorrect");
        }
        populateOrderWithDemandUpdate(orderDemand);
        orderDemand.setDemandStatus(OrderDemandEnum.VALIDATED_DEMAND);
        orderDemandRepository.save(orderDemand);
        log.info("Passage du statut de la demande à validé");
        return new ResponseEntity<>(orderDemand, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Valide la demande de suppression de la commande (restreint aux admins)")
    @PatchMapping("/admin/validate/delete")
    public ResponseEntity<OrderDemand> validateDeleteDemand(@RequestBody OrderDemand orderDemand){
        log.info("Début méthode : validateDeleteDemand()");
        validateOrderDemand(orderDemand);
        if(orderDemand.getId() == null || !orderDemandRepository.findById(orderDemand.getId()).isPresent()) {
            log.error("La demande fournie n'existe pas");
            throw new OrderDemandNotFoundException("La demande fournie n'existe pas");
        }
        validateSessions(orderDemand.getOrderDemandSessions());
        if(!orderDemand.getStatus().equals(OrderStatusEnum.DELETE_DEMAND)) {
            log.error("Le statut de la demande est incorrect");
            throw new OrderDemandValidationException("Le statut de la demande est incorrect");
        }
        orderDemand.getOrder().setStatus(OrderStatusEnum.CANCELED);
        orderDemand.setDemandStatus(OrderDemandEnum.VALIDATED_DEMAND);
        orderDemandRepository.save(orderDemand);
        log.info("Passage du statut de la demande à validé");
        return new ResponseEntity<>(orderDemand, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Supprime une demande")
    @DeleteMapping("/{demandId}")
    public ResponseEntity<String> deleteOrderDemand(@PathVariable Long demandId){
        log.info("Début méthode : deleteOrderDemand()");
        Optional<OrderDemand> orderDemand = orderDemandRepository.findById(demandId);
        if(!orderDemand.isPresent()) {
            log.error("La demande n'a pu être trouvée");
            throw new OrderDemandNotFoundException("La demande n'a pu être trouvée");
        }
        orderDemandRepository.deleteById(demandId);
        log.info("Suppression de la demande d'id : "+demandId);
        return new ResponseEntity<>("La demande d'id : "+ demandId + " a bien été supprimée", HttpStatus.GONE);
    }

    private void validateOrderDemand(OrderDemand orderDemand){
        if(orderDemand == null) throw new OrderDemandValidationException("La demande fournie  est nulle");
        if(orderDemand.getOrder() == null) throw new OrderDemandValidationException("La demande n'est lié à aucune commande");
        if(!orderRepository.findById(orderDemand.getOrder().getId()).isPresent()) throw new OrderNotFoundException("La commande d'id "+ orderDemand.getOrder().getId() +" n'existe pas");
        if(!msUserAccountProxy.getUserById(orderDemand.getUserAccountId()).isPresent()) throw new UserAccountNotFoundException("L'utilisateur lié à cette demande n'existe pas ");
        if(orderDemand.getOrderDemandSessions().isEmpty()) throw new OrderDemandValidationException("La commande liée à cette demande n'est liée à aucune Session");
    }

    private void populateOrderWithDemandUpdate(OrderDemand orderDemand){

        orderDemand.getOrder().setIsPaid(orderDemand.getIsPaid());

        if(orderDemand.getOrder().getIsPaid()){
            orderDemand.getOrder().setStatus(OrderStatusEnum.FINALIZED);
        }else{
            orderDemand.getOrder().setStatus(OrderStatusEnum.NOT_PAID);
        }

        if(orderDemand.getStatus().equals(OrderStatusEnum.UPDATE_DEMAND)){
            orderSessionRepository.deleteAll(orderDemand.getOrder().getOrderSessions());
        }
        List<OrderSession> orderSessions = new ArrayList<>(0);
        orderDemand.getOrder().getOrderSessions().iterator().forEachRemaining(orderSession -> {
            orderSessionRepository.delete(orderSession);
        });
        orderDemand.getOrderDemandSessions().iterator().forEachRemaining(orderDemandSession -> {
                OrderSession orderSession = new OrderSession(orderDemand.getOrder(),orderDemandSession.getNbOrder(), orderDemand.getOrder().getId(), orderDemandSession.getSessionId());
                orderSessions.add(orderSession);
        });
        orderDemand.getOrder().setOrderSessions(orderSessions);

        orderDemand.getOrder().setUserAccountId(orderDemand.getUserAccountId());
        orderDemand.getOrder().setOrderDate(orderDemand.getOrderDate());
    }

    private void validateSessions(List<OrderDemandSession> orderDemandSessions){
        if(orderDemandSessions.isEmpty())throw new SessionNotFoundException("Aucune session à rechercher");
        List<Long> sessionIdsList = new ArrayList<>(0);
        orderDemandSessions.forEach(orderSession -> sessionIdsList.add(orderSession.getSessionId()));
        List<SessionBean> sessionBeans = msAdventureProxy.getAllById(sessionIdsList);
        if (sessionBeans == null || sessionBeans.size()!= sessionIdsList.size()) throw new SessionNotFoundException("Certaines des sessions fournies n'existent pas");
    }
}
