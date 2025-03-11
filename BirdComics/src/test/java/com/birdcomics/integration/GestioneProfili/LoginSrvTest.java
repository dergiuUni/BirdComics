package com.birdcomics.integration.GestioneProfili;

import com.birdcomics.Bean.RuoloBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.GestioneProfili.Controller.LoginSrv;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class LoginSrvTest {

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
    private LoginSrv loginSrv;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginSrv = new LoginSrv(); // Inizializza la servlet
        loginSrv.profileService = profileService; // Inietta il mock del servizio
    }

    @Test
    void testDoGet_ValidLogin_Cliente() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta per un cliente
        when(request.getParameter("email")).thenReturn("cliente@example.com");
        when(request.getParameter("password")).thenReturn("password123");

        // Configura il mock del servizio per restituire "valid" come stato di login
        when(profileService.login("cliente@example.com", "password123")).thenReturn("valid");

        // Configura il mock del servizio per restituire i dettagli dell'utente
        UserBean user = new UserBean();
        user.setEmail("cliente@example.com");
        when(profileService.getUserDetails("cliente@example.com")).thenReturn(user);

        // Configura il mock del servizio per restituire i ruoli dell'utente
        List<String> userTypes = new ArrayList<>();
        userTypes.add(RuoloBean.Cliente.toString()); // Ruolo Cliente
        when(profileService.getUserTypes("cliente@example.com")).thenReturn(userTypes);

        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/UserProfileServlet")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        loginSrv.doGet(request, response);

        // Verifica che la sessione sia stata configurata correttamente
        verify(session).setAttribute("userdata", user);
        verify(session).setAttribute("email", "cliente@example.com");
        verify(session).setAttribute("usertype", userTypes);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_ValidLogin_Admin() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta per un admin
        when(request.getParameter("email")).thenReturn("gestoregenerale@birdcomics.com");
        when(request.getParameter("password")).thenReturn("password123");

        // Configura il mock del servizio per restituire "valid" come stato di login
        when(profileService.login("gestoregenerale@birdcomics.com", "password123")).thenReturn("valid");

        // Configura il mock del servizio per restituire i dettagli dell'utente
        UserBean user = new UserBean();
        user.setEmail("gestoregenerale@birdcomics.com");
        when(profileService.getUserDetails("gestoregenerale@birdcomics.com")).thenReturn(user);

        // Configura il mock del servizio per restituire i ruoli dell'utente
        List<String> userTypes = new ArrayList<>();
        userTypes.add(RuoloBean.GestoreGenerale.toString()); // Ruolo GestoreGenerale
        when(profileService.getUserTypes("gestoregenerale@birdcomics.com")).thenReturn(userTypes);

        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/UserProfileServlet")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        loginSrv.doGet(request, response);

        // Verifica che la sessione sia stata configurata correttamente
        verify(session).setAttribute("userdata", user);
        verify(session).setAttribute("email", "gestoregenerale@birdcomics.com");
        verify(session).setAttribute("usertype", userTypes);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_InvalidLogin() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta per un login non valido
        when(request.getParameter("email")).thenReturn("cliente@example.com");
        when(request.getParameter("password")).thenReturn("wrongpassword");

        // Configura il mock del servizio per restituire "Login Denied! Invalid email or password." come stato di login
        when(profileService.login("cliente@example.com", "wrongpassword")).thenReturn("Login Denied! Invalid email or password.");

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("login.jsp?message=Login Denied! Invalid email or password.")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        loginSrv.doGet(request, response);

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di errore
        verify(requestDispatcher).include(request, response);
    }

    @Test
    void testDoGet_SQLException() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta
        when(request.getParameter("email")).thenReturn("cliente@example.com");
        when(request.getParameter("password")).thenReturn("password123");

        // Configura il mock del servizio per lanciare un'eccezione SQLException
        when(profileService.login("cliente@example.com", "password123")).thenThrow(new SQLException("Database error"));

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("login.jsp?message=Login Denied! Invalid email or password.")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        loginSrv.doGet(request, response);

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di errore
        verify(requestDispatcher).include(request, response);
    }

    @Test
    void testDoGet_MultipleRoles() throws ServletException, IOException, SQLException {
        // Configura i parametri della richiesta per un utente con più ruoli
        when(request.getParameter("email")).thenReturn("dipendente@birdcomics.com");
        when(request.getParameter("password")).thenReturn("password123");

        // Configura il mock del servizio per restituire "valid" come stato di login
        when(profileService.login("dipendente@birdcomics.com", "password123")).thenReturn("valid");

        // Configura il mock del servizio per restituire i dettagli dell'utente
        UserBean user = new UserBean();
        user.setEmail("dipendente@birdcomics.com");
        when(profileService.getUserDetails("dipendente@birdcomics.com")).thenReturn(user);

        // Configura il mock del servizio per restituire più ruoli
        List<String> userTypes = new ArrayList<>();
        userTypes.add(RuoloBean.Spedizioniere.toString());
        userTypes.add(RuoloBean.Magazziniere.toString());
        when(profileService.getUserTypes("dipendente@birdcomics.com")).thenReturn(userTypes);

        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/UserProfileServlet")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        loginSrv.doGet(request, response);

        // Verifica che la sessione sia stata configurata correttamente
        verify(session).setAttribute("userdata", user);
        verify(session).setAttribute("email", "dipendente@birdcomics.com");
        verify(session).setAttribute("usertype", userTypes);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }
}