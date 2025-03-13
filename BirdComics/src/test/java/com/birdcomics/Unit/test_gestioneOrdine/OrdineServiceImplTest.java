package com.birdcomics.Unit.test_gestioneOrdine;

import com.birdcomics.Bean.FatturaBean;
import com.birdcomics.Bean.GenereBean;
import com.birdcomics.Bean.IndirizzoBean;
import com.birdcomics.Bean.OrderBean;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.Dao.CartServiceDAO;
import com.birdcomics.Dao.OrderServiceDAO;
import com.birdcomics.Dao.ProductServiceDAO;
import com.birdcomics.Dao.UserServiceDAO;
import com.birdcomics.GestioneOrdine.Controller.PaymentServices;
import com.birdcomics.GestioneOrdine.Service.OrdineServiceImpl;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import javax.servlet.http.HttpSession;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrdineServiceImplTest {

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
    private PaymentServices paymentServices; // Dichiarazione del mock

    @Mock
    private HttpSession session;
    
    private static final String CLIENT_ID = "ASuCpKOzpG4whs0IxSBo_DH7Kkq-j-o9bNcikgPghWSkPB-jlaftSbtzCnPEmewvQeqscCgjQ74DMK5T";
    private static final String CLIENT_SECRET = "EJcpDuXi8-B4JvdJc5gTv9p8sioOWWrwpXmFpuSs_ufS293eT6BqHnIEg4HJPPLpMBlulOXCW-01J32S";
    private static final String MODE = "sandbox"; // PayPal sandbox environment for testing

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inizializzazione dei mock
    }

    @Test
    public void testCreaOrdine_success() throws Exception {
        // Mock delle dipendenze
        OrderServiceDAO mockOrderServiceDAO = mock(OrderServiceDAO.class);
        ProductServiceDAO mockProductServiceDAO = mock(ProductServiceDAO.class);
        UserServiceDAO mockUserServiceDAO = mock(UserServiceDAO.class);
        CartServiceDAO mockCartServiceDAO = mock(CartServiceDAO.class);
        PaymentServices mockPaymentServices = mock(PaymentServices.class);

        OrdineServiceImpl ordineService = new OrdineServiceImpl(mockOrderServiceDAO, mockProductServiceDAO,
                mockCartServiceDAO, mockUserServiceDAO, mockPaymentServices);

        // Mock dell'utente
        UserBean mockUser = new UserBean();
        mockUser.setEmail("user@example.com");
        mockUser.setNome("John");
        mockUser.setCognome("Doe");
        mockUser.setNumeroTelefono("1234567890");
        mockUser.setIndirizzo(new IndirizzoBean("New York", "Main St", "123", "12345"));
        when(mockUserServiceDAO.getUserDetails("user@example.com")).thenReturn(mockUser);

        // Mock del pagamento
        Payment mockPayment = mock(Payment.class);
        Payer mockPayer = mock(Payer.class);
        PayerInfo mockPayerInfo = mock(PayerInfo.class);
        Transaction mockTransaction = mock(Transaction.class);
        ItemList mockItemList = mock(ItemList.class);
        Item mockItem = mock(Item.class);

        // Configura il mock per restituire un oggetto Payment valido
        when(mockPayment.getPayer()).thenReturn(mockPayer);
        when(mockPayer.getPayerInfo()).thenReturn(mockPayerInfo);
        when(mockPayment.getTransactions()).thenReturn(Arrays.asList(mockTransaction));
        when(mockTransaction.getItemList()).thenReturn(mockItemList);
        when(mockItemList.getItems()).thenReturn(Arrays.asList(mockItem));
        when(mockItem.getSku()).thenReturn("1");
        when(mockItem.getQuantity()).thenReturn("2");

        // Configura il mock per restituire un oggetto Payment
        when(mockPaymentServices.executePayment("paymentId", "payerId")).thenReturn(mockPayment);

        // Mock del prodotto
        List<GenereBean> generi = new ArrayList<>();
        generi.add(new GenereBean("Action"));
        ProductBean mockProduct = new ProductBean(1, "Product 1", "Desc 1", 10.0f, "image.jpg", generi);
        when(mockProductServiceDAO.getProductsByID("1")).thenReturn(mockProduct);

        // Mock del DAO per l'ordine
        when(mockOrderServiceDAO.addOrder(any(OrderBean.class))).thenReturn(true); // Usa thenReturn invece di doNothing

        // Chiama il metodo da testare
        ordineService.creaOrdine("paymentId", "payerId", "user@example.com", mock(HttpSession.class));

        // Verifica che addOrder sia stato chiamato una volta
        verify(mockOrderServiceDAO, times(1)).addOrder(any(OrderBean.class));
    }
  
    
    @Test
    public void testAuthorizePayment() {
        // Crea un contesto API con le credenziali PayPal
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        try {
            // Creazione di una lista di oggetti ProductBean (già esistenti nel tuo codice)
            List<ProductBean> products = new ArrayList<>();
            products.add(new ProductBean(1, "Fumetto1", "Description of Fumetto1", 10.00f, "image1.jpg", new ArrayList<>()));

            // Crea una transazione per il primo prodotto
            ProductBean product = products.get(0);
            Transaction transaction = new Transaction();
            
            // Imposta l'importo della transazione
            Amount amount = new Amount();
            amount.setTotal(String.valueOf(product.getPrice())); // Prezzo del prodotto
            amount.setCurrency("USD");  // Moneta (ad esempio USD, cambia a seconda delle tue esigenze)
            transaction.setAmount(amount);

            // Imposta la descrizione della transazione
            transaction.setDescription(product.getDescription());

            // Crea l'oggetto Payment con le informazioni del pagamento
            Payment payment = new Payment()
                    .setIntent("sale")  // Modalità di pagamento, ad esempio "sale" per completare il pagamento
                    .setPayer(new Payer().setPaymentMethod("paypal"))  // Metodo di pagamento tramite PayPal
                    .setTransactions(List.of(transaction))  // Passa solo una transazione
                    .setRedirectUrls(new RedirectUrls()
                            .setCancelUrl("http://www.example.com/cancel")  // URL di annullamento
                            .setReturnUrl("http://www.example.com/return"));  // URL di ritorno dopo pagamento

            // Esegui la creazione del pagamento
            Payment createdPayment = payment.create(apiContext);

            // Ottieni l'URL di approvazione per indirizzare l'utente a PayPal
            String approvalUrl = createdPayment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))  // Filtra l'URL di approvazione
                    .map(Links::getHref)  // Usa Links per ottenere il link
                    .findFirst()
                    .orElse(null);

            // Assicurati che l'URL di approvazione non sia nullo
            assertNotNull(approvalUrl, "Approval URL should not be null");

            // Stampa l'URL di approvazione (per test e debug)
            System.out.println("Approval URL: " + approvalUrl);

        } catch (PayPalRESTException e) {
            // Stampa l'errore se qualcosa va storto
            e.printStackTrace();
            fail("Error occurred while creating PayPal payment: " + e.getMessage());
        }
    }

    @Test
    public void testProcessaPagamento() throws PayPalRESTException {
        // Arrange
        String paymentId = "PAYID-M7JUKCQ4N132373FM351221N"; // Usa l'ID del pagamento valido
        String payerId = "GZZU9UG8Z5ND8"; // Usa l'ID del pagatore valido

        Payment mockPayment = mock(Payment.class);
        when(mockPayment.getState()).thenReturn("approved"); // Simula un pagamento approvato

        // Configura il mock per restituire un oggetto Payment valido
        when(paymentServices.executePayment(paymentId, payerId)).thenReturn(mockPayment);

        // Act
        Payment processedPayment = ordineServiceImpl.processaPagamento(paymentId, payerId);

        // Assert
        assertNotNull(processedPayment); // Verifica che il pagamento sia stato eseguito
        assertEquals("approved", processedPayment.getState()); // Verifica che il pagamento sia approvato
        verify(paymentServices, times(1)).executePayment(paymentId, payerId); // Verifica che il metodo sia stato chiamato
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
