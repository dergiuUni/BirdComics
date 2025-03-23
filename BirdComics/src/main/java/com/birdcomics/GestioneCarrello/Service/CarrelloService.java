package com.birdcomics.GestioneCarrello.Service;

import com.birdcomics.Model.Bean.CartBean;
import com.birdcomics.Model.Bean.CartItem;
import com.birdcomics.Model.Bean.ProductBean;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;


public interface CarrelloService {
    List<ProductBean> visualizzaProdottiCarrello(List<CartItem> cartItems) throws SQLException;
    float calculateTotalAmount(List<CartItem> cartItems) throws SQLException;
	void svuotaCarrello(HttpSession session, String email) throws SQLException;
	String modificaQuantita(HttpSession session, String email, String prodId, int pQty) throws SQLException;
	List<CartItem> visualizzaCarrello(HttpSession session, String email) throws SQLException;
	void rimuoviFumetto(HttpSession session, String email, String prodId) throws SQLException;
	void aggiungiFumetto(HttpSession session, String email, String prodId, int pQty) throws SQLException;
	CartBean loadCartFromDB(HttpSession session, String email);
}
