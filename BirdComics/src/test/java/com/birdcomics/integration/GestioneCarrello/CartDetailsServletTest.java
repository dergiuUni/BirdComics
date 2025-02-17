package com.birdcomics.integration.GestioneCarrello;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.GestioneCarrello.Controller.CartDetailsServlet;
import com.birdcomics.GestioneCarrello.Service.CarrelloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CartDetailsServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CarrelloService cartService;

    @InjectMocks
    private CartDetailsServlet cartDetailsServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartDetailsServlet = new CartDetailsServlet(cartService); // Usa il costruttore con iniezione di dipendenze
    }

    @Test
    void testProcessRequest_SessionExpired() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione (utente non loggato)
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn(null);

        // Esegui il metodo processRequest
        cartDetailsServlet.processRequest(request, response);

        // Verifica che la risposta sia stata reindirizzata alla pagina di login
        verify(response).sendRedirect("login.jsp?message=Session Expired, Login Again!!");
    }

    @Test
    void testProcessRequest_AddProductToCart() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");

        // Configura i parametri della richiesta per aggiungere un prodotto
        when(request.getParameter("add")).thenReturn("1");
        when(request.getParameter("uid")).thenReturn("testUser");
        when(request.getParameter("pid")).thenReturn("123");
        when(request.getParameter("avail")).thenReturn("10");
        when(request.getParameter("qty")).thenReturn("2");

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/cartDetails.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo processRequest
        cartDetailsServlet.processRequest(request, response);

        // Verifica che il prodotto sia stato aggiunto al carrello
        verify(cartService).addToCart("testUser", "123", 1);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testProcessRequest_RemoveProductFromCart() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");

        // Configura i parametri della richiesta per rimuovere un prodotto
        when(request.getParameter("add")).thenReturn("0");
        when(request.getParameter("uid")).thenReturn("testUser");
        when(request.getParameter("pid")).thenReturn("123");

        // Configura i parametri aggiuntivi per evitare NumberFormatException
        when(request.getParameter("avail")).thenReturn("10"); // Valore fittizio
        when(request.getParameter("qty")).thenReturn("2");   // Valore fittizio

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/cartDetails.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo processRequest
        cartDetailsServlet.processRequest(request, response);

        // Verifica che il prodotto sia stato rimosso dal carrello
        verify(cartService).removeFromCart("testUser", "123");

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testProcessRequest_GetCartDetails() throws ServletException, IOException, SQLException {
        // Configura il mock della sessione
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");

        // Configura i parametri della richiesta (nessuna azione di aggiunta/rimozione)
        when(request.getParameter("add")).thenReturn(null);

        // Simula gli articoli del carrello e i prodotti
        List<CartBean> cartItems = new ArrayList<>();
        CartBean cartItem = new CartBean();
        cartItem.setUserId("testUser");
        cartItem.setProdId("123");
        cartItem.setQuantity(2);
        cartItems.add(cartItem);

        List<ProductBean> products = new ArrayList<>();
        ProductBean product = new ProductBean();
        product.setId(123);
        product.setName("Fumetto 1");
        product.setPrice(10.0f);
        products.add(product);

        when(cartService.getCartItems("testUser")).thenReturn(cartItems);
        when(cartService.getProductsFromCart(cartItems)).thenReturn(products);
        when(cartService.calculateTotalAmount(cartItems)).thenReturn(20.0f);

        // Configura il mock del RequestDispatcher
        when(request.getRequestDispatcher("/cartDetails.jsp")).thenReturn(requestDispatcher);

        // Esegui il metodo processRequest
        cartDetailsServlet.processRequest(request, response);

        // Verifica che gli attributi siano stati impostati correttamente
        verify(request).setAttribute("cartItems", cartItems);
        verify(request).setAttribute("productItems", products);
        verify(request).setAttribute("totAmount", 20.0f);

        // Verifica che il RequestDispatcher sia stato chiamato
        verify(requestDispatcher).forward(request, response);
    }


}