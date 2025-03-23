package com.birdcomics.Unit.Dao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.birdcomics.Model.Bean.IndirizzoBean;
import com.birdcomics.Model.Bean.MagazzinoBean;
import com.birdcomics.Model.Bean.ScaffaliBean;
import com.birdcomics.Model.Dao.IndirizzoDAO;
import com.birdcomics.Model.Dao.MagazzinoDAO;
import com.birdcomics.Model.Dao.ScaffaleDao;
import com.birdcomics.Utils.DBUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MagazzinoDAOTest {

    @Mock
    private DBUtil dbUtil;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private IndirizzoDAO indirizzoDao;

    @Mock
    private ScaffaleDao scaffaleDao;

    @InjectMocks
    private MagazzinoDAO magazzinoDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddMagazzino() throws SQLException {
        // Dati di test
        IndirizzoBean indirizzo = new IndirizzoBean("Città", "Via", "123", "12345");
        MagazzinoBean magazzino = new MagazzinoBean("Magazzino1", indirizzo, new ArrayList<>());

        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        // Simula il comportamento di IndirizzoDao
        when(indirizzoDao.ifExists(anyString(), anyString(), anyString(), anyString())).thenReturn(false);

        // Simula il ResultSet per la query di IndirizzoDao
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Simula che l'indirizzo non esista

        // Simula l'inserimento dell'indirizzo e del magazzino
        when(preparedStatement.executeUpdate()).thenReturn(1); // Simula un inserimento riuscito

        // Esegui il metodo da testare
        String result = magazzinoDao.addMagazzino(magazzino);

        // Verifica il risultato
        assertEquals("magazzino Registered Successfully!", result);
        verify(preparedStatement, times(2)).executeUpdate(); // Verifica che executeUpdate sia chiamato due volte
    }

    @Test
    public void testRemoveMagazzino() throws SQLException {
        // Dati di test
        IndirizzoBean indirizzo = new IndirizzoBean("Città", "Via", "123", "12345");
        MagazzinoBean magazzino = new MagazzinoBean("Magazzino1", indirizzo, new ArrayList<>());

        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        // Esegui il metodo da testare
        String result = magazzinoDao.removeMagazzino(magazzino);

        // Verifica il risultato
        assertEquals("magazzino cancellato", result);
        verify(preparedStatement, times(1)).executeUpdate(); // Verifica che executeUpdate sia chiamato una volta
    }

    @Test
    public void testGetMagazzini() throws SQLException {
        // Dati di test
        IndirizzoBean indirizzo = new IndirizzoBean("Città", "Via", "123", "12345");
        ArrayList<MagazzinoBean> magazzini = new ArrayList<>();
        magazzini.add(new MagazzinoBean("Magazzino1", indirizzo, new ArrayList<>()));

        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("nome")).thenReturn("Magazzino1");
        when(resultSet.getString("nomeCitta")).thenReturn("Città");
        when(resultSet.getString("via")).thenReturn("Via");
        when(resultSet.getString("numeroCivico")).thenReturn("123");
        when(resultSet.getString("cap")).thenReturn("12345");

        // Simula il comportamento di ScaffaleDao
        when(scaffaleDao.getScaffaleMagazzino(anyString())).thenReturn(new ArrayList<>());

        // Esegui il metodo da testare
        ArrayList<MagazzinoBean> result = magazzinoDao.getMagazzini();

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Magazzino1", result.get(0).getNome());
    }
}
