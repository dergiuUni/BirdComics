package com.birdcomics.GestioneMagazzino;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.birdcomics.DatabaseImplementator.DBUtil;
import com.birdcomics.GestioneCatalogo.ProductBean;
import com.birdcomics.GestioneCatalogo.ProductServiceDAO;
import com.birdcomics.GestioneIndirizzo.IndirizzoBean;
import com.birdcomics.GestioneIndirizzo.IndirizzoDao;

public class MagazzinoDao {
	public ScaffaliBean addMagazzino(MagazzinoBean magazzino) throws SQLException {
	    String status = "User Registration Failed!";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	    	IndirizzoDao in = new IndirizzoDao(con);
	    	if(!in.ifExists(magazzino.getIndirizzo().getNomeCitta(), magazzino.getIndirizzo().getVia(), magazzino.getIndirizzo().getNumeroCivico(), magazzino.getIndirizzo().getCap())) {
	    		ps = con.prepareStatement("INSERT INTO Indirizzo (nomeCitta, via, numeroCivico, cap) VALUES (?, ?, ?, ?)");
	    		ps.setString(1, magazzino.getIndirizzo().getNomeCitta());
		        ps.setString(2, magazzino.getIndirizzo().getVia());
		        ps.setInt(3, magazzino.getIndirizzo().getNumeroCivico());
		        ps.setString(4, magazzino.getIndirizzo().getCap());
		        ps.executeUpdate();
	    	}
	    	
	        ps = con.prepareStatement("INSERT INTO Magazzino (nome, nomeCitta, via, numeroCivico, cap) VALUES (?, ?, ?, ?, ?)");

	        ps.setString(1, magazzino.getNome());
	        ps.setString(2, magazzino.getIndirizzo().getNomeCitta());
	        ps.setString(3, magazzino.getIndirizzo().getVia());
	        ps.setInt(4, magazzino.getIndirizzo().getNumeroCivico());
	        ps.setString(5, magazzino.getIndirizzo().getCap());
	        
	        int k = ps.executeUpdate();

	        if (k > 0) {
	            
	        	status = "User Registered Successfully!";
	        }

	    } catch (SQLException e) {
	        status = "Error: " + e.getMessage();
	        e.printStackTrace();
	    } finally {
	        DBUtil.closeConnection(ps);
	    }

	    return null;
	}
	
	public void removeMagazzino(MagazzinoBean magazzino) throws SQLException {
	    String status = "User Registration Failed!";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	    	
	        ps = con.prepareStatement("DELETE FROM Magazzino where nome = ?");

	        ps.setString(1, magazzino.getNome());

	        ps.executeUpdate();

	    } catch (SQLException e) {
	        status = "Error: " + e.getMessage();
	        e.printStackTrace();
	    } finally {
	        DBUtil.closeConnection(ps);
	    }

	}
	
	public ArrayList<MagazzinoBean> getMagazzini() throws SQLException {
		Connection con = DBUtil.getConnection();
		ArrayList<MagazzinoBean> magazzii = new ArrayList<MagazzinoBean>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select * from Magazzino");

			rs = ps.executeQuery();

			if (rs.next()) {
				ScaffaleDao sc = new ScaffaleDao();
				IndirizzoBean in = new IndirizzoBean(rs.getString("nomeCitta"), rs.getString("via"), rs.getInt("numeroCivico"), rs.getString("cap"));
				MagazzinoBean mag = new MagazzinoBean(rs.getString("nome"), in, sc.getScaffaleMagazzino(rs.getString("nome")));
				magazzii.add(mag);
			}
				

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return magazzii;
	}
}
