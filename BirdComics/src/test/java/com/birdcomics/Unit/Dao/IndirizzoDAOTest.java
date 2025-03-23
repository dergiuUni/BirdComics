package com.birdcomics.Unit.Dao;

import com.birdcomics.Model.Bean.IndirizzoBean;
import com.birdcomics.Model.Dao.IndirizzoDAO;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class IndirizzoDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private IndirizzoDAO indirizzoDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        indirizzoDAO = new IndirizzoDAO(connection);
    }

    @Test
    void testCreateIndirizzo_Success() throws SQLException {
        IndirizzoBean indirizzo = new IndirizzoBean();
        indirizzo.setNomeCitta("Roma");
        indirizzo.setVia("Via Roma");
        indirizzo.setNumeroCivico("10");
        indirizzo.setCap("00100");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        indirizzoDAO.create(indirizzo);

        verify(preparedStatement, times(1)).setString(1, "Roma");
        verify(preparedStatement, times(1)).setString(2, "Via Roma");
        verify(preparedStatement, times(1)).setString(3, "10");
        verify(preparedStatement, times(1)).setString(4, "00100");
    }

    @Test
    void testCreateIndirizzo_SQLException() throws SQLException {
        IndirizzoBean indirizzo = new IndirizzoBean();
        indirizzo.setNomeCitta("Roma");
        indirizzo.setVia("Via Roma");
        indirizzo.setNumeroCivico("10");
        indirizzo.setCap("00100");

        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> indirizzoDAO.create(indirizzo));
    }

    @Test
    void testIfExists_True() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        boolean exists = indirizzoDAO.ifExists("Roma", "Via Roma", "10", "00100");

        assertTrue(exists);
        verify(preparedStatement, times(1)).setString(1, "Roma");
        verify(preparedStatement, times(1)).setString(2, "Via Roma");
        verify(preparedStatement, times(1)).setString(3, "10");
        verify(preparedStatement, times(1)).setString(4, "00100");
    }

    @Test
    void testIfExists_False() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        boolean exists = indirizzoDAO.ifExists("Roma", "Via Roma", "10", "00100");

        assertFalse(exists);
        verify(preparedStatement, times(1)).setString(1, "Roma");
        verify(preparedStatement, times(1)).setString(2, "Via Roma");
        verify(preparedStatement, times(1)).setString(3, "10");
        verify(preparedStatement, times(1)).setString(4, "00100");
    }

    @Test
    void testIfExists_SQLException() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> indirizzoDAO.ifExists("Roma", "Via Roma", "10", "00100"));
    }
}