package com.birdcomics.GestioneProfili.Cliente;

import java.util.Date;

import com.birdcomics.GestioneProfili.UserBean;

public class Cliente extends UserBean{
	private CartBean carrello; 
	
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
