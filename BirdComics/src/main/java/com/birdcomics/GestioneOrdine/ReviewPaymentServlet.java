/**
 * ReviewPaymentServlet class - show review payment page.
 * @author Nam Ha Minh
 * @copyright https://codeJava.net
 */
package com.birdcomics.GestioneOrdine;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

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
			
			HttpSession session = request.getSession();
			PayerInfo payerInfo = payment.getPayer().getPayerInfo();
			UserBean u = new UserBean();
			UserServiceDAO us = new UserServiceDAO();
			System.out.println((String) session.getAttribute("username") );
			
			u = us.getUserDetails((String) session.getAttribute("username"));
			
			CartServiceDAO c = new CartServiceDAO();
			c.deleteAllCartItems((String) session.getAttribute("username"));
			Transaction transaction = payment.getTransactions().get(0);
			ItemList itemList = transaction.getItemList();
			List<Item> items = itemList.getItems();
			
			String dataNascitaStr = LocalDate.now().toString();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Imposta il fuso orario a UTC per evitare offset
			java.util.Date parsedDate;
			java.sql.Date dataNascita = null;
			try {
				parsedDate = sdf.parse(dataNascitaStr);
				dataNascita = new java.sql.Date(parsedDate.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			FatturaBean f = new FatturaBean(22, u.getNome(), u.getCognome(), u.getNumeroTelefono(), u.getIndirizzo().getNomeCitta(), u.getIndirizzo().getVia(), u.getIndirizzo().getNumeroCivico(), u.getIndirizzo().getCap() );
			OrderBean o = new OrderBean(u.getEmail(), paymentId, "Non Spedito", dataNascita);
			// imposto i dettagli dell'ordine
			o.setIdFattura(f);
			
			for (Item item : items) {
				ProductBean p = new ProductBean();
				ProductServiceDAO pa = new ProductServiceDAO();
				
				p = pa.getProductsByID(item.getSku());
				o.addFumetti(p, Integer.valueOf(item.getQuantity()));
			}
			
			OrderServiceDAO os = new OrderServiceDAO();
			os.addOrder(o);
			
			
		
			
			
	
			response.sendRedirect("index.jsp");
			
			
			
		} catch (PayPalRESTException | SQLException ex) {
			request.setAttribute("errorMessage", ex.getMessage());
			ex.printStackTrace();
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} 	
		
	}

}
