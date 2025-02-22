package com.birdcomics.integration.GestioneMagazzino;


import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.Bean.ScaffaliBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.GestioneMagazzino.Controller.GestioneMagazziniere;
import com.birdcomics.GestioneMagazzino.Service.MagazzinoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
@RunWith(MockitoJUnitRunner.class)
public class GestioneMagazziniereTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private MagazzinoService magazzinoService;

    @Mock
    private RequestDispatcher requestDispatcher;

    @InjectMocks
    private GestioneMagazziniere servlet;

    private final String testUsername = "test@example.com";

    @Before
    public void setUp() throws Exception {
        servlet = new GestioneMagazziniere();
        servlet.magazzinoService = magazzinoService; // Manually inject mock service

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn(testUsername);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGetWithResults() throws ServletException, IOException, SQLException {
        // Setup mock data
        List<ScaffaliBean> mockScaffali = new ArrayList<>();
        mockScaffali.add(new ScaffaliBean());
        mockScaffali.add(new ScaffaliBean());

        // Mock the service to return the mockScaffali list for any UserBean
        when(magazzinoService.getScaffaleMagazzino(any(UserBean.class))).thenReturn(mockScaffali);

        // Execute servlet
        servlet.doGet(request, response);

        // Verify behavior
        verify(request).setAttribute("message", "Showing Results ");
        verify(request).setAttribute("listaScaffali", mockScaffali);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetWithoutResults() throws ServletException, IOException, SQLException {
        // Setup empty results
        List<ScaffaliBean> emptyScaffali = new ArrayList<>();

        // Mock the service to return an empty list for any UserBean
        when(magazzinoService.getScaffaleMagazzino(any(UserBean.class))).thenReturn(emptyScaffali);

        // Execute servlet
        servlet.doGet(request, response);

        // Verify behavior
        verify(request).setAttribute("message", "No items found for the search ");
        verify(request).setAttribute("listaScaffali", emptyScaffali);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetWithSQLException() throws ServletException, IOException, SQLException {
        // Mock the service to throw an SQLException for any UserBean
        when(magazzinoService.getScaffaleMagazzino(any(UserBean.class))).thenThrow(new SQLException("DB error"));

        // Execute servlet
        servlet.doGet(request, response);

        // Verify forward still happens even on error
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        servlet.doPost(request, response);
        verify(requestDispatcher).forward(request, response);
    }
}