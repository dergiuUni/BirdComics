package com.birdcomics.GestioneOrdine.Service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.sql.SQLException;
import java.util.List;

import com.birdcomics.Bean.OrderBean;
import com.birdcomics.Bean.ProductBean;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Transaction;

public interface OrdineService {

    // Metodo per autorizzare un pagamento PayPal
    String authorizePayment(List<Transaction> product, Payer payer) throws PayPalRESTException;

    // Metodo per eseguire il pagamento PayPal
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;

    // Metodo per ottenere i dettagli di un pagamento PayPal
    Payment getPaymentDetails(String paymentId) throws PayPalRESTException;

	List<OrderBean> getOrdiniNonSpediti() throws SQLException;

	List<OrderBean> getOrdiniPerUtente(String userName) throws SQLException;

	void processPaymentAndCreateOrder(String paymentId, String payerId, String username) throws SQLException;
}
