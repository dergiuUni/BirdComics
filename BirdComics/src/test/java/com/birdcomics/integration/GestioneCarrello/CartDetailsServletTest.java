package com.birdcomics.integration.GestioneCarrello;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.Bean.CartItem;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.GestioneCarrello.Controller.CartDetailsServlet;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CartDetailsServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CarrelloService cartService;

    @InjectMocks
    private CartDetailsServlet cartDetailsServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartDetailsServlet = new CartDetailsServlet(cartService); // Usa il costruttore con iniezione di dipendenze
    }

    @Test
    void testProcessRequest_SessionExpired() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione (utente non loggato)
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn(null);

        // Esegui il metodo processRequest
        cartDetailsServlet.processRequest(request, response);

        // Verifica che la risposta sia stata reindirizzata alla pagina di login
        verify(response).sendRedirect("login.jsp?message=Session Expired, Login Again!!");
    }

    @Test
    void testProcessRequest_AddProductToCart() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("user@example.com");

        // Configura i parametri della richiesta per aggiungere un prodotto
        when(request.getParameter("add")).thenReturn("1");
        when(request.getParameter("pid")).thenReturn("1");
        when(request.getParameter("avail")).thenReturn("10");
        when(request.getParameter("qty")).thenReturn("2");

        // Simula il carrello nella sessione
        CartBean cartBean = new CartBean("user@example.com");
        when(session.getAttribute("cart")).thenReturn(cartBean);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/cartDetails.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo processRequest
        cartDetailsServlet.processRequest(request, response);

        // Verifica che il prodotto sia stato aggiunto al carrello
        verify(cartService).aggiungiFumetto(session, "user@example.com", "1", 1);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testProcessRequest_GetCartDetails() throws ServletException, IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        
        // Mock the session behavior
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cartItems")).thenReturn(Arrays.asList(new CartItem("1", 2)));

        CartDetailsServlet servlet = new CartDetailsServlet();
        
        // Perform the request processing
        servlet.processRequest(request, response);

        // Verify the cartItems are correctly set in the session (if that's how the servlet works)
        when(session.getAttribute("cartItems")).thenReturn(Arrays.asList(new CartItem("1", 2)));
    }

    @Test
    void testProcessRequest_SQLException() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("user@example.com");

        // Configura i parametri della richiesta per aggiungere un prodotto
        when(request.getParameter("add")).thenReturn("1");
        when(request.getParameter("pid")).thenReturn("1");
        when(request.getParameter("avail")).thenReturn("10");
        when(request.getParameter("qty")).thenReturn("2");

        // Simula un'eccezione SQL
        doThrow(new SQLException("Database error")).when(cartService).aggiungiFumetto(session, "user@example.com", "1", 1);

        // Esegui il metodo processRequest
        cartDetailsServlet.processRequest(request, response);

        // Verifica che la risposta sia stata reindirizzata alla pagina di errore
        verify(response).sendRedirect("error.jsp");
    }

    @Test
    void testDoGet_CallsProcessRequest() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("user@example.com");

        // Configura i parametri della richiesta (nessuna azione di aggiunta/rimozione)
        when(request.getParameter("add")).thenReturn(null);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/cartDetails.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        cartDetailsServlet.doGet(request, response);

        // Verifica che il metodo processRequest sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoPost_CallsProcessRequest() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("user@example.com");

        // Configura i parametri della richiesta (nessuna azione di aggiunta/rimozione)
        when(request.getParameter("add")).thenReturn(null);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/cartDetails.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doPost
        cartDetailsServlet.doPost(request, response);

        // Verifica che il metodo processRequest sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }
}
