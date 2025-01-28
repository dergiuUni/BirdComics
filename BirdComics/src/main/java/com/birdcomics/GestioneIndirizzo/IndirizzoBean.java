package com.birdcomics.GestioneIndirizzo;

public class IndirizzoBean {
    private String nomeCitta;
    private String via;
    private int numeroCivico;
    private String cvc;

    // Costruttore
    public IndirizzoBean (String nomeCitta, String via, int numeroCivico, String cvc) {
        this.nomeCitta = nomeCitta;
        this.via = via;
        this.numeroCivico = numeroCivico;
        this.cvc = cvc;
    }
    
    public IndirizzoBean() {
    	
    }

    // Getter e Setter
    public String getNomeCitta() {
        return nomeCitta;
    }

    public void setNomeCitta(String nomeCitta) {
        this.nomeCitta = nomeCitta;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public int getNumeroCivico() {
        return numeroCivico;
    }

    public void setNumeroCivico(int numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

 

	    
}
