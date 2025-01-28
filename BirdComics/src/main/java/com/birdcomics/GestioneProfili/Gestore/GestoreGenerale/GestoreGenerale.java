package com.birdcomics.GestioneProfili.Gestore.GestoreGenerale;

import java.util.Date;

import com.birdcomics.GestioneProfili.Gestore.Gestore;

public class GestoreGenerale extends Gestore{
	protected void setPassword(String password) {
		this.password = password;
	}

	protected void setNome(String nome) {
		this.nome = nome;
	}

	protected void setCognome(String cognome) {
		this.cognome = cognome;
	}

	protected void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

}
