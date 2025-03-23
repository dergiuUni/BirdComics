package com.birdcomics.Unit.test_gestioneMagazzino;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.birdcomics.Model.Bean.MagazzinoBean;
import com.birdcomics.Model.Bean.ScaffaliBean;
import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.Model.Dao.MagazzinoDAO;
import com.birdcomics.Model.Dao.ScaffaleDao;
import com.birdcomics.Model.Dao.UserServiceDAO;
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
    private MagazzinoDAO magazzinoDao;

    @Mock
    private UserServiceDAO userServiceDao;

    @InjectMocks
    private MagazzinoServiceImpl magazzinoService;

    private final String testEmail = "test@example.com";
    private UserBean user;
    private MagazzinoBean magazzino;

    @Before
    public void setUp() {
        // Initialize test data
        magazzino = new MagazzinoBean();
        magazzino.setNome("Magazzino1");

        user = new UserBean();
        user.setEmail(testEmail);
        user.setMagazzino(magazzino);
    }

    @Test
    public void testGetScaffaleMagazzino() throws SQLException {
        // Setup mock data
        ArrayList<ScaffaliBean> mockScaffali = new ArrayList<>(Arrays.asList(
            new ScaffaliBean(),
            new ScaffaliBean()
        ));

        // Mock DAO behavior
        when(userServiceDao.getUserDetails(testEmail)).thenReturn(user);
        when(scaffaleDao.getScaffaleMagazzino(magazzino.getNome())).thenReturn(mockScaffali);

        // Execute service method
        List<ScaffaliBean> result = magazzinoService.getScaffaleMagazzino(testEmail);

        // Verify behavior
        assertEquals(mockScaffali, result);
        verify(userServiceDao).getUserDetails(testEmail);
        verify(scaffaleDao).getScaffaleMagazzino(magazzino.getNome());
    }

    @Test
    public void testGetAllMagazzini() throws SQLException {
        // Setup mock data
        ArrayList<MagazzinoBean> mockMagazzini = new ArrayList<>(Arrays.asList(
            new MagazzinoBean(),
            new MagazzinoBean()
        ));

        // Mock DAO behavior
        when(magazzinoDao.getMagazzini()).thenReturn(mockMagazzini);

        // Execute service method
        List<MagazzinoBean> result = magazzinoService.getAllMagazzini();

        // Verify behavior
        assertEquals(mockMagazzini, result);
        verify(magazzinoDao).getMagazzini();
    }

    @Test(expected = SQLException.class)
    public void testGetScaffaleMagazzinoThrowsSQLException() throws SQLException {
        // Mock DAO to throw SQLException
        when(userServiceDao.getUserDetails(testEmail)).thenReturn(user);
        when(scaffaleDao.getScaffaleMagazzino(magazzino.getNome())).thenThrow(new SQLException("DB error"));

        // Execute service method
        magazzinoService.getScaffaleMagazzino(testEmail);
    }

    @Test(expected = SQLException.class)
    public void testGetAllMagazziniThrowsSQLException() throws SQLException {
        // Mock DAO to throw SQLException
        when(magazzinoDao.getMagazzini()).thenThrow(new SQLException("DB error"));

        // Execute service method
        magazzinoService.getAllMagazzini();
    }
}