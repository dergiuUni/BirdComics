package com.birdcomics.Unit.Dao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.Model.Bean.GenereBean;
import com.birdcomics.Model.Dao.GenereDAO;
import com.birdcomics.Utils.DBUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GenereDAOTest {

    @Mock
    private DBUtil dbUtil;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private GenereDAO genereDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetGeneri() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Genere")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce true una volta, poi false
        when(resultSet.getString("genere")).thenReturn("Azione");

        // Esegui il metodo da testare
        List<GenereBean> result = genereDAO.getGeneri();

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Azione", result.get(0).getGenere());

        // Verifica le interazioni con i mock
        verify(preparedStatement, times(1)).executeQuery();
        verify(dbUtil, times(1)).getConnection();
        verify(dbUtil, times(1)).closeConnection(resultSet);
        verify(dbUtil, times(1)).closeConnection(preparedStatement);
        verify(dbUtil, times(1)).closeConnection(connection);
    }
}
