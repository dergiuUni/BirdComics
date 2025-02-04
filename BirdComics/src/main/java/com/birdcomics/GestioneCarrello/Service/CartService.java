package com.birdcomics.GestioneCarrello.Service;

import com.birdcomics.GestioneCarrello.CartBean;
import com.birdcomics.GestioneCatalogo.ProductBean;
import java.sql.SQLException;
import java.util.List;


public interface CartService {
    void addToCart(String userId, String prodId, int pQty) throws SQLException;
    void removeFromCart(String userId, String prodId) throws SQLException;
    List<CartBean> getCartItems(String userId) throws SQLException;
    List<ProductBean> getProductsFromCart(List<CartBean> cartItems) throws SQLException;
    float calculateTotalAmount(List<CartBean> cartItems) throws SQLException;
    void emptyCart(String userId) throws SQLException;
    String updateProductInCart(String userId, String prodId, int pQty) throws SQLException;  // Nuovo metodo
}
