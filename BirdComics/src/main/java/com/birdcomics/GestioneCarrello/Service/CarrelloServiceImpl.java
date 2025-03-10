package com.birdcomics.GestioneCarrello.Service;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.Bean.CartItem;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Dao.CartServiceDAO;
import com.birdcomics.Dao.ProductServiceDAO;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarrelloServiceImpl implements CarrelloService {
    private CartServiceDAO cartServiceDAO;
    private ProductServiceDAO productServiceDAO;

    public CarrelloServiceImpl() {
        this.cartServiceDAO = new CartServiceDAO();
        this.productServiceDAO = new ProductServiceDAO();
    }

    @Override
    public void addToCart(HttpSession session, String userId, String prodId, int pQty) throws SQLException {
        ProductBean product = productServiceDAO.getProductsByID(prodId);
        int availableQty = productServiceDAO.getAllQuantityProductsById(product);
        
        if (availableQty > 0) {
            cartServiceDAO.addProductToCart(session, userId, prodId, pQty);
        }
    }

    @Override
    public void removeFromCart(HttpSession session, String userId, String prodId) throws SQLException {
        cartServiceDAO.removeProductFromCart(session, userId, prodId);
    }

    @Override
    public List<CartItem> getCartItems(HttpSession session, String userId) throws SQLException {
        CartBean cart = cartServiceDAO.getCartFromSession(session, userId);
        return cart.getCartItems();
    }

    @Override
    public List<ProductBean> getProductsFromCart(List<CartItem> cartItems) throws SQLException {
        List<ProductBean> products = new ArrayList<>();
        for (CartItem item : cartItems) {
            ProductBean product = productServiceDAO.getProductsByID(item.getProdId());
            products.add(product);
        }
        return products;
    }

    @Override
    public float calculateTotalAmount(List<CartItem> cartItems) throws SQLException {
        float totAmount = 0;
        for (CartItem item : cartItems) {
            ProductBean product = productServiceDAO.getProductsByID(item.getProdId());
            totAmount += product.getPrice() * item.getQuantity();
        }
        return totAmount;
    }
    
    @Override
    public void emptyCart(HttpSession session, String userId) throws SQLException {
        cartServiceDAO.deleteAllCartItems(session, userId);
    }
    
    @Override
    public String updateProductInCart(HttpSession session, String userId, String prodId, int pQty) throws SQLException {
       return cartServiceDAO.updateProductToCart(userId, prodId, pQty);
    }

    @Override
    public CartBean loadCartFromDB(HttpSession session, String email) {
        // Crea un nuovo oggetto CartBean per l'utente
        CartBean cart = new CartBean(email);

        // Usa il CartServiceDAO per ottenere gli articoli del carrello dal database
        try {
            // Ottieni gli articoli del carrello dal database utilizzando l'email dell'utente
            cart = cartServiceDAO.loadCartFromDB(session, email);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ritorna il carrello caricato dal database
        return cart;
    }

 
}

