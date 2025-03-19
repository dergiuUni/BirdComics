package com.birdcomics.Unit.Dao;

import com.birdcomics.Model.Bean.IndirizzoBean;
import com.birdcomics.Model.Bean.MagazzinoBean;
import com.birdcomics.Model.Dao.IndirizzoDao;
import com.birdcomics.Model.Dao.MagazzinoDao;
import com.birdcomics.Utils.DBUtil;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MagazzinoDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private IndirizzoDao indirizzoDao;

    private MagazzinoDao magazzinoDao;
    private MockedStatic<DBUtil> mockedDBUtil;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        magazzinoDao = new MagazzinoDao();

        // Mock dei metodi statici di DBUtil
        mockedDBUtil = mockStatic(DBUtil.class);
        when(DBUtil.getConnection()).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        mockedDBUtil.close();
    }

    // ===================================================
    // Test per il metodo: addMagazzino
    // ===================================================

    @Test
    void testAddMagazzino_Success() throws SQLException {
        MagazzinoBean magazzino = new MagazzinoBean();
        magazzino.setNome("Magazzino1");
        IndirizzoBean indirizzo = new IndirizzoBean("Città1", "Via1", "123", "12345");
        magazzino.setIndirizzo(indirizzo);

        // Mock del comportamento del database
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet); // Simula il ResultSet per la query SELECT
        when(resultSet.next()).thenReturn(false); // Simula che l'indirizzo non esista già
        when(preparedStatement.executeUpdate()).thenReturn(1); // Simula l'inserimento avvenuto con successo

        String result = magazzinoDao.addMagazzino(magazzino);

        assertEquals("magazzino Registered Successfully!", result);
    }

    @Test
    void testAddMagazzino_IndirizzoExists() throws SQLException {
        MagazzinoBean magazzino = new MagazzinoBean();
        magazzino.setNome("Magazzino1");
        IndirizzoBean indirizzo = new IndirizzoBean("Città1", "Via1", "123", "12345");
        magazzino.setIndirizzo(indirizzo);


        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(preparedStatement.executeUpdate()).thenReturn(1); // Inserimento avvenuto con successo

        
        
        // Simula che l'indirizzo esista già
        when(indirizzoDao.ifExists(anyString(), anyString(), anyString(), anyString())).thenReturn(true);

        String result = magazzinoDao.addMagazzino(magazzino);

        assertEquals("magazzino Registered Successfully!", result);
    }

    @Test
    void testAddMagazzino_SQLException() throws SQLException {
        MagazzinoBean magazzino = new MagazzinoBean();
        magazzino.setNome("Magazzino1");
        IndirizzoBean indirizzo = new IndirizzoBean("Città1", "Via1", "123", "12345");
        magazzino.setIndirizzo(indirizzo);

        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        String result = magazzinoDao.addMagazzino(magazzino);

        assertEquals("Error sql", result);
      }

    // ===================================================
    // Test per il metodo: removeMagazzino
    // ===================================================

    @Test
    void testRemoveMagazzino_Success() throws SQLException {
        MagazzinoBean magazzino = new MagazzinoBean();
        magazzino.setNome("Magazzino1");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1); // Rimozione avvenuta con successo

        String x = magazzinoDao.removeMagazzino(magazzino);

        assertEquals("magazzino cancellato", x);
    }

    @Test
    void testRemoveMagazzino_SQLException() throws SQLException {
        MagazzinoBean magazzino = new MagazzinoBean();
        magazzino.setNome("Magazzino1");

        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        String x = magazzinoDao.removeMagazzino(magazzino);

        assertEquals("Error sql", x);

    }

    
    // ===================================================
    // Test per il metodo: getMagazzini
    // ===================================================

    @Test
    void testGetMagazzini_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Simula un solo risultato
        when(resultSet.getString("nome")).thenReturn("Magazzino1");
        when(resultSet.getString("nomeCitta")).thenReturn("Città1");
        when(resultSet.getString("via")).thenReturn("Via1");
        when(resultSet.getInt("numeroCivico")).thenReturn(123);
        when(resultSet.getString("cap")).thenReturn("12345");

        ArrayList<MagazzinoBean> magazzini = magazzinoDao.getMagazzini();

        assertNotNull(magazzini);
        assertEquals(1, magazzini.size());
        assertEquals("Magazzino1", magazzini.get(0).getNome());
    }

    @Test
    void testGetMagazzini_EmptyResult() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Nessun risultato

        ArrayList<MagazzinoBean> magazzini = magazzinoDao.getMagazzini();

        assertTrue(magazzini.isEmpty());
    }

    @Test
    void testGetMagazzini_SQLException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        ArrayList<MagazzinoBean> magazzini = magazzinoDao.getMagazzini();

        assertTrue(magazzini.isEmpty());
    }
}