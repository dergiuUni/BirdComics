package com.birdcomics.GestioneCatalogo.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.GestioneCatalogo.Service.CatalogoService;
import com.birdcomics.GestioneCatalogo.Service.CatalogoServiceImpl;

@WebServlet("/ProductListServlet")
public class ProductListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CatalogoService catalogoService;

    public ProductListServlet() {
        super();
        this.catalogoService = new CatalogoServiceImpl();  // Inizializzazione del servizio
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProductBean> products = new ArrayList<>();
        String search = request.getParameter("search");
        String type = request.getParameter("type");
        String message = "";

        // Recupera la sessione
        HttpSession session = request.getSession(false); // Usa false per non creare una nuova sessione se non esiste
        List<String> userTypes = null;

        if (session != null) {
            // Recupera userType dalla sessione
            userTypes = (List<String>) session.getAttribute("usertype");
        }

        try {
            // Logica per il recupero dei prodotti
            if (type != null && !type.isEmpty()) {
                // Se è specificato un tipo, recupera i prodotti per tipo
                products = catalogoService.getAllProductsByType(type); // Usa il servizio
                message = "Products in " + type;
            } else if (search != null && !search.isEmpty()) {
                // Se è specificata una ricerca, gestisci la ricerca in base al tipo di utente
                if (userTypes != null && userTypes.contains("GestoreCatalogo")) {
                    // Se l'utente è un GestoreCatalogo, usa searchAllProductsGestore
                    products = catalogoService.searchAllProductsGestore(search); // Usa il servizio
                } else {
                    // Altrimenti, usa searchAllProducts
                    products = catalogoService.searchAllProducts(search); // Usa il servizio
                }

                // Imposta il messaggio in base ai risultati della ricerca
                if (products.isEmpty()) {
                    message = "No products found.";
                } else {
                    message = "Showing Results for '" + search + "'";
                }
            } else {
                // Se non è specificato né un tipo né una ricerca, recupera tutti i prodotti
                products = catalogoService.getAllProducts(); // Usa il servizio
                message = "All Products";
            }

            // Imposta gli attributi della richiesta
            request.setAttribute("products", products);
            request.setAttribute("message", message);

        } catch (SQLException e) {
            e.printStackTrace();
            // Gestire l'eccezione in modo appropriato
            request.setAttribute("errorMessage", "An error occurred while retrieving products.");
        }

        // Reindirizza alla pagina JSP appropriata in base al tipo di utente
        if (userTypes != null && userTypes.contains("GestoreCatalogo")) {
            // Se l'utente è un GestoreCatalogo, reindirizza a visualizzaCatalogo.jsp
            request.getRequestDispatcher("/visualizzaCatalogo.jsp").forward(request, response);
        } else {
            // Altrimenti, reindirizza a index.jsp
            request.getRequestDispatcher("/index.jsp").forward(request, response);
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