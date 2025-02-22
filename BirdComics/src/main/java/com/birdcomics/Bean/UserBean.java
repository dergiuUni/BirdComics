package com.birdcomics.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class UserBean implements Serializable {
	
	protected String email, password, nome, cognome, numeroTelefono;
    protected java.sql.Date dataNascita;
	protected IndirizzoBean indirizzo;
	protected ArrayList<RuoloBean> ruolo = new ArrayList<RuoloBean>();
	protected MagazzinoBean magazzino;
	




	public UserBean() {
    }

	public UserBean(String email, String password, String nome, String cognome, String numeroTelefono, java.sql.Date dataNascita, IndirizzoBean indirizzo, ArrayList<RuoloBean> ruolo) {
		super();
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.numeroTelefono = numeroTelefono;
		this.dataNascita = dataNascita;
		this.indirizzo = indirizzo;
		this.ruolo = ruolo;
	}
	
	public UserBean(String email, String password, String nome, String cognome, String numeroTelefono, java.sql.Date dataNascita, IndirizzoBean indirizzo, ArrayList<RuoloBean> ruolo, MagazzinoBean magazzino) {
		super();
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.numeroTelefono = numeroTelefono;
		this.dataNascita = dataNascita;
		this.indirizzo = indirizzo;
		this.ruolo = ruolo;
		this.magazzino = magazzino;
	}

	public  String getEmail() {
		return email;
	}
	
	public  void setEmail(String email) {
		this.email = email ;
	}

	public  String getPassword() {
		return password;
	}

	public  String getNome() {
		return nome;
	}
	
	

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public  String getCognome() {
		return cognome;
	}
	
	public  String getNumeroTelefono() {
		return numeroTelefono;
	}
	
	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public  Date getDataNascita() {
		return dataNascita;
	}

	public IndirizzoBean getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(IndirizzoBean indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public ArrayList<RuoloBean> getRuoloBean() {
		return ruolo;
	}

	public void setRuoloBean(ArrayList<RuoloBean> ruoloBean) {
		this.ruolo = ruoloBean;
	}
	
	public void addRuolo(RuoloBean ruolo) {
		this.ruolo.add(ruolo);
	}
	
	public boolean isRuolo(RuoloBean test) {
		for (RuoloBean ruoloBean : ruolo) {
			if(ruoloBean.toString() == test.toString()) {
				return true;
			}
		}
		return false;
	}
	
	public MagazzinoBean getMagazzino() {
		return magazzino;
	}

	public  void setMagazzino(MagazzinoBean magazzino) {
		this.magazzino = magazzino;
	}
}
