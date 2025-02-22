package com.birdcomics.GestioneOrdine.Controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.GestioneOrdine.Service.OrdineService;
import com.birdcomics.GestioneOrdine.Service.OrdineServiceImpl;

@WebServlet("/review_payment")
public class ReviewPaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrdineService ordineService;

    public ReviewPaymentServlet() {
        this.ordineService = new OrdineServiceImpl(); // Inizializza il servizio
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");
        String email = (String) request.getSession().getAttribute("email");

        try {
            if (paymentId != null && payerId != null && email != null) {
                // Usa il servizio per processare il pagamento e creare l'ordine
                ordineService.processPaymentAndCreateOrder(paymentId, payerId, email);
                // Reindirizza alla home page dopo l'elaborazione
                response.sendRedirect("index.jsp");
            } else {
                // Se manca qualche parametro, invia un errore
                request.setAttribute("errorMessage", "Missing required parameters.");
                request.getRequestDispatcher("error2.jsp").forward(request, response);
            }
        } catch (SQLException ex) {
            // Gestisci eventuali errori
            request.setAttribute("errorMessage", ex.getMessage());
            ex.printStackTrace();
            request.getRequestDispatcher("error2.jsp").forward(request, response);
        }
    }
}
