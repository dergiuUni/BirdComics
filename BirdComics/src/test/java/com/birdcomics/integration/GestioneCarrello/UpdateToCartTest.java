package com.birdcomics.integration.GestioneCarrello;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Dao.ProductServiceDAO;
import com.birdcomics.GestioneCarrello.Controller.UpdateToCart;
import com.birdcomics.GestioneCarrello.Service.CarrelloServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UpdateToCartTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private CarrelloServiceImpl cartService;

    @Mock
    private ProductServiceDAO productDao;

    @Mock
    private ProductBean product;

    @Mock
    private PrintWriter printWriter;

    @Mock
    private RequestDispatcher requestDispatcher;

    @InjectMocks
    private UpdateToCart updateToCartServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateToCartServlet = new UpdateToCart();
        when(request.getSession()).thenReturn(session); // Configura il mock della sessione
    }

    @Test
    void testDoGet_SessionExpired() throws ServletException, IOException {
        // Configura il mock della sessione (utente non loggato)
        when(session.getAttribute("email")).thenReturn(null);

        // Esegui il metodo doGet
        updateToCartServlet.doGet(request, response);

        // Verifica che l'utente sia stato reindirizzato alla pagina di login
        verify(response).sendRedirect("login.jsp?message=Session Expired, Login Again!!");
    }

    @Test
    void testDoGet_SuccessfulUpdate() throws Exception {
        // Configura il mock della sessione
        when(session.getAttribute("email")).thenReturn("testuser@example.com");
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("3");

        // Mock dei servizi
        try (
            MockedConstruction<CarrelloServiceImpl> mockedCart = mockConstruction(CarrelloServiceImpl.class,
                (mock, context) -> {
                    // Simula il comportamento di updateProductInCart
                    when(mock.updateProductInCart(any(HttpSession.class), anyString(), anyString(), anyInt())).thenReturn("Success");
                    // Simula il comportamento di loadCartFromDB
                    when(mock.loadCartFromDB(any(HttpSession.class), anyString())).thenReturn(new CartBean("testuser@example.com"));
                });

            MockedConstruction<ProductServiceDAO> mockedDao = mockConstruction(ProductServiceDAO.class,
                (mock, context) -> {
                    ProductBean p = new ProductBean();
                    p.setName("Test Product");
                    when(mock.getProductsByID("123")).thenReturn(p);
                    when(mock.getAllQuantityProductsById(any(ProductBean.class))).thenReturn(10); // Quantità disponibile
                })
        ) {
            // Esegui il metodo doGet
            updateToCartServlet.doGet(request, response);

            // Verifica che il carrello sia stato aggiornato
            CarrelloServiceImpl cartService = mockedCart.constructed().get(0);
            verify(cartService).updateProductInCart(session, "testuser@example.com", "123", 3);

            // Verifica che la risposta sia stata reindirizzata correttamente
            verify(response).sendRedirect("CartDetailsServlet");
        }
    }
    @Test
    void testDoGet_InsufficientQuantity() throws Exception {
        // Configura il mock della sessione
        when(session.getAttribute("email")).thenReturn("testuser@example.com");
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("15"); // Quantità richiesta > disponibile

        // Mock dei servizi
        try (
            MockedConstruction<CarrelloServiceImpl> mockedCart = mockConstruction(CarrelloServiceImpl.class,
                (mock, context) -> {
                    // Simula il comportamento di updateProductInCart
                    when(mock.updateProductInCart(any(HttpSession.class), anyString(), anyString(), anyInt())).thenReturn("Adjusted");
                    // Simula il comportamento di loadCartFromDB
                    when(mock.loadCartFromDB(any(HttpSession.class), anyString())).thenReturn(new CartBean("testuser@example.com"));
                });

            MockedConstruction<ProductServiceDAO> mockedDao = mockConstruction(ProductServiceDAO.class,
                (mock, context) -> {
                    ProductBean p = new ProductBean();
                    p.setName("Test Product");
                    when(mock.getProductsByID("123")).thenReturn(p);
                    when(mock.getAllQuantityProductsById(any(ProductBean.class))).thenReturn(10); // Solo 10 disponibili
                })
        ) {
            // Esegui il metodo doGet
            updateToCartServlet.doGet(request, response);

            // Verifica che la quantità sia stata aggiustata
            CarrelloServiceImpl cartService = mockedCart.constructed().get(0);
            verify(cartService).updateProductInCart(session, "testuser@example.com", "123", 10);

            // Verifica che il messaggio sia stato impostato nella sessione
            verify(session).setAttribute("message", "Only 10 of Test Product are available in the shop! So we are adding only 10 products to your cart.");

            // Verifica che la risposta sia stata reindirizzata correttamente
            verify(response).sendRedirect("CartDetailsServlet");
        }
    }

    @Test
    void testDoGet_SQLException() throws Exception {
        // Configura il mock della sessione
        when(session.getAttribute("email")).thenReturn("testuser@example.com");
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("2");

        // Mock dei servizi con eccezione SQL
        try (
            MockedConstruction<CarrelloServiceImpl> mockedCart = mockConstruction(CarrelloServiceImpl.class,
                (mock, context) -> {
                    doThrow(new SQLException("Database error")).when(mock).updateProductInCart(any(HttpSession.class), anyString(), anyString(), anyInt());
                });

            MockedConstruction<ProductServiceDAO> mockedDao = mockConstruction(ProductServiceDAO.class,
                (mock, context) -> {
                    ProductBean p = new ProductBean();
                    p.setName("Test Product");
                    when(mock.getProductsByID("123")).thenReturn(p);
                    when(mock.getAllQuantityProductsById(any(ProductBean.class))).thenReturn(10);
                })
        ) {
            // Esegui il metodo doGet
            updateToCartServlet.doGet(request, response);

            // Verifica che l'utente sia stato reindirizzato alla pagina di errore
            verify(response).sendRedirect("error.jsp?message=Error updating cart.");
        }
    }

    @Test
    void testDoPost_CallsDoGet() throws Exception {
        // Configura il mock della sessione
        when(session.getAttribute("email")).thenReturn("testuser@example.com");
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("2");

        // Mock dei servizi
        try (
            MockedConstruction<CarrelloServiceImpl> mockedCart = mockConstruction(CarrelloServiceImpl.class,
                (mock, context) -> {
                    // Simula il comportamento di updateProductInCart
                    when(mock.updateProductInCart(any(HttpSession.class), anyString(), anyString(), anyInt())).thenReturn("Success");
                    // Simula il comportamento di loadCartFromDB
                    when(mock.loadCartFromDB(any(HttpSession.class), anyString())).thenReturn(new CartBean("testuser@example.com"));
                });

            MockedConstruction<ProductServiceDAO> mockedDao = mockConstruction(ProductServiceDAO.class,
                (mock, context) -> {
                    ProductBean p = new ProductBean();
                    p.setName("Test Product");
                    when(mock.getProductsByID("123")).thenReturn(p);
                    when(mock.getAllQuantityProductsById(any(ProductBean.class))).thenReturn(10);
                })
        ) {
            // Esegui il metodo doPost
            updateToCartServlet.doPost(request, response);

            // Verifica che doGet sia stato chiamato
            verify(request).getParameter("pid");
            verify(request).getParameter("pqty");

            // Verifica che la risposta sia stata reindirizzata correttamente
            verify(response).sendRedirect("CartDetailsServlet");
        }
    }
}