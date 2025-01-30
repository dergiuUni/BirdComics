package com.birdcomics.GestioneCatalogo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductBean implements Serializable {

    private int id; // corrisponde a id nella tabella Fumetto
    private String name; // corrisponde a nome
    private String description; // corrisponde a descrizione
    private float price; // corrisponde a prezzo
    private String image;
    private boolean active; // corrisponde a active

    public ProductBean() {
    }

    public ProductBean(int id, String name, String description, float price, String image, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.active = active;
    }

    // Getter e Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
