package com.birdcomics.GestioneMagazzino;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.birdcomics.DatabaseImplementator.DBUtil;
import com.birdcomics.GestioneCatalogo.ProductBean;
import com.birdcomics.GestioneIndirizzo.IndirizzoBean;
import com.birdcomics.GestioneIndirizzo.IndirizzoDao;
import com.birdcomics.GestioneProfili.RuoloBean;
import com.birdcomics.GestioneProfili.UserBean;

public class ScaffaleDao {
	public ScaffaliBean registerScaffale(int quantitaMassima) throws SQLException {
	    String status = "User Registration Failed!";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	    	
	        ps = con.prepareStatement("INSERT INTO Scaffali (quantitaMassima) VALUES (?)", java.sql.Statement.RETURN_GENERATED_KEYS);

	        ps.setInt(1, quantitaMassima);

	        int k = ps.executeUpdate();

	        if (k > 0) {
	            
	        	ResultSet rs = null;
	        	rs = ps.getGeneratedKeys();
	        	if (rs.next()) {
	        		ScaffaliBean scaffale = new ScaffaliBean();
                    scaffale.setId(rs.getInt(1)); // O getInt(1) se l'ID è un int
                    return scaffale;
                    
                }
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


	
	public ArrayList<ScaffaliBean> getScaffaleMagazzino(String nomeMagazzino) throws SQLException {
		Connection con = DBUtil.getConnection();
		ArrayList<ScaffaliBean> scaffali = new ArrayList<ScaffaliBean>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select idScaffale, quantita, quantitaMassima idFumetto  from MagazzinoScaffali, Scaffali where idMagazzino=? and idScaffale = id");

			ps.setString(1, nomeMagazzino);

			rs = ps.executeQuery();

			if (rs.next()) {
				ProductBean pr = new ProductBean();
				
				ScaffaliBean scaffale = new ScaffaliBean(rs.getInt(""), rs.getInt(""), rs.getInt(""), rs.getInt(""));
				sca
			}
				

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return flag;
	}
}
