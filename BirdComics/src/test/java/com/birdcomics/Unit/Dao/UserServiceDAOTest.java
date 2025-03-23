package com.birdcomics.Unit.Dao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.birdcomics.Model.Bean.IndirizzoBean;
import com.birdcomics.Model.Bean.MagazzinoBean;
import com.birdcomics.Model.Bean.RuoloBean;
import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.Model.Dao.UserServiceDAO;
import com.birdcomics.Utils.DBUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceDAOTest {

    @Mock
    private DBUtil dbUtil;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private UserServiceDAO userServiceDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() throws SQLException {
        // Dati di test
        String email = "test@example.com";
        String password = "password";
        String nome = "Nome";
        String cognome = "Cognome";
        String telefono = "1234567890";
        Date dataNascita = Date.valueOf("1990-01-01");
        String citta = "Città";
        String via = "Via";
        String numeroCivico = "123";
        String cap = "12345";
        ArrayList<RuoloBean> ruoli = new ArrayList<>();
        ruoli.add(RuoloBean.Cliente);
        MagazzinoBean magazzino = null;

        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Simula il comportamento di isRegistered
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Simula che l'utente non esista

        // Esegui il metodo da testare
        String result = userServiceDAO.registerUser(email, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli, magazzino);

        // Verifica il risultato
        assertEquals("User Registered Successfully!", result);
        verify(preparedStatement, times(3)).executeUpdate(); // Verifica che executeUpdate sia chiamato tre volte
    }

    @Test
    public void testIsRegistered() throws SQLException {
        // Dati di test
        String email = "test@example.com";

        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Simula che l'utente esista

        // Esegui il metodo da testare
        boolean result = userServiceDAO.isRegistered(email);

        // Verifica il risultato
        assertTrue(result);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testIsValidCredential() throws SQLException {
        // Dati di test
        String email = "test@example.com";
        String password = "password";

        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Simula che le credenziali siano valide

        // Esegui il metodo da testare
        String result = userServiceDAO.isValidCredential(email, password);

        // Verifica il risultato
        assertEquals("valid", result);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetUserDetails() throws SQLException {
        // Dati di test
        String email = "test@example.com";
        ArrayList<RuoloBean> ruoli = new ArrayList<>();
        ruoli.add(RuoloBean.Cliente);

        // Inizializza IndirizzoBean
        IndirizzoBean indirizzo = new IndirizzoBean("Città", "Via", "123", "12345");

        // Inizializza MagazzinoBean
        MagazzinoBean magazzino = new MagazzinoBean("Magazzino1", indirizzo, new ArrayList<>());

        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Configura il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce true una volta, poi false
        when(resultSet.getString("email")).thenReturn(email);
        when(resultSet.getString("pass")).thenReturn("password");
        when(resultSet.getString("nome")).thenReturn("Nome");
        when(resultSet.getString("cognome")).thenReturn("Cognome");
        when(resultSet.getString("telefono")).thenReturn("1234567890");
        when(resultSet.getDate("dataNascita")).thenReturn(Date.valueOf("1990-01-01"));
        when(resultSet.getString("nomeCitta")).thenReturn(indirizzo.getNomeCitta()); // Usa IndirizzoBean
        when(resultSet.getString("via")).thenReturn(indirizzo.getVia()); // Usa IndirizzoBean
        when(resultSet.getString("numeroCivico")).thenReturn(indirizzo.getNumeroCivico()); // Usa IndirizzoBean
        when(resultSet.getString("cap")).thenReturn(indirizzo.getCap()); // Usa IndirizzoBean
        when(resultSet.getString("nomeMagazzino")).thenReturn(magazzino.getNome()); // Usa MagazzinoBean
        when(resultSet.getString("idRuolo")).thenReturn("Cliente");

        // Esegui il metodo da testare
        UserBean result = userServiceDAO.getUserDetails(email);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(indirizzo.getNomeCitta(), result.getIndirizzo().getNomeCitta()); // Verifica IndirizzoBean
        assertEquals(magazzino.getNome(), result.getMagazzino().getNome()); // Verifica MagazzinoBean
        verify(preparedStatement, times(1)).executeQuery();
    }


    @Test
    public void testUpdateUserDetails() throws SQLException {
        // Dati di test
        String email = "test@example.com";
        String password = "password";
        String nome = "NuovoNome";
        String cognome = "NuovoCognome";
        String telefono = "0987654321";
        String nomeCitta = "NuovaCittà";
        String via = "NuovaVia";
        String numeroCivico = "456";
        String cap = "54321";

        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Simula il comportamento di IndirizzoDao.ifExists
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Simula che l'indirizzo non esista

        // Esegui il metodo da testare
        userServiceDAO.updateUserDetails(email, password, nome, cognome, telefono, nomeCitta, via, numeroCivico, cap);

        // Verifica che il metodo executeUpdate sia chiamato
        verify(preparedStatement, times(2)).executeUpdate();
    }


    @Test
    public void testDeleteUser() throws SQLException {
        // Dati di test
        String email = "test@example.com";

        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Esegui il metodo da testare
        userServiceDAO.deleteUser(email);

        // Verifica che il metodo executeUpdate sia chiamato due volte
        verify(preparedStatement, times(2)).executeUpdate();
    }
}
