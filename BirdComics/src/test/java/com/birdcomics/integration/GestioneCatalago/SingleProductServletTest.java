package com.birdcomics.integration.GestioneCatalago;

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.GestioneCatalogo.Controller.SingleProductServlet;
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
import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class SingleProductServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CatalogoService catalogoService;

    @InjectMocks
    private SingleProductServlet singleProductServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        singleProductServlet = new SingleProductServlet(); // Inizializza la servlet
        singleProductServlet.catalogoService = catalogoService; // Inietta il mock del servizio
    }

    @Test
    void testDoGet_ProductFound() throws ServletException, IOException, SQLException {
        // Configura il parametro della richiesta
        when(request.getParameter("pid")).thenReturn("123");

        // Configura il mock del servizio per restituire un prodotto
        ProductBean product = new ProductBean();
        product.setId(1);
        product.setName("Prodotto di Test");
        when(catalogoService.getProductById("123")).thenReturn(product);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/singleProduct.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        singleProductServlet.doGet(request, response);

        // Verifica che il prodotto sia stato impostato come attributo della richiesta
        verify(request).setAttribute("product", product);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_ProductNotFound() throws ServletException, IOException, SQLException {
        // Configura il parametro della richiesta
        when(request.getParameter("pid")).thenReturn("999");

        // Configura il mock del servizio per lanciare un'eccezione SQLException
        when(catalogoService.getProductById("999")).thenThrow(new SQLException("Prodotto non trovato"));

        // Configura il mock del RequestDispatcher per la pagina di errore
        when(request.getRequestDispatcher("error.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        singleProductServlet.doGet(request, response);

        // Verifica che il messaggio di errore sia stato impostato
        verify(request).setAttribute("errorMessage", "Errore nel recupero del prodotto.");

        // Verifica che il RequestDispatcher sia stato chiamato per la pagina di errore
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_NoProductId() throws ServletException, IOException, SQLException {
        // Configura il parametro della richiesta come null
        when(request.getParameter("pid")).thenReturn(null);

        // Esegui il metodo doGet
        singleProductServlet.doGet(request, response);

        // Verifica che non ci siano interazioni con il CatalogoService
        verify(catalogoService, never()).getProductById(anyString());

        // Verifica che non ci siano interazioni con il RequestDispatcher
        verify(requestDispatcher, never()).forward(request, response);
    }
}