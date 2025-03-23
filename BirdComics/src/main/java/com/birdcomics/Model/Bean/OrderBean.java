package com.birdcomics.Model.Bean;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;


@SuppressWarnings("serial")
public class OrderBean implements Serializable {

	private String idUtente;
	private int id;
	private String idPaypal;
	private String shipped;
	private Date dataEffettuato;
	private FatturaBean idFattura;
	private HashMap<ProductBean, Integer> fumetti = new HashMap<>();;
	
	public OrderBean() {
		super();
	}
	
	public OrderBean(String emailUtente, int id, String idPaypal, String shipped, Date dataEffettuato, FatturaBean idFattura) {
		super();
		this.idUtente = emailUtente;
		this.id = id;
		this.idPaypal = idPaypal;
		this.shipped = shipped;
		this.dataEffettuato = dataEffettuato;
		this.idFattura = idFattura;
	}
	
	public OrderBean(String emailUtente,  String idPaypal, String shipped, Date dataEffettuato) {
		super();
		this.idUtente = emailUtente;
		this.idPaypal = idPaypal;
		this.shipped = shipped;
		this.dataEffettuato = dataEffettuato;
	}


	public int getId() {
		return id;
	}


	public String getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(String emailUtente) {
		this.idUtente = emailUtente;
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

	public String getShipped() {
		return shipped;
	}

	public void setShipped(String shipped) {
		this.shipped = shipped;
	}

	public Date getDataEffettuato() {
		return dataEffettuato;
	}

	public void setDataEffettuato(Date dataEffettuato) {
		this.dataEffettuato = dataEffettuato;
	}

	public FatturaBean getIdFattura() {
		return idFattura;
	}

	public void setIdFattura(FatturaBean idFattura) {
		this.idFattura = idFattura;
	}

	public HashMap<ProductBean, Integer> getFumetti() {
		return fumetti;
	}

	public void setFumetti(HashMap<ProductBean, Integer> fumetti) {
		this.fumetti = fumetti;
	}

	public void addFumetti(ProductBean fumetto, int quantita) {
		this.fumetti.put(fumetto, quantita);
	}
	
	
	
	

}
