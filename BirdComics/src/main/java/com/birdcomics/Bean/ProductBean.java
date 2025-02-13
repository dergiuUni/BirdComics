package com.birdcomics.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ProductBean implements Serializable {

    private int id; // corrisponde a id nella tabella Fumetto
    private String name; // corrisponde a nome
    private String description; // corrisponde a descrizione
    private float price; // corrisponde a prezzo
    private String image;
    //private boolean active; // corrisponde a active
    private List<GenereBean> generi = new ArrayList<GenereBean>();

    public ProductBean() {
    }

    public ProductBean(int id, String name, String description, float price, String image, List<GenereBean> generi) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.generi = generi;
    }

    
    public String getGeneri() {
		String x = "";
		for (GenereBean s : generi) {
			x += " " + s.toString();
		}
    	return x;
	}
    
	public void addGenere(GenereBean genere) {
		this.generi.add(genere);
	}

	public void setGeneri(List<GenereBean> generi) {
		this.generi = generi;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	public void addGenere(String genere) {
		if(ifExistsGenere(genere) == false) {
			GenereBean g = new GenereBean();
			g.setGenere(genere);
			generi.add(g);
		}
		
	}
	
	public boolean ifExistsGenere(String genere) {
		for (GenereBean x : generi) {
			if(genere == x.getGenere()) {
				return true;
			}
		}
		return false;
	}
	
}
