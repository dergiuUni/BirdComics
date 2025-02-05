package com.birdcomics.GestioneProfili;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.birdcomics.GestioneProfili.*;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class manageGestoriMagazzini
 */
@WebServlet("/GestioneUtentiServlet")
public class GestioneUtentiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GestioneUtentiServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Ottieni la lista degli utenti dal servizio
		HttpSession session = request.getSession();
		UserServiceDAO uDao = new UserServiceDAO();
		List<RuoloBean> ruoloUtenti = new ArrayList<>();
		List<UserBean> utenti = new ArrayList<>();
		List<String> userTypes = (List<String>) session.getAttribute("usertype"); 
		try {

			if(userTypes.contains(RuoloBean.GestoreGenerale.toString())) {
				ruoloUtenti.add(RuoloBean.GestoreMagazzino);
				utenti =  uDao.getUsersByRole(ruoloUtenti, null);
			}
			else if(userTypes.contains(RuoloBean.GestoreMagazzino.toString())) {
				ruoloUtenti.add(RuoloBean.RisorseUmane);
				utenti =  uDao.getUsersByRole(ruoloUtenti, null);
			}
			else if(userTypes.contains(RuoloBean.RisorseUmane.toString())) {
				ruoloUtenti.add(RuoloBean.Assistenza);
				ruoloUtenti.add(RuoloBean.Finanza);
				ruoloUtenti.add(RuoloBean.GestoreCatalogo);
				ruoloUtenti.add(RuoloBean.Magazziniere);
				ruoloUtenti.add(RuoloBean.Spedizioniere);
				utenti =  uDao.getUsersByRole(ruoloUtenti, null); 	
			}
		} catch (SQLException e) {
			request.setAttribute("message", "Errore nel recupero degli utenti.");
			e.printStackTrace();
		}


		request.setAttribute("utenti", utenti);

		// Invia alla JSP per visualizzare la lista
		request.getRequestDispatcher("manageUsers.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
