package com.birdcomics.integration.GestioneOrdine;

import com.birdcomics.Bean.OrderBean;
import com.birdcomics.GestioneOrdine.Controller.OrderDetailsServlet;
import com.birdcomics.GestioneOrdine.Service.OrdineService;
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

class OrderDetailsServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private OrdineService ordineService;

    @InjectMocks
    private OrderDetailsServlet orderDetailsServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderDetailsServlet = new OrderDetailsServlet(ordineService); // Usa il costruttore con iniezione
    }

    @Test
    void testDoGet_UserLoggedIn() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione per simulare un utente loggato
        String email = "test@example.com";
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn(email);

        // Configura il mock del servizio per restituire una lista di ordini
        List<OrderBean> orders = new ArrayList<>();
        orders.add(new OrderBean());
        when(ordineService.getOrdiniPerUtente(email)).thenReturn(orders);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/orderDetails.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        orderDetailsServlet.doGet(request, response);

        // Verifica che gli ordini siano stati impostati come attributo della richiesta
        verify(request).setAttribute("orders", orders);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_UserNotLoggedIn() throws ServletException, IOException {
        // Configura il mock della sessione per simulare un utente non loggato
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn(null);

        // Esegui il metodo doGet
        orderDetailsServlet.doGet(request, response);

        // Verifica che il reindirizzamento alla pagina di login sia stato eseguito
        verify(response).sendRedirect("login.jsp");
    }

    @Test
    void testDoGet_SQLException() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione per simulare un utente loggato
        String email = "test@example.com";
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn(email);

        // Configura il mock del servizio per lanciare un'eccezione SQLException
        when(ordineService.getOrdiniPerUtente(email)).thenThrow(new SQLException("Database error"));

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/orderDetails.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        orderDetailsServlet.doGet(request, response);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoPost() throws ServletException, IOException {
        // Crea uno spy dell'istanza reale di OrderDetailsServlet
        OrderDetailsServlet spyServlet = spy(orderDetailsServlet);

        // Configura il mock della richiesta e della risposta
        when(request.getSession()).thenReturn(session);

        // Esegui il metodo doPost
        spyServlet.doPost(request, response);

        // Verifica che il metodo doGet sia stato chiamato
        verify(spyServlet).doGet(request, response);
    }
}