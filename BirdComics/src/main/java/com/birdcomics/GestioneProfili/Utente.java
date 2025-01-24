package com.birdcomics.GestioneProfili;

import java.util.Date;

public class Utente{
	protected String email, password, nome, cognome, numeroTelefono;
	protected Date dataNascita;
	
	public  Utente() {
		// TODO Auto-generated constructor stub
	}

	public  Utente(String email, String password, String nome, String cognome, String numeroTelefono, Date dataNascita) {
		super();
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.numeroTelefono = numeroTelefono;
		this.dataNascita = dataNascita;
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
}
