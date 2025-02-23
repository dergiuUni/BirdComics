package com.birdcomics.GestioneProfili.Controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

        String message;

        // Controlla la sessione
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            message = "Successfully Logged Out!";
        } else {
            message = "You were not logged in!";
        }

        // Usa il ProfileService per eventuali azioni aggiuntive di logout
        profileService.logout(request);

        // Reindirizza l'utente alla pagina di login con il messaggio corretto
        RequestDispatcher rd = request.getRequestDispatcher("login.jsp?message=" + message);
        rd.forward(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
