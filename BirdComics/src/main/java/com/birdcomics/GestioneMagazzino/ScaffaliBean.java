package com.birdcomics.GestioneMagazzino;

import com.birdcomics.GestioneCatalogo.ProductBean;

public class ScaffaliBean {
	private int id;
	private ProductBean fumetto;
	private int quantitaOccupata;
	private int quantitaMassima;
	
	
	
	public ScaffaliBean() {
		super();
	}
	
	public ScaffaliBean(ProductBean fumetto, int quantitaOccupata, int quantitaMassima) {
		super();
		this.fumetto = fumetto;
		this.quantitaOccupata = quantitaOccupata;
		this.quantitaMassima = quantitaMassima;
	}
	
	public ScaffaliBean(int id, ProductBean fumetto, int quantitaOccupata, int quantitaMassima) {
		super();
		this.id = id;
		this.fumetto = fumetto;
		this.quantitaOccupata = quantitaOccupata;
		this.quantitaMassima = quantitaMassima;
	}
	
	
	
	
	protected int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}
	

	protected ProductBean getFumetto() {
		return fumetto;
	}
	
	protected void setFumetto(ProductBean fumetto) {
		this.fumetto = fumetto;
	}
	protected int getQuantitaOccupata() {
		return quantitaOccupata;
	}
	protected void setQuantitaOccupata(int quantitaOccupata) {
		this.quantitaOccupata = quantitaOccupata;
	}
	protected int getQuantitaMassima() {
		return quantitaMassima;
	}
	
	
}
