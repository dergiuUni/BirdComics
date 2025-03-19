package com.birdcomics.Model.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.birdcomics.Model.Bean.IndirizzoBean;

public class IndirizzoDao {
	 private Connection connection;

	    // Costruttore per ottenere la connessione al DB
	    public IndirizzoDao(Connection connection) {
	        this.connection = connection;
	    }

	    // Crea un nuovo indirizzo
	    public void create(IndirizzoBean indirizzo) throws SQLException {
	        String query = "INSERT INTO Indirizzo (nomeCitta, via, numeroCivico, cap) VALUES (?, ?, ?, ?)";

	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setString(1, indirizzo.getNomeCitta());
	            stmt.setString(2, indirizzo.getVia());
	            stmt.setString(3, indirizzo.getNumeroCivico());
	            stmt.setString(4, indirizzo.getCap());
	            stmt.executeUpdate();
	        }
	    }

	    // Leggi un indirizzo dato un set di parametri (nomeCitta, via, numeroCivico, cap)
	    public  boolean ifExists (String nomeCitta, String via, String numeroCivico, String cap) throws SQLException {
	        String query = "SELECT * FROM Indirizzo WHERE nomeCitta = ? AND via = ? AND numeroCivico = ? AND cap = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setString(1, nomeCitta);
	            stmt.setString(2, via);
	            stmt.setString(3, numeroCivico);
	            stmt.setString(4, cap);

	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return true;
	            }
	        }
	        return false; // Se non trovato
	    }

}
