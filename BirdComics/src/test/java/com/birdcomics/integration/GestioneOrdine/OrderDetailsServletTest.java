package com.birdcomics.integration.GestioneOrdine;


import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.Bean.OrderBean;
import com.birdcomics.GestioneOrdine.Controller.OrderDetailsServlet;
import com.birdcomics.GestioneOrdine.Service.OrdineService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderDetailsServletTest {

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
    private OrderDetailsServlet servlet;
    
    private final String testUsername = "testUser";
    private final List<OrderBean> mockOrders = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        // Inietta il mock del servizio usando reflection
        Field serviceField = OrderDetailsServlet.class.getDeclaredField("ordineService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, ordineService);
        
        // Configura comportamento mock
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/orderDetails.jsp")).thenReturn(requestDispatcher);
        
        // Dati di test
        mockOrders.add(new OrderBean());
        mockOrders.add(new OrderBean());
    }

    @Test
    public void testDoGet_UserLoggedIn() throws Exception {
        // Configura mock
        when(session.getAttribute("username")).thenReturn(testUsername);
        when(ordineService.getOrdiniPerUtente(testUsername)).thenReturn(mockOrders);
        
        // Esegui servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(ordineService).getOrdiniPerUtente(testUsername);
        verify(request).setAttribute("orders", mockOrders);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_UserNotLoggedIn() throws Exception {
        // Configura mock
        when(session.getAttribute("username")).thenReturn(null);
        
        // Esegui servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(response).sendRedirect("login.jsp");
        verify(ordineService, never()).getOrdiniPerUtente(anyString());
    }

    @Test
    public void testDoGet_SQLError() throws Exception {
        // Configura mock
        when(session.getAttribute("username")).thenReturn(testUsername);
        when(ordineService.getOrdiniPerUtente(testUsername)).thenThrow(new SQLException("DB error"));
        
        // Esegui servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoGet_EmptyOrders() throws Exception {
        // Configura mock
        when(session.getAttribute("username")).thenReturn(testUsername);
        when(ordineService.getOrdiniPerUtente(testUsername)).thenReturn(new ArrayList<>());
        
        // Esegui servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(request).setAttribute(eq("orders"), anyList());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException, SQLException {
        // Configura mock
        when(session.getAttribute("username")).thenReturn(testUsername);
        
        // Esegui servlet
        servlet.doPost(request, response);
        
        // Verifiche
        verify(ordineService).getOrdiniPerUtente(testUsername);
    }
}