package com.birdcomics.Dao;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Dao.CartServiceDAO;
import com.birdcomics.Utils.DBUtil;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class ProductServiceDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private ProductServiceDAO productServiceDAO;
    private MockedStatic<DBUtil> mockedDBUtil;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        productServiceDAO = new ProductServiceDAO();

        // Mock static DBUtil method
        mockedDBUtil = mockStatic(DBUtil.class);
        when(DBUtil.getConnection()).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        mockedDBUtil.close();
    }

    @Test
    void testAddProduct_Success() throws SQLException {
        String name = "Test Product";
        String description = "Test Description";
        float price = 9.99f;
        String image = "test.jpg";
        String[] genres = {"Action", "Fantasy"};

        // Mock della prima query (inserimento del fumetto)
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1); // Mock fumettoId

        // **Mock della seconda query (inserimento dei generi)**
        PreparedStatement genrePreparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement("insert into Genere_Fumetto (genere, idFumetto) values(?,?)"))
                .thenReturn(genrePreparedStatement);

        // Simula esecuzione senza errori per i generi
        when(genrePreparedStatement.executeUpdate()).thenReturn(1);

        // Esegui il metodo
        String result = productServiceDAO.addProduct(name, description, price, image, genres);

        // Verifica che il metodo funzioni correttamente
        assertEquals("Product Added Successfully", result);

  
    }
    
    @Test
    void testAddProduct_Failure() throws SQLException {
        String name = "Test Product";
        String description = "Test Description";
        float price = 9.99f;
        String image = "test.jpg";
        String[] genres = {"Action", "Fantasy"};

        // Mock della prima query (inserimento del fumetto) che fallisce
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0); // Simula fallimento inserimento

        // Esegui il metodo
        String result = productServiceDAO.addProduct(name, description, price, image, genres);

        // Verifica che il metodo restituisca il messaggio di errore
        assertEquals("Product Registration Failed!", result);
    }

    @Test
    void testRemoveProduct_Success() throws SQLException {
        String prodId = "1";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        String result = productServiceDAO.removeProduct(prodId);
        assertEquals("Fumetto Cancellato!", result);
    }

    @Test
    void testRemoveProduct_Failure() throws SQLException {
        String prodId = "1";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        String result = productServiceDAO.removeProduct(prodId);
        assertEquals("Product Removal Failed!", result);
    }

    @Test
    void testGetAllProducts() throws SQLException {
        // Mocking della connessione e del PreparedStatement
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simuliamo che ci siano 3 prodotti nel ResultSet
        when(resultSet.next()).thenReturn(true, true, true, false); // 3 prodotti, poi stop

        // Dati per i 3 prodotti
        when(resultSet.getInt(1)).thenReturn(1, 2, 3); // ID dei prodotti
        when(resultSet.getString(2)).thenReturn("Test Product 1", "Test Product 2", "Test Product 3"); // Nomi dei prodotti
        when(resultSet.getString(3)).thenReturn("Test Description 1", "Test Description 2", "Test Description 3"); // Descrizioni
        when(resultSet.getFloat(4)).thenReturn(9.99f, 19.99f, 29.99f); // Prezzi
        when(resultSet.getString(5)).thenReturn("test1.jpg", "test2.jpg", "test3.jpg"); // Immagini

        // Chiamata al metodo da testare
        List<ProductBean> products = productServiceDAO.getAllProducts();

        // Verifica che la lista contenga 3 prodotti
        assertEquals(3, products.size());

        // Verifica che i dati dei prodotti siano corretti
        assertEquals("Test Product 1", products.get(0).getName());
        assertEquals("Test Description 1", products.get(0).getDescription());
        assertEquals(9.99f, products.get(0).getPrice());
        assertEquals("test1.jpg", products.get(0).getImage());

        assertEquals("Test Product 2", products.get(1).getName());
        assertEquals("Test Description 2", products.get(1).getDescription());
        assertEquals(19.99f, products.get(1).getPrice());
        assertEquals("test2.jpg", products.get(1).getImage());

        assertEquals("Test Product 3", products.get(2).getName());
        assertEquals("Test Description 3", products.get(2).getDescription());
        assertEquals(29.99f, products.get(2).getPrice());
        assertEquals("test3.jpg", products.get(2).getImage());
    }
    
    @Test
    void testGetAllProductsEmpty() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simuliamo un ResultSet vuoto
        when(resultSet.next()).thenReturn(false); 

        List<ProductBean> products = productServiceDAO.getAllProducts();

        // Dobbiamo assicurarci che la lista sia vuota
        assertNotNull(products);
        assertEquals(0, products.size());
    }
    
    @Test
    void testGetAllQuantityProductsById_Success() throws SQLException {
        // Dati di test
        ProductBean product = new ProductBean();
        product.setId(1); // ID del prodotto che stiamo cercando

        // Mock del PreparedStatement
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Mock del ResultSet
        when(resultSet.next()).thenReturn(true); // Simula che ci sia un risultato
        when(resultSet.getInt(1)).thenReturn(10); // Somma delle quantità (ad esempio, 10)

        // Esegui il metodo
        int result = productServiceDAO.getAllQuantityProductsById(product);

        // Verifica il risultato
        assertEquals(10, result); // Ci aspettiamo che la somma delle quantità sia 10
    }
    
    
    @Test
    void testGetAllQuantityProductsById_NoResult() throws SQLException {
        // Dati di test
        ProductBean product = new ProductBean();
        product.setId(1); // ID del prodotto che stiamo cercando

        // Mock del PreparedStatement
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Mock del ResultSet
        when(resultSet.next()).thenReturn(false); // Nessun risultato

        // Esegui il metodo
        int result = productServiceDAO.getAllQuantityProductsById(product);

        // Verifica che il risultato sia 0, perché non sono state trovate quantità
        assertEquals(0, result);
    }




}
