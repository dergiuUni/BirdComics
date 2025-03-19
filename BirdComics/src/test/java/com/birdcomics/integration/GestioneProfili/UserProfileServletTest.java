package com.birdcomics.integration.GestioneProfili;

import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.GestioneProfili.Controller.UserProfileServlet;
import com.birdcomics.GestioneProfili.Service.ProfileService;
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

import static org.mockito.Mockito.*;

class UserProfileServletTest {

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
    private UserProfileServlet userProfileServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userProfileServlet = new UserProfileServlet(); // Inizializza la servlet
        userProfileServlet.profileService = profileService; // Inietta il mock del servizio
    }

    @Test
    void testDoGet_UserLoggedIn() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("testUser");

        // Configura il mock del servizio per restituire un utente
        UserBean user = new UserBean();
        user.setEmail("test@example.com");
        when(profileService.getUserDetails("testUser")).thenReturn(user);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/userProfile.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        userProfileServlet.doGet(request, response);

        // Verifica che l'attributo "user" sia stato impostato correttamente
        verify(request).setAttribute("user", user);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_UserNotLoggedIn() throws ServletException, IOException {
        // Configura il mock della sessione (utente non loggato)
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn(null);

        // Esegui il metodo doGet
        userProfileServlet.doGet(request, response);

        // Verifica che la risposta sia stata reindirizzata alla pagina di login
        verify(response).sendRedirect("login.jsp?message=Session Expired, Login Again!!");
    }

    @Test
    void testDoGet_SQLException() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("testUser");

        // Configura il mock del servizio per lanciare un'eccezione SQLException
        when(profileService.getUserDetails("testUser")).thenThrow(new SQLException("Database error"));

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/userProfile.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        userProfileServlet.doGet(request, response);

        // Verifica che l'eccezione sia stata gestita (ad esempio, loggata)
        // In questo caso, il test verifica che il metodo non lanci un'eccezione
        verify(requestDispatcher).forward(request, response);
    }
}