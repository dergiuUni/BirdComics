package com.birdcomics.Dao;

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
    void testAddProductToCart_NewProduct_Success() throws SQLException {
        String userId = "cliente@example.com";
        String prodId = "1";
        int prodQty = 0;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false); // Il prodotto non è già nel carrello

        when(preparedStatement.executeUpdate()).thenReturn(1); // Inserimento avvenuto con successo

        String result = cartServiceDAO.addProductToCart(userId, prodId, prodQty);
        System.out.println(result);

        assertEquals("Product Successfully added to Cart!", result);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetAllCartItems_Success() throws SQLException {
        String userId = "cliente@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn(userId);
        when(resultSet.getString("idFumetto")).thenReturn("1");
        when(resultSet.getInt("quantita")).thenReturn(3);

        List<CartBean> result = cartServiceDAO.getAllCartItems(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getProdId());

        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    void testRemoveProductFromCart_Success() throws SQLException {
        String userId = "cliente@example.com";
        String prodId = "1";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("quantita")).thenReturn(1); // L'ultimo pezzo da rimuovere

        when(preparedStatement.executeUpdate()).thenReturn(1); // Rimozione avvenuta con successo

        String result = cartServiceDAO.removeProductFromCart(userId, prodId);
        System.out.println(preparedStatement);
       

        assertEquals("Product Successfully removed from the Cart!", result);

        verify(preparedStatement, times(1)).executeUpdate();
        System.out.println(times(1)).executeUpdate().toString());
    }

    @Test
    void testGetCartCount_Success() throws SQLException {
        String userId = "cliente@example.com";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(5);

        int result = cartServiceDAO.getCartCount(userId);

        assertEquals(5, result);

        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    void testUpdateProductToCart_Success() throws SQLException {
        String userId = "cliente@example.com";
        String prodId = "1";
        int prodQty = 5;

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Il prodotto è già nel carrello

        when(preparedStatement.executeUpdate()).thenReturn(1); // Aggiornamento avvenuto con successo

        String result = cartServiceDAO.updateProductToCart(userId, prodId, prodQty);

        assertEquals("Product Successfully Updated to Cart!", result);

        verify(preparedStatement, times(1)).executeUpdate();
    }
}
