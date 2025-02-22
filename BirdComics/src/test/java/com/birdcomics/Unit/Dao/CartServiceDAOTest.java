package com.birdcomics.Unit.Dao;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.Dao.CartServiceDAO;
import com.birdcomics.Utils.DBUtil;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CartServiceDAOTest {

    @Mock
    private Connection connection;  

    @Mock
    private PreparedStatement preparedStatement;  

    @Mock
    private ResultSet resultSet;  

    private CartServiceDAO cartServiceDAO;
    private MockedStatic<DBUtil> mockedDBUtil;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        cartServiceDAO = new CartServiceDAO();

        // Mock dei metodi statici di DBUtil
        mockedDBUtil = mockStatic(DBUtil.class);
        when(DBUtil.getConnection()).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        mockedDBUtil.close();
    }

    @Test
    void testAddProductToCartNewProduct() throws SQLException {
        String userId = "client@example.com";
        String prodId = "1";
        int prodQty = 23;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Il prodotto non è già nel carrello

        when(preparedStatement.executeUpdate()).thenReturn(1); // Inserimento avvenuto con successo

        String result = cartServiceDAO.addProductToCart(userId, prodId, prodQty);

        assertEquals("Product Successfully added to Cart!", result);
    }
    
    @Test
    void testAddProductToCartExistsProduct() throws SQLException {
        String userId = "client@example.com";
        String prodId = "1";
        int prodQty = 23;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Il prodotto non è già nel carrello

        when(preparedStatement.executeUpdate()).thenReturn(1); // Inserimento avvenuto con successo

        String result = cartServiceDAO.addProductToCart(userId, prodId, prodQty);

        assertEquals("Product Successfully Updated to Cart!", result);
    }
    
    @Test
    void testAddProductToCartExistsProductExecuteQueryFailed() throws SQLException {
        String userId = "client@example.com";
        String prodId = "1";
        int prodQty = 23;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Il prodotto non è già nel carrello

        when(preparedStatement.executeUpdate()).thenReturn(0); // Inserimento avvenuto con successo

        String result = cartServiceDAO.addProductToCart(userId, prodId, prodQty);

        assertEquals("Failed to Add into Cart", result);
    }
    
    @Test
    void testGetAllCartItems() throws SQLException {
        String userId = "client@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false); // Simula un solo risultato
        when(resultSet.getString("id")).thenReturn(userId);
        when(resultSet.getString("idFumetto")).thenReturn("1");
        when(resultSet.getInt("quantita")).thenReturn(2);

        List<CartBean> cartItems = cartServiceDAO.getAllCartItems(userId);

        assertNotNull(cartItems);
        assertEquals(1, cartItems.size());
        assertEquals(userId, cartItems.get(0).getUserId());
        assertEquals("1", cartItems.get(0).getProdId());
        assertEquals(2, cartItems.get(0).getQuantity());
    }
    
    @Test
    void testGetAllCartItems_EmptyCart() throws SQLException {
        String userId = "empty@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Nessun risultato

        List<CartBean> cartItems = cartServiceDAO.getAllCartItems(userId);

        assertTrue(cartItems.isEmpty());
    }

    @Test
    void testGetAllCartItems_MultipleItems() throws SQLException {
        String userId = "multi@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false); // Due risultati
        
        // Mock dei valori per le due righe
        when(resultSet.getString("id")).thenReturn(userId, userId);
        when(resultSet.getString("idFumetto")).thenReturn("1", "2");
        when(resultSet.getInt("quantita")).thenReturn(3, 5);

        List<CartBean> cartItems = cartServiceDAO.getAllCartItems(userId);

        assertEquals(2, cartItems.size());
        assertEquals("1", cartItems.get(0).getProdId());
        assertEquals("2", cartItems.get(1).getProdId());
        assertEquals(3, cartItems.get(0).getQuantity());
        assertEquals(5, cartItems.get(1).getQuantity());
    }

    @Test
    void testGetAllCartItems_SQLException() throws SQLException {
        String userId = "error@example.com";

        when(connection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database error"));

        List<CartBean> cartItems = cartServiceDAO.getAllCartItems(userId);

        assertTrue(cartItems.isEmpty());
    }
 
    
    @Test
    void testGetCartCount() throws SQLException {
        String userId = "client@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(5);

        int count = cartServiceDAO.getCartCount(userId);

        assertEquals(5, count);
    }
    
    @Test
    void testGetCartCount_NoItems() throws SQLException {
        String userId = "empty@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Nessun risultato

        int count = cartServiceDAO.getCartCount(userId);

        assertEquals(0, count);
    }

    @Test
    void testGetCartCount_SQLException() throws SQLException {
        String userId = "error@example.com";

        when(connection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database error"));

        int count = cartServiceDAO.getCartCount(userId);

        assertEquals(0, count);
    }
    
    
    @Test
    void testRemoveProductFromCart() throws SQLException {
        String userId = "client@example.com";
        String prodId = "1";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("quantita")).thenReturn(1);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        String status = cartServiceDAO.removeProductFromCart(userId, prodId);

        assertEquals("Product Successfully removed from the Cart!", status);
    }
    
    @Test
    void testRemoveProductFromCart_ProductNotInCart() throws SQLException {
        String userId = "client@example.com";
        String prodId = "999";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Prodotto non trovato

        String status = cartServiceDAO.removeProductFromCart(userId, prodId);

        assertEquals("Product Not Available in the cart!", status);
    }

    @Test
    void testRemoveProductFromCart_UpdateFails() throws SQLException {
        String userId = "client@example.com";
        String prodId = "1";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("quantita")).thenReturn(1);
        when(preparedStatement.executeUpdate()).thenReturn(0); // Update fallito

        String status = cartServiceDAO.removeProductFromCart(userId, prodId);

        assertEquals("Product Removal Failed", status);
    }
    
   
    
    @Test
    void testUpdateProductToCart() throws SQLException {
        String userId = "client@example.com";
        String prodId = "1";
        int prodQty = 5;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        String status = cartServiceDAO.updateProductToCart(userId, prodId, prodQty);

        assertEquals("Product Successfully Updated to Cart!", status);
    }
    @Test
    void testUpdateProductToCart_ProductNotInCart() throws SQLException {
        String userId = "client@example.com";
        String prodId = "999";
        int prodQty = 5;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Prodotto non trovato
        when(preparedStatement.executeUpdate()).thenReturn(1); // Inserimento avvenuto

        String status = cartServiceDAO.updateProductToCart(userId, prodId, prodQty);

        assertEquals("Product Successfully Updated to Cart!", status);
    }

    @Test
    void testUpdateProductToCart_UpdateFails() throws SQLException {
        String userId = "client@example.com";
        String prodId = "1";
        int prodQty = 5;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(preparedStatement.executeUpdate()).thenReturn(0); // Update fallito

        String status = cartServiceDAO.updateProductToCart(userId, prodId, prodQty);

        assertEquals("Failed to Add into Cart", status);
    }
    
    @Test
    void testGetProductCount() throws SQLException {
        String userId = "client@example.com";
        String prodId = "1";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(3);

        int count = cartServiceDAO.getProductCount(userId, prodId);

        assertEquals(3, count);
    }
    @Test
    void testGetProductCount_NoItems() throws SQLException {
        String userId = "client@example.com";
        String prodId = "999";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Nessun risultato

        int count = cartServiceDAO.getProductCount(userId, prodId);

        assertEquals(0, count);
    }

    @Test
    void testGetProductCount_SQLException() throws SQLException {
        String userId = "client@example.com";
        String prodId = "1";

        when(connection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database error"));

        int count = cartServiceDAO.getProductCount(userId, prodId);

        assertEquals(0, count);
    }
    
    @Test
    void testGetCartItemCount() throws SQLException {
        String userId = "client@example.com";
        String itemId = "1";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.wasNull()).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(2);

        int count = cartServiceDAO.getCartItemCount(userId, itemId);
        System.out.println(count);
        assertEquals(2, count);
    }
    @Test
    void testGetCartItemCount_NoItems() throws SQLException {
        String userId = "client@example.com";
        String itemId = "999";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Nessun risultato

        int count = cartServiceDAO.getCartItemCount(userId, itemId);

        assertEquals(0, count);
    }

    @Test
    void testGetCartItemCount_SQLException() throws SQLException {
        String userId = "client@example.com";
        String itemId = "1";

        when(connection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database error"));

        int count = cartServiceDAO.getCartItemCount(userId, itemId);

        assertEquals(0, count);
    }
    
    
    
    
    
    
    
    
    @Test
    void testDeleteAllCartItems() throws SQLException {
        String email = "client@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        cartServiceDAO.deleteAllCartItems(email);

        verify(preparedStatement, times(1)).executeUpdate();
    }
    @Test
    void testDeleteAllCartItems_NoItems() throws SQLException {
        String email = "empty@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0); // Nessuna riga eliminata

        cartServiceDAO.deleteAllCartItems(email);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    
}
