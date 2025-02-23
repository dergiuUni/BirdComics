package com.birdcomics.integration.GestioneOrdine;


import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.Bean.*;
import com.birdcomics.Dao.*;
import com.birdcomics.GestioneOrdine.Controller.PaymentServices;
import com.birdcomics.GestioneOrdine.Service.OrdineServiceImpl;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrdineServiceImplTest {

    @Mock
    private OrderServiceDAO orderServiceDAO;
    
    @Mock
    private ProductServiceDAO productServiceDAO;
    
    @Mock
    private UserServiceDAO userServiceDAO;
    
    @Mock
    private CartServiceDAO cartServiceDAO;
    
    @Mock
    private PaymentServices paymentServices;
    
    @InjectMocks
    private OrdineServiceImpl ordineService;
    
    private final String testUsername = "testUser";
    private final String paymentId = "PAY-123";
    private final String payerId = "PAYER-456";
    private final UserBean testUser = new UserBean();
    private final ProductBean testProduct = new ProductBean();

    @Before
    public void setUp() {
        // Configura utente di test
        testUser.setEmail(testUsername);
        testUser.setIndirizzo(new IndirizzoBean("Roma" ,"Via Roma", "123", "00100"));
        
        // Configura prodotto di test
        testProduct.setId(1);
        testProduct.setName("Test Product");
        testProduct.setPrice(10.0f);
    }

    @Test
    public void testGetOrdiniNonSpediti() throws SQLException {
        // Configura mock
        List<OrderBean> expectedOrders = new ArrayList<>();
        when(orderServiceDAO.getAllOrderDetailsNoShipped()).thenReturn(expectedOrders);
        
        // Esegui test
        List<OrderBean> result = ordineService.getOrdiniNonSpediti();
        
        // Verifiche
        assertEquals(expectedOrders, result);
        verify(orderServiceDAO).getAllOrderDetailsNoShipped();
    }

    @Test
    public void testGetOrdiniPerUtente() throws SQLException {
        // Configura mock
        List<OrderBean> expectedOrders = new ArrayList<>();
        when(orderServiceDAO.getAllOrderDetails(testUsername)).thenReturn(expectedOrders);
        
        // Esegui test
        List<OrderBean> result = ordineService.getOrdiniPerUtente(testUsername);
        
        // Verifiche
        assertEquals(expectedOrders, result);
        verify(orderServiceDAO).getAllOrderDetails(testUsername);
    }

    @Test
    public void testProcessPaymentAndCreateOrder_Success() throws Exception {
        // Configura mock
        when(userServiceDAO.getUserDetails(testUsername)).thenReturn(testUser);
        when(productServiceDAO.getProductsByID(anyString())).thenReturn(testProduct);
        
        Payment mockPayment = new Payment();
        Transaction transaction = new Transaction();
        ItemList itemList = new ItemList();
        itemList.setItems(List.of(new Item().setSku("1").setQuantity("2")));
        transaction.setItemList(itemList);
        mockPayment.setTransactions(List.of(transaction));
        
        when(paymentServices.executePayment(paymentId, payerId)).thenReturn(mockPayment);
        
        // Esegui test
        ordineService.processPaymentAndCreateOrder(paymentId, payerId, testUsername);
        
        // Verifiche
        verify(cartServiceDAO).deleteAllCartItems(testUsername);
        verify(orderServiceDAO).addOrder(any(OrderBean.class));
    }

    @Test(expected = SQLException.class)
    public void testProcessPaymentAndCreateOrder_UserNotFound() throws Exception {
        // Configura mock
        when(userServiceDAO.getUserDetails(testUsername)).thenReturn(null);
        
        // Esegui test
        ordineService.processPaymentAndCreateOrder(paymentId, payerId, testUsername);
    }

    @Test
    public void testProcessPaymentAndCreateOrder_PayPalError() throws Exception {
        // Configura mock
        when(userServiceDAO.getUserDetails(testUsername)).thenReturn(testUser);
        when(paymentServices.executePayment(paymentId, payerId))
            .thenThrow(new PayPalRESTException("Error"));
        
        // Esegui test
        ordineService.processPaymentAndCreateOrder(paymentId, payerId, testUsername);
        
        // Verifiche
        verify(orderServiceDAO, never()).addOrder(any());
    }

    @Test
    public void testAuthorizePayment() throws PayPalRESTException {
        // Configura dati di test
        Payer payer = new Payer().setPaymentMethod("paypal");
        Transaction transaction = (Transaction) new Transaction()
            .setAmount(new Amount().setTotal("10.00"))
            .setDescription("Test transaction");
        
        // Esegui test
        String result = ordineService.authorizePayment(
            List.of(transaction), 
            payer
        );
        
        // Verifiche
        assertNotNull(result);
        assertTrue(result.contains("paypal.com"));
    }

    @Test
    public void testExecutePayment() throws PayPalRESTException {
        // Configura mock
        Payment mockPayment = new Payment().setState("approved");
        when(paymentServices.executePayment(paymentId, payerId)).thenReturn(mockPayment);
        
        // Esegui test
        Payment result = ordineService.executePayment(paymentId, payerId);
        
        // Verifiche
        assertEquals("approved", result.getState());
    }

    @Test
    public void testGetPaymentDetails() throws PayPalRESTException {
        // Configura mock
        Payment expectedPayment = new Payment().setId(paymentId);
        when(paymentServices.getPaymentDetails(paymentId)).thenReturn(expectedPayment);
        
        // Esegui test
        Payment result = ordineService.getPaymentDetails(paymentId);
        
        // Verifiche
        assertEquals(paymentId, result.getId());
    }
}