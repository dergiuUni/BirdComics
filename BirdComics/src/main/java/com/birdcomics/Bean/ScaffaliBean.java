package com.birdcomics.Bean;

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
	
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

	public ProductBean getFumetto() {
		return fumetto;
	}
	
	public void setFumetto(ProductBean fumetto) {
		this.fumetto = fumetto;
	}
	public int getQuantitaOccupata() {
		return quantitaOccupata;
	}
	public void setQuantitaOccupata(int quantitaOccupata) {
		this.quantitaOccupata = quantitaOccupata;
	}
	public int getQuantitaMassima() {
		return quantitaMassima;
	}
	
	
}
