/**
 * ReviewPaymentServlet class - show review payment page.
 * @author Nam Ha Minh
 * @copyright https://codeJava.net
 */
package com.birdcomics.GestioneOrdine;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.apache.catalina.User;

import com.birdcomics.GestioneCarrello.CartServiceDAO;
import com.birdcomics.GestioneCatalogo.ProductBean;
import com.birdcomics.GestioneCatalogo.ProductServiceDAO;
import com.birdcomics.GestioneProfili.UserBean;
import com.birdcomics.GestioneProfili.UserServiceDAO;
import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;


@WebServlet("/review_payment")
public class ReviewPaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ReviewPaymentServlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String paymentId = request.getParameter("paymentId");
		String payerId = request.getParameter("PayerID");
		
		
		/*
		PayerInfo payerInfo = payment.getPayer().getPayerInfo();
		Transaction transaction = payment.getTransactions().get(0);
		*/
		
		
		try {
			PaymentServices paymentServices = new PaymentServices();
			Payment payment = paymentServices.executePayment(paymentId, payerId);
			
			//devo azzerare il carrello del cliente
			CartServiceDAO c = new CartServiceDAO();
			
			
			//////////////////////////////////////////////////////////////////////
			PayerInfo payerInfo = payment.getPayer().getPayerInfo();
			UserBean u = new UserBean();
			UserServiceDAO us = new UserServiceDAO();
			u = us.getUserDetails(payerInfo.getEmail(), request.getParameter("password"));
			c.deleteAllCartItems(payerInfo.getEmail());
			Transaction transaction = payment.getTransactions().get(0);
			ItemList itemList = transaction.getItemList();
			List<Item> items = itemList.getItems();
			String transactionId = transaction.getRelatedResources().get(0).getSale().getId();

			
			OrderBean o = new OrderBean(payerInfo.getEmail(), transactionId, false, java.sql.Date.valueOf(LocalDate.now()));
			// imposto i dettagli dell'ordine
			
			for (Item item : items) {
				ProductBean p = new ProductBean();
				ProductServiceDAO pa = new ProductServiceDAO();
				
				p = pa.getProductsByID(item.getSku());
				o.addFumetti(p, Integer.valueOf(item.getQuantity()));
			}
			
			//manca fare la fattura nell'ordine

			
		
			
			
	
			response.sendRedirect("index.jsp");
			
			
			
		} catch (PayPalRESTException ex) {
			request.setAttribute("errorMessage", ex.getMessage());
			ex.printStackTrace();
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

}
