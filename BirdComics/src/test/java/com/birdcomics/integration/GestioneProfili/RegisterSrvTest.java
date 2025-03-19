package com.birdcomics.integration.GestioneProfili;

import com.birdcomics.Model.Bean.RuoloBean;
import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.Model.Bean.MagazzinoBean;
import com.birdcomics.Model.Bean.IndirizzoBean;
import com.birdcomics.Model.Bean.ScaffaliBean;
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
import java.util.ArrayList;
import java.util.List;

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
    void testRegisterCliente() throws ServletException, IOException, SQLException, ParseException {
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
        when(profileService.registraAccount(
                eq("mario.rossi@example.com"), eq("password123"), eq("Mario"), eq("Rossi"),
                eq("1234567890"), any(Date.class), eq("Roma"), eq("Via Roma"), eq("10"), eq("00100"),
                argThat(ruoli -> ruoli.contains(RuoloBean.Cliente)), // Verifica che il ruolo Cliente sia presente
                isNull() // Passa null per il magazzino
        )).thenReturn("Registration successful!");

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("register.jsp?message=Registration successful!")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        registerSrv.doGet(request, response);

        // Verifica che il metodo registerUser sia stato chiamato con i parametri corretti
        verify(profileService).registraAccount(
                eq("mario.rossi@example.com"), eq("password123"), eq("Mario"), eq("Rossi"),
                eq("1234567890"), any(Date.class), eq("Roma"), eq("Via Roma"), eq("10"), eq("00100"),
                argThat(ruoli -> ruoli.contains(RuoloBean.Cliente)),
                isNull() // Verifica che il magazzino sia null
        );

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di successo
        verify(requestDispatcher).forward(request, response);
    }
    @Test
    void testRegisterPasswordNotMatching() throws ServletException, IOException {
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
        when(request.getParameter("dataNascita")).thenReturn("1990-01-01");

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("register.jsp?message=Password not matching!")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        registerSrv.doGet(request, response);

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di errore
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testRegisterWithRoles() throws ServletException, IOException, SQLException, ParseException {
        // Configura i parametri della richiesta per un nuovo utente con magazzino
        when(request.getParameter("nome")).thenReturn("Anna");
        when(request.getParameter("cognome")).thenReturn("Bianchi");
        when(request.getParameter("email")).thenReturn("annabianchi@birdcomics.com");
        when(request.getParameter("telefono")).thenReturn("1234567890");
        when(request.getParameter("nomeCitta")).thenReturn("Milano");
        when(request.getParameter("via")).thenReturn("Via Milano");
        when(request.getParameter("numeroCivico")).thenReturn("20");
        when(request.getParameter("cap")).thenReturn("20100");
        when(request.getParameter("password")).thenReturn("securePass123");
        when(request.getParameter("confirmPassword")).thenReturn("securePass123");
        when(request.getParameter("dataNascita")).thenReturn("1985-05-15");
        
        // Simula una sessione attiva con un utente GestoreGenerale
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("email")).thenReturn("manager@example.com");
        
        UserBean manager = new UserBean();
        manager.addRuolo(RuoloBean.GestoreGenerale);
        MagazzinoBean magazzino = new MagazzinoBean();
        magazzino.setNome("Magazzino Milano");
        manager.setMagazzino(magazzino);

        when(profileService.getUserDetails("manager@example.com")).thenReturn(manager);
        
        // Configura il mock del servizio per restituire un messaggio di successo
        when(profileService.registraAccount(
                eq("annabianchi@birdcomics.com"), eq("securePass123"), eq("Anna"), eq("Bianchi"),
                eq("1234567890"), any(Date.class), eq("Milano"), eq("Via Milano"), eq("20"), eq("20100"),
                argThat(ruoli -> ruoli.contains(RuoloBean.GestoreMagazzino)),
                eq(magazzino)
        )).thenReturn("User Registered Successfully!");

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("register.jsp?message=User Registered Successfully!")).thenReturn(requestDispatcher);

        // Esegui il metodo doGet
        registerSrv.doGet(request, response);

        // Verifica che il metodo registerUser sia stato chiamato con i parametri corretti
        verify(profileService).registraAccount(
                eq("annabianchi@birdcomics.com"), eq("securePass123"), eq("Anna"), eq("Bianchi"),
                eq("1234567890"), any(Date.class), eq("Milano"), eq("Via Milano"), eq("20"), eq("20100"),
                argThat(ruoli -> ruoli.contains(RuoloBean.GestoreMagazzino)),
                eq(magazzino)
        );

        // Verifica che il RequestDispatcher sia stato chiamato con il messaggio di successo
        verify(requestDispatcher).forward(request, response);
    }
    //registrazione con account gia esistente

}