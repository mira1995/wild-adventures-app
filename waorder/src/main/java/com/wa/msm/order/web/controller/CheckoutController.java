package com.wa.msm.order.web.controller;

import com.stripe.model.Charge;
import com.wa.msm.order.bean.ChargeRequest;
import com.wa.msm.order.service.StripeService;
import com.wa.msm.order.web.exception.ChargeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
public class CheckoutController {

    @Autowired
    private StripeService stripeService;

    @CrossOrigin
    @PostMapping("/charge")
    public ResponseEntity<Charge> charge(@RequestBody ChargeRequest chargeRequest){
        Charge charge = null;
        try{
            DateFormat df = new SimpleDateFormat("DD/MM/yyyy HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String orderDate = df.format(today);
            chargeRequest.setDescription(orderDate+" wild adventures transaction number : "+Math.random());
            charge = stripeService.charge(chargeRequest);
        }catch( Exception e){
            throw  new ChargeException("Erreur lors de la transaction via Stripe");
        }

        return new ResponseEntity<>(charge, HttpStatus.ACCEPTED);
    }

}
