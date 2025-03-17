package com.birdcomics.integration.GestioneCarrello;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.GestioneCarrello.Controller.AddToCart;
import com.birdcomics.GestioneCarrello.Service.CarrelloService;
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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AddToCartTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private CarrelloService cartService;

    @InjectMocks
    private AddToCart addToCartServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addToCartServlet = new AddToCart(cartService); // Usa il costruttore con iniezione di dipendenze
    }

    @Test
    void testDAggiuntaFumettoCarrelloSuccess() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("testuser@example.com");

        // Configura i parametri della richiesta
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("2");

        // Esegui il metodo doPost
        addToCartServlet.doPost(request, response);

        // Verifica che il metodo addToCart sia stato chiamato correttamente
        verify(cartService).aggiungiFumetto(session, "testuser@example.com", "123", 2);

        // Verifica che la risposta sia stata reindirizzata correttamente
        verify(response).sendRedirect("CartDetailsServlet");
    }

    @Test
    void testAggiuntaFumettoCarrello_SQLException() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("testuser@example.com");

        // Configura i parametri della richiesta
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("2");

        // Simula un'eccezione SQL
        doThrow(new SQLException("Database error")).when(cartService).aggiungiFumetto(session, "testuser@example.com", "123", 2);

        // Esegui il metodo doPost
        addToCartServlet.doPost(request, response);

        // Verifica che l'eccezione sia stata gestita e l'utente reindirizzato alla pagina di errore
        verify(response).sendRedirect("error.jsp?message=Error adding product to cart.");
    }
}