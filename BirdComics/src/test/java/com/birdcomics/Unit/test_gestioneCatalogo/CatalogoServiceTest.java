package com.birdcomics.Unit.test_gestioneCatalogo;

import com.birdcomics.Model.Bean.GenereBean;
import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.Model.Dao.ProductServiceDAO;
import com.birdcomics.GestioneCatalogo.Service.CatalogoServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CatalogoServiceTest {

    @Mock
    private ProductServiceDAO productServiceDAO; // Mock del DAO

    @InjectMocks
    private CatalogoServiceImpl catalogoService; // Inietta il mock nel service

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inizializza i mock
    }

    // ===================================================
    // Test per il metodo: getAllProducts
    // ===================================================

    @Test
    void testGetAllProducts_Success() throws SQLException {
        // Configura il mock per restituire una lista di prodotti
        List<ProductBean> mockProducts = new ArrayList<>();
        List<GenereBean> generi1 = new ArrayList<>();
        generi1.add(new GenereBean("Action"));
        List<GenereBean> generi2 = new ArrayList<>();
        generi2.add(new GenereBean("Comedy"));

        mockProducts.add(new ProductBean(1, "Product 1", "Description 1", 10.0f, "image1.jpg", generi1));
        mockProducts.add(new ProductBean(2, "Product 2", "Description 2", 20.0f, "image2.jpg", generi2));

        when(productServiceDAO.getAllProducts()).thenReturn(mockProducts);

        // Esegui il metodo da testare
        List<ProductBean> result = catalogoService.visualizzaCatalogo();

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).getAllProducts();
    }

    @Test
    void testGetAllProducts_SQLException() throws SQLException {
        // Simula un'eccezione SQL
        when(productServiceDAO.getAllProducts()).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare e verifica che l'eccezione venga propagata
        assertThrows(SQLException.class, () -> catalogoService.visualizzaCatalogo());

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).getAllProducts();
    }

    // ===================================================
    // Test per il metodo: getProductById
    // ===================================================

    @Test
    void testGetProductById_Success() throws SQLException {
        // Configura il mock per restituire un prodotto
        List<GenereBean> generi = new ArrayList<>();
        generi.add(new GenereBean("Action"));
        ProductBean mockProduct = new ProductBean(1, "Product 1", "Description 1", 10.0f, "image1.jpg", generi);

        when(productServiceDAO.getProductsByID("1")).thenReturn(mockProduct);

        // Esegui il metodo da testare
        ProductBean result = catalogoService.visualizzaDettagli("1");

        // Verifica il risultato
        assertNotNull(result);
        assertEquals("Product 1", result.getName());
        assertEquals(10.0f, result.getPrice());

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).getProductsByID("1");
    }

    @Test
    void testGetProductById_SQLException() throws SQLException {
        // Simula un'eccezione SQL
        when(productServiceDAO.getProductsByID("1")).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare e verifica che l'eccezione venga propagata
        assertThrows(SQLException.class, () -> catalogoService.visualizzaDettagli("1"));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).getProductsByID("1");
    }

    // ===================================================
    // Test per il metodo: addProduct
    // ===================================================

    @Test
    void testAddProduct_Success() throws SQLException {
        // Configura il mock per restituire un messaggio di successo
        when(productServiceDAO.addProduct("Product 1", "Description 1", 10.0f, "image1.jpg", new String[]{"Action"}))
                .thenReturn("Product Added Successfully");

        // Esegui il metodo da testare
        String result = catalogoService.addFumetto("Product 1", "Description 1", 10.0f, "image1.jpg", new String[]{"Action"});

        // Verifica il risultato
        assertEquals("Product Added Successfully", result);

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).addProduct("Product 1", "Description 1", 10.0f, "image1.jpg", new String[]{"Action"});
    }


    // ===================================================
    // Test per il metodo: searchAllProductsGestore
    // ===================================================

    @Test
    void testSearchAllProductsGestore_Success() throws SQLException {
        // Configura il mock per restituire una lista di prodotti
        List<ProductBean> mockProducts = new ArrayList<>();
        List<GenereBean> generi1 = new ArrayList<>();
        generi1.add(new GenereBean("Action"));
        List<GenereBean> generi2 = new ArrayList<>();
        generi2.add(new GenereBean("Comedy"));

        mockProducts.add(new ProductBean(1, "Product 1", "Description 1", 10.0f, "image1.jpg", generi1));
        mockProducts.add(new ProductBean(2, "Product 2", "Description 2", 20.0f, "image2.jpg", generi2));

        when(productServiceDAO.searchAllProductsGestore("search")).thenReturn(mockProducts);

        // Esegui il metodo da testare
        List<ProductBean> result = catalogoService.ricercaID("search");

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).searchAllProductsGestore("search");
    }

    @Test
    void testSearchAllProductsGestore_SQLException() throws SQLException {
        // Simula un'eccezione SQL
        when(productServiceDAO.searchAllProductsGestore("search")).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare e verifica che l'eccezione venga propagata
        assertThrows(SQLException.class, () -> catalogoService.ricercaID("search"));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).searchAllProductsGestore("search");
    }

    // ===================================================
    // Test per il metodo: getAllProductsByType
    // ===================================================

    @Test
    void testGetAllProductsByType_Success() throws SQLException {
        // Configura il mock per restituire una lista di prodotti
        List<ProductBean> mockProducts = new ArrayList<>();
        List<GenereBean> generi1 = new ArrayList<>();
        generi1.add(new GenereBean("Action"));
        List<GenereBean> generi2 = new ArrayList<>();
        generi2.add(new GenereBean("Comedy"));

        mockProducts.add(new ProductBean(1, "Product 1", "Description 1", 10.0f, "image1.jpg", generi1));
        mockProducts.add(new ProductBean(2, "Product 2", "Description 2", 20.0f, "image2.jpg", generi2));

        when(productServiceDAO.getAllProductsByType("Action")).thenReturn(mockProducts);

        // Esegui il metodo da testare
        List<ProductBean> result = catalogoService.ricercaGenere("Action");

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).getAllProductsByType("Action");
    }

    @Test
    void testGetAllProductsByType_SQLException() throws SQLException {
        // Simula un'eccezione SQL
        when(productServiceDAO.getAllProductsByType("Action")).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare e verifica che l'eccezione venga propagata
        assertThrows(SQLException.class, () -> catalogoService.ricercaGenere("Action"));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).getAllProductsByType("Action");
    }

    // ===================================================
    // Test per il metodo: searchAllProducts
    // ===================================================

    @Test
    void testSearchAllProducts_Success() throws SQLException {
        // Configura il mock per restituire una lista di prodotti
        List<ProductBean> mockProducts = new ArrayList<>();
        List<GenereBean> generi1 = new ArrayList<>();
        generi1.add(new GenereBean("Action"));
        List<GenereBean> generi2 = new ArrayList<>();
        generi2.add(new GenereBean("Comedy"));

        mockProducts.add(new ProductBean(1, "Product 1", "Description 1", 10.0f, "image1.jpg", generi1));
        mockProducts.add(new ProductBean(2, "Product 2", "Description 2", 20.0f, "image2.jpg", generi2));

        when(productServiceDAO.searchAllProducts("search")).thenReturn(mockProducts);

        // Esegui il metodo da testare
        List<ProductBean> result = catalogoService.ricercaTitolo("search");

        // Verifica il risultato
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).searchAllProducts("search");
    }

    @Test
    void testSearchAllProducts_SQLException() throws SQLException {
        // Simula un'eccezione SQL
        when(productServiceDAO.searchAllProducts("search")).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare e verifica che l'eccezione venga propagata
        assertThrows(SQLException.class, () -> catalogoService.ricercaTitolo("search"));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).searchAllProducts("search");
    }

    // ===================================================
    // Test per il metodo: removeProduct
    // ===================================================

    @Test
    void testRemoveProduct_Success() throws SQLException {
        // Configura il mock per restituire un messaggio di successo
        when(productServiceDAO.removeProduct("1")).thenReturn("Product removed successfully!");

        // Esegui il metodo da testare
        String result = catalogoService.rmFumetto("1");

        // Verifica il risultato
        assertEquals("Product removed successfully!", result);

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).removeProduct("1");
    }

    @Test
    void testRemoveProduct_SQLException() throws SQLException {
        // Simula un'eccezione SQL
        when(productServiceDAO.removeProduct("1")).thenThrow(new SQLException("Database error"));

        // Esegui il metodo da testare e verifica che l'eccezione venga propagata
        assertThrows(SQLException.class, () -> catalogoService.rmFumetto("1"));

        // Verifica che il metodo del DAO sia stato chiamato
        verify(productServiceDAO, times(1)).removeProduct("1");
    }
}