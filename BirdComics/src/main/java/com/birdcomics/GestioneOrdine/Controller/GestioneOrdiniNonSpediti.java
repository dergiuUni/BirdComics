package com.birdcomics.GestioneOrdine.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Model.Bean.OrderBean;
import com.birdcomics.GestioneOrdine.Service.OrdineService;
import com.birdcomics.GestioneOrdine.Service.OrdineServiceImpl;

@WebServlet("/GestioneOrdiniNonSpediti")
public class GestioneOrdiniNonSpediti extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrdineService ordineService;  // Riferimento al servizio

    public GestioneOrdiniNonSpediti() {
        super();
        this.ordineService = new OrdineServiceImpl();  // Inizializza il servizio
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<OrderBean> products = new ArrayList<>();
        String message = "";

        try {
            products = ordineService.getOrdiniNonSpediti();  // Ottieni gli ordini non spediti dal servizio

            if (products.isEmpty()) {
                message = "No items found";        
            }

            request.setAttribute("message", message);
            request.setAttribute("products", products);

        } catch (SQLException e) {
            e.printStackTrace();
            // Gestire l'eccezione in modo appropriato
        }

        request.getRequestDispatcher("/visualizzaOrdiniNonSpediti.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
