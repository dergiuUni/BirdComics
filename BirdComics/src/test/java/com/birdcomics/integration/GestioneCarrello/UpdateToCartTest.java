package com.birdcomics.integration.GestioneCarrello;

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Dao.ProductServiceDAO;
import com.birdcomics.GestioneCarrello.Controller.UpdateToCart;
import com.birdcomics.GestioneCarrello.Service.CarelloServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UpdateToCartTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private CarelloServiceImpl cartService;

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
        // Inizializza manualmente i mock
        MockitoAnnotations.openMocks(this);

        // Associa il mock della sessione alla richiesta
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void testDoGet_SessionExpired() throws ServletException, IOException {
        when(session.getAttribute("email")).thenReturn(null);

        updateToCartServlet.doGet(request, response);

        verify(response).sendRedirect("login.jsp?message=Session Expired, Login Again!!");
    }

    @Test
    void testDoGet_SuccessfulUpdate() throws Exception {
        // Mock session e parametri
        when(session.getAttribute("email")).thenReturn("testUser");
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("3");

        // Mock dei servizi
        try (
            MockedConstruction<CarelloServiceImpl> mockedCart = mockConstruction(CarelloServiceImpl.class,
                (mock, context) -> when(mock.updateProductInCart(anyString(), anyString(), anyInt())).thenReturn("Success"));

            MockedConstruction<ProductServiceDAO> mockedDao = mockConstruction(ProductServiceDAO.class,
                (mock, context) -> {
                    ProductBean p = new ProductBean();
                    p.setName("Test Product");
                    when(mock.getProductsByID("123")).thenReturn(p);
                    when(mock.getAllQuantityProductsById(any())).thenReturn(5);
                })
        ) {
            // Mock del writer della risposta
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            when(response.getWriter()).thenReturn(printWriter);

            when(request.getRequestDispatcher("CartDetailsServlet")).thenReturn(requestDispatcher);

            // Esegui
            updateToCartServlet.doGet(request, response);

            // Verifica
            CarelloServiceImpl cartService = mockedCart.constructed().get(0);
            verify(cartService).updateProductInCart("testUser", "123", 3);

            printWriter.flush();
            assertTrue(stringWriter.toString().contains("Success"));
        }
    }

    @Test
    void testDoGet_InsufficientQuantity() throws Exception {
        when(session.getAttribute("email")).thenReturn("testUser");
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("10"); // Quantità > disponibile

        try (
            MockedConstruction<CarelloServiceImpl> mockedCart = mockConstruction(CarelloServiceImpl.class,
                (mock, context) -> when(mock.updateProductInCart(anyString(), anyString(), anyInt())).thenReturn("Adjusted"));

            MockedConstruction<ProductServiceDAO> mockedDao = mockConstruction(ProductServiceDAO.class,
                (mock, context) -> {
                    ProductBean p = new ProductBean();
                    p.setName("Test Product");
                    when(mock.getProductsByID("123")).thenReturn(p);
                    when(mock.getAllQuantityProductsById(any())).thenReturn(5); // Disponibili solo 5
                })
        ) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            when(response.getWriter()).thenReturn(printWriter);
            when(request.getRequestDispatcher("CartDetailsServlet")).thenReturn(requestDispatcher);

            updateToCartServlet.doGet(request, response);

            // Verifica la quantità aggiustata
            CarelloServiceImpl cartService = mockedCart.constructed().get(0);
            verify(cartService).updateProductInCart("testUser", "123", 5);

            printWriter.flush();
            String output = stringWriter.toString();
            assertTrue(output.contains("Only 5 of Test Product"));
        }
    }

    @Test
    void testDoGet_SQLException() throws Exception {
        when(session.getAttribute("email")).thenReturn("testUser");
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("2");

        try (
            MockedConstruction<CarelloServiceImpl> mockedCart = mockConstruction(CarelloServiceImpl.class,
                (mock, context) -> when(mock.updateProductInCart(anyString(), anyString(), anyInt())).thenThrow(new SQLException("DB Error")));

            MockedConstruction<ProductServiceDAO> mockedDao = mockConstruction(ProductServiceDAO.class,
                (mock, context) -> {
                    when(mock.getProductsByID("123")).thenReturn(new ProductBean());
                    when(mock.getAllQuantityProductsById(any())).thenReturn(5);
                })
        ) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            when(response.getWriter()).thenReturn(printWriter);

            updateToCartServlet.doGet(request, response);

            printWriter.flush();
            assertTrue(stringWriter.toString().contains("null")); // Stato null dall'eccezione
        }
    }

    @Test
    void testDoPost_CallsDoGet() throws ServletException, IOException {
        when(session.getAttribute("email")).thenReturn("testUser");
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("pqty")).thenReturn("2");

        try (
            MockedConstruction<CarelloServiceImpl> ignoredCart = mockConstruction(CarelloServiceImpl.class);
            MockedConstruction<ProductServiceDAO> ignoredDao = mockConstruction(ProductServiceDAO.class)
        ) {
            updateToCartServlet.doPost(request, response);
            verify(request).getParameter("pid"); // Verifica che doGet sia stato chiamato
        }
    }
}