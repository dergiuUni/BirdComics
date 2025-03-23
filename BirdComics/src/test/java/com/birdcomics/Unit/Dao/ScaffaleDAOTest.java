package com.birdcomics.Unit.Dao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.Model.Bean.ScaffaliBean;
import com.birdcomics.Model.Dao.ScaffaleDao;
import com.birdcomics.Utils.DBUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ScaffaleDAOTest {

    @Mock
    private DBUtil dbUtil;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ScaffaleDao scaffaleDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddScaffale() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(java.sql.Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Simula il ResultSet per i generated keys
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1); // ID generato

        // Esegui il metodo da testare
        ScaffaliBean result = scaffaleDao.addScaffale(100);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testRemoveScaffale() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Dati di test
        ProductBean fumetto = new ProductBean();
        fumetto.setId(1);

        // Esegui il metodo da testare
        String status = scaffaleDao.removeScaffale(fumetto);

        // Verifica il risultato
        assertEquals("Scaffale Removed Successfully!", status);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testAddFumetto() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Dati di test
        ScaffaliBean scaffale = new ScaffaliBean();
        scaffale.setQuantitaOccupata(10);
        scaffale.setFumetto(new ProductBean(1, "Fumetto1", "Descrizione", 10.0f, "Copertina.jpg", new ArrayList<>()));

        // Esegui il metodo da testare
        String status = scaffaleDao.addFumetto(scaffale);

        // Verifica il risultato
        assertEquals("Fumetto Added to Scaffale Successfully!", status);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testModifyFumetto() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Dati di test
        ScaffaliBean scaffale = new ScaffaliBean();
        scaffale.setId(1);
        scaffale.setQuantitaOccupata(20);
        scaffale.setFumetto(new ProductBean(1, "Fumetto1", "Descrizione", 10.0f, "Copertina.jpg", new ArrayList<>()));

        // Esegui il metodo da testare
        String status = scaffaleDao.modifyFumetto(scaffale);

        // Verifica il risultato
        assertEquals("Fumetto Modified Successfully!", status);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testModifyQuantityFumetto() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Dati di test
        ScaffaliBean scaffale = new ScaffaliBean();
        scaffale.setId(1);
        scaffale.setQuantitaOccupata(20);

        // Esegui il metodo da testare
        String status = scaffaleDao.modifyQuantityFumetto(scaffale);

        // Verifica il risultato
        assertEquals("Fumetto Quantity Modified Successfully!", status);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteFumetto() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Dati di test
        ScaffaliBean scaffale = new ScaffaliBean();
        scaffale.setId(1);

        // Esegui il metodo da testare
        String status = scaffaleDao.deleteFumetto(scaffale);

        // Verifica il risultato
        assertEquals("Fumetto Deleted Successfully!", status);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetScaffaleMagazzino() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce true una volta, poi false
        when(resultSet.getInt("idScaffale")).thenReturn(1);
        when(resultSet.getInt("quantita")).thenReturn(10);
        when(resultSet.getInt("quantitaMassima")).thenReturn(100);

        // Dati di test
        String nomeMagazzino = "Magazzino1";

        // Esegui il metodo da testare
        ArrayList<ScaffaliBean> result = scaffaleDao.getScaffaleMagazzino(nomeMagazzino);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(preparedStatement, times(1)).executeQuery();
    }
}
