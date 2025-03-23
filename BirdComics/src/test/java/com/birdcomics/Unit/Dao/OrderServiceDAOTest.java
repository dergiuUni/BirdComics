package com.birdcomics.Unit.Dao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.birdcomics.Model.Bean.FatturaBean;
import com.birdcomics.Model.Bean.OrderBean;
import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.Model.Bean.ScaffaliBean;
import com.birdcomics.Model.Dao.FatturaServiceDAO;
import com.birdcomics.Model.Dao.OrderServiceDAO;
import com.birdcomics.Model.Dao.ScaffaleDao;
import com.birdcomics.Utils.DBUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrderServiceDAOTest {

    @Mock
    private DBUtil dbUtil;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private FatturaServiceDAO fatturaServiceDAO;

    @Mock
    private ScaffaleDao scaffaleDao;

    @InjectMocks
    private OrderServiceDAO orderServiceDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrder() throws SQLException {
        // Preparazione dei dati di test
        OrderBean order = new OrderBean();
        order.setIdUtente("client@example.com");
        order.setIdPaypal("PAYPAL123");
        order.setShipped("Non Spedito");
        order.setDataEffettuato(new Date(System.currentTimeMillis()));

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

        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1); // Simula l'ID generato

        when(fatturaServiceDAO.addFattura(any(FatturaBean.class))).thenReturn(true);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Simula un solo scaffale
        when(resultSet.getString("Magazzino.nome")).thenReturn("Magazzino1");
        when(resultSet.getInt("Scaffali.id")).thenReturn(1);
        when(resultSet.getInt("Scaffali.quantita")).thenReturn(10);
        when(scaffaleDao.modifyQuantityFumetto(any(ScaffaliBean.class))).thenReturn("Fumetto Quantity Modified Successfully!");

        // Esegui il metodo da testare
        boolean result = orderServiceDAO.addOrder(order);

        // Verifica il risultato
        assertTrue(result);

        // Verifica che i metodi siano stati chiamati correttamente
        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement, times(1)).getGeneratedKeys();
        verify(dbUtil, times(1)).getConnection();
        verify(dbUtil, times(1)).closeConnection(preparedStatement);
    }


    @Test
    void testGetAllOrderDetails() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Ordine WHERE emailUtente = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce una riga
        when(resultSet.getString("emailUtente")).thenReturn("test@example.com");
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("idpaypal")).thenReturn("PAYPAL123");
        when(resultSet.getString("shipped")).thenReturn("ORDER PLACED");
        when(resultSet.getDate("dataEffettuato")).thenReturn(new Date(System.currentTimeMillis()));
        when(resultSet.getInt("idFattura")).thenReturn(1);

        // Simula il comportamento di FatturaServiceDAO
        FatturaBean fattura = new FatturaBean();
        when(fatturaServiceDAO.getFatturaById(1)).thenReturn(fattura);

        // Esegui il metodo da testare
        List<OrderBean> result = orderServiceDAO.getAllOrderDetails("test@example.com");

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.size());
        OrderBean order = result.get(0);
        assertEquals("test@example.com", order.getIdUtente());
        assertEquals(1, order.getId());
        assertEquals("PAYPAL123", order.getIdPaypal());
        assertEquals("ORDER PLACED", order.getShipped());
        assertNotNull(order.getDataEffettuato());

        // Verifica le interazioni con i mock
        verify(preparedStatement, times(1)).executeQuery();
        verify(preparedStatement, times(1)).setString(1, "test@example.com");
        verify(dbUtil, times(1)).getConnection();
        verify(dbUtil, times(1)).closeConnection(resultSet);
        verify(dbUtil, times(1)).closeConnection(preparedStatement);
    }

    @Test
    void testUpdateShipped() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("UPDATE Ordine SET shipped = ? WHERE id = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Dati di test
        OrderBean order = new OrderBean();
        order.setId(1);
        order.setShipped("SHIPPED");

        // Esegui il metodo da testare
        boolean result = orderServiceDAO.updateShipped(order);

        // Verifica il risultato
        assertTrue(result);

        // Verifica le interazioni con i mock
        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement, times(1)).setString(1, "SHIPPED");
        verify(preparedStatement, times(1)).setInt(2, 1);
        verify(dbUtil, times(1)).getConnection();
        verify(dbUtil, times(1)).closeConnection(preparedStatement);
    }

    @Test
    void testGetAllOrderDetailsNoShipped() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement("SELECT * FROM Ordine WHERE shipped = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce una riga
        when(resultSet.getString("emailUtente")).thenReturn("test@example.com");
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("idpaypal")).thenReturn("PAYPAL123");
        when(resultSet.getString("shipped")).thenReturn("ORDER PLACED");
        when(resultSet.getDate("dataEffettuato")).thenReturn(new Date(System.currentTimeMillis()));
        when(resultSet.getInt("idFattura")).thenReturn(1);

        // Simula il comportamento di FatturaServiceDAO
        FatturaBean fattura = new FatturaBean();
        when(fatturaServiceDAO.getFatturaById(1)).thenReturn(fattura);

        // Esegui il metodo da testare
        List<OrderBean> result = orderServiceDAO.getAllOrderDetailsNoShipped();

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.size());
        OrderBean order = result.get(0);
        assertEquals("test@example.com", order.getIdUtente());
        assertEquals(1, order.getId());
        assertEquals("PAYPAL123", order.getIdPaypal());
        assertEquals("ORDER PLACED", order.getShipped());
        assertNotNull(order.getDataEffettuato());

        // Verifica le interazioni con i mock
        verify(preparedStatement, times(1)).executeQuery();
        verify(preparedStatement, times(1)).setString(1, "ORDER PLACED");
        verify(dbUtil, times(1)).getConnection();
        verify(dbUtil, times(1)).closeConnection(resultSet);
        verify(dbUtil, times(1)).closeConnection(preparedStatement);
    }
}
