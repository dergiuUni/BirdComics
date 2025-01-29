package com.birdcomics.GestioneMagazzino;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.birdcomics.DatabaseImplementator.DBUtil;
import com.birdcomics.GestioneIndirizzo.IndirizzoBean;
import com.birdcomics.GestioneIndirizzo.IndirizzoDao;
import com.birdcomics.GestioneProfili.RuoloBean;
import com.birdcomics.GestioneProfili.UserBean;

public class ScaffaleDao {
	public String registerScaffale(ScaffaliBean scaffale) throws SQLException {
	    String status = "User Registration Failed!";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	    	
	        ps = con.prepareStatement("INSERT INTO Scaffali (quantita, quantitaMassima) VALUES (?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS);

	        ps.setInt(1, scaffale.getQuantitaOccupata());
	        ps.setInt(2, scaffale.getQuantitaMassima());

	        int k = ps.executeUpdate();

	        if (k > 0) {
	            
	        	ResultSet rs = null;
	        	rs = ps.getGeneratedKeys();
	        	if (rs.next()) {
                    scaffale.setId(rs.getInt(1)); // O getInt(1) se l'ID è un int
                    
                }
	        	
	        	ps = con.prepareStatement("INSERT INTO Scaffali (quantita, quantitaMassima) VALUES (?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS);

		        ps.setInt(1, scaffale.getQuantitaOccupata());
		        ps.setInt(2, scaffale.getQuantitaMassima());

	        	
	        	status = "User Registered Successfully!";
	        }

	    } catch (SQLException e) {
	        status = "Error: " + e.getMessage();
	        e.printStackTrace();
	    } finally {
	        DBUtil.closeConnection(ps);
	    }

	    return status;
	}


	
	public boolean isRegistered(String emailId) throws SQLException {
		boolean flag = false;

		Connection con = DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select * from Utente where email=?");

			ps.setString(1, emailId);

			rs = ps.executeQuery();

			if (rs.next())
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return flag;
	}
}
