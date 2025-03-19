package com.birdcomics.Model.Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.Model.Bean.IndirizzoBean;
import com.birdcomics.Model.Bean.MagazzinoBean;
import com.birdcomics.Model.Bean.RuoloBean;
import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.Utils.DBUtil;


public class UserServiceDAO {

	public String registerUser(String email, String password, String nome, String cognome, String telefono,
			Date dataNascita, String citta, String via, String numeroCivico, String cap, ArrayList<RuoloBean> ruoli,
			MagazzinoBean magazzino) throws SQLException {
	    String status = "User Registration Failed!";
	    UserBean newUser;
	    if(magazzino != null) {
	    	newUser = new UserBean(email, password, nome,cognome, telefono, dataNascita, new IndirizzoBean(citta,via, numeroCivico, cap), ruoli, magazzino);
	    }
	    else {
	    	newUser = new UserBean(email, password, nome,cognome, telefono, dataNascita, new IndirizzoBean(citta,via, numeroCivico, cap), ruoli);
	    }
	    
	    List  <RuoloBean> ruolo = newUser.getRuolo();
	    boolean isRegtd = isRegistered(newUser.getEmail());

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
	    	if(!in.ifExists(newUser.getIndirizzo().getNomeCitta(),  newUser.getIndirizzo().getVia(),  newUser.getIndirizzo().getNumeroCivico(),  newUser.getIndirizzo().getCap())) {
	    		ps = con.prepareStatement("INSERT INTO Indirizzo (nomeCitta, via, numeroCivico, cap) VALUES (?, ?, ?, ?)");
	    		ps.setString(1, newUser.getIndirizzo().getNomeCitta());
		        ps.setString(2, newUser.getIndirizzo().getVia());
		        ps.setString(3, newUser.getIndirizzo().getNumeroCivico());
		        ps.setString(4, newUser.getIndirizzo().getCap());
		        ps.executeUpdate();
	    	}
	    	
	        ps = con.prepareStatement("INSERT INTO Utente (email, pass, nome, cognome, telefono, nomeCitta, via, numeroCivico, cap, dataNascita) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

	        ps.setString(1, newUser.getEmail());
	        ps.setString(2, newUser.getPassword());
	        ps.setString(3, newUser.getNome());
	        ps.setString(4, newUser.getCognome());
	        ps.setString(5, newUser.getNumeroTelefono());
	        ps.setString(6, newUser.getIndirizzo().getNomeCitta());
	        ps.setString(7, newUser.getIndirizzo().getVia());
	        ps.setString(8, newUser.getIndirizzo().getNumeroCivico());
	        ps.setString(9, newUser.getIndirizzo().getCap());
	        ps.setDate(10, (Date) newUser.getDataNascita());

	        System.out.println();
	        int k = ps.executeUpdate();

	        if (k > 0) {
	            
	        	for (RuoloBean ruoloBean : ruolo) {
					ps = con.prepareStatement("INSERT INTO Utente_Ruolo (idRuolo, emailUtente, nomeMagazzino) VALUES (?, ?, ?)");
					ps.setString(1, ruoloBean.toString());
					ps.setString(2, newUser.getEmail());
					if(magazzino != null) {
						ps.setString(3, newUser.getMagazzino().getNome());
					}
					else {
						ps.setString(3, null);
					}
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
		String status = "Login Denied! Incorrect email or Password";

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
				IndirizzoBean in = new IndirizzoBean(rs.getString("nomeCitta"), rs.getString("via"), rs.getString("numeroCivico"), rs.getString("cap"));
				MagazzinoBean m = new MagazzinoBean();
				m.setNome(rs.getString("nomeMagazzino"));
				user = new UserBean(rs.getString("email"), rs.getString("pass"), rs.getString("nome"), rs.getString("cognome"), rs.getString("telefono"), rs.getDate("dataNascita") ,in, null, m);
				ru.add(RuoloBean.fromString(rs.getString("idRuolo")));
				
				while (rs.next()) {
					ru.add(RuoloBean.fromString(rs.getString("idRuolo")));
				}
				user.setRuolo(ru);
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
	public void updateUserDetails(String email, String password, String nome, String cognome, String telefono, String nomeCitta, String via, String numeroCivico, String cap) throws SQLException {
	
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	 
	    	IndirizzoBean indirizzo = new IndirizzoBean(nomeCitta, via, numeroCivico, cap);
	    	IndirizzoDao in = new IndirizzoDao(con);
	    	if(!in.ifExists(nomeCitta, via, numeroCivico, cap)) {
	    		ps = con.prepareStatement("INSERT INTO Indirizzo (nomeCitta, via, numeroCivico, cap) VALUES (?, ?, ?, ?)");
	    		ps.setString(1, indirizzo.getNomeCitta());
		        ps.setString(2, indirizzo.getVia());
		        ps.setString(3, indirizzo.getNumeroCivico());
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
	        ps.setString(6, indirizzo.getNumeroCivico());
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
	public List<UserBean> getUsersByRole(List<RuoloBean> ruoli, String nomeMagazzino) throws SQLException {
	    List<UserBean> usersList = new ArrayList<>();
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	        // Costruzione della query SQL
	        StringBuilder sql = new StringBuilder("SELECT Utente.email, Utente.nome, Utente.cognome, Utente.telefono, ")
	            .append("Utente.nomeCitta, Utente.via, Utente.numeroCivico, Utente.cap, Utente.dataNascita ")
	            .append("FROM Utente ")
	            .append("JOIN Utente_Ruolo ON Utente.email = Utente_Ruolo.emailUtente ")
	            .append("WHERE Utente_Ruolo.idRuolo IN (");

	        // Aggiungi i ruoli nella query (in modo dinamico)
	        for (int i = 0; i < ruoli.size(); i++) {
	            sql.append("?");
	            if (i < ruoli.size() - 1) {
	                sql.append(", ");
	            }
	        }

	        // Se viene fornito un nomeMagazzino, aggiungi anche il filtro per magazzino
	        if (nomeMagazzino != null) {
	            sql.append(") AND Utente_Ruolo.nomeMagazzino = ?");
	        } else {
	            sql.append(")");
	        }

	        // Prepara la query
	        ps = con.prepareStatement(sql.toString());

	        // Imposta i parametri dei ruoli
	        for (int i = 0; i < ruoli.size(); i++) {
	            ps.setString(i + 1, ruoli.get(i).toString()); // Usa .toString() per convertire l'enum in stringa
	        }

	        // Se c'è un nomeMagazzino, imposta anche questo parametro
	        if (nomeMagazzino != null) {
	            ps.setString(ruoli.size() + 1, nomeMagazzino);
	        }

	        // Esegui la query
	        rs = ps.executeQuery();

	        // Elenco degli utenti trovati
	        while (rs.next()) {
	            IndirizzoBean indirizzo = new IndirizzoBean(rs.getString("nomeCitta"), rs.getString("via"), 
	                                                        rs.getString("numeroCivico"), rs.getString("cap"));

	            UserBean user = new UserBean(
	                rs.getString("email"), 
	                null, // Non vogliamo la password
	                rs.getString("nome"), 
	                rs.getString("cognome"), 
	                rs.getString("telefono"), 
	                rs.getDate("dataNascita"), 
	                indirizzo, 
	                null // Puoi eventualmente aggiungere qui i ruoli
	            );

	            // Aggiungi l'utente alla lista
	            usersList.add(user);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e; // Rilancia l'eccezione
	    } finally {
	        DBUtil.closeConnection(rs);
	        DBUtil.closeConnection(ps);
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
