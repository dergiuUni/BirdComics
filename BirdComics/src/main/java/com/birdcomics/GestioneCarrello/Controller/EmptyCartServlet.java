package com.birdcomics.GestioneCarrello.Controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.GestioneCarrello.Service.CarelloServiceImpl;

@WebServlet("/EmptyCartServlet")
public class EmptyCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EmptyCartServlet() {
        super();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");

        if (userName == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
            return;
        }

        CarelloServiceImpl cartService = new CarelloServiceImpl(); // Usa il servizio

        try {
            cartService.emptyCart(userName); // Svuota il carrello
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Errore durante lo svuotamento del carrello");
            return;
        }

        response.sendRedirect("CartDetailsServlet");  // Redirige alla pagina del carrello
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
