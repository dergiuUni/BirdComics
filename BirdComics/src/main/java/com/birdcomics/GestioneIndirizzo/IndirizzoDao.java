package com.birdcomics.GestioneIndirizzo;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IndirizzoDao {
	 private Connection connection;

	    // Costruttore per ottenere la connessione al DB
	    public IndirizzoDao(Connection connection) {
	        this.connection = connection;
	    }

	    // Crea un nuovo indirizzo
	    public void create(IndirizzoBean indirizzo) throws SQLException {
	        String query = "INSERT INTO Indirizzo (nomeCitta, via, numeroCivico, cvc) VALUES (?, ?, ?, ?)";

	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setString(1, indirizzo.getNomeCitta());
	            stmt.setString(2, indirizzo.getVia());
	            stmt.setInt(3, indirizzo.getNumeroCivico());
	            stmt.setString(4, indirizzo.getCvc());
	            stmt.executeUpdate();
	        }
	    }

	    // Leggi un indirizzo dato un set di parametri (nomeCitta, via, numeroCivico, cvc)
	    public  boolean ifExists (String nomeCitta, String via, int numeroCivico, String cvc) throws SQLException {
	        String query = "SELECT * FROM Indirizzo WHERE nomeCitta = ? AND via = ? AND numeroCivico = ? AND cvc = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setString(1, nomeCitta);
	            stmt.setString(2, via);
	            stmt.setInt(3, numeroCivico);
	            stmt.setString(4, cvc);

	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return true;
	            }
	        }
	        return false; // Se non trovato
	    }

}
