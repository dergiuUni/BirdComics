package com.birdcomics.integration.GestioneOrdine;


import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.GestioneOrdine.Controller.ReviewPaymentServlet;
import com.birdcomics.GestioneOrdine.Service.OrdineService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReviewPaymentServletTest {

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private OrdineService ordineService;
    
    @Mock
    private RequestDispatcher requestDispatcher;
    
    @InjectMocks
    private ReviewPaymentServlet servlet;
    
    private final String testPaymentId = "PAY-123";
    private final String testPayerId = "PAYER-456";
    private final String testUsername = "testUser";

    @Before
    public void setUp() throws Exception {
        // Inietta manualmente il mock del servizio
        Field serviceField = ReviewPaymentServlet.class.getDeclaredField("ordineService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, ordineService);
        
        // Configura comportamento base
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("error2.jsp")).thenReturn(requestDispatcher);
    }

    @Test
    public void testSuccessfulPaymentProcessing() throws Exception {
        // Configura parametri
        when(request.getParameter("paymentId")).thenReturn(testPaymentId);
        when(request.getParameter("PayerID")).thenReturn(testPayerId);
        when(session.getAttribute("username")).thenReturn(testUsername);
        
        // Esegui servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(ordineService).processPaymentAndCreateOrder(testPaymentId, testPayerId, testUsername);
        verify(response).sendRedirect("index.jsp");
    }

    @Test
    public void testMissingPaymentId() throws Exception {
        // Configura parametri mancanti
        when(request.getParameter("paymentId")).thenReturn(null);
        when(request.getParameter("PayerID")).thenReturn(testPayerId);
        when(session.getAttribute("username")).thenReturn(testUsername);
        
        // Esegui servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(request).setAttribute("errorMessage", "Missing required parameters.");
        verify(requestDispatcher).forward(request, response);
        verify(ordineService, never()).processPaymentAndCreateOrder(any(), any(), any());
    }

    @Test
    public void testMissingPayerId() throws Exception {
        // Configura parametri mancanti
        when(request.getParameter("paymentId")).thenReturn(testPaymentId);
        when(request.getParameter("PayerID")).thenReturn(null);
        when(session.getAttribute("username")).thenReturn(testUsername);
        
        // Esegui servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(request).setAttribute("errorMessage", "Missing required parameters.");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testUserNotLoggedIn() throws Exception {
        // Configura utente non loggato
        when(request.getParameter("paymentId")).thenReturn(testPaymentId);
        when(request.getParameter("PayerID")).thenReturn(testPayerId);
        when(session.getAttribute("username")).thenReturn(null);
        
        // Esegui servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(request).setAttribute("errorMessage", "Missing required parameters.");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testSQLExceptionHandling() throws Exception {
        // Configura parametri validi
        when(request.getParameter("paymentId")).thenReturn(testPaymentId);
        when(request.getParameter("PayerID")).thenReturn(testPayerId);
        when(session.getAttribute("username")).thenReturn(testUsername);
        
        // Simula eccezione
        doThrow(new SQLException("Database error"))
            .when(ordineService).processPaymentAndCreateOrder(any(), any(), any());
        
        // Esegui servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(request).setAttribute("errorMessage", "Database error");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testEmptyParameters() throws Exception {
        // Configura tutti i parametri null
        when(request.getParameter("paymentId")).thenReturn(null);
        when(request.getParameter("PayerID")).thenReturn(null);
        when(session.getAttribute("username")).thenReturn(null);
        
        // Esegui servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(request).setAttribute("errorMessage", "Missing required parameters.");
        verify(requestDispatcher).forward(request, response);
    }
}