package com.birdcomics.GestioneProfilo.Controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.GestioneProfilo.Service.ProfileService;
import com.birdcomics.GestioneProfilo.Service.ProfileServiceImpl;

@WebServlet("/RemoveUserServlet")
public class RemoveUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ProfileService profileService;

    public RemoveUserServlet() {
        super();
        this.profileService = new ProfileServiceImpl(); // Inizializza il servizio
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userEmail = request.getParameter("email");
        String status = null;

        if (userEmail != null && !userEmail.trim().isEmpty()) {
            try {
                // Usa ProfileService per eliminare l'utente
                profileService.rimuoviAccount(userEmail);
            } catch (SQLException e) {
                status = "Errore di database: " + e.getMessage();
                e.printStackTrace();
                request.setAttribute("status", status);  // Aggiungi il messaggio di errore alla request
            }
        }

        // Invia alla servlet che gestisce l'elenco degli utenti
        RequestDispatcher rd = request.getRequestDispatcher("/GestioneUtentiServlet");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
