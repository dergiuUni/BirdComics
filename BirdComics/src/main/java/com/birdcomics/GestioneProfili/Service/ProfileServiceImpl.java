package com.birdcomics.GestioneProfili.Service;

import com.birdcomics.Bean.RuoloBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.Dao.UserServiceDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ProfileServiceImpl implements ProfileService {

	private UserServiceDAO userDao;

	// Costruttore che permette di passare un DAO specifico (utile per test)
	public ProfileServiceImpl(UserServiceDAO userDao) {
		this.userDao = userDao;
	}

	// Costruttore predefinito
	public ProfileServiceImpl() {
		this.userDao = new UserServiceDAO();
	}

	@Override
	public UserBean getUserDetails(String username) throws SQLException {
		return userDao.getUserDetails(username);
	}

	@Override
	public List<UserBean> getUsersByRole(List<RuoloBean> roles, String magazzino) throws SQLException {
		return userDao.getUsersByRole(roles, magazzino);
	}

	@Override
	public void removeUser(String userEmail) throws SQLException {
		// Usa UserServiceDAO per rimuovere l'utente
		userDao.deleteUser(userEmail);
	}

	@Override
	public String validateCredentials(String userName, String password) throws SQLException {
		return userDao.isValidCredential(userName, password); // Delegato al DAO
	}

	@Override
	public List<String> getUserTypes(String userName) throws SQLException {
		List<String> userTypes = new ArrayList<>();
		List<RuoloBean> roles = userDao.getUserType(userName);
		for (RuoloBean role : roles) {
			userTypes.add(role.toString());
		}
		return userTypes; // Recupera i tipi di ruolo dell'utente
	}

	@Override
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();  // Invalida la sessione dell'utente
	}

	public String registerUser(String email, String password, String nome, String cognome, String telefono, 
			java.sql.Date dataNascita, String citta, String via, int numeroCivico, 
			String cap, ArrayList<RuoloBean> ruoli) throws SQLException {
		// Delego la registrazione al UserServiceDAO
		return userDao.registerUser(email, password, nome, cognome, telefono, dataNascita, 
				citta, via, numeroCivico, cap, ruoli);

	}
}
