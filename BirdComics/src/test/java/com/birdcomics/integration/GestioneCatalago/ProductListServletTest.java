package com.birdcomics.integration.GestioneCatalago;

import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.GestioneCatalogo.Controller.ProductListServlet;
import com.birdcomics.GestioneCatalogo.Service.CatalogoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ProductListServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CatalogoService catalogoService;

    @InjectMocks
    private ProductListServlet productListServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productListServlet = new ProductListServlet();
        productListServlet.catalogoService = catalogoService; // Inietta manualmente il mock del servizio
    }

    @Test
    void testDoGet_WithType() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta
        when(request.getParameter("type")).thenReturn("Comic");
        when(request.getParameter("search")).thenReturn(null);

        // Configura il mock del servizio per restituire una lista di prodotti
        List<ProductBean> products = new ArrayList<>();
        ProductBean product = new ProductBean();
        product.setId(1);
        product.setName("Comic Book 1");
        products.add(product);
        when(catalogoService.ricercaGenere("Comic")).thenReturn(products);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        productListServlet.doGet(request, response);

        // Verifica che i prodotti siano stati impostati come attributo della richiesta
        verify(request).setAttribute("products", products);
        verify(request).setAttribute("message", "Products in Comic");

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_WithSearch_GestoreCatalogo() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta
        when(request.getParameter("search")).thenReturn("Avengers");
        when(request.getParameter("type")).thenReturn(null);

        // Configura il mock della sessione per simulare un GestoreCatalogo
        List<String> userTypes = new ArrayList<>();
        userTypes.add("GestoreCatalogo");
        when(session.getAttribute("usertype")).thenReturn(userTypes);
        when(request.getSession(false)).thenReturn(session);

        // Configura il mock del servizio per restituire una lista di prodotti
        List<ProductBean> products = new ArrayList<>();
        ProductBean product = new ProductBean();
        product.setId(2);
        product.setName("Avengers Comic");
        products.add(product);
        when(catalogoService.ricercaID("Avengers")).thenReturn(products);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/visualizzaCatalogo.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        productListServlet.doGet(request, response);

        // Verifica che i prodotti siano stati impostati come attributo della richiesta
        verify(request).setAttribute("products", products);
        verify(request).setAttribute("message", "Showing Results for 'Avengers'");

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_WithSearch_RegularUser() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta
        when(request.getParameter("search")).thenReturn("Avengers");
        when(request.getParameter("type")).thenReturn(null);

        // Configura il mock della sessione per simulare un utente normale
        when(request.getSession(false)).thenReturn(null);

        // Configura il mock del servizio per restituire una lista di prodotti
        List<ProductBean> products = new ArrayList<>();
        ProductBean product = new ProductBean();
        product.setId(2);
        product.setName("Avengers Comic");
        products.add(product);
        when(catalogoService.ricercaTitolo("Avengers")).thenReturn(products);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        productListServlet.doGet(request, response);

        // Verifica che i prodotti siano stati impostati come attributo della richiesta
        verify(request).setAttribute("products", products);
        verify(request).setAttribute("message", "Showing Results for 'Avengers'");

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_NoSearchOrType() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta
        when(request.getParameter("search")).thenReturn(null);
        when(request.getParameter("type")).thenReturn(null);

        // Configura il mock del servizio per restituire tutti i prodotti
        List<ProductBean> products = new ArrayList<>();
        ProductBean product = new ProductBean();
        product.setId(3);
        product.setName("Generic Product");
        products.add(product);
        when(catalogoService.visualizzaCatalogo()).thenReturn(products);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        productListServlet.doGet(request, response);

        // Verifica che i prodotti siano stati impostati come attributo della richiesta
        verify(request).setAttribute("products", products);
        verify(request).setAttribute("message", "All Products");

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_SQLException() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta
        when(request.getParameter("search")).thenReturn("Avengers");
        when(request.getParameter("type")).thenReturn(null);

        // Configura il mock del servizio per lanciare un'eccezione SQLException
        when(catalogoService.ricercaTitolo("Avengers")).thenThrow(new SQLException("Database error"));

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        productListServlet.doGet(request, response);

        // Verifica che il messaggio di errore sia stato impostato
        verify(request).setAttribute("errorMessage", "An error occurred while retrieving products.");

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }
}