package com.birdcomics.Unit.Dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.birdcomics.Model.Bean.CartBean;
import com.birdcomics.Model.Bean.CartItem;
import com.birdcomics.Model.Dao.CartServiceDAO;
import com.birdcomics.Utils.DBUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

public class CartServiceDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartServiceDAO cartServiceDAO;

    private MockedStatic<DBUtil> mockedDBUtil;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        mockedDBUtil = mockStatic(DBUtil.class);
        when(DBUtil.getConnection()).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        mockedDBUtil.close();
    }

    @Test
    void testGetCartFromSession() {
        String userId = "user@example.com";
        CartBean cart = new CartBean(userId);

        when(session.getAttribute("cartBean")).thenReturn(cart);

        CartBean result = cartServiceDAO.getCartFromSession(session, userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(session, times(1)).getAttribute("cartBean");
    }

    @Test
    void testAddProductToCart_NewProduct() throws SQLException {
        String userId = "user@example.com";
        String prodId = "1";
        int prodQty = 2;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Prodotto non presente nel carrello

        String result = cartServiceDAO.addProductToCart(session, userId, prodId, prodQty);

        assertEquals("Product added to cart successfully!", result);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testAddProductToCart_ExistingProduct() throws SQLException {
        String userId = "user@example.com";
        String prodId = "1";
        int prodQty = 2;

        // Mock the DB interactions
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Exit loop after first result
        when(resultSet.getInt("quantita")).thenReturn(3);
        when(resultSet.getString("idFumetto")).thenReturn(prodId);

        // Call the method
        String result = cartServiceDAO.addProductToCart(session, userId, prodId, prodQty);

        // Verify the outcome
        assertEquals("Product added to cart successfully!", result);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testAddProductToCart_SQLException() throws SQLException {
        String userId = "user@example.com";
        String prodId = "1";
        int prodQty = 2;

        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        String result = cartServiceDAO.addProductToCart(session, userId, prodId, prodQty);

        assertTrue(result.startsWith("Error adding product to cart:"));
    }

    @Test
    void testRemoveProductFromCart() throws SQLException {
        String userId = "user@example.com";
        String prodId = "1";

        // Mock the DB interactions
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet); // Mock the result set
        when(resultSet.next()).thenReturn(false); // Simulate no matching rows

        // Call the method
        String result = cartServiceDAO.removeProductFromCart(session, userId, prodId);

        // Verify the result
        assertEquals("Product removed from cart successfully!", result);
        verify(preparedStatement, times(1)).executeUpdate();
    }


    @Test
    void testRemoveProductFromCart_SQLException() throws SQLException {
        String userId = "user@example.com";
        String prodId = "1";

        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        String result = cartServiceDAO.removeProductFromCart(session, userId, prodId);

        assertTrue(result.startsWith("Error removing product from cart:"));
    }

    @Test
    void testUpdateProductToCart() throws SQLException {
        String userId = "user@example.com";
        String prodId = "1";
        int prodQty = 5;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        String result = cartServiceDAO.updateProductToCart(userId, prodId, prodQty);

        assertEquals("Product updated in cart successfully!", result);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testUpdateProductToCart_SQLException() throws SQLException {
        String userId = "user@example.com";
        String prodId = "1";
        int prodQty = 5;

        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        String result = cartServiceDAO.updateProductToCart(userId, prodId, prodQty);

        assertTrue(result.startsWith("Error updating cart:"));
    }

    @Test
    void testLoadCartFromDB() throws SQLException {
        String userId = "user@example.com";

        // Configura i mock per il PreparedStatement e ResultSet
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Simula un solo risultato
        when(resultSet.getString("idFumetto")).thenReturn("1");
        when(resultSet.getInt("quantita")).thenReturn(2);

        // Esegui il metodo sotto test
        CartBean result = cartServiceDAO.loadCartFromDB(session, userId);

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(1, result.getCartItems().size());
        verify(session, times(1)).setAttribute("cartBean", result);
    }
    @Test
    void testLoadCartFromDB_SQLException() throws SQLException {
        String userId = "user@example.com";

        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        CartBean result = cartServiceDAO.loadCartFromDB(session, userId);

        assertNotNull(result);
        assertEquals(0, result.getCartItems().size());
    }

    @Test
    void testDeleteAllCartItems() throws SQLException {
        String userId = "user@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        cartServiceDAO.deleteAllCartItems(session, userId);

        verify(preparedStatement, times(1)).executeUpdate();
        verify(session, times(1)).removeAttribute("cartBean");
    }

    @Test
    void testDeleteAllCartItems_SQLException() throws SQLException {
        String userId = "user@example.com";

        // Simula un'eccezione SQLException quando viene chiamato executeUpdate
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Database error"));

        // Verifica che l'eccezione venga lanciata
        assertThrows(SQLException.class, () -> cartServiceDAO.deleteAllCartItems(session, userId));

        // Verifica che executeUpdate sia stato chiamato
        verify(preparedStatement, times(1)).executeUpdate();

        // Verifica che la connessione sia stata chiusa
        mockedDBUtil.verify(() -> DBUtil.closeConnection(preparedStatement), times(1));
    }


}