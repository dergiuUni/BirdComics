package com.birdcomics.GestioneProfili;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.DatabaseImplementator.DBUtil;
import com.birdcomics.GestioneIndirizzo.IndirizzoBean;
import com.birdcomics.GestioneIndirizzo.IndirizzoDao;


public class UserServiceDAO {


	public String registerUser(String email, String password, String nome, String cognome, String numeroTelefono, java.sql.Date dataNascita, String nomeCitta, String via, int numeroCivico, String cap, ArrayList<RuoloBean> ruolo) throws SQLException {
		IndirizzoBean indirizzo = new IndirizzoBean(nomeCitta, via, numeroCivico, cap);
		UserBean user = new UserBean(email, password, nome, cognome, numeroTelefono, dataNascita, indirizzo, ruolo);

	    String status = "User Registration Failed!";

	    boolean isRegtd = isRegistered(user.getEmail());

	    if (isRegtd) {
	        status = "Email Id Already Registered!";
	        return status;
	    }

	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	        // Modifica della query per includere il campo usertype
	    	IndirizzoDao in = new IndirizzoDao(con);
	    	if(!in.ifExists(nomeCitta, via, numeroCivico, cap)) {
	    		ps = con.prepareStatement("INSERT INTO Indirizzo (nomeCitta, via, numeroCivico, cap) VALUES (?, ?, ?, ?)");
	    		ps.setString(1, indirizzo.getNomeCitta());
		        ps.setString(2, indirizzo.getVia());
		        ps.setInt(3, indirizzo.getNumeroCivico());
		        ps.setString(4, indirizzo.getCap());
		        ps.executeUpdate();
	    	}
	    	
	        ps = con.prepareStatement("INSERT INTO Utente (email, pass, nome, cognome, telefono, nomeCitta, via, numeroCivico, cap, dataNascita) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

	        ps.setString(1, user.getEmail());
	        ps.setString(2, user.getPassword());
	        ps.setString(3, user.getNome());
	        ps.setString(4, user.getCognome());
	        ps.setString(5, user.getNumeroTelefono());
	        ps.setString(6, indirizzo.getNomeCitta());
	        ps.setString(7, indirizzo.getVia());
	        ps.setInt(8, indirizzo.getNumeroCivico());
	        ps.setString(9, indirizzo.getCap());
	        ps.setDate(10, (Date) user.getDataNascita());

	        System.out.println();
	        int k = ps.executeUpdate();

	        if (k > 0) {
	            
	        	for (RuoloBean ruoloBean : ruolo) {
					ps = con.prepareStatement("INSERT INTO Utente_Ruolo (idRuolo, emailUtente) VALUES (?, ?)");
					ps.setString(1, ruoloBean.toString());
					ps.setString(2, user.getEmail());
					ps.executeUpdate();
				}
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

	
	public String isValidCredential(String emailId, String password) throws SQLException {
		String status = "Login Denied! Incorrect Username or Password";

		Connection con = DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement("select * from Utente where email=? and pass=?");

			ps.setString(1, emailId);
			ps.setString(2, password);

			rs = ps.executeQuery();

			if (rs.next())
				status = "valid";

		} catch (SQLException e) {
			status = "Error: " + e.getMessage();
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);
		
		

		return status;
	}

	
	public UserBean getUserDetails(String emailId) throws SQLException {

		UserBean user = null;

		Connection con = DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<RuoloBean> ru = new ArrayList<RuoloBean>();
		
		try {
			ps = con.prepareStatement("select * from Utente, Utente_Ruolo where Utente.email=? and Utente.email = Utente_Ruolo.emailUtente");
			ps.setString(1, emailId);
			
			rs = ps.executeQuery();
			if (rs.next()) {
				IndirizzoBean in = new IndirizzoBean(rs.getString("nomeCitta"), rs.getString("via"), rs.getInt("numeroCivico"), rs.getString("cap"));
				user = new UserBean(rs.getString("email"), rs.getString("pass"), rs.getString("nome"), rs.getString("cognome"), rs.getString("telefono"), rs.getDate("dataNascita") ,in, null );
				
				
				while (rs.next()) {
					ru.add(RuoloBean.fromString(rs.getString("idRuolo")));
				}
				user.setRuoloBean(ru);
			}
			
			
			return user;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);
		return user;
	}



	
	public List<RuoloBean> getUserType(String emailId) throws SQLException {
	    String userType = "";
	    List<RuoloBean> ruolo = new ArrayList<RuoloBean>();
	    
	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement("select idRuolo from Utente_Ruolo where emailUtente=?")) {

	        ps.setString(1, emailId);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
            		ruolo.add(RuoloBean.fromString(rs.getString("idRuolo")));
	            }
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return ruolo;
	}


// vedere se mettere i limiti e modificarlo
	public void updateUserDetails(String email, String password, String nome, String cognome, String telefono, String nomeCitta, String via, int numeroCivico, String cap) throws SQLException {
	
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	 
	    	IndirizzoBean indirizzo = new IndirizzoBean(nomeCitta, via, numeroCivico, cap);
	    	IndirizzoDao in = new IndirizzoDao(con);
	    	if(!in.ifExists(nomeCitta, via, numeroCivico, cap)) {
	    		ps = con.prepareStatement("INSERT INTO Indirizzo (nomeCitta, via, numeroCivico, cap) VALUES (?, ?, ?, ?)");
	    		ps.setString(1, indirizzo.getNomeCitta());
		        ps.setString(2, indirizzo.getVia());
		        ps.setInt(3, indirizzo.getNumeroCivico());
		        ps.setString(4, indirizzo.getCap());
		        ps.executeUpdate();
	    	}
	        // Prepare the SQL update statement
	        String updateSQL = "UPDATE Utente SET nome = ?, cognome = ?, telefono = ?, nomeCitta = ?, via = ?, numeroCivico = ?, cap = ? WHERE email = ? AND pass = ?";
	        ps = con.prepareStatement(updateSQL);

	        // Set the parameters for the update statement
	        ps.setString(1, nome);
	        ps.setString(2, cognome);
	        ps.setString(3, telefono);
	        ps.setString(4, indirizzo.getNomeCitta());
	        ps.setString(5, indirizzo.getVia());
	        ps.setInt(6, indirizzo.getNumeroCivico());
	        ps.setString(7, indirizzo.getCap());
	        ps.setString(8, email);
	        ps.setString(9, password);

	        // Execute the update statement
	        ps.executeUpdate();
	   
	     
	    }
		
	public String updateRuolo(String email, String password, ArrayList<RuoloBean> ruolo) throws SQLException {
	    String status = "User Registration Failed!";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
        	for (RuoloBean ruoloBean : ruolo) {
				ps = con.prepareStatement("INSERT INTO Utente_Ruolo (idRuolo, emailUtente) VALUES (?, ?)");
				ps.setString(1, ruoloBean.toString());
				ps.setString(2, email);
			}
        	status = "User Registered Successfully!";
        }catch (SQLException e) {
	        status = "Error: " + e.getMessage();
	        e.printStackTrace();
	    } finally {
	        DBUtil.closeConnection(ps);
	    }

	    return status;
	}
	public List<UserBean> getUsersByRole(List <RuoloBean> ruolo, String nomeMagazzino) throws SQLException {
	    List<UserBean> usersList = new ArrayList<>();

	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    
	    try {
	        // SQL query to get users based on their role
	        String sql = "SELECT Utente.email, Utente.nome, Utente.cognome, Utente.telefono, Utente.nomeCitta, Utente.via, Utente.numeroCivico, Utente.cap, Utente.dataNascita "
	                   + "FROM Utente "
	                   + "JOIN Utente_Ruolo ON Utente.email = Utente_Ruolo.emailUtente "
	                   + "WHERE Utente_Ruolo.idRuolo = ?";
	        ps = con.prepareStatement(sql);
	        ps.setString(1, ruolo.toString()); // Set the role ID in the query

	        rs = ps.executeQuery();

	        while (rs.next()) {
	            // Create IndirizzoBean object for user address
	            IndirizzoBean indirizzo = new IndirizzoBean(rs.getString("nomeCitta"), rs.getString("via"), rs.getInt("numeroCivico"), rs.getString("cap"));
	            
	            // Create UserBean object for each user (without password)
	            UserBean user = new UserBean(
	                rs.getString("email"), 
	                null, // Password is not needed
	                rs.getString("nome"), 
	                rs.getString("cognome"), 
	                rs.getString("telefono"), 
	                rs.getDate("dataNascita"), 
	                indirizzo, 
	                null // You can populate user roles here if needed
	            );
	            
	            // Add the user to the list
	            usersList.add(user);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        DBUtil.closeConnection(ps);
	        DBUtil.closeConnection(rs);
	    }

	    return usersList;
	}


	public void deleteUser(String email) throws SQLException {
	    //String status = "";
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	        // Avvia una transazione
	        con.setAutoCommit(false);

	        // Elimina prima i ruoli associati all'utente
	        ps = con.prepareStatement("DELETE FROM Utente_Ruolo WHERE emailUtente = ?");
	        ps.setString(1, email);
	        ps.executeUpdate();

	        // Elimina l'utente dalla tabella Utente
	        ps = con.prepareStatement("DELETE FROM Utente WHERE email = ?");
	        ps.setString(1, email);
	        int affectedRows = ps.executeUpdate();

	        if (affectedRows > 0) {
	            //status = "Utente cancellato!";
	            con.commit(); // Conferma la transazione
	        } else {
	            //status = "Utente non trovato!";
	            con.rollback(); // Annulla la transazione se l'utente non è stato trovato
	        }

	    } catch (SQLException e) {
	        //status = "Error: " + e.getMessage();
	        e.printStackTrace();
	        con.rollback(); // Rollback in caso di errore
	    } finally {
	        DBUtil.closeConnection(ps);
	        con.setAutoCommit(true); // Ripristina il comportamento di default delle transazioni
	    }

	    //return status;
	}


}
