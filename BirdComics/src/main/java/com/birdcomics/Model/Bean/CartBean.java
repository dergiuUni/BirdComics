package com.birdcomics.Model.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CartBean implements Serializable {

    private String userId;
    private List<CartItem> cartItems;

    // 🔧 Costruttore vuoto
    public CartBean() {
        this.cartItems = new ArrayList<>();
    }

    // 🔧 Costruttore con userId
    public CartBean(String userId) {
        this.userId = userId;
        this.cartItems = new ArrayList<>();
    }

    // Getter e Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    // Aggiungi un prodotto al carrello
    public void addItem(CartItem item) {
        boolean itemFound = false;

        for (CartItem cartItem : cartItems) {
            if (cartItem.getProdId().equals(item.getProdId())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            cartItems.add(item);
        }
    }

    // Rimuovi un prodotto dal carrello
    public void removeItem(String prodId) {
        cartItems.removeIf(cartItem -> cartItem.getProdId().equals(prodId));
    }
}
