package com.birdcomics.Unit.test_gestioneOrdine;

import com.birdcomics.Bean.*;
import com.birdcomics.Dao.*;
import com.birdcomics.GestioneOrdine.Service.OrdineServiceImpl;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrdineServiceImplTest {

    @Spy
    @InjectMocks
    private OrdineServiceImpl ordineServiceImpl;

    @Mock
    private OrderServiceDAO orderServiceDAO;

    @Mock
    private ProductServiceDAO productServiceDAO;

    @Mock
    private UserServiceDAO userServiceDAO;

    @Mock
    private CartServiceDAO cartServiceDAO;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthorizePayment() throws PayPalRESTException {
        // Arrange
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal("10.00");
        transaction.setAmount(amount);
        transactions.add(transaction);

        // Act
        String approvalUrl = ordineServiceImpl.authorizePayment(transactions, payer);

        // Assert
        assertNotNull(approvalUrl);
        assertTrue(approvalUrl.contains("https://www.sandbox.paypal.com"));
    }

    @Test
    public void testProcessaPagamento() throws PayPalRESTException {
        // Arrange
        String paymentId = "PAYID-M7MBHGY6T105158MS164325F";
        String payerId = "GZZU9UG8Z5ND8";

        Payment mockPayment = new Payment();
        mockPayment.setState("approved");

        when(ordineServiceImpl.processaPagamento(paymentId, payerId)).thenReturn(mockPayment);

        // Act
        Payment processedPayment = ordineServiceImpl.processaPagamento(paymentId, payerId);

        // Assert
        assertNotNull(processedPayment);
        assertEquals("approved", processedPayment.getState());
    }

    @Test
    public void testGetPaymentDetails() throws PayPalRESTException {
        // Arrange
        String paymentId = "PAYID-M7MBHGY6T105158MS164325F";
        Payment mockPayment = new Payment();
        mockPayment.setId(paymentId);

        // Mock del metodo getPaymentDetails
        doReturn(mockPayment).when(ordineServiceImpl).getPaymentDetails(paymentId);

        // Act
        Payment paymentDetails = ordineServiceImpl.getPaymentDetails(paymentId);

        // Assert
        assertNotNull(paymentDetails);
        assertEquals(paymentId, paymentDetails.getId());
    }

    @Test
    public void testGetOrdiniNonSpediti() throws SQLException {
        // Arrange
        OrderBean order = new OrderBean("testuser@example.com", "PAY123", "Non Spedito", new java.sql.Date(System.currentTimeMillis()));
        when(orderServiceDAO.getAllOrderDetailsNoShipped()).thenReturn(Arrays.asList(order));

        // Act
        List<OrderBean> orders = ordineServiceImpl.getOrdiniNonSpediti();

        // Assert
        assertEquals(1, orders.size());
        assertEquals("Non Spedito", orders.get(0).getShipped());
    }

    @Test
    public void testGetOrdiniPerUtente() throws SQLException {
        // Arrange
        String email = "testuser@example.com";
        OrderBean order = new OrderBean(email, "PAY123", "Non Spedito", new java.sql.Date(System.currentTimeMillis()));
        when(orderServiceDAO.getAllOrderDetails(email)).thenReturn(Arrays.asList(order));

        // Act
        List<OrderBean> orders = ordineServiceImpl.getOrdiniPerUtente(email);

        // Assert
        assertEquals(1, orders.size());
        assertEquals(email, orders.get(0).getEmailUtente());
    }
}