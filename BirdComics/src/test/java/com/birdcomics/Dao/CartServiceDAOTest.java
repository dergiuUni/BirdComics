package com.birdcomics.Dao;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Utils.DBUtil;


public class CartServiceDAOTest {



    @Test
    public void testAddProductToCart_ProductNotInCart_Success() throws Exception {
        // ----- Setup dei parametri di input -----
        String userId = "test@sor.c";
        int prodId = 1;
        int prodQty = 3;
        String expectedStatus = "Product Successfully added to Cart!";

        // Mock per la connessione
        Connection mockCon = mock(Connection.class);
        PreparedStatement mockSelectStmt = mock(PreparedStatement.class);
        PreparedStatement mockInsertStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        // Mock di DBUtil.getConnection()
        try (MockedStatic<DBUtil> dbUtilMock = Mockito.mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(mockCon);

            // Mock delle query
            when(mockCon.prepareStatement("SELECT * FROM CarrelloCliente WHERE id=? AND idFumetto=?"))
                .thenReturn(mockSelectStmt);
            when(mockSelectStmt.executeQuery()).thenReturn(mockRs);
            when(mockRs.next()).thenReturn(false);

            // Mock per l'INSERT
            when(mockCon.prepareStatement("INSERT INTO CarrelloCliente VALUES (?, ?, ?)"))
                .thenReturn(mockInsertStmt);
            when(mockInsertStmt.executeUpdate()).thenReturn(1);
            
            // Mock per il ProductServiceDAO
            ProductServiceDAO productServiceDAOMock = mock(ProductServiceDAO.class);
            
            
            // problema qua probabilmente
            ProductBean productBeanMock = mock(ProductBean.class);
            when(productBeanMock.isActive()).thenReturn(true);
            when(productBeanMock.getId()).thenReturn(prodId);
            when(productBeanMock.getName()).thenReturn("Test Product");
            when(productServiceDAOMock.getProductsByID(String.valueOf(prodId))).thenReturn(productBeanMock);

            // Test logic
            CartServiceDAO cartDAO = new CartServiceDAO();
            String actualStatus = cartDAO.addProductToCart(userId, String.valueOf(prodId), prodQty);

            // Verifica
            assertEquals(expectedStatus, actualStatus);

            // Verifica che il PreparedStatement per l'INSERT sia configurato correttamente
            verify(mockInsertStmt).setString(1, userId);
            verify(mockInsertStmt).setInt(prodId, prodQty);
            verify(mockInsertStmt).setInt(3, prodQty);
        }
    }
	
	
	
	
	
	


}
