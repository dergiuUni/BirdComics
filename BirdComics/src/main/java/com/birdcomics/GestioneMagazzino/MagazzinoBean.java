package com.birdcomics.GestioneMagazzino;

import java.util.ArrayList;

import com.birdcomics.GestioneIndirizzo.IndirizzoBean;

public class MagazzinoBean {
	private String nome;
	IndirizzoBean indirizzo;
	ArrayList<ScaffaliBean> scaffali;
	
	
	public MagazzinoBean(	) {
		super();
	}
	
	public MagazzinoBean(String nome, IndirizzoBean indirizzo, ArrayList<ScaffaliBean> scaffali) {
		super();
		this.nome = nome;
		this.indirizzo = indirizzo;
		this.scaffali = scaffali;
	}
	
	
	protected String getNome() {
		return nome;
	}
	protected void setNome(String nome) {
		this.nome = nome;
	}
	protected IndirizzoBean getIndirizzo() {
		return indirizzo;
	}
	protected void setIndirizzo(IndirizzoBean indirizzo) {
		this.indirizzo = indirizzo;
	}
	protected ArrayList<ScaffaliBean> getScaffali() {
		return scaffali;
	}
	protected void setScaffali(ArrayList<ScaffaliBean> scaffali) {
		this.scaffali = scaffali;
	}
	
	
}
