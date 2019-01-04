package com.wa.msm.order.web.controller;

import com.stripe.model.Charge;
import com.wa.msm.order.bean.ChargeRequest;
import com.wa.msm.order.service.StripeService;
import com.wa.msm.order.web.exception.ChargeException;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Api(description = "API dédiée aux interractions avec le service de paiement stripe")
@RestController
@RequestMapping(value = "/api")
public class CheckoutController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StripeService stripeService;

    @CrossOrigin
    @PostMapping("/charge")
    public ResponseEntity<String> charge(@RequestBody ChargeRequest chargeRequest){
        log.info("Début de la méthode : charge()");
        Charge charge = null;
        try{
            DateFormat df = new SimpleDateFormat("DD/MM/yyyy HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String orderDate = df.format(today);
            chargeRequest.setDescription(orderDate+" wild adventures transaction number : "+Math.random());
            log.info("Appel du service de paiement et envci de la transaction");
            charge = stripeService.charge(chargeRequest);
        }catch( Exception e){
            log.error("Erreur lors de la transaction via Stripe");
            throw  new ChargeException("Erreur lors de la transaction via Stripe");
        }

        return new ResponseEntity<>("Paiement effectué", HttpStatus.ACCEPTED);
    }

}
