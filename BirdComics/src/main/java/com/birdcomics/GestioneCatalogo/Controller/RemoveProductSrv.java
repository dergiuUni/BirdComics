package com.birdcomics.GestioneCatalogo.Controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.GestioneCatalogo.Service.CatalogoService;
import com.birdcomics.GestioneCatalogo.Service.CatalogoServiceImpl;


@WebServlet("/RemoveProductSrv")
public class RemoveProductSrv extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CatalogoService catalogoService;

    public RemoveProductSrv() {
        super();
        this.catalogoService = new CatalogoServiceImpl();  // Inizializzazione del servizio
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String prodId = request.getParameter("prodid");

        if (prodId == null || prodId.isEmpty()) {
            // Se l'ID del prodotto è null o vuoto, reindirizza con un messaggio di errore
            RequestDispatcher rd = request.getRequestDispatcher("./ProductListServlet?message=Error: Product ID is missing");
            rd.forward(request, response);
            return;
        }

        String status = null;
        try {
            status = catalogoService.removeProduct(prodId);  // Chiamata al servizio per rimuovere il prodotto
        } catch (SQLException e) {
            e.printStackTrace();
            status = "Error removing product";
        }

        RequestDispatcher rd = request.getRequestDispatcher("./ProductListServlet?message=" + status);
        rd.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}
