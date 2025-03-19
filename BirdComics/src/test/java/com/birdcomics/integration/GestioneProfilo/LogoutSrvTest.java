package com.birdcomics.integration.GestioneProfilo;

import com.birdcomics.GestioneProfilo.Controller.LogoutSrv;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

class LogoutSrvTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private LogoutSrv logoutSrv;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logoutSrv = new LogoutSrv(); // Inizializza la servlet
        logoutSrv.profileService = profileService; // Inietta il mock del servizio
    }

    @Test
    void testDoGet_LogoutSuccess() throws ServletException, IOException {
        // Configura il mock della sessione
        when(request.getSession(false)).thenReturn(session);
        
        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("login.jsp?message=Successfully Logged Out!")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        logoutSrv.doGet(request, response);

        // Verifica che la sessione sia stata invalidata
        verify(session).invalidate();
        
        // Verifica che il metodo logout del servizio sia stato chiamato
        verify(profileService).logout(request);

        // Verifica il reindirizzamento con il messaggio di successo
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_NoSession() throws ServletException, IOException {
        // Simula l'assenza di una sessione
        when(request.getSession(false)).thenReturn(null);
        
        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("login.jsp?message=You were not logged in!")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        logoutSrv.doGet(request, response);

        // Verifica che la sessione non venga invalidata (perché è già null)
        verify(session, never()).invalidate();
        
        // Verifica che il metodo logout del servizio sia stato comunque chiamato
        verify(profileService).logout(request);

        // Verifica il reindirizzamento con il messaggio corretto
        verify(requestDispatcher).forward(request, response);
    }


}