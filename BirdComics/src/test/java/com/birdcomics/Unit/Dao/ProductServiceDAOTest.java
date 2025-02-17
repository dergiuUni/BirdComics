package com.birdcomics.Unit.Dao;

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Dao.ProductServiceDAO;
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

        // Mock dei metodi statici di DBUtil
        mockedDBUtil = mockStatic(DBUtil.class);
        when(DBUtil.getConnection()).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        mockedDBUtil.close();
    }

    @Test
    void testAddProduct() throws SQLException {
        String name = "Test Product";
        String description = "Test Description";
        float price = 10.0f;
        String image = "test_image.jpg";
        String[] selectedGenres = {"Action", "Adventure"};

        // Configura il mock per il PreparedStatement principale
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
            .thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        // Configura il mock per il PreparedStatement dei generi
        PreparedStatement genrePsMock = mock(PreparedStatement.class);
        when(connection.prepareStatement("insert into Genere_Fumetto (genere, idFumetto) values(?,?)"))
            .thenReturn(genrePsMock);
        when(genrePsMock.executeUpdate()).thenReturn(1);

        String status = productServiceDAO.addProduct(name, description, price, image, selectedGenres);

        assertEquals("Product Added Successfully", status);

        // Verifica che il PreparedStatement dei generi sia stato chiamato correttamente
        verify(genrePsMock, times(2)).setString(anyInt(), anyString());
        verify(genrePsMock, times(2)).setInt(anyInt(), anyInt());
        verify(genrePsMock, times(2)).executeUpdate();
        verify(genrePsMock, times(2)).close();
    }
    
    @Test
    void testRemoveProduct() throws SQLException {
        String prodId = "1";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        String status = productServiceDAO.removeProduct(prodId);

        assertEquals("Fumetto Cancellato!", status);
    }

    @Test
    void testGetAllProducts() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id")).thenReturn(1, 2);
        when(resultSet.getString("nome")).thenReturn("Product 1", "Product 2");
        when(resultSet.getString("descrizione")).thenReturn("Description 1", "Description 2");
        when(resultSet.getFloat("prezzo")).thenReturn(10.0f, 20.0f);
        when(resultSet.getString("immagine")).thenReturn("image1.jpg", "image2.jpg");
        when(resultSet.getString("genere")).thenReturn("Action", "Adventure");

        List<ProductBean> products = productServiceDAO.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getName());
        assertEquals("Product 2", products.get(1).getName());
    }

    @Test
    void testGetAllProductsByType() throws SQLException {
        String type = "Action";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("nome")).thenReturn("Product 1");
        when(resultSet.getString("descrizione")).thenReturn("Description 1");
        when(resultSet.getFloat("prezzo")).thenReturn(10.0f);
        when(resultSet.getString("immagine")).thenReturn("image1.jpg");
        when(resultSet.getString("genere")).thenReturn("Action");

        List<ProductBean> products = productServiceDAO.getAllProductsByType(type);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product 1", products.get(0).getName());
    }

    @Test
    void testSearchAllProducts() throws SQLException {
        String search = "Action";

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("nome")).thenReturn("Product 1");
        when(resultSet.getString("descrizione")).thenReturn("Description 1");
        when(resultSet.getFloat("prezzo")).thenReturn(10.0f);
        when(resultSet.getString("immagine")).thenReturn("image1.jpg");
        when(resultSet.getString("genere")).thenReturn("Action");

        List<ProductBean> products = productServiceDAO.searchAllProducts(search);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product 1", products.get(0).getName());
    }

    @Test
    void testGetProductsByID() throws SQLException {
        String idString = "1";

        // Configura il mock per il PreparedStatement
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        // Simula il comportamento del ResultSet
        when(resultSet.next()).thenReturn(true, true, false); // Due righe: una per il prodotto e una per il genere
        when(resultSet.getInt("id")).thenReturn(1); // ID del prodotto
        when(resultSet.getString("nome")).thenReturn("Product 1"); // Nome del prodotto
        when(resultSet.getString("descrizione")).thenReturn("Description 1"); // Descrizione del prodotto
        when(resultSet.getFloat("prezzo")).thenReturn(10.0f); // Prezzo del prodotto
        when(resultSet.getString("immagine")).thenReturn("image1.jpg"); // Immagine del prodotto
        when(resultSet.getString("genere")).thenReturn("Action", "Adventure"); // Generi associati

        ProductBean product = productServiceDAO.getProductsByID(idString);

        assertNotNull(product);
        assertEquals("Product 1", product.getName());
        assertEquals(2, product.getGeneri().size()); // Verifica che ci siano due generi
    }
    
    
    @Test
    void testGetAllQuantityProductsById() throws SQLException {
        ProductBean product = new ProductBean();
        product.setId(1);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(10);

        int quantity = productServiceDAO.getAllQuantityProductsById(product);

        assertEquals(10, quantity);
    }
}