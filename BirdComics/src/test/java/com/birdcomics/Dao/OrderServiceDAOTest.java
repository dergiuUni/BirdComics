package com.birdcomics.Dao;

import com.birdcomics.Bean.FatturaBean;
import com.birdcomics.Bean.OrderBean;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Bean.ScaffaliBean;
import com.birdcomics.Utils.DBUtil;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private FatturaServiceDAO fatturaServiceDAO;

    private OrderServiceDAO orderServiceDAO;
    private MockedStatic<DBUtil> mockedDBUtil;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        orderServiceDAO = new OrderServiceDAO();

        // Mock dei metodi statici di DBUtil
        mockedDBUtil = mockStatic(DBUtil.class);
        when(DBUtil.getConnection()).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        mockedDBUtil.close();
    }

    // ===================================================
    // Test per il metodo: addOrder
    // ===================================================

    @Test
    void testAddOrder_Success() throws SQLException {
        OrderBean order = new OrderBean();
        order.setEmailUtente("client@example.com");
        order.setIdPaypal("PAYPAL123");
        order.setShipped("Non Spedito");
        order.setDataEffettuato(new java.sql.Date(System.currentTimeMillis()));
        FatturaBean fattura = new FatturaBean();
        fattura.setId(1);
        order.setIdFattura(fattura);

        HashMap<ProductBean, Integer> fumetti = new HashMap<>();
        ProductBean product = new ProductBean();
        product.setId(1);
        product.setName("Fumetto1");
        product.setDescription("Descrizione1");
        product.setPrice(10.0f);
        fumetti.put(product, 5);
        order.setFumetti(fumetti);

        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1); // Simula l'ID generato

        // Mock per la query di selezione degli scaffali
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Simula un solo scaffale
        when(resultSet.getString("Magazzino.nome")).thenReturn("Magazzino1");
        when(resultSet.getInt("Scaffali.id")).thenReturn(1);
        when(resultSet.getInt("Scaffali.quantita")).thenReturn(10);

        boolean result = orderServiceDAO.addOrder(order);

        assertTrue(result);
    }

    @Test
    void testAddOrder_SQLException() throws SQLException {
        OrderBean order = new OrderBean();
        order.setEmailUtente("client@example.com");
        order.setIdPaypal("PAYPAL123");
        order.setShipped("Non Spedito");
        order.setDataEffettuato(new java.sql.Date(System.currentTimeMillis()));
        FatturaBean fattura = new FatturaBean();
        fattura.setId(1);
        order.setIdFattura(fattura);

        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
            .thenThrow(new SQLException("Database error"));

        boolean result = orderServiceDAO.addOrder(order);

        assertFalse(result);
    }

    // ===================================================
    // Test per il metodo: getAllOrderDetails
    // ===================================================

    @Test
    void testGetAllOrderDetails_Success() throws SQLException {
        String userEmailId = "client@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Simula un solo ordine
        when(resultSet.getString("emailUtente")).thenReturn(userEmailId);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("idpaypal")).thenReturn("PAYPAL123");
        when(resultSet.getString("shipped")).thenReturn("Non Spedito");
        when(resultSet.getDate("dataEffettuato")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getInt("idFattura")).thenReturn(1);

        List<OrderBean> orderList = orderServiceDAO.getAllOrderDetails(userEmailId);

        assertNotNull(orderList);
        assertEquals(1, orderList.size());
        assertEquals(userEmailId, orderList.get(0).getEmailUtente());
    }

    @Test
    void testGetAllOrderDetails_EmptyResult() throws SQLException {
        String userEmailId = "client@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Nessun risultato

        List<OrderBean> orderList = orderServiceDAO.getAllOrderDetails(userEmailId);

        assertTrue(orderList.isEmpty());
    }

    @Test
    void testGetAllOrderDetails_SQLException() throws SQLException {
        String userEmailId = "client@example.com";

        when(connection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database error"));

        List<OrderBean> orderList = orderServiceDAO.getAllOrderDetails(userEmailId);

        assertTrue(orderList.isEmpty());
    }

    // ===================================================
    // Test per il metodo: updateShipped
    // ===================================================

    @Test
    void testUpdateShipped_Success() throws SQLException {
        OrderBean order = new OrderBean();
        order.setId(1);
        order.setShipped("Spedito");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1); // Aggiornamento avvenuto con successo

        boolean result = orderServiceDAO.updateShipped(order);

        assertTrue(result);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testUpdateShipped_SQLException() throws SQLException {
        OrderBean order = new OrderBean();
        order.setId(1);
        order.setShipped("Spedito");

        when(connection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database error"));

        boolean result = orderServiceDAO.updateShipped(order);

        assertFalse(result);
    }

    // ===================================================
    // Test per il metodo: getAllOrderDetailsNoShipped
    // ===================================================

    @Test
    void testGetAllOrderDetailsNoShipped_Success() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Simula un solo ordine
        when(resultSet.getString("emailUtente")).thenReturn("client@example.com");
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("idpaypal")).thenReturn("PAYPAL123");
        when(resultSet.getString("shipped")).thenReturn("Non Spedito");
        when(resultSet.getDate("dataEffettuato")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getInt("idFattura")).thenReturn(1);

        List<OrderBean> orderList = orderServiceDAO.getAllOrderDetailsNoShipped();

        assertNotNull(orderList);
        assertEquals(1, orderList.size());
        assertEquals("Non Spedito", orderList.get(0).getShipped());
    }

    @Test
    void testGetAllOrderDetailsNoShipped_EmptyResult() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Nessun risultato

        List<OrderBean> orderList = orderServiceDAO.getAllOrderDetailsNoShipped();

        assertTrue(orderList.isEmpty());
    }

    @Test
    void testGetAllOrderDetailsNoShipped_SQLException() throws SQLException {
        when(connection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database error"));

        List<OrderBean> orderList = orderServiceDAO.getAllOrderDetailsNoShipped();

        assertTrue(orderList.isEmpty());
    }
}