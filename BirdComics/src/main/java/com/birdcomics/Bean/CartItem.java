package com.birdcomics.Bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CartItem implements Serializable {

    public String prodId;   // ID del prodotto
    private int quantity;    // Quantità del prodotto

    // Costruttore di default
    public CartItem() {}

    // Costruttore con parametri
    public CartItem(String prodId, int quantity) {
        this.prodId = prodId;
        this.quantity = quantity;
    }

    // Getters e Setters
    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
