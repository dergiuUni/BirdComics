package com.birdcomics.integration.GestioneMagazzino;


import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Bean.MagazzinoBean;
import com.birdcomics.GestioneMagazzino.Controller.ListaMagazziniServlet;
import com.birdcomics.GestioneMagazzino.Service.MagazzinoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ListaMagazziniServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private MagazzinoService magazzinoService;

    @Mock
    private RequestDispatcher requestDispatcher;

    private ListaMagazziniServlet servlet;

    @Before
    public void setUp() throws Exception {
        servlet = new ListaMagazziniServlet();
        
        // Inject mock service using reflection
        Field serviceField = ListaMagazziniServlet.class.getDeclaredField("magazzinoService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, magazzinoService);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGetWithResults() throws ServletException, IOException, SQLException {
        // Setup mock data
        List<MagazzinoBean> mockList = Arrays.asList(
            new MagazzinoBean(),
            new MagazzinoBean()
        );

        when(magazzinoService.getAllMagazzini()).thenReturn(mockList);

        // Execute servlet
        servlet.doGet(request, response);

        // Verify behavior
        verify(request).setAttribute("listaMagazzini", mockList);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetWithEmptyList() throws ServletException, IOException, SQLException {
        // Setup empty list
        when(magazzinoService.getAllMagazzini()).thenReturn(new ArrayList<>());

        // Execute servlet
        servlet.doGet(request, response);

        // Verify behavior
        verify(request).setAttribute("listaMagazzini", new ArrayList<>());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetWithSQLException() throws ServletException, IOException, SQLException {
        // Setup exception
        when(magazzinoService.getAllMagazzini()).thenThrow(new SQLException("Database error"));

        // Execute servlet
        servlet.doGet(request, response);

        // Verify error handling
        verify(request).setAttribute("message", "Errore nel recupero dei magazzini.");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        // Execute post
        servlet.doPost(request, response);
        
        // Verify forward happens
        verify(requestDispatcher).forward(request, response);
    }
}