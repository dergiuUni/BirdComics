package com.birdcomics.Unit.Dao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.Model.Bean.FatturaBean;
import com.birdcomics.Model.Dao.FatturaServiceDAO;
import com.birdcomics.Utils.DBUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FatturaDAOTest {

    @Mock
    private DBUtil dbUtil;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private FatturaServiceDAO fatturaServiceDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddFattura() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(java.sql.Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Simula il ResultSet per i generated keys
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1); // ID generato

        // Dati di test
        FatturaBean fattura = new FatturaBean();
        fattura.setIva(22);
        fattura.setNome("Mario");
        fattura.setCognome("Rossi");
        fattura.setTelefono("1234567890");
        fattura.setNomeCittaCliente("Roma");
        fattura.setViaCliente("Via Roma");
        fattura.setNumeroCivicoCliente("1");
        fattura.setCapCliente("00100");

        // Esegui il metodo da testare
        boolean result = fatturaServiceDAO.addFattura(fattura);

        // Verifica il risultato
        assertTrue(result);
        assertEquals(1, fattura.getId()); // Verifica che l'ID sia stato impostato correttamente

        // Verifica le interazioni con i mock
        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement, times(1)).getGeneratedKeys();
        verify(dbUtil, times(1)).getConnection();
        verify(dbUtil, times(1)).closeConnection(preparedStatement);
    }

    @Test
    public void testGetAllFattura() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("select * from Fattura")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce true una volta, poi false
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("iva")).thenReturn(22);
        when(resultSet.getString("nome")).thenReturn("Mario");
        when(resultSet.getString("cognome")).thenReturn("Rossi");
        when(resultSet.getString("telefono")).thenReturn("1234567890");
        when(resultSet.getString("nomeCittaCliente")).thenReturn("Roma");
        when(resultSet.getString("viaCliente")).thenReturn("Via Roma");
        when(resultSet.getString("numeroCivicoCliente")).thenReturn("1");
        when(resultSet.getString("capCliente")).thenReturn("00100");

        // Esegui il metodo da testare
        List<FatturaBean> result = fatturaServiceDAO.getAllFattura();

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.size());
        FatturaBean fattura = result.get(0);
        assertEquals(1, fattura.getId());
        assertEquals(22, fattura.getIva());
        assertEquals("Mario", fattura.getNome());
        assertEquals("Rossi", fattura.getCognome());
        assertEquals("1234567890", fattura.getTelefono());
        assertEquals("Roma", fattura.getNomeCittaCliente());
        assertEquals("Via Roma", fattura.getViaCliente());
        assertEquals("1", fattura.getNumeroCivicoCliente());
        assertEquals("00100", fattura.getCapCliente());

        // Verifica le interazioni con i mock
        verify(preparedStatement, times(1)).executeQuery();
        verify(dbUtil, times(1)).getConnection();
        verify(dbUtil, times(1)).closeConnection(resultSet);
        verify(dbUtil, times(1)).closeConnection(preparedStatement);
    }

    @Test
    public void testGetFatturaById() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Fattura Where id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true); // Restituisce true una volta
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("iva")).thenReturn(22);
        when(resultSet.getString("nome")).thenReturn("Mario");
        when(resultSet.getString("cognome")).thenReturn("Rossi");
        when(resultSet.getString("telefono")).thenReturn("1234567890");
        when(resultSet.getString("nomeCittaCliente")).thenReturn("Roma");
        when(resultSet.getString("viaCliente")).thenReturn("Via Roma");
        when(resultSet.getString("numeroCivicoCliente")).thenReturn("1");
        when(resultSet.getString("capCliente")).thenReturn("00100");

        // Esegui il metodo da testare
        FatturaBean result = fatturaServiceDAO.getFatturaById(1);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(22, result.getIva());
        assertEquals("Mario", result.getNome());
        assertEquals("Rossi", result.getCognome());
        assertEquals("1234567890", result.getTelefono());
        assertEquals("Roma", result.getNomeCittaCliente());
        assertEquals("Via Roma", result.getViaCliente());
        assertEquals("1", result.getNumeroCivicoCliente());
        assertEquals("00100", result.getCapCliente());

        // Verifica le interazioni con i mock
        verify(preparedStatement, times(1)).executeQuery();
        verify(preparedStatement, times(1)).setInt(1, 1);
        verify(dbUtil, times(1)).getConnection();
        verify(dbUtil, times(1)).closeConnection(resultSet);
        verify(dbUtil, times(1)).closeConnection(preparedStatement);
    }
}
