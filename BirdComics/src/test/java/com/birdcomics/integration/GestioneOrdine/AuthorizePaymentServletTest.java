package com.birdcomics.integration.GestioneOrdine;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.Bean.IndirizzoBean;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.Dao.CartServiceDAO;
import com.birdcomics.Dao.ProductServiceDAO;
import com.birdcomics.Dao.UserServiceDAO;
import com.birdcomics.GestioneOrdine.Controller.AuthorizePaymentServlet;
import com.birdcomics.GestioneOrdine.Controller.PaymentServices;
import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class AuthorizePaymentServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private CartServiceDAO cartServiceDAO;

    @Mock
    private ProductServiceDAO productServiceDAO;

    @Mock
    private UserServiceDAO userServiceDAO;

    @Mock
    private PaymentServices paymentServices;

    @InjectMocks
    private AuthorizePaymentServlet authorizePaymentServlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authorizePaymentServlet = new AuthorizePaymentServlet();
    }

    @Test
    void testDoPost_SuccessfulPaymentAuthorization() throws ServletException, IOException, PayPalRESTException, SQLException {
        // Configura il mock della sessione per simulare un utente loggato
        String email = "test@example.com";
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn(email);

        // Configura il mock del UserServiceDAO per restituire un utente
        UserBean user = new UserBean();
        user.setNome("Mario");
        user.setCognome("Rossi");
        user.setEmail(email);
        user.setNumeroTelefono("1234567890");
        user.setIndirizzo(new IndirizzoBean("Roma", "Via Roma", "123", "00100"));
        when(userServiceDAO.getUserDetails(email)).thenReturn(user);

        // Configura il mock del CartServiceDAO per restituire una lista di carrelli
        List<CartBean> cartItems = new ArrayList<>();
        CartBean cartItem = new CartBean();
        cartItem.setProdId("1");
        cartItem.setQuantity(2);
        cartItems.add(cartItem);
        when(cartServiceDAO.getAllCartItems(email)).thenReturn(cartItems);

        // Configura il mock del ProductServiceDAO per restituire un prodotto
        ProductBean product = new ProductBean();
        product.setId(1);
        product.setName("Comic Book");
        product.setDescription("A great comic book");
        product.setPrice(19.99f);
        when(productServiceDAO.getProductsByID("1")).thenReturn(product);

        // Configura il mock di PaymentServices per restituire un URL di approvazione PayPal
        String approvalUrl = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=EC-123456789";
        when(paymentServices.authorizePayment(anyList(), any(Payer.class))).thenReturn(approvalUrl);

        // Esegui il metodo doPost
        try {
            authorizePaymentServlet.doPost(request, response);
        } catch (Exception e) {
            System.err.println("Exception during doPost: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per far fallire il test
        }

        // Verifica che il reindirizzamento all'URL di approvazione PayPal sia stato eseguito
        verify(response).sendRedirect(approvalUrl);
    }

    @Test
    void testDoPost_ExceptionHandling() throws ServletException, IOException, PayPalRESTException, SQLException {
        // Configura il mock della sessione per simulare un utente loggato
        String email = "test@example.com";
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("email")).thenReturn(email);

        // Configura il mock del UserServiceDAO per lanciare un'eccezione
        when(userServiceDAO.getUserDetails(email)).thenThrow(new RuntimeException("Database error"));

        // Esegui il metodo doPost
        authorizePaymentServlet.doPost(request, response);

        // Verifica che il reindirizzamento alla pagina di errore sia stato eseguito
        verify(response).sendRedirect("error.jsp");
    }
}