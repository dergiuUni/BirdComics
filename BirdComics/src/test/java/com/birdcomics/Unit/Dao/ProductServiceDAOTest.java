package com.birdcomics.Unit.Dao;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.Model.Dao.ProductServiceDAO;
import com.birdcomics.Utils.DBUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProductServiceDAOTest {

    @Mock
    private DBUtil dbUtil;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private ProductServiceDAO productServiceDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProduct() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Simula il ResultSet per i generated keys
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1); // ID generato

        // Simula il PreparedStatement per l'inserimento dei generi
        when(connection.prepareStatement("insert into Genere_Fumetto (genere, idFumetto) values(?,?)")).thenReturn(preparedStatement);

        // Dati di test
        String[] selectedGenres = {"Azione", "Avventura"};

        // Esegui il metodo da testare
        String status = productServiceDAO.addProduct("Fumetto1", "Descrizione", 10.0f, "image.jpg", selectedGenres);

        // Verifica il risultato
        assertEquals("Product Added Successfully", status);

        verify(preparedStatement, times(3)).executeUpdate(); // 1 per il fumetto + 2 per i generi

    }



    @Test
    public void testRemoveProduct() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Dati di test
        String prodId = "1";

        // Esegui il metodo da testare
        String status = productServiceDAO.removeProduct(prodId);

        // Verifica il risultato
        assertEquals("Fumetto Cancellato!", status);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetAllProducts() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce true una volta, poi false
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("nome")).thenReturn("Fumetto1");
        when(resultSet.getString("descrizione")).thenReturn("Descrizione");
        when(resultSet.getFloat("prezzo")).thenReturn(10.0f);

        // Esegui il metodo da testare
        List<ProductBean> result = productServiceDAO.getAllProducts();

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetAllQuantityProductsById() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce true una volta, poi false
        when(resultSet.getInt(1)).thenReturn(10);

        // Dati di test
        ProductBean product = new ProductBean();
        product.setId(1);

        // Esegui il metodo da testare
        int result = productServiceDAO.getAllQuantityProductsById(product);

        // Verifica il risultato
        assertEquals(10, result);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetAllProductsByType() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce true una volta, poi false
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("nome")).thenReturn("Fumetto1");
        when(resultSet.getString("descrizione")).thenReturn("Descrizione");
        when(resultSet.getFloat("prezzo")).thenReturn(10.0f);
        when(resultSet.getString("genere")).thenReturn("Azione");

        // Dati di test
        String type = "Azione";

        // Esegui il metodo da testare
        List<ProductBean> result = productServiceDAO.getAllProductsByType(type);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testSearchAllProducts() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce true una volta, poi false
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("nome")).thenReturn("Fumetto1");
        when(resultSet.getString("descrizione")).thenReturn("Descrizione");
        when(resultSet.getFloat("prezzo")).thenReturn(10.0f);
        when(resultSet.getString("genere")).thenReturn("Azione");

        // Dati di test
        String search = "Fumetto1";

        // Esegui il metodo da testare
        List<ProductBean> result = productServiceDAO.searchAllProducts(search);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testSearchAllProductsGestore() throws SQLException {
        // Simula il comportamento de DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce true una volta, poi false
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("nome")).thenReturn("Fumetto1");
        when(resultSet.getString("descrizione")).thenReturn("Descrizione");
        when(resultSet.getFloat("prezzo")).thenReturn(10.0f);
        when(resultSet.getString("genere")).thenReturn("Azione");

        // Dati di test
        String search = "Fumetto1";

        // Esegui il metodo da testare
        List<ProductBean> result = productServiceDAO.searchAllProductsGestore(search);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetProductsByID() throws SQLException {
        // Simula il comportamento di DBUtil
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento di ResultSet
        when(resultSet.next()).thenReturn(true, false); // Restituisce true una volta, poi false
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("nome")).thenReturn("Fumetto1");
        when(resultSet.getString("descrizione")).thenReturn("Descrizione");
        when(resultSet.getFloat("prezzo")).thenReturn(10.0f);
        when(resultSet.getString("genere")).thenReturn("Azione");

        // Dati di test
        String idString = "1";

        // Esegui il metodo da testare
        ProductBean result = productServiceDAO.getProductsByID(idString);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(preparedStatement, times(1)).executeQuery();
    }
}
