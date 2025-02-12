package com.birdcomics.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.birdcomics.Bean.IndirizzoBean;
import com.birdcomics.Bean.MagazzinoBean;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Bean.ScaffaliBean;
import com.birdcomics.Utils.DBUtil;

public class MagazzinoDao {
	public String addMagazzino(MagazzinoBean magazzino) throws SQLException {
	    String status = "add magazzino Failed!";
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
	            
	        	status = "magazzino Registered Successfully!";
	        }

	    } catch (SQLException e) {
	        status = "Error sql";
	        e.printStackTrace();
	    } finally {
	        DBUtil.closeConnection(ps);
	    }
	    return status;
	}
	
	public String removeMagazzino(MagazzinoBean magazzino) throws SQLException {
	    String status = "Delete magazzino Failed!";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	    	
	        ps = con.prepareStatement("DELETE FROM Magazzino where nome = ?");

	        ps.setString(1, magazzino.getNome());

	        ps.executeUpdate();
	        
	        status = "magazzino cancellato";
	        
	    } catch (SQLException e) {
	        status = "Error sql";
	        e.printStackTrace();
	    } finally {
	        DBUtil.closeConnection(ps);
	    }
	    
	    return status;

	}
	
	public ArrayList<MagazzinoBean> getMagazzini() throws SQLException {
		Connection con = DBUtil.getConnection();
		ArrayList<MagazzinoBean> magazzini = new ArrayList<MagazzinoBean>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select * from Magazzino");

			rs = ps.executeQuery();

			while (rs.next()) {
				ScaffaleDao sc = new ScaffaleDao();
				IndirizzoBean in = new IndirizzoBean(rs.getString("nomeCitta"), rs.getString("via"), rs.getInt("numeroCivico"), rs.getString("cap"));
				MagazzinoBean mag = new MagazzinoBean(rs.getString("nome"), in, sc.getScaffaleMagazzino(rs.getString("nome")));
				magazzini.add(mag);
			}
				

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return magazzini;
	}
}
