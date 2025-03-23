package com.birdcomics.Unit.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

import com.birdcomics.Model.Bean.CartBean;
import com.birdcomics.Model.Bean.CartItem;
import com.birdcomics.Model.Dao.CartServiceDAO;
import com.birdcomics.Utils.DBUtil;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CartServiceDAOTest {

    @Mock
    private DBUtil dbUtil;

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCartFromSession() {
        String email = "user@example.com";
        CartBean cart = new CartBean(email);

        when(session.getAttribute("cartBean")).thenReturn(cart);

        CartBean result = cartServiceDAO.getCartFromSession(session, email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    public void testAddProductToCart() throws SQLException {
        String email = "user@example.com";
        String prodId = "1";
        int prodQty = 2;

        // Simula il comportamento del CarrelloCliente
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Nessun risultato nel carrello

        String result = cartServiceDAO.addProductToCart(session, email, prodId, prodQty);

        assertEquals("Product added to cart successfully!", result);
        verify(preparedStatement, times(3)).setString(1, email); // Aspettati tre chiamate
        verify(preparedStatement, times(2)).setString(2, prodId);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testRemoveProductFromCart() throws SQLException {
        String email = "user@example.com";
        String prodId = "1";

        // Simula il comportamento del CarrelloCliente
        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        // Simula il comportamento di loadCartFromDB
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Nessun risultato nel carrello

        String result = cartServiceDAO.removeProductFromCart(session, email, prodId);

        assertEquals("Product removed from cart successfully!", result);
        verify(preparedStatement, times(2)).setString(1, email); // Aspettati due chiamate
        verify(preparedStatement, times(1)).setString(2, prodId);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateProductToCart() throws SQLException {
        String email = "user@example.com";
        String prodId = "1";
        int prodQty = 3;

        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        String result = cartServiceDAO.updateProductToCart(email, prodId, prodQty);

        assertEquals("Product updated in cart successfully!", result);
        verify(preparedStatement, times(1)).setInt(1, prodQty);
        verify(preparedStatement, times(1)).setString(2, email);
        verify(preparedStatement, times(1)).setString(3, prodId);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testLoadCartFromDB() throws SQLException {
        String email = "user@example.com";
        String prodId = "1";
        int quantity = 2;

        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("idFumetto")).thenReturn(prodId);
        when(resultSet.getInt("quantita")).thenReturn(quantity);

        CartBean result = cartServiceDAO.loadCartFromDB(session, email);

        assertNotNull(result);
        assertEquals(1, result.getCartItems().size());
        assertEquals(prodId, result.getCartItems().get(0).getProdId());
        assertEquals(quantity, result.getCartItems().get(0).getQuantity());
    }

    @Test
    public void testDeleteAllCartItems() throws SQLException {
        String email = "user@example.com";

        when(dbUtil.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        cartServiceDAO.deleteAllCartItems(session, email);

        verify(preparedStatement, times(1)).setString(1, email);
        verify(preparedStatement, times(1)).executeUpdate();
        verify(session, times(1)).removeAttribute("cartBean");
    }
}
