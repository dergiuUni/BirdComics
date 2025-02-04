package com.birdcomics.GestioneMagazzino;

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
@WebServlet("/GestoriMagazziniServlet")
public class GestoriMagazziniServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestoriMagazziniServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Ottieni la lista degli utenti dal servizio
    	UserServiceDAO uDao = new UserServiceDAO();
        List<UserBean> gestoriMagazzino = new ArrayList<>();
        
        try {
            // Metodo che recupera gli utenti dal database (aggiungere logica per ottenere gli utenti reali)
        	gestoriMagazzino =  uDao.getUsersByRole("GestoreMagazzino");// Funzione per ottenere gli utenti
        } catch (SQLException e) {
        	request.setAttribute("message", "Errore nel recupero dei gestori magazzino.");
            e.printStackTrace();
        }
        

        request.setAttribute("gestoriMagazzino", gestoriMagazzino);
    
        // Invia alla JSP per visualizzare la lista
        request.getRequestDispatcher("manageUsers.jsp").forward(request, response);
        }
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
