package com.birdcomics.GestioneOrdine.Controller;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.birdcomics.GestioneOrdine.Service.OrdineService;
import com.birdcomics.GestioneOrdine.Service.OrdineServiceImpl;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Transaction;

import java.util.List;

public class PaymentServices {

    private OrdineService ordineService;  // Dichiarazione del servizio

    // Costruttore che inizializza il servizio
    public PaymentServices() {
        this.ordineService = new OrdineServiceImpl();  // Instanza il servizio
    }

    public String authorizePayment(List<Transaction> product, Payer payer) throws PayPalRESTException {
        return ordineService.authorizePayment(product, payer);  // Delegazione al servizio
    }


    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        return ordineService.executePayment(paymentId, payerId);  // Delegazione al servizio
    }

    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
        return ordineService.getPaymentDetails(paymentId);  // Delegazione al servizio
    }
}
