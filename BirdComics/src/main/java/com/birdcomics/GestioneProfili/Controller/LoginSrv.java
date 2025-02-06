package com.birdcomics.GestioneProfili.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.Bean.RuoloBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.GestioneProfili.Service.ProfileService;
import com.birdcomics.GestioneProfili.Service.ProfileServiceImpl;

@WebServlet("/LoginSrv")
public class LoginSrv extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProfileService profileService;

    public LoginSrv() {
        super();
        this.profileService = new ProfileServiceImpl(); // Inizializza il servizio di profilo
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");

        // Usa il ProfileService per validare le credenziali e ottenere i dettagli dell'utente
        String status = "Login Denied! Invalid Username or password.";
        try {
            status = profileService.validateCredentials(userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if ("valid".equalsIgnoreCase(status)) {
            try {
                // Ottieni i dettagli dell'utente e il ruolo tramite il servizio
                UserBean user = profileService.getUserDetails(userName);
                List<String> userType = profileService.getUserTypes(userName);

                // Gestisci la sessione
                HttpSession session = request.getSession();
                session.setAttribute("userdata", user);
                session.setAttribute("username", userName);
                session.setAttribute("usertype", userType);

                // Redirigi all'UserProfileServlet
                RequestDispatcher rd = request.getRequestDispatcher("/UserProfileServlet");
                rd.forward(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                // Gestisci eventuali errori SQL durante il recupero dell'utente
            }
        } else {
            // Se login non valido, invia un messaggio di errore
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp?message=" + status);
            rd.include(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
