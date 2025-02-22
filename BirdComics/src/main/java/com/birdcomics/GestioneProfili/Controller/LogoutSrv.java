package com.birdcomics.GestioneProfili.Controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.GestioneProfili.Service.ProfileService;
import com.birdcomics.GestioneProfili.Service.ProfileServiceImpl;

@WebServlet("/LogoutSrv")
public class LogoutSrv extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ProfileService profileService;

    public LogoutSrv() {
        super();
        this.profileService = new ProfileServiceImpl();  // Usa ProfileService per gestire il logout
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        // Usa il ProfileService per invalidare la sessione
        profileService.logout(request);

        // Reindirizza l'utente alla pagina di login con un messaggio
        RequestDispatcher rd = request.getRequestDispatcher("login.jsp?message=Successfully Logged Out!");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
