package com.birdcomics.integration.GestioneProfilo;

import com.birdcomics.GestioneProfilo.Controller.RemoveUserServlet;
import com.birdcomics.GestioneProfilo.Service.ProfileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class RemoveUserServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private RemoveUserServlet removeUserServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        removeUserServlet = new RemoveUserServlet(); // Inizializza la servlet
        removeUserServlet.profileService = profileService; // Inietta il mock del servizio
    }

    @Test
    void testDoGet_UserRemovedSuccessfully() throws ServletException, IOException, SQLException {
        // Configura il mock della richiesta per restituire un'email valida
        when(request.getParameter("userId")).thenReturn("test@example.com");

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/GestioneUtentiServlet")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        removeUserServlet.doGet(request, response);

        // Verifica che il metodo removeUser sia stato chiamato con l'email corretta
        verify(profileService).rimuoviAccount("test@example.com");

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_UserEmailIsNull() throws ServletException, IOException, SQLException {
        // Configura il mock della richiesta per restituire un'email nulla
        when(request.getParameter("userId")).thenReturn(null);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/GestioneUtentiServlet")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        removeUserServlet.doGet(request, response);

        // Verifica che il metodo removeUser non sia stato chiamato
        verify(profileService, never()).rimuoviAccount(anyString());

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_SQLExceptionThrown() throws ServletException, IOException, SQLException {
        // Configura il mock della richiesta per restituire un'email valida
        when(request.getParameter("userId")).thenReturn("test@example.com");

        // Configura il mock del servizio per lanciare un'eccezione SQLException
        doThrow(new SQLException("Database error")).when(profileService).rimuoviAccount("test@example.com");

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/GestioneUtentiServlet")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        removeUserServlet.doGet(request, response);

        // Verifica che l'eccezione sia stata gestita (ad esempio, loggata)
        // In questo caso, il test verifica che il metodo non lanci un'eccezione
        verify(request).setAttribute("status", "Errore di database: Database error");

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }
}