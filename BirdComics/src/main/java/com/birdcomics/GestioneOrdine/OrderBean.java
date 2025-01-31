package com.birdcomics.GestioneOrdine;
import java.io.Serializable;
import java.sql.Date;


@SuppressWarnings("serial")
public class OrderBean implements Serializable {

	String emailUtente;
	int id;
	String idPaypal;
	boolean shipped;
	Date dataEffettuato;
	int idFattura;
	
	public OrderBean() {
		super();
	}
	
	public OrderBean(String emailUtente, int id, String idPaypal, boolean shipped, Date dataEffettuato, int idFattura) {
		super();
		this.emailUtente = emailUtente;
		this.id = id;
		this.idPaypal = idPaypal;
		this.shipped = shipped;
		this.dataEffettuato = dataEffettuato;
		this.idFattura = idFattura;
	}


	public int getId() {
		return id;
	}


	public String getEmailUtente() {
		return emailUtente;
	}

	public void setEmailUtente(String emailUtente) {
		this.emailUtente = emailUtente;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdPaypal() {
		return idPaypal;
	}

	public void setIdPaypal(String idPaypal) {
		this.idPaypal = idPaypal;
	}

	public boolean isShipped() {
		return shipped;
	}

	public void setShipped(boolean shipped) {
		this.shipped = shipped;
	}

	public Date getDataEffettuato() {
		return dataEffettuato;
	}

	public void setDataEffettuato(Date dataEffettuato) {
		this.dataEffettuato = dataEffettuato;
	}

	public int getIdFattura() {
		return idFattura;
	}

	public void setIdFattura(int idFattura) {
		this.idFattura = idFattura;
	}

}
