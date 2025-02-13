package com.birdcomics.GestioneCatalogo.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.GestioneCatalogo.Service.CatalogoService;
import com.birdcomics.GestioneCatalogo.Service.CatalogoServiceImpl;

@WebServlet("/ProductListServlet")
public class ProductListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CatalogoService catalogoService;

    public ProductListServlet() {
        super();
        this.catalogoService = new CatalogoServiceImpl();  // Inizializzazione del servizio
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String type = request.getParameter("type");
            String search = request.getParameter("search");
            List<ProductBean> products;
            String message;

            // Logica per il recupero dei prodotti
            if (type != null && !type.isEmpty()) {
                products = catalogoService.getAllProductsByType(type);  // Chiamata al servizio per ottenere i prodotti per tipo
                message = "Products in " + type;
            } else if (search != null && !search.isEmpty()) {
                products = catalogoService.searchAllProducts(search);  // Chiamata al servizio per la ricerca dei prodotti
                message = "Search results for '" + search + "'";
            } else {
                products = catalogoService.getAllProducts();  // Chiamata al servizio per ottenere tutti i prodotti
                for (ProductBean x : products) {
                	System.out.println(x.getId());
                }                
                message = "All Products";
            }

            // Se non ci sono prodotti
            if (products.isEmpty()) {
                message = "No products found.";
            }

            // Imposta gli attributi sulla richiesta per essere utilizzati nella JSP
            request.setAttribute("products", products);
            request.setAttribute("message", message);

            // Forward alla pagina JSP
            request.getRequestDispatcher("/index.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void destroy() {
        // Puoi chiudere eventuali risorse come il DAO, se necessario
    }
}
