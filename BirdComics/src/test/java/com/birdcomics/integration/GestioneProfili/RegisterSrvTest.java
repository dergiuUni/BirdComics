package com.birdcomics.integration.GestioneProfili;

import com.birdcomics.Bean.RuoloBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.GestioneProfili.Controller.RegisterSrv;
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
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import static org.mockito.Mockito.*;

class RegisterSrvTest {

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
    private RegisterSrv registerSrv;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registerSrv = new RegisterSrv(); // Inizializza la servlet
        registerSrv.profileService = profileService; // Inietta il mock del servizio
    }

    @Test
    void testDoGet_RegisterCliente() throws ServletException, IOException, SQLException, ParseException {
        // Configura i parametri della richiesta per un nuovo cliente
        when(request.getParameter("nome")).thenReturn("Mario");
        when(request.getParameter("cognome")).thenReturn("Rossi");
        when(request.getParameter("email")).thenReturn("mario.rossi@example.com");
        when(request.getParameter("telefono")).thenReturn("1234567890");
        when(request.getParameter("nomeCitta")).thenReturn("Roma");
        when(request.getParameter("via")).thenReturn("Via Roma");
        when(request.getParameter("numeroCivico")).thenReturn("10");
        when(request.getParameter("cap")).thenReturn("00100");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(request.getParameter("dataNascita")).thenReturn("1990-01-01");

        // Configura il mock della sessione per simulare l'assenza di una sessione attiva
        when(request.getSession(false)).thenReturn(null); // Nessuna sessione attiva

        // Configura il mock del servizio per restituire un messaggio di successo
        when(profileService.registerUser(
                eq("mario.rossi@example.com"), eq("password123"), eq("Mario"), eq("Rossi"),
                eq("1234567890"), any(Date.class), eq("Roma"), eq("Via Roma"), eq("10"), eq("00100"),
                argThat(ruoli -> ruoli.contains(RuoloBean.Cliente)) // Verifica che il ruolo Cliente sia presente
        )).thenReturn("Registration successful!");

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("register.jsp?message=Registration successful!")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        registerSrv.doGet(request, response);

        // Verifica che il metodo registerUser sia stato chiamato con i parametri corretti
        verify(profileService).registerUser(
                eq("mario.rossi@example.com"), eq("password123"), eq("Mario"), eq("Rossi"),
                eq("1234567890"), any(Date.class), eq("Roma"), eq("Via Roma"), eq("10"), eq("00100"),
                argThat(ruoli -> ruoli.contains(RuoloBean.Cliente))
        );

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di successo
        verify(requestDispatcher).forward(request, response);
    }
    @Test
    void testDoGet_PasswordNotMatching() throws ServletException, IOException {
        // Configura i parametri della richiesta con password non corrispondenti
        when(request.getParameter("nome")).thenReturn("Mario");
        when(request.getParameter("cognome")).thenReturn("Rossi");
        when(request.getParameter("email")).thenReturn("mario.rossi@example.com");
        when(request.getParameter("telefono")).thenReturn("1234567890");
        when(request.getParameter("nomeCitta")).thenReturn("Roma");
        when(request.getParameter("via")).thenReturn("Via Roma");
        when(request.getParameter("numeroCivico")).thenReturn("10");
        when(request.getParameter("cap")).thenReturn("00100");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("wrongpassword");
        when(request.getParameter("dataNascita")).thenReturn("1990-01-01"); // Aggiungi questo parametro

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("register.jsp?message=Password not matching!")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        registerSrv.doGet(request, response);

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di errore
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGet_RegisterGestoreMagazzino() throws ServletException, IOException, SQLException, ParseException {
        // Configura i parametri della richiesta per un nuovo GestoreMagazzino
        when(request.getParameter("nome")).thenReturn("Luigi");
        when(request.getParameter("cognome")).thenReturn("Verdi");
        when(request.getParameter("email")).thenReturn("luigi.verdi@example.com");
        when(request.getParameter("telefono")).thenReturn("0987654321");
        when(request.getParameter("nomeCitta")).thenReturn("Milano");
        when(request.getParameter("via")).thenReturn("Via Milano");
        when(request.getParameter("numeroCivico")).thenReturn("20");
        when(request.getParameter("cap")).thenReturn("20100");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(request.getParameter("dataNascita")).thenReturn("1985-05-05");

        // Configura il mock della sessione per simulare un GestoreGenerale attivo
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("gestoregenerale@birdcomics.com");

        // Configura il mock del servizio per restituire i dettagli dell'utente attivo
        UserBean gestoreGenerale = new UserBean();
        gestoreGenerale.setEmail("gestoregenerale@birdcomics.com");
        gestoreGenerale.addRuolo(RuoloBean.GestoreGenerale);
        when(profileService.getUserDetails("gestoregenerale@birdcomics.com")).thenReturn(gestoreGenerale);

        // Configura il mock del servizio per restituire un messaggio di successo
        when(profileService.registerUser(
                eq("luigi.verdi@example.com"), eq("password123"), eq("Luigi"), eq("Verdi"),
                eq("0987654321"), any(Date.class), eq("Milano"), eq("Via Milano"), eq("20"), eq("20100"),
                argThat(ruoli -> ruoli.contains(RuoloBean.GestoreMagazzino)) // Verifica che il ruolo GestoreMagazzino sia presente
        )).thenReturn("Registration successful!");

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("register.jsp?message=Registration successful!")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        registerSrv.doGet(request, response);

        // Verifica che il metodo registerUser sia stato chiamato con i parametri corretti
        verify(profileService).registerUser(
                eq("luigi.verdi@example.com"), eq("password123"), eq("Luigi"), eq("Verdi"),
                eq("0987654321"), any(Date.class), eq("Milano"), eq("Via Milano"), eq("20"), eq("20100"),
                argThat(ruoli -> ruoli.contains(RuoloBean.GestoreMagazzino))
        );

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di successo
        verify(requestDispatcher).forward(request, response);
    }
}