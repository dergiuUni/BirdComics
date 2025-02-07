package com.birdcomics.Dao;
import org.junit.jupiter.api.*;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import org.mockito.MockedStatic;

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Utils.DBUtil;

public class CartServiceDAOTest {

    @Mock
    private Connection mockCon;  // Mock per la connessione
    @Mock
    private PreparedStatement mockSelectStmt;  // Mock per il PreparedStatement della SELECT
    @Mock
    private PreparedStatement mockInsertStmt;  // Mock per il PreparedStatement dell'INSERT
    @Mock
    private ResultSet mockRs;  // Mock per il ResultSet

    @Mock
    private ProductServiceDAO productServiceDAOMock;  // Mock per il ProductServiceDAO
    @Mock
    private ProductBean productBeanMock;  // Mock per il ProductBean

    private CartServiceDAO cartServiceDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        cartServiceDAO = new CartServiceDAO();  // Istanza della classe da testare

        // Mock di DBUtil.getConnection() per restituire la connessione mockata
        try (MockedStatic<DBUtil> dbUtilMock = Mockito.mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(mockCon);

            // Mock della SELECT query per verificare se il prodotto è già nel carrello
            when(mockCon.prepareStatement("SELECT * FROM CarrelloCliente WHERE id=? AND idFumetto=?"))
                .thenReturn(mockSelectStmt);
            when(mockSelectStmt.executeQuery()).thenReturn(mockRs);
            when(mockRs.next()).thenReturn(false);  // Nessun risultato trovato, quindi inseriamo il prodotto

            // Mock dell'INSERT query per aggiungere un prodotto al carrello
            when(mockCon.prepareStatement("INSERT INTO CarrelloCliente VALUES (?, ?, ?)"))
                .thenReturn(mockInsertStmt);
            when(mockInsertStmt.executeUpdate()).thenReturn(1);  // Simula l'inserimento riuscito

            // Mock per ProductServiceDAO e ProductBean
            when(productServiceDAOMock.getProductsByID("1")).thenReturn(productBeanMock);
            when(productBeanMock.isActive()).thenReturn(true);  // Il prodotto è attivo
            when(productBeanMock.getId()).thenReturn(1);
            when(productBeanMock.getName()).thenReturn("Test Product");

            // Quando il CartServiceDAO chiama getProductsByID, simuliamo il comportamento desiderato
            cartServiceDAO = new CartServiceDAO();  // Passiamo il mock del ProductServiceDAO al costruttore
        }
    }

    @Test
    void testAddProductToCart_ProductNotInCart_Success() throws SQLException {
        // Parametri di input
        String userId = "test@sor.c";
        int prodId = 1;
        int prodQty = 3;
        String expectedStatus = "Product Successfully added to Cart!";

        // Chiamata al metodo addProductToCart
        String actualStatus = cartServiceDAO.addProductToCart(userId, String.valueOf(prodId), prodQty);

        // Verifica che il risultato sia quello atteso
        assertEquals(expectedStatus, actualStatus);

        // Verifica che la query SELECT sia stata eseguita correttamente
        verify(mockSelectStmt).setString(1, userId);
        verify(mockSelectStmt).setInt(2, prodId);

        // Verifica che la query INSERT sia stata eseguita correttamente
        verify(mockInsertStmt).setString(1, userId);
        verify(mockInsertStmt).setInt(2, prodId);
        verify(mockInsertStmt).setInt(3, prodQty);

        // Verifica che la connessione sia stata ottenuta e rilasciata correttamente
        try (MockedStatic<DBUtil> dbUtilMock = Mockito.mockStatic(DBUtil.class)) {
            dbUtilMock.verify(DBUtil::getConnection, times(1));
            dbUtilMock.verify(() -> DBUtil.closeConnection(mockCon), times(1));
        }
    }
}
