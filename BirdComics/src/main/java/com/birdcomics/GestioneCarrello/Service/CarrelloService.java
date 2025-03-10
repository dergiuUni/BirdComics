package com.birdcomics.GestioneCarrello.Service;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.Bean.CartItem;
import com.birdcomics.Bean.ProductBean;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;


public interface CarrelloService {
    List<ProductBean> getProductsFromCart(List<CartItem> cartItems) throws SQLException;
    float calculateTotalAmount(List<CartItem> cartItems) throws SQLException;
	void emptyCart(HttpSession session, String userId) throws SQLException;
	String updateProductInCart(HttpSession session, String userId, String prodId, int pQty) throws SQLException;
	List<CartItem> getCartItems(HttpSession session, String userId) throws SQLException;
	void removeFromCart(HttpSession session, String userId, String prodId) throws SQLException;
	void addToCart(HttpSession session, String userId, String prodId, int pQty) throws SQLException;
	CartBean loadCartFromDB(HttpSession session, String email);
}
