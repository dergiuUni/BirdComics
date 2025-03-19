package com.birdcomics.GestioneProfilo.Service;

import com.birdcomics.Model.Bean.MagazzinoBean;
import com.birdcomics.Model.Bean.RuoloBean;
import com.birdcomics.Model.Bean.UserBean;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface ProfileService {
   
    UserBean getUserDetails(String email) throws SQLException;
    List<UserBean> getUsersByRole(List<RuoloBean> roles, String magazzino) throws SQLException;
    void rimuoviAccount(String userEmail) throws SQLException;
	String login(String email, String password) throws SQLException;
	List<String> getUserTypes(String email) throws SQLException;
	void logout(HttpServletRequest request);
	 String registraAccount(String email, String password, String nome, String cognome, String telefono, 
             java.sql.Date dataNascita, String citta, String via, String numeroCivico, 
             String cap, ArrayList<RuoloBean> ruoli, MagazzinoBean magazzino) throws SQLException;
}
