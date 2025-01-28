package com.birdcomics.GestioneProfili;

import java.io.Serializable;
import java.util.Date;

import com.birdcomics.GestioneIndirizzo.IndirizzoBean;

@SuppressWarnings("serial")
public class UserBean implements Serializable {
	
	protected String email, password, nome, cognome, numeroTelefono;
    protected java.sql.Date dataNascita;
	protected IndirizzoBean indirizzo;
	protected RuoloBean ruolo;
	


	public UserBean() {
    }

	public UserBean(String email, String password, String nome, String cognome, String numeroTelefono, java.sql.Date dataNascita, IndirizzoBean indirizzo, RuoloBean ruolo) {
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

	public  String getEmail() {
		return email;
	}

	public  String getPassword() {
		return password;
	}

	public  String getNome() {
		return nome;
	}

	public  String getCognome() {
		return cognome;
	}
	
	public  String getNumeroTelefono() {
		return numeroTelefono;
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
	
    protected RuoloBean getRuoloBean() {
		return ruolo;
	}

	protected void setRuoloBean(RuoloBean ruoloBean) {
		this.ruolo = ruoloBean;
	}
}
