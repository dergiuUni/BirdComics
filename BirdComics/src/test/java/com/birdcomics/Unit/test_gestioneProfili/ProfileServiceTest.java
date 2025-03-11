package com.birdcomics.Unit.test_gestioneProfili;

import com.birdcomics.Bean.RuoloBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.Dao.UserServiceDAO;
import com.birdcomics.GestioneProfili.Service.ProfileServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    @Mock
    private UserServiceDAO userDao; // Mock del DAO

    @InjectMocks
    private ProfileServiceImpl profileService; // Inietta il mock nel service

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inizializza i mock
    }

    // Test di successo
    @Test
    void testGetUserDetails_Success() throws SQLException {
        // Configura il mock
        String email = "test@example.com";
        UserBean expectedUser = new UserBean();
        expectedUser.setEmail(email);
        when(userDao.getUserDetails(email)).thenReturn(expectedUser);

        // Esegui il metodo da testare
        UserBean result = profileService.getUserDetails(email);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(email, result.getEmail());

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).getUserDetails(email);
    }

    // Test di fallimento
    @Test
    void testGetUserDetails_Fail() throws SQLException {
        // Configura il mock per simulare un errore
        String email = "test@example.com";
        when(userDao.getUserDetails(email)).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare e verifica che sollevi un'eccezione
        assertThrows(SQLException.class, () -> profileService.getUserDetails(email));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).getUserDetails(email);
    }

    // Test di successo
    @Test
    void testGetUsersByRole_Success() throws SQLException {
        // Configura il mock
        List<RuoloBean> roles = new ArrayList<>();
        roles.add(RuoloBean.GestoreGenerale); // Usa i valori dell'enum
        roles.add(RuoloBean.Cliente);
        String magazzino = "Warehouse1";
        List<UserBean> expectedUsers = new ArrayList<>();
        expectedUsers.add(new UserBean());
        when(userDao.getUsersByRole(roles, magazzino)).thenReturn(expectedUsers);

        // Esegui il metodo da testare
        List<UserBean> result = profileService.getUsersByRole(roles, magazzino);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.size());

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).getUsersByRole(roles, magazzino);
    }

    // Test di fallimento
    @Test
    void testGetUsersByRole_Fail() throws SQLException {
        // Configura il mock per simulare un errore
        List<RuoloBean> roles = new ArrayList<>();
        roles.add(RuoloBean.GestoreGenerale);
        String magazzino = "Warehouse1";
        when(userDao.getUsersByRole(roles, magazzino)).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare e verifica che sollevi un'eccezione
        assertThrows(SQLException.class, () -> profileService.getUsersByRole(roles, magazzino));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).getUsersByRole(roles, magazzino);
    }

    // Test di successo
    @Test
    void testRemoveUser_Success() throws SQLException {
        // Configura il mock
        String userEmail = "test@example.com";
        doNothing().when(userDao).deleteUser(userEmail);

        // Esegui il metodo da testare
        profileService.rimuoviAccount(userEmail);

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).deleteUser(userEmail);
    }

    // Test di fallimento
    @Test
    void testRemoveUser_Fail() throws SQLException {
        // Configura il mock per simulare un errore
        String userEmail = "test@example.com";
        doThrow(new SQLException("Database error")).when(userDao).deleteUser(userEmail);

        // Esegui il metodo da testare e verifica che sollevi un'eccezione
        assertThrows(SQLException.class, () -> profileService.rimuoviAccount(userEmail));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).deleteUser(userEmail);
    }

    // Test di successo
    @Test
    void testValidateCredentials_Success() throws SQLException {
        // Configura il mock
        String email = "test@example.com";
        String password = "password123";
        String expectedResult = "SUCCESS";
        when(userDao.isValidCredential(email, password)).thenReturn(expectedResult);

        // Esegui il metodo da testare
        String result = profileService.login(email, password);

        // Verifica il risultato
        assertEquals(expectedResult, result);

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).isValidCredential(email, password);
    }

    // Test di fallimento
    @Test
    void testValidateCredentials_Fail() throws SQLException {
        // Configura il mock per simulare un errore
        String email = "test@example.com";
        String password = "password123";
        when(userDao.isValidCredential(email, password)).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare e verifica che sollevi un'eccezione
        assertThrows(SQLException.class, () -> profileService.login(email, password));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).isValidCredential(email, password);
    }

    // Test di successo
    @Test
    void testGetUserTypes_Success() throws SQLException {
        // Configura il mock
        String email = "test@example.com";
        List<RuoloBean> roles = new ArrayList<>();
        roles.add(RuoloBean.GestoreGenerale); // Usa i valori dell'enum
        roles.add(RuoloBean.Cliente);
        when(userDao.getUserType(email)).thenReturn(roles);

        // Esegui il metodo da testare
        List<String> result = profileService.getUserTypes(email);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("GestoreGenerale")); // Usa il valore di toString()
        assertTrue(result.contains("Cliente"));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).getUserType(email);
    }

    // Test di fallimento
    @Test
    void testGetUserTypes_Fail() throws SQLException {
        // Configura il mock per simulare un errore
        String email = "test@example.com";
        when(userDao.getUserType(email)).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare e verifica che sollevi un'eccezione
        assertThrows(SQLException.class, () -> profileService.getUserTypes(email));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).getUserType(email);
    }

    // Test di successo
    @Test
    void testLogout_Success() {
        // Configura il mock
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        doNothing().when(session).invalidate();

        // Esegui il metodo da testare
        profileService.logout(request);

        // Verifica che la sessione sia stata invalidata
        verify(session, times(1)).invalidate();
    }

    // Test di fallimento (non applicabile, poiché il metodo non lancia eccezioni)
    @Test
    void testLogout_Fail() {
        // Non ci sono casi di fallimento per il metodo logout, poiché non lancia eccezioni
    }

    // Test di successo
    @Test
    void testRegisterUser_Success() throws SQLException {
        // Configura il mock
        String email = "test@example.com";
        String password = "password123";
        String nome = "John";
        String cognome = "Doe";
        String telefono = "1234567890";
        java.sql.Date dataNascita = java.sql.Date.valueOf("1990-01-01");
        String citta = "New York";
        String via = "Main St";
        String numeroCivico = "123";
        String cap = "12345";
        ArrayList<RuoloBean> ruoli = new ArrayList<>();
        ruoli.add(RuoloBean.Cliente); // Usa i valori dell'enum
        String expectedResult = "SUCCESS";
        when(userDao.registerUser(email, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli, null))
                .thenReturn(expectedResult);

        // Esegui il metodo da testare
        String result = profileService.registraAccount(email, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli, null);

        // Verifica il risultato
        assertEquals(expectedResult, result);

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).registerUser(email, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli, null);
    }

    // Test di fallimento
    @Test
    void testRegisterUser_Fail() throws SQLException {
        // Configura il mock per simulare un errore
        String email = "test@example.com";
        String password = "password123";
        String nome = "John";
        String cognome = "Doe";
        String telefono = "1234567890";
        java.sql.Date dataNascita = java.sql.Date.valueOf("1990-01-01");
        String citta = "New York";
        String via = "Main St";
        String numeroCivico = "123";
        String cap = "12345";
        ArrayList<RuoloBean> ruoli = new ArrayList<>();
        ruoli.add(RuoloBean.Cliente);
        when(userDao.registerUser(email, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli, null))
                .thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare e verifica che sollevi un'eccezione
        assertThrows(SQLException.class, () -> profileService.registraAccount(email, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli, null));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(userDao, times(1)).registerUser(email, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli, null);
    }
}