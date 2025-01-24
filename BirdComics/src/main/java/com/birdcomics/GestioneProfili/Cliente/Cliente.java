package com.birdcomics.GestioneProfili.Cliente;

import java.util.Date;

import com.birdcomics.GestioneProfili.Utente;

public class Cliente extends Utente{
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

	protected void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}	
	
}
