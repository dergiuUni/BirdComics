package com.birdcomics.Unit.Dao;

import com.birdcomics.Bean.RuoloBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.Dao.IndirizzoDao;
import com.birdcomics.Dao.UserServiceDAO;
import com.birdcomics.Utils.DBUtil;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.ArrayList;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private IndirizzoDao indirizzoDao;

    private UserServiceDAO userServiceDAO;
    private MockedStatic<DBUtil> mockedDBUtil;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        userServiceDAO = new UserServiceDAO();

        // Mock dei metodi statici di DBUtil
        mockedDBUtil = mockStatic(DBUtil.class);
        when(DBUtil.getConnection()).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        mockedDBUtil.close();
    }

    // ===================================================
    // Test per il metodo: registerUser
    // ===================================================

    @Test
    void testRegisterUser_Success() throws SQLException {
        // Dati di input
        String email = "test@example.com";
        String password = "password";
        String nome = "Nome";
        String cognome = "Cognome";
        String numeroTelefono = "1234567890";
        Date dataNascita = Date.valueOf("1990-01-01");
        String nomeCitta = "Città";
        String via = "Via";
        String numeroCivico = "123";
        String cap = "12345";
        ArrayList<RuoloBean> ruoli = new ArrayList<>();
        ruoli.add(RuoloBean.Cliente);

        // Mock del comportamento del database per isRegistered
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Simula che l'email non sia già registrata

        // Mock del comportamento del database per registerUser
        when(preparedStatement.executeUpdate()).thenReturn(1); // Simula l'inserimento avvenuto con successo

        // Esegui il metodo da testare
        String result = userServiceDAO.registerUser(email, password, nome, cognome, numeroTelefono, dataNascita, nomeCitta, via, numeroCivico, cap, ruoli, null);

        // Verifica il risultato
        assertEquals("User Registered Successfully!", result);
    }

    @Test
    void testRegisterUser_EmailAlreadyRegistered() throws SQLException {
        // Dati di input
        String email = "test@example.com";
        String password = "password";
        String nome = "Nome";
        String cognome = "Cognome";
        String numeroTelefono = "1234567890";
        Date dataNascita = Date.valueOf("1990-01-01");
        String nomeCitta = "Città";
        String via = "Via";
        String numeroCivico = "123";
        String cap = "12345";
        ArrayList<RuoloBean> ruoli = new ArrayList<>();
        ruoli.add(RuoloBean.Cliente);

        // Simula che l'email sia già registrata
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Simula che l'email esista già

        // Esegui il metodo da testare
        String result = userServiceDAO.registerUser(email, password, nome, cognome, numeroTelefono, dataNascita, nomeCitta, via, numeroCivico, cap, ruoli, null);

        // Verifica il risultato
        assertEquals("Email Id Already Registered!", result);
    }

    @Test
    void testRegisterUser_SQLException() throws SQLException {
        // Dati di input
        String email = "test@example.com";
        String password = "password";
        String nome = "Nome";
        String cognome = "Cognome";
        String numeroTelefono = "1234567890";
        Date dataNascita = Date.valueOf("1990-01-01");
        String nomeCitta = "Città";
        String via = "Via";
        String numeroCivico = "123";
        String cap = "12345";
        ArrayList<RuoloBean> ruoli = new ArrayList<>();
        ruoli.add(RuoloBean.Cliente);

        // Simula un'eccezione SQL
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare
        String result = userServiceDAO.registerUser(email, password, nome, cognome, numeroTelefono, dataNascita, nomeCitta, via, numeroCivico, cap, ruoli, null);

        // Verifica il risultato
        assertTrue(result.startsWith("Error: "));
    }

    // ===================================================
    // Test per il metodo: isValidCredential
    // ===================================================

    @Test
    void testIsValidCredential_Valid() throws SQLException {
        // Dati di input
        String email = "test@example.com";
        String password = "password";

        // Mock del comportamento del database
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Simula che le credenziali siano valide

        // Esegui il metodo da testare
        String result = userServiceDAO.isValidCredential(email, password);

        // Verifica il risultato
        assertEquals("valid", result);
    }

    @Test
    void testIsValidCredential_Invalid() throws SQLException {
        // Dati di input
        String email = "test@example.com";
        String password = "password";

        // Mock del comportamento del database
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Simula che le credenziali non siano valide

        // Esegui il metodo da testare
        String result = userServiceDAO.isValidCredential(email, password);

        // Verifica il risultato
        assertEquals("Login Denied! Incorrect email or Password", result);
    }

    @Test
    void testIsValidCredential_SQLException() throws SQLException {
        // Dati di input
        String email = "test@example.com";
        String password = "password";

        // Simula un'eccezione SQL
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare
        String result = userServiceDAO.isValidCredential(email, password);

        // Verifica il risultato
        assertTrue(result.startsWith("Error: "));
    }

    // ===================================================
    // Test per il metodo: getUserDetails
    // ===================================================

    @Test
    void testGetUserDetails_Success() throws SQLException {
        // Dati di input
        String email = "test@example.com";

        // Mock del comportamento del database
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula che il ResultSet contenga due righe (un utente con due ruoli)
        when(resultSet.next()).thenReturn(true, true, false); // Due righe, poi false per terminare
        when(resultSet.getString("email")).thenReturn(email, email); // Email uguale per entrambe le righe
        when(resultSet.getString("nome")).thenReturn("Nome", "Nome");
        when(resultSet.getString("cognome")).thenReturn("Cognome", "Cognome");
        when(resultSet.getString("telefono")).thenReturn("1234567890", "1234567890");
        when(resultSet.getDate("dataNascita")).thenReturn(Date.valueOf("1990-01-01"), Date.valueOf("1990-01-01"));
        when(resultSet.getString("nomeCitta")).thenReturn("Città", "Città");
        when(resultSet.getString("via")).thenReturn("Via", "Via");
        when(resultSet.getInt("numeroCivico")).thenReturn(123, 123);
        when(resultSet.getString("cap")).thenReturn("12345", "12345");
        when(resultSet.getString("idRuolo")).thenReturn("Cliente", "Magazziniere"); // Due ruoli diversi

        // Esegui il metodo da testare
        UserBean user = userServiceDAO.getUserDetails(email);

        // Verifica il risultato
        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals("Nome", user.getNome());
        assertEquals("Cognome", user.getCognome());

        // Verifica che i ruoli siano stati correttamente aggiunti
        assertNotNull(user.getRuolo());
        assertEquals(2, user.getRuolo().size());
        assertTrue(user.getRuolo().contains(RuoloBean.Cliente));
        assertTrue(user.getRuolo().contains(RuoloBean.Magazziniere));
    }

    @Test
    void testGetUserDetails_NotFound() throws SQLException {
        // Dati di input
        String email = "test@example.com";

        // Mock del comportamento del database
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Simula che l'utente non esista

        // Esegui il metodo da testare
        UserBean user = userServiceDAO.getUserDetails(email);

        // Verifica il risultato
        assertNull(user);
    }

    @Test
    void testGetUserDetails_SQLException() throws SQLException {
        // Dati di input
        String email = "test@example.com";

        // Simula un'eccezione SQL
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare
        UserBean user = userServiceDAO.getUserDetails(email);

        // Verifica il risultato
        assertNull(user);
    }
}