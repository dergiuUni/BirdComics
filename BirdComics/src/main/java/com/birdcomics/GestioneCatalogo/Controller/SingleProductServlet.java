package com.birdcomics.GestioneCatalogo.Controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.GestioneCatalogo.Service.CatalogoService;
import com.birdcomics.GestioneCatalogo.Service.CatalogoServiceImpl;

@WebServlet("/SingleProductServlet")
public class SingleProductServlet extends HttpServlet {
    
    public CatalogoService catalogoService;

    public SingleProductServlet() {
        super();
        this.catalogoService = new CatalogoServiceImpl();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("pid");

        if (id != null) {
            try {
                // Usa il CatalogoService per ottenere il prodotto
                ProductBean product = catalogoService.getProductById(id);
                request.setAttribute("product", product);
                
                // Forward alla pagina per la visualizzazione
                request.getRequestDispatcher("/singleProduct.jsp").forward(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Errore nel recupero del prodotto.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        }
    }
}
