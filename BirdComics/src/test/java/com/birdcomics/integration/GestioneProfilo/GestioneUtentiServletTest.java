package com.birdcomics.integration.GestioneProfilo;

import com.birdcomics.Model.Bean.MagazzinoBean;
import com.birdcomics.Model.Bean.RuoloBean;
import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.GestioneProfilo.Controller.GestioneUtentiServlet;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class GestioneUtentiServletTest {

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
    private GestioneUtentiServlet gestioneUtentiServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gestioneUtentiServlet = new GestioneUtentiServlet(); // Inizializza la servlet
        gestioneUtentiServlet.profileService = profileService; // Inietta il mock del servizio
    }

    @Test
    void testDoGet_GestoreGenerale() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("gestoreGenerale@example.com");

        // Configura il mock per restituire i ruoli dell'utente
        List<String> userTypes = new ArrayList<>();
        userTypes.add(RuoloBean.GestoreGenerale.toString());
        when(session.getAttribute("usertype")).thenReturn(userTypes);

        // Configura il mock del servizio per restituire un utente
        UserBean user = new UserBean();
        user.setEmail("gestoreGenerale@example.com");
        when(profileService.getUserDetails("gestoreGenerale@example.com")).thenReturn(user);

        // Configura il mock del servizio per restituire una lista di utenti
        List<UserBean> utenti = new ArrayList<>();
        when(profileService.getUsersByRole(anyList(), isNull())).thenReturn(utenti);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("manageUsers.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        gestioneUtentiServlet.doGet(request, response);

        // Verifica che l'attributo "utenti" sia stato impostato correttamente
        verify(request).setAttribute("utenti", utenti);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_GestoreMagazzino() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("gestoreMagazzino@example.com");

        // Configura il mock per restituire i ruoli dell'utente
        List<String> userTypes = new ArrayList<>();
        userTypes.add(RuoloBean.GestoreMagazzino.toString());
        when(session.getAttribute("usertype")).thenReturn(userTypes);

        // Configura il mock del servizio per restituire un utente con un magazzino
        UserBean user = new UserBean();
        user.setEmail("gestoreMagazzino@example.com");
        MagazzinoBean magazzino = new MagazzinoBean();
        magazzino.setNome("Magazzino1");
        user.setMagazzino(magazzino);
        when(profileService.getUserDetails("gestoreMagazzino@example.com")).thenReturn(user);

        // Configura il mock del servizio per restituire una lista di utenti
        List<UserBean> utenti = new ArrayList<>();
        when(profileService.getUsersByRole(anyList(), eq("Magazzino1"))).thenReturn(utenti);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("manageUsers.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        gestioneUtentiServlet.doGet(request, response);

        // Verifica che l'attributo "utenti" sia stato impostato correttamente
        verify(request).setAttribute("utenti", utenti);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_RisorseUmane() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("risorseUmane@example.com");

        // Configura il mock per restituire i ruoli dell'utente
        List<String> userTypes = new ArrayList<>();
        userTypes.add(RuoloBean.RisorseUmane.toString());
        when(session.getAttribute("usertype")).thenReturn(userTypes);

        // Configura il mock del servizio per restituire un utente con un magazzino
        UserBean user = new UserBean();
        user.setEmail("risorseUmane@example.com");
        MagazzinoBean magazzino = new MagazzinoBean();
        magazzino.setNome("Magazzino1");
        user.setMagazzino(magazzino);
        when(profileService.getUserDetails("risorseUmane@example.com")).thenReturn(user);

        // Configura il mock del servizio per restituire una lista di utenti
        List<UserBean> utenti = new ArrayList<>();
        when(profileService.getUsersByRole(anyList(), eq("Magazzino1"))).thenReturn(utenti);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("manageUsers.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        gestioneUtentiServlet.doGet(request, response);

        // Verifica che l'attributo "utenti" sia stato impostato correttamente
        verify(request).setAttribute("utenti", utenti);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_SQLException() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("test@example.com");

        // Configura il mock per restituire i ruoli dell'utente
        List<String> userTypes = new ArrayList<>();
        userTypes.add(RuoloBean.GestoreGenerale.toString());
        when(session.getAttribute("usertype")).thenReturn(userTypes);

        // Configura il mock del servizio per lanciare un'eccezione SQLException
        when(profileService.getUserDetails("test@example.com")).thenThrow(new SQLException("Database error"));

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("manageUsers.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        gestioneUtentiServlet.doGet(request, response);

        // Verifica che l'eccezione sia stata gestita (ad esempio, loggata)
        verify(request).setAttribute("message", "Errore nel recupero degli utenti.");

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }
}