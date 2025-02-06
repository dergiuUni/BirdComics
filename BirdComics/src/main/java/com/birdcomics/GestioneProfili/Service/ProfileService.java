package com.birdcomics.GestioneProfili.Service;

import com.birdcomics.Bean.RuoloBean;
import com.birdcomics.Bean.UserBean;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface ProfileService {
   
    UserBean getUserDetails(String username) throws SQLException;
    List<UserBean> getUsersByRole(List<RuoloBean> roles, String magazzino) throws SQLException;
    void removeUser(String userEmail) throws SQLException;
	String validateCredentials(String userName, String password) throws SQLException;
	List<String> getUserTypes(String userName) throws SQLException;
	void logout(HttpServletRequest request);
	 String registerUser(String email, String password, String nome, String cognome, String telefono, 
             java.sql.Date dataNascita, String citta, String via, int numeroCivico, 
             String cap, ArrayList<RuoloBean> ruoli) throws SQLException;
}
