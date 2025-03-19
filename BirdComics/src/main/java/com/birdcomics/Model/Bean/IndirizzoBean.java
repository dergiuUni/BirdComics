package com.birdcomics.Model.Bean;

public class IndirizzoBean {
    private String nomeCitta;
    private String via;
    private String numeroCivico;
    private String cap;

    // Costruttore
    public IndirizzoBean (String nomeCitta, String via, String numeroCivico, String cap) {
        this.nomeCitta = nomeCitta;
        this.via = via;
        this.numeroCivico = numeroCivico;
        this.cap = cap;
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

    public String getNumeroCivico() {
        return numeroCivico;
    }

    public void setNumeroCivico(String numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

 

	    
}
