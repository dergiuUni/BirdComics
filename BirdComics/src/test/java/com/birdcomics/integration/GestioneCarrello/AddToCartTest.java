package com.birdcomics.integration.GestioneCarrello;

import com.birdcomics.GestioneCarrello.Controller.AddToCart;
import com.birdcomics.GestioneCarrello.Service.CarrelloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

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
    void testDoPost_SessionValid() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");

        // Configura i parametri della richiesta
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("2");

        // Esegui il metodo doPost
        addToCartServlet.doPost(request, response);

        // Verifica che il metodo addToCart sia stato chiamato correttamente
        verify(cartService).addToCart("testUser", "123", 2);

        // Verifica che la risposta sia stata reindirizzata correttamente
        verify(response).sendRedirect("CartDetailsServlet");
    }

    @Test
    void testDoPost_SessionExpired() throws ServletException, IOException {
        // Configura il mock della sessione (utente non loggato)
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn(null);

        // Esegui il metodo doPost
        addToCartServlet.doPost(request, response);

        // Verifica che la risposta sia stata reindirizzata alla pagina di login
        verify(response).sendRedirect("login.jsp?message=Session Expired, Login Again to Continue!");
    }

    @Test
    void testDoPost_SQLException() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");

        // Configura i parametri della richiesta
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("2");

        // Simula un'eccezione SQL
        doThrow(new SQLException("Database error")).when(cartService).addToCart("testUser", "123", 2);

        // Esegui il metodo doPost
        addToCartServlet.doPost(request, response);

        // Verifica che l'eccezione sia stata gestita e l'utente reindirizzato alla pagina di errore
        verify(response).sendRedirect("error.jsp");
    }

    @Test
    void testDoGet_CallsDoPost() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");

        // Configura i parametri della richiesta
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("2");

        // Esegui il metodo doGet
        addToCartServlet.doGet(request, response);

        // Verifica che doGet chiami doPost
        verify(cartService).addToCart("testUser", "123", 2);
        verify(response).sendRedirect("CartDetailsServlet");
    }
}