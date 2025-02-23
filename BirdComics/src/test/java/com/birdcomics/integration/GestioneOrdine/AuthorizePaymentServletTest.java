package com.birdcomics.integration.GestioneOrdine;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.birdcomics.Bean.*;
import com.birdcomics.Dao.*;
import com.birdcomics.GestioneOrdine.Controller.AuthorizePaymentServlet;
import com.birdcomics.GestioneOrdine.Controller.PaymentServices;
import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizePaymentServletTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private CartServiceDAO cartServiceDAO;
    @Mock private ProductServiceDAO productServiceDAO;
    @Mock private UserServiceDAO userServiceDAO;
    @Mock private PaymentServices paymentServices;

    @InjectMocks
    private AuthorizePaymentServlet servlet;

    private final String testUsername = "test@example.com";
    private final UserBean testUser = new UserBean();
    private final List<CartBean> testCartItems = new ArrayList<>();
    private final ProductBean testProduct = new ProductBean();

    
    
    @Mock
    private CartServiceDAO mockCartServiceDAO;
    @Mock
    private ProductServiceDAO mockProductServiceDAO;
    @Mock
    private UserServiceDAO mockUserServiceDAO;
    @Mock
    private PaymentServices mockPaymentServices;
    
    @InjectMocks
    private AuthorizePaymentServlet authorizePaymentServlet;
    
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private HttpSession mockSession;

    
    @Before
    public void setUp() throws Exception {
        // Set up mock data
        testUser.setEmail(testUsername);
        testUser.setNome("John");
        testUser.setCognome("Doe");
        testUser.setIndirizzo(new IndirizzoBean("Roma", "Via Roma", "123", "00100"));
        
        testProduct.setId(1);
        testProduct.setPrice(10.0f);
        
        CartBean cartItem = new CartBean(testUsername, "1", 2);
        testCartItems.add(cartItem);

        // Mock behavior
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn(testUsername);
        when(userServiceDAO.getUserDetails(testUsername)).thenReturn(testUser);
        when(cartServiceDAO.getAllCartItems(testUsername)).thenReturn(testCartItems);
        when(productServiceDAO.getProductsByID("1")).thenReturn(testProduct);
        when(paymentServices.authorizePayment(anyList(), any(Payer.class))).thenReturn("https://paypal.com");
    }

    @Test
    public void testDoPost() throws ServletException, IOException, SQLException, PayPalRESTException {
        // Arrange
        String username = "testuser";
        UserBean user = new UserBean();
        user.setNome("John");
        user.setCognome("Doe");
        user.setEmail("john.doe@example.com");
        user.setNumeroTelefono("123456789");
        
        CartBean cartItem = new CartBean();
        cartItem.setProdId(String.valueOf(1));
        cartItem.setQuantity(2);
        
        ProductBean product = new ProductBean();
        product.setId(1);
        product.setName("Product 1");
        product.setDescription("Description of Product 1");
        product.setPrice(10.0f);

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockSession.getAttribute("username")).thenReturn(username);
        when(mockUserServiceDAO.getUserDetails(username)).thenReturn(user);
        when(mockCartServiceDAO.getAllCartItems(username)).thenReturn(Collections.singletonList(cartItem));
        when(mockProductServiceDAO.getProductsByID(String.valueOf(1))).thenReturn(product);

        // Act
        authorizePaymentServlet.doPost(mockRequest, mockResponse);

        // Assert
        verify(mockPaymentServices).authorizePayment(anyList(), any());
        verify(mockResponse).sendRedirect(anyString());
    }
    @Test
    public void testSuccessfulPayment() throws Exception {
        // Mock necessary methods if needed, e.g.:
        when(request.getSession().getAttribute("username")).thenReturn("testuser");

        // Mock behavior for PaymentServices to simulate successful payment
        String redirectUrl = "https://paypal.com";
        when(paymentServices.authorizePayment(anyList(), any(Payer.class))).thenReturn(redirectUrl);
        
        // Call the servlet's doPost method
        servlet.doPost(request, response);

        // Verify that authorizePayment was called
        verify(paymentServices).authorizePayment(anyList(), any(Payer.class));

        // Verify that the response redirects to the correct URL
        verify(response).sendRedirect(redirectUrl);
    }

    @Test
    public void testEmptyCart() throws Exception {
        // Test case when the cart is empty
        when(cartServiceDAO.getAllCartItems(testUsername)).thenReturn(new ArrayList<>());

        servlet.doPost(request, response);

        // Verify that payment was not attempted
        verify(paymentServices, never()).authorizePayment(anyList(), any(Payer.class));
    }

    @Test
    public void testUserNotFound() throws Exception {
        // Test case when user is not found
        when(userServiceDAO.getUserDetails(testUsername)).thenReturn(null);

        servlet.doPost(request, response);

        // Verify that payment was not attempted
        verify(paymentServices, never()).authorizePayment(anyList(), any(Payer.class));
    }

    @Test
    public void testPayPalError() throws Exception {
        // Test case when PayPal throws an exception
        PayPalRESTException payPalException = new PayPalRESTException("PayPal REST exception");

        // Using Mockito.doThrow() to throw a checked exception
        doThrow(payPalException).when(paymentServices).authorizePayment(anyList(), any(Payer.class));

        servlet.doPost(request, response);

        // Verify that the error page is redirected (you might need to change this based on your actual error handling)
        verify(response).sendRedirect("error.jsp");
    }

}
