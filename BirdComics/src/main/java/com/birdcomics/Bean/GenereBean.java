package com.birdcomics.Bean;

public class GenereBean {

	public GenereBean() {
		super();
	}

	public GenereBean(String genere) {
		super();
		this.genere = genere;
	}

	private String genere;

	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	@Override
	public String toString() {
		return ""+genere;
	}
	
	
	
}
