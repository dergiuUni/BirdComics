/**
 * AuthorizePaymentServlet class - requests PayPal for payment.
 * @author Nam Ha Minh
 * @copyright https://codeJava.net
 */
package com.birdcomics.GestioneOrdine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.birdcomics.GestioneCarrello.CartBean;
import com.birdcomics.GestioneCarrello.CartServiceDAO;
import com.birdcomics.GestioneCatalogo.ProductBean;
import com.birdcomics.GestioneCatalogo.ProductServiceDAO;
import com.birdcomics.GestioneProfili.UserBean;
import com.birdcomics.GestioneProfili.UserServiceDAO;
import com.paypal.api.payments.Address;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;


@WebServlet("/authorize_payment")
public class AuthorizePaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AuthorizePaymentServlet() {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CartServiceDAO carts = new CartServiceDAO();
		ProductBean p = new ProductBean();
		ProductServiceDAO ps = new ProductServiceDAO();
		List<CartBean> ca = new ArrayList<CartBean>();
		List<Item> items = new ArrayList<>();
		Transaction transaction = new Transaction();
		ItemList itemList = new ItemList();
		PaymentServices paymentServices = new PaymentServices();
		double totalAmount = 0;
		double totalTax = 0;
		double totalSubtotal = 0;
		
		
		UserBean usb = new UserBean();
		UserServiceDAO usd = new UserServiceDAO();
		
		
		
		try {
			usb = usd.getUserDetails(request.getSession().getAttribute("username").toString());
				
			ca = carts.getAllCartItems(request.getSession().getAttribute("username").toString());
			for (CartBean c : ca) {
				 p = ps.getProductsByID(c.prodId);
				 
				 Item item = new Item();
			    item.setCurrency("EUR");
			    item.setSku(String.valueOf(p.getId()));
			    item.setName(p.getName());
			    item.setDescription(p.getDescription());
			    item.setPrice(String.format("%.2f", p.getPrice()).replace(",", "."));  // Prezzo senza tasse
			    item.setTax(String.format("%.2f", p.getPrice()*0.22).replace(",", "."));  // Tassa per il prodotto
			    item.setQuantity(String.valueOf(c.getQuantity()));
		    	totalSubtotal += p.getPrice() * c.getQuantity();
		    	totalTax += (p.getPrice() * c.getQuantity()) * 0.22;
			    items.add(item);
			    
			}
			
			itemList.setItems(items);
			
			
			ShippingAddress shippingAddress = new ShippingAddress();
			shippingAddress.setRecipientName(usb.getNome().concat(usb.getCognome()));
			shippingAddress.setCity(usb.getIndirizzo().getNomeCitta());
			shippingAddress.setCountryCode("IT");
			shippingAddress.setLine1(usb.getIndirizzo().getVia().concat(" " + usb.getIndirizzo().getNumeroCivico()));
			shippingAddress.setPostalCode(usb.getIndirizzo().getCap());
			shippingAddress.setPhone(usb.getNumeroTelefono());

			// Assegna l'indirizzo di spedizione all'ItemList
			itemList.setShippingAddress(shippingAddress);
			
			
			
			Amount amount = new Amount();
			Details details = new Details();
			details.setSubtotal(String.format("%.2f", totalSubtotal).replace(",", "."));
			details.setTax(String.format("%.2f", totalTax).replace(",", "."));
			amount.setCurrency("EUR");
			totalAmount = totalSubtotal + totalTax;
			amount.setTotal(String.format("%.2f", totalAmount).replace(",", "."));  // Somma totale dell'importo
			amount.setDetails(details);  
			transaction.setAmount(amount);
			transaction.setDescription("Descrizione dell'ordine: ......");
			transaction.setItemList(itemList);
			
			List<Transaction> listTransaction = new ArrayList<>();
			listTransaction.add(transaction);	
			
			
			
			
			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");
			Address a = new Address();
			a.setCity(usb.getIndirizzo().getNomeCitta());
			a.setCountryCode("IT");
			a.setLine1(usb.getIndirizzo().getVia().concat(String.valueOf(usb.getIndirizzo().getNumeroCivico())));
			a.setPostalCode(usb.getIndirizzo().getCap());
			a.setPhone(usb.getNumeroTelefono());
			
			PayerInfo payerInfo = new PayerInfo();
			payerInfo.setFirstName(usb.getNome())
					 .setLastName(usb.getCognome())
					 .setEmail(usb.getEmail())
					 .setBillingAddress(a);
			
			payer.setPayerInfo(payerInfo);
				
			response.sendRedirect(paymentServices.authorizePayment(listTransaction, payer));
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	

}






