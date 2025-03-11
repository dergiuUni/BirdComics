package com.birdcomics.integration.GestioneCatalago;

import com.birdcomics.GestioneCatalogo.Controller.RemoveProductSrv;
import com.birdcomics.GestioneCatalogo.Service.CatalogoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class RemoveProductSrvTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CatalogoService catalogoService;

    private RemoveProductSrv removeProductSrv;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        removeProductSrv = new RemoveProductSrv(catalogoService); // Usa il costruttore con iniezione
    }

    @Test
    void testDoGet_RemoveProductSuccess() throws ServletException, IOException, SQLException {
        // Configura il parametro della richiesta
        when(request.getParameter("prodid")).thenReturn("123");

        // Configura il mock del servizio per restituire uno stato di successo
        when(catalogoService.rmFumetto("123")).thenReturn("Product removed successfully");

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("./ProductListServlet?message=Product removed successfully"))
                .thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        removeProductSrv.doGet(request, response);

        // Verifica che il metodo del servizio sia stato chiamato
        verify(catalogoService).rmFumetto("123");

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio corretto
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_RemoveProductFailure() throws ServletException, IOException, SQLException {
        // Configura il parametro della richiesta
        when(request.getParameter("prodid")).thenReturn("999");

        // Configura il mock del servizio per lanciare un'eccezione SQLException
        when(catalogoService.rmFumetto("999")).thenThrow(new SQLException("Database error"));

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("./ProductListServlet?message=Error removing product"))
                .thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        removeProductSrv.doGet(request, response);

        // Verifica che il metodo del servizio sia stato chiamato
        verify(catalogoService).rmFumetto("999");

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di errore
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_NoProductId() throws ServletException, IOException, SQLException {
        // Configura il parametro della richiesta come null
        when(request.getParameter("prodid")).thenReturn(null);

        // Configura il mock del RequestDispatcher per gestire il reindirizzamento
        when(request.getRequestDispatcher("./ProductListServlet?message=Error: Product ID is missing"))
                .thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        removeProductSrv.doGet(request, response);

        // Verifica che non ci siano interazioni con il CatalogoService
        verify(catalogoService, never()).rmFumetto(anyString());

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di errore corretto
        verify(requestDispatcher).forward(request, response);
    }
}