package com.birdcomics.integration.GestioneCarrello;

import com.birdcomics.GestioneCarrello.Controller.EmptyCartServlet;
import com.birdcomics.GestioneCarrello.Service.CarelloServiceImpl;
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

class EmptyCartServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private CarelloServiceImpl cartService;

    @InjectMocks
    private EmptyCartServlet emptyCartServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emptyCartServlet = new EmptyCartServlet();
    }

    @Test
    void testDoGet_SessionValid() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");

        // Simula il comportamento del CarelloServiceImpl
        try (MockedConstruction<CarelloServiceImpl> mocked = mockConstruction(CarelloServiceImpl.class, (mock, context) -> {
            doNothing().when(mock).emptyCart("testUser"); // Simula lo svuotamento del carrello
        })) {

            // Esegui il metodo doGet
            emptyCartServlet.doGet(request, response);

            // Verifica che il carrello sia stato svuotato
            verify(mocked.constructed().get(0)).emptyCart("testUser");

            // Verifica che la risposta sia stata reindirizzata correttamente
            verify(response).sendRedirect("CartDetailsServlet");
        }
    }

    @Test
    void testDoGet_SessionExpired() throws ServletException, IOException {
        // Configura il mock della sessione (utente non loggato)
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn(null);

        // Esegui il metodo doGet
        emptyCartServlet.doGet(request, response);

        // Verifica che la risposta sia stata reindirizzata alla pagina di login
        verify(response).sendRedirect("login.jsp?message=Session Expired, Login Again!!");
    }

    @Test
    void testDoGet_SQLException() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");

        // Simula un'eccezione SQL durante lo svuotamento del carrello
        try (MockedConstruction<CarelloServiceImpl> mocked = mockConstruction(CarelloServiceImpl.class, (mock, context) -> {
            doThrow(new SQLException("Database error")).when(mock).emptyCart("testUser");
        })) {

            // Esegui il metodo doGet
            emptyCartServlet.doGet(request, response);

            // Verifica che l'eccezione sia stata gestita e l'utente reindirizzato alla pagina di errore
            verify(response).sendRedirect("error.jsp?message=Errore durante lo svuotamento del carrello");
        }
    }

    @Test
    void testDoPost_CallsDoGet() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");

        // Simula il comportamento del CarelloServiceImpl
        try (MockedConstruction<CarelloServiceImpl> mocked = mockConstruction(CarelloServiceImpl.class, (mock, context) -> {
            doNothing().when(mock).emptyCart("testUser"); // Simula lo svuotamento del carrello
        })) {

            // Esegui il metodo doPost
            emptyCartServlet.doPost(request, response);

            // Verifica che doPost chiami doGet
            verify(mocked.constructed().get(0)).emptyCart("testUser");
            verify(response).sendRedirect("CartDetailsServlet");
        }
    }
}