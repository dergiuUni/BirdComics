package com.birdcomics.GestioneCarrello.Controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.GestioneCarrello.Service.CarrelloServiceImpl;

@WebServlet("/EmptyCartServlet")
public class EmptyCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EmptyCartServlet() {
        super();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        if (email == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
            return;
        }

        CarrelloServiceImpl cartService = new CarrelloServiceImpl(); // Usa il servizio

        try {
            // Svuota il carrello nel database
            cartService.svuotaCarrello(session, email);

            // Crea un nuovo carrello vuoto e aggiorna la sessione
            CartBean cartBean = new CartBean(email);
            session.setAttribute("cart", cartBean);

            // Mostra un messaggio di conferma
            session.setAttribute("message", "Cart emptied successfully!");

            // Reindirizza alla pagina del carrello
            response.sendRedirect("CartDetailsServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Errore durante lo svuotamento del carrello");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}