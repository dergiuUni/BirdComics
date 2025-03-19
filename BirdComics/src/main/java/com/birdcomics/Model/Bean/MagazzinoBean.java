package com.birdcomics.Model.Bean;

import java.util.ArrayList;

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
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public IndirizzoBean getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(IndirizzoBean indirizzo) {
		this.indirizzo = indirizzo;
	}
	public ArrayList<ScaffaliBean> getScaffali() {
		return scaffali;
	}
	public void setScaffali(ArrayList<ScaffaliBean> scaffali) {
		this.scaffali = scaffali;
	}
	
	
}
