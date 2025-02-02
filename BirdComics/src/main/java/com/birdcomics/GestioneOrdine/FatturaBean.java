package com.birdcomics.GestioneOrdine;

public class FatturaBean {
	private int id, iva;
	private String nome, cognome, telefono, nomeCittaCliente, viaCliente, numeroCivicoCliente, capCliente, nomeCittaMagazzino, viaMagazzino, numeroCivicoMagazzino, capMagazzino, nomeMagazzino;
	
	
	
	
	

	
	
	public FatturaBean(int id, int iva, String nome, String cognome, String telefono, String nomeCittaCliente,
			String viaCliente, String numeroCivicoCliente, String capCliente, String nomeCittaMagazzino,
			String viaMagazzino, String numeroCivicoMagazzino, String capMagazzino, String nomeMagazzino) {
		super();
		this.id = id;
		this.iva = iva;
		this.nome = nome;
		this.cognome = cognome;
		this.telefono = telefono;
		this.nomeCittaCliente = nomeCittaCliente;
		this.viaCliente = viaCliente;
		this.numeroCivicoCliente = numeroCivicoCliente;
		this.capCliente = capCliente;
		this.nomeCittaMagazzino = nomeCittaMagazzino;
		this.viaMagazzino = viaMagazzino;
		this.numeroCivicoMagazzino = numeroCivicoMagazzino;
		this.capMagazzino = capMagazzino;
		this.nomeMagazzino = nomeMagazzino;
	}



	public FatturaBean() {
	}
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCognome() {
		return cognome;
	}


	public void setCognome(String cognome) {
		this.cognome = cognome;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public int getIva() {
		return iva;
	}
	public void setIva(int iva) {
		this.iva = iva;
	}
	public String getNomeCittaCliente() {
		return nomeCittaCliente;
	}
	public void setNomeCittaCliente(String nomeCittaCliente) {
		this.nomeCittaCliente = nomeCittaCliente;
	}
	public String getViaCliente() {
		return viaCliente;
	}
	public void setViaCliente(String viaCliente) {
		this.viaCliente = viaCliente;
	}
	public String getNumeroCivicoCliente() {
		return numeroCivicoCliente;
	}
	public void setNumeroCivicoCliente(String numeroCivicoCliente) {
		this.numeroCivicoCliente = numeroCivicoCliente;
	}
	public String getCapCliente() {
		return capCliente;
	}
	public void setCapCliente(String capCliente) {
		this.capCliente = capCliente;
	}
	public String getNomeCittaMagazzino() {
		return nomeCittaMagazzino;
	}
	public void setNomeCittaMagazzino(String nomeCittaMagazzino) {
		this.nomeCittaMagazzino = nomeCittaMagazzino;
	}
	public String getViaMagazzino() {
		return viaMagazzino;
	}
	public void setViaMagazzino(String viaMagazzino) {
		this.viaMagazzino = viaMagazzino;
	}
	public String getNumeroCivicoMagazzino() {
		return numeroCivicoMagazzino;
	}
	public void setNumeroCivicoMagazzino(String numeroCivicoMagazzino) {
		this.numeroCivicoMagazzino = numeroCivicoMagazzino;
	}
	public String getCapMagazzino() {
		return capMagazzino;
	}
	public void setCapMagazzino(String capMagazzino) {
		this.capMagazzino = capMagazzino;
	}
	public String getNomeMagazzino() {
		return nomeMagazzino;
	}
	public void setNomeMagazzino(String nomeMagazzino) {
		this.nomeMagazzino = nomeMagazzino;
	}

	


}
