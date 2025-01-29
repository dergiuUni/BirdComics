package com.birdcomics.GestioneProfili;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.birdcomics.GestioneIndirizzo.IndirizzoBean;

@SuppressWarnings("serial")
public class UserBean implements Serializable {
	
	protected String email, password, nome, cognome, numeroTelefono;
    protected java.sql.Date dataNascita;
	protected IndirizzoBean indirizzo;
	protected ArrayList<RuoloBean> ruolo;
	


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
	
    protected ArrayList<RuoloBean> getRuoloBean() {
		return ruolo;
	}

	protected void setRuoloBean(ArrayList<RuoloBean> ruoloBean) {
		this.ruolo = ruoloBean;
	}
	
	protected void addRuolo(RuoloBean ruolo) {
		this.ruolo.add(ruolo);
	}
	
	protected boolean isRuolo(RuoloBean test) {
		for (RuoloBean ruoloBean : ruolo) {
			if(ruoloBean.toString() == test.toString()) {
				return true;
			}
		}
		return false;
	}
}
