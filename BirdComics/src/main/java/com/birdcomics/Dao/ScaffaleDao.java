package com.birdcomics.Dao;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.birdcomics.Bean.IndirizzoBean;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Bean.RuoloBean;
import com.birdcomics.Bean.ScaffaliBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.Utils.DBUtil;

public class ScaffaleDao {
	public ScaffaliBean addScaffale(int quantitaMassima) throws SQLException {
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
	
	public void removeScaffale(ProductBean fumetto) throws SQLException {
	    String status = "User Registration Failed!";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	    	
	        ps = con.prepareStatement("DELETE FROM Scaffali where id = ?");

	        ps.setInt(1, fumetto.getId());

	        ps.executeUpdate();

	    } catch (SQLException e) {
	        status = "Error: " + e.getMessage();
	        e.printStackTrace();
	    } finally {
	        DBUtil.closeConnection(ps);
	    }

	}
	
	public void addFumetto(ScaffaliBean scaffale) throws SQLException {
	    String status = "User Registration Failed!";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	    	
	        ps = con.prepareStatement("INSERT INTO Scaffali (quantita, idFumetto) VALUES (?, ?)");

	        ps.setInt(1, scaffale.getQuantitaOccupata());
	        ps.setInt(2, scaffale.getFumetto().getId());

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
	}
	
	public void modifyQuantityFumetto(ScaffaliBean scaffale) throws SQLException {
	    String status = "User Registration Failed!";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	    	
	        ps = con.prepareStatement("UPDATE Scaffali SET quantita = ? where id = ?");

	        ps.setInt(1, scaffale.getQuantitaOccupata());
	        ps.setInt(2, scaffale.getId());

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
	}
	
	public void modifyFumetto(ScaffaliBean scaffale) throws SQLException {
	    String status = "User Registration Failed!";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	    	
	        ps = con.prepareStatement("UPDATE Scaffali SET quantita = ?, idFumetto = ? where id = ?");

	        ps.setInt(1, scaffale.getQuantitaOccupata());
	        ps.setInt(2, scaffale.getFumetto().getId());
	        ps.setInt(3, scaffale.getId());

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
	}

	public void deleteFumetto(ScaffaliBean scaffale) throws SQLException {
	    String status = "User Registration Failed!";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	    	
	        ps = con.prepareStatement("UPDATE Scaffali SET quantita = null, idFumetto = null idFumetto = ? where id = ?");

	        ps.setInt(1, scaffale.getId());

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
	}
	
	public ArrayList<ScaffaliBean> getScaffaleMagazzino(String nomeMagazzino) throws SQLException {
		Connection con = DBUtil.getConnection();
		ArrayList<ScaffaliBean> scaffali = new ArrayList<ScaffaliBean>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select idScaffale, quantita, quantitaMassima, idFumetto  from MagazzinoScaffali, Scaffali where idMagazzino=? and idScaffale = id");

			ps.setString(1, nomeMagazzino);

			rs = ps.executeQuery();

			while (rs.next()) {
				ProductBean fumetto = new ProductBean();
				ProductServiceDAO ser = new ProductServiceDAO();
				fumetto = ser.getProductsByID(String.valueOf(rs.getInt("idFumetto")));
				
				ScaffaliBean scaffale = new ScaffaliBean(rs.getInt("idScaffale"), fumetto, rs.getInt("quantita"), rs.getInt("quantitaMassima"));
				scaffali.add(scaffale);
			}
				

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return scaffali;
	}
}
