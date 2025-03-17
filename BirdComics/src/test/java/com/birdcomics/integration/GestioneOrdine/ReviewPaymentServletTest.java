package com.birdcomics.integration.GestioneOrdine;

import com.birdcomics.GestioneOrdine.Controller.ReviewPaymentServlet;
import com.birdcomics.GestioneOrdine.Service.OrdineService;
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

public class ReviewPaymentServletTest {

    @Mock
    private OrdineService ordineService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    private ReviewPaymentServlet reviewPaymentServlet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inizializza il servlet con il mock di OrdineService
        reviewPaymentServlet = new ReviewPaymentServlet(ordineService);

        // Configura il comportamento del mock per la sessione
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testCreateOrderSuccess() throws Exception {
        // Arrange
        String paymentId = "PAYMENT123";
        String payerId = "PAYER123";
        String email = "user@example.com";

        when(request.getParameter("paymentId")).thenReturn(paymentId);
        when(request.getParameter("PayerID")).thenReturn(payerId);
        when(session.getAttribute("email")).thenReturn(email);

        // Act
        reviewPaymentServlet.doGet(request, response);

        // Assert
        // Verifica che il metodo creaOrdine sia stato chiamato con i parametri corretti
        verify(ordineService, times(1)).creaOrdine(paymentId, payerId, email, session);

        // Verifica che il servlet faccia il redirect alla home page
        verify(response, times(1)).sendRedirect("./OrderDetailsServlet");
    }

    @Test
    public void testParametersAreMissing_thenForwardToErrorPage() throws Exception {
        // Arrange
        when(request.getParameter("paymentId")).thenReturn(null); // Manca il parametro paymentId

        when(request.getRequestDispatcher("error2.jsp")).thenReturn(requestDispatcher);

        // Act
        reviewPaymentServlet.doGet(request, response);

        // Assert
        // Verifica che il messaggio di errore sia impostato
        verify(request, times(1)).setAttribute("errorMessage", "Missing required parameters.");

        // Verifica che il servlet invii alla pagina di errore
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    public void testSQLExceptionOccurs_thenForwardToErrorPage() throws Exception {
        // Arrange
        String paymentId = "PAYMENT123";
        String payerId = "PAYER123";
        String email = "user@example.com";

        when(request.getParameter("paymentId")).thenReturn(paymentId);
        when(request.getParameter("PayerID")).thenReturn(payerId);
        when(session.getAttribute("email")).thenReturn(email);

        // Simula un'eccezione SQL
        doThrow(new SQLException("Database error")).when(ordineService).creaOrdine(paymentId, payerId, email, session);

        when(request.getRequestDispatcher("error2.jsp")).thenReturn(requestDispatcher);

        // Act
        reviewPaymentServlet.doGet(request, response);

        // Assert
        // Verifica che il messaggio di errore sia impostato
        verify(request, times(1)).setAttribute("errorMessage", "Database error");

        // Verifica che il servlet invii alla pagina di errore
        verify(requestDispatcher, times(1)).forward(request, response);
    }
}
