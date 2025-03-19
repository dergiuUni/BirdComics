package com.birdcomics.GestioneProfili.Controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.GestioneProfili.Service.ProfileService;
import com.birdcomics.GestioneProfili.Service.ProfileServiceImpl;

@WebServlet("/UserProfileServlet")
public class UserProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public ProfileService profileService;

    public UserProfileServlet() {
        super();
        this.profileService = new ProfileServiceImpl();  // Inizializza il servizio
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = (String) request.getSession().getAttribute("email");
        if (email != null) {
            try {
                // Usa ProfileService per ottenere i dettagli dell'utente
                UserBean user = profileService.getUserDetails(email);
                request.setAttribute("user", user);  // Imposta l'utente nella request
            } catch (SQLException e) {
                e.printStackTrace();
            }
            request.getRequestDispatcher("/userProfile.jsp").forward(request, response);  // Forward alla pagina del profilo
        }
        else {
        	 response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
             return; // Importante: interrompi l'esecuzione del metodo
        }
        	
    }
}
