package com.birdcomics.Dao;

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Bean.ScaffaliBean;
import com.birdcomics.Utils.DBUtil;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.sql.*;
import java.util.ArrayList;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ScaffaleDaoTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private ProductServiceDAO productServiceDAO;

    private ScaffaleDao scaffaleDao;
    private MockedStatic<DBUtil> mockedDBUtil;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        scaffaleDao = new ScaffaleDao();
        mockedDBUtil = mockStatic(DBUtil.class);
        
        // Mock static DBUtil methods
        when(DBUtil.getConnection()).thenReturn(connection);
        
        // Mock ProductServiceDAO
        try (MockedConstruction<ProductServiceDAO> mocked = mockConstruction(ProductServiceDAO.class)) {
            when(productServiceDAO.getProductsByID(anyString())).thenReturn(new ProductBean());
        }
    }

    @AfterEach
    void tearDown() {
        mockedDBUtil.close();
    }

    @Test
    void testAddScaffale_Success() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(123);

        ScaffaliBean result = scaffaleDao.addScaffale(100);
        
        assertNotNull(result);
        assertEquals(123, result.getId());
        verify(preparedStatement).setInt(1, 100);
    }

    @Test
    void testAddScaffale_Failure() throws SQLException {
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        ScaffaliBean result = scaffaleDao.addScaffale(100);
        
        assertNull(result);
    }

    @Test
    void testRemoveScaffale_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        ProductBean fumetto = new ProductBean();
        fumetto.setId(456);
        scaffaleDao.removeScaffale(fumetto);
        
        verify(preparedStatement).setInt(1, 456);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testAddFumetto_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        ScaffaliBean scaffale = new ScaffaliBean();
        scaffale.setQuantitaOccupata(10);
        ProductBean fumetto = new ProductBean();
        fumetto.setId(5);
        scaffale.setFumetto(fumetto);
        
        scaffaleDao.addFumetto(scaffale);
        
        verify(preparedStatement).setInt(1, 10);
        verify(preparedStatement).setInt(2, 5);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testModifyQuantityFumetto_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        ScaffaliBean scaffale = new ScaffaliBean();
        scaffale.setId(789);
        scaffale.setQuantitaOccupata(15);
        
        scaffaleDao.modifyQuantityFumetto(scaffale);
        
        verify(preparedStatement).setInt(1, 15);
        verify(preparedStatement).setInt(2, 789);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testGetScaffaleMagazzino_Success() throws SQLException {
        // Configura il mock del PreparedStatement e ResultSet
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula due righe nel ResultSet
        when(resultSet.next()).thenReturn(true, true, false); // Due righe, poi false per terminare il ciclo
        when(resultSet.getInt("idScaffale")).thenReturn(1, 2); // ID degli scaffali
        when(resultSet.getInt("quantita")).thenReturn(10, 20); // Quantità occupata
        when(resultSet.getInt("quantitaMassima")).thenReturn(100, 200); // Quantità massima
        when(resultSet.getInt("idFumetto")).thenReturn(5, 6); // ID dei fumetti

        // Mock del ProductServiceDAO per restituire un ProductBean valido
        ProductBean fumetto1 = new ProductBean();
        fumetto1.setId(5);
        ProductBean fumetto2 = new ProductBean();
        fumetto2.setId(6);

        try (MockedConstruction<ProductServiceDAO> mocked = mockConstruction(ProductServiceDAO.class, 
            (mock, context) -> {
                when(mock.getProductsByID("5")).thenReturn(fumetto1);
                when(mock.getProductsByID("6")).thenReturn(fumetto2);
            })) {

            // Esegui il metodo da testare
            ArrayList<ScaffaliBean> result = scaffaleDao.getScaffaleMagazzino("mag1");

            // Verifica i risultati
            assertEquals(2, result.size());

            // Verifica il primo scaffale
            ScaffaliBean scaffale1 = result.get(0);
            assertEquals(1, scaffale1.getId());
            assertEquals(10, scaffale1.getQuantitaOccupata());
            assertEquals(100, scaffale1.getQuantitaMassima());
            assertEquals(5, scaffale1.getFumetto().getId());

            // Verifica il secondo scaffale
            ScaffaliBean scaffale2 = result.get(1);
            assertEquals(2, scaffale2.getId());
            assertEquals(20, scaffale2.getQuantitaOccupata());
            assertEquals(200, scaffale2.getQuantitaMassima());
            assertEquals(6, scaffale2.getFumetto().getId());
        }
    }

    @Test
    void testDeleteFumetto_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        ScaffaliBean scaffale = new ScaffaliBean();
        scaffale.setId(999);
        
        scaffaleDao.deleteFumetto(scaffale);
        
        verify(preparedStatement).setInt(1, 999);
        verify(preparedStatement).executeUpdate();
    }


}