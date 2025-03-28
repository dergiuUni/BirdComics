package com.birdcomics.GestioneOrdine.Service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.birdcomics.Model.Bean.OrderBean;
import com.birdcomics.Model.Bean.ProductBean;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Transaction;

public interface OrdineService {

    // Metodo per autorizzare un pagamento PayPal
    String authorizePayment(List<Transaction> product, Payer payer) throws PayPalRESTException;

    // Metodo per eseguire il pagamento PayPal
    Payment processaPagamento(String paymentId, String payerId) throws PayPalRESTException;

    // Metodo per ottenere i dettagli di un pagamento PayPal
    Payment getPaymentDetails(String paymentId) throws PayPalRESTException;

	List<OrderBean> getOrdiniNonSpediti() throws SQLException;

	List<OrderBean> getOrdiniPerUtente(String email) throws SQLException;

	void creaOrdine(String paymentId, String payerId, String email, HttpSession session) throws SQLException;
}
