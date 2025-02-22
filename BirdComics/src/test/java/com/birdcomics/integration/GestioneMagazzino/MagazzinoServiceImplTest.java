package com.birdcomics.integration.GestioneMagazzino;



import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.birdcomics.Bean.MagazzinoBean;
import com.birdcomics.Bean.ScaffaliBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.Dao.MagazzinoDao;
import com.birdcomics.Dao.ScaffaleDao;
import com.birdcomics.GestioneMagazzino.Service.MagazzinoServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MagazzinoServiceImplTest {

    @Mock
    private ScaffaleDao scaffaleDao;

    @Mock
    private MagazzinoDao magazzinoDao;

    @InjectMocks
    private MagazzinoServiceImpl magazzinoService;

    private UserBean user;
    private MagazzinoBean magazzino;

    @Before
    public void setUp() {
        // Initialize test data
        magazzino = new MagazzinoBean();
        magazzino.setNome("Magazzino1");

        user = new UserBean();
        user.setMagazzino(magazzino);
    }

    @Test
    public void testGetScaffaleMagazzino() throws SQLException {
        // Setup mock data
        List<ScaffaliBean> mockScaffali = new ArrayList<>(Arrays.asList(
            new ScaffaliBean(),
            new ScaffaliBean()
        ));

        // Mock DAO behavior
        when(scaffaleDao.getScaffaleMagazzino(magazzino.getNome())).thenReturn((ArrayList<ScaffaliBean>) mockScaffali);

        // Execute service method
        List<ScaffaliBean> result = magazzinoService.getScaffaleMagazzino(user);

        // Verify behavior
        assertEquals(mockScaffali, result);
        verify(scaffaleDao).getScaffaleMagazzino(magazzino.getNome());
    }

    @Test
    public void testGetAllMagazzini() throws SQLException {
        // Setup mock data
        List<MagazzinoBean> mockMagazzini = new ArrayList<>(Arrays.asList(
            new MagazzinoBean(),
            new MagazzinoBean()
        ));

        // Mock DAO behavior
        when(magazzinoDao.getMagazzini()).thenReturn((ArrayList<MagazzinoBean>) mockMagazzini);

        // Execute service method
        List<MagazzinoBean> result = magazzinoService.getAllMagazzini();

        // Verify behavior
        assertEquals(mockMagazzini, result);
        verify(magazzinoDao).getMagazzini();
    }

    @Test(expected = SQLException.class)
    public void testGetScaffaleMagazzinoThrowsSQLException() throws SQLException {
        // Mock DAO to throw SQLException
        when(scaffaleDao.getScaffaleMagazzino(magazzino.getNome())).thenThrow(new SQLException("DB error"));

        // Execute service method
        magazzinoService.getScaffaleMagazzino(user);
    }

    @Test(expected = SQLException.class)
    public void testGetAllMagazziniThrowsSQLException() throws SQLException {
        // Mock DAO to throw SQLException
        when(magazzinoDao.getMagazzini()).thenThrow(new SQLException("DB error"));

        // Execute service method
        magazzinoService.getAllMagazzini();
    }
}