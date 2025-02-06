package com.birdcomics.GestioneProfili.Controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Bean.UserBean;
import com.birdcomics.GestioneProfili.Service.ProfileService;
import com.birdcomics.GestioneProfili.Service.ProfileServiceImpl;

@WebServlet("/UserProfileServlet")
public class UserProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ProfileService profileService;

    public UserProfileServlet() {
        super();
        this.profileService = new ProfileServiceImpl();  // Inizializza il servizio
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = (String) request.getSession().getAttribute("username");
        if (userName != null) {
            try {
                // Usa ProfileService per ottenere i dettagli dell'utente
                UserBean user = profileService.getUserDetails(userName);
                request.setAttribute("user", user);  // Imposta l'utente nella request
            } catch (SQLException e) {
                e.printStackTrace();
            }
            request.getRequestDispatcher("/userProfile.jsp").forward(request, response);  // Forward alla pagina del profilo
        }
    }
}
