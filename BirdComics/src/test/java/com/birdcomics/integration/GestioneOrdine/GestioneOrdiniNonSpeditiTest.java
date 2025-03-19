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

import com.birdcomics.Model.Bean.OrderBean;
import com.birdcomics.GestioneOrdine.Controller.GestioneOrdiniNonSpediti;
import com.birdcomics.GestioneOrdine.Service.OrdineService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GestioneOrdiniNonSpeditiTest {

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private OrdineService ordineService;
    
    @Mock
    private RequestDispatcher requestDispatcher;
    
    @InjectMocks
    private GestioneOrdiniNonSpediti servlet;
    
    private List<OrderBean> mockOrders;

    @Before
    public void setUp() throws Exception {
        // Inietta manualmente il mock del servizio
        Field serviceField = GestioneOrdiniNonSpediti.class.getDeclaredField("ordineService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, ordineService);
        
        // Configura il mock del dispatcher
        when(request.getRequestDispatcher("/visualizzaOrdiniNonSpediti.jsp")).thenReturn(requestDispatcher);
        
        // Dati di test
        mockOrders = new ArrayList<>();
        mockOrders.add(new OrderBean());
        mockOrders.add(new OrderBean());
    }

    @Test
    public void testDoGet_WithOrders() throws Exception {
        // Configura il mock del servizio
        when(ordineService.getOrdiniNonSpediti()).thenReturn(mockOrders);
        
        // Esegui il servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(ordineService).getOrdiniNonSpediti();
        verify(request).setAttribute("products", mockOrders);
        verify(request).setAttribute("message", "");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_NoOrders() throws Exception {
        // Configura il mock per lista vuota
        when(ordineService.getOrdiniNonSpediti()).thenReturn(new ArrayList<>());
        
        // Esegui il servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(request).setAttribute("message", "No items found");
        verify(request).setAttribute("products", new ArrayList<>());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_SQLException() throws Exception {
        // Configura il mock per eccezione
        when(ordineService.getOrdiniNonSpediti()).thenThrow(new SQLException("DB error"));
        
        // Esegui il servlet
        servlet.doGet(request, response);
        
        // Verifiche
        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPost() throws ServletException, IOException, SQLException {
        // Configura comportamento base
        when(ordineService.getOrdiniNonSpediti()).thenReturn(mockOrders);
        
        // Esegui POST
        servlet.doPost(request, response);
        
        // Verifiche
        verify(ordineService).getOrdiniNonSpediti();
        verify(requestDispatcher).forward(request, response);
    }
}