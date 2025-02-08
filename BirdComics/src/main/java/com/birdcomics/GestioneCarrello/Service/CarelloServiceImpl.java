package com.birdcomics.GestioneCarrello.Service;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Dao.CartServiceDAO;
import com.birdcomics.Dao.ProductServiceDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarelloServiceImpl implements CarrelloService {
    private CartServiceDAO cartServiceDAO;
    private ProductServiceDAO productServiceDAO;

    public CarelloServiceImpl() {
        this.cartServiceDAO = new CartServiceDAO();
        this.productServiceDAO = new ProductServiceDAO();
    }

    @Override
    public void addToCart(String userId, String prodId, int pQty) throws SQLException {
        ProductBean product = productServiceDAO.getProductsByID(prodId);
        int availableQty = productServiceDAO.getAllQuantityProductsById(product);

        int cartQty = cartServiceDAO.getProductCount(userId, prodId);
        int totalQtyToAdd = pQty + cartQty;

        // Verifica se la quantità totale supera la disponibilità
        if(availableQty > 0) {
        if (totalQtyToAdd > availableQty) {
            totalQtyToAdd = availableQty;
        }

        // Aggiungi al carrello (o aggiorna la quantità)
        cartServiceDAO.updateProductToCart(userId, prodId, totalQtyToAdd);
        }
        
    }

    @Override
    public void removeFromCart(String userId, String prodId) throws SQLException {
        cartServiceDAO.removeProductFromCart(userId, prodId);
    }

    @Override
    public List<CartBean> getCartItems(String userId) throws SQLException {
        return cartServiceDAO.getAllCartItems(userId);  // Ottieni gli articoli del carrello
    }

    @Override
    public List<ProductBean> getProductsFromCart(List<CartBean> cartItems) throws SQLException {
        List<ProductBean> products = new ArrayList<>();
        for (CartBean item : cartItems) {
            ProductBean product = productServiceDAO.getProductsByID(item.getProdId());
            products.add(product);
        }
        return products;
    }

    @Override
    public float calculateTotalAmount(List<CartBean> cartItems) throws SQLException {
        float totAmount = 0;
        for (CartBean item : cartItems) {
            ProductBean product = productServiceDAO.getProductsByID(item.getProdId());
            totAmount += product.getPrice() * item.getQuantity();
        }
        return totAmount;
    }
    
    @Override
    public void emptyCart(String userId) throws SQLException {
        cartServiceDAO.deleteAllCartItems(userId);  // Svuota tutti gli articoli dal carrello
    }
    
    @Override
    public String updateProductInCart(String userId, String prodId, int pQty) throws SQLException {
       return cartServiceDAO.updateProductToCart(userId, prodId, pQty);  // Metodo per aggiornare la quantità nel carrello
    }
}
