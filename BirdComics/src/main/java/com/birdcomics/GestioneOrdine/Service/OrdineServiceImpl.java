package com.birdcomics.GestioneOrdine.Service;

import com.birdcomics.Model.Bean.FatturaBean;
import com.birdcomics.Model.Bean.OrderBean;
import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.Model.Dao.CartServiceDAO;
import com.birdcomics.Model.Dao.OrderServiceDAO;
import com.birdcomics.Model.Dao.ProductServiceDAO;
import com.birdcomics.Model.Dao.UserServiceDAO;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpSession;

public class OrdineServiceImpl implements OrdineService {

    private static final String CLIENT_ID = "ASuCpKOzpG4whs0IxSBo_DH7Kkq-j-o9bNcikgPghWSkPB-jlaftSbtzCnPEmewvQeqscCgjQ74DMK5T";
    private static final String CLIENT_SECRET = "EJcpDuXi8-B4JvdJc5gTv9p8sioOWWrwpXmFpuSs_ufS293eT6BqHnIEg4HJPPLpMBlulOXCW-01J32S";
    private static final String MODE = "sandbox";

    private OrderServiceDAO orderServiceDAO;
    private ProductServiceDAO productServiceDAO;
    private UserServiceDAO userServiceDAO;
    private CartServiceDAO cartServiceDAO;

    // Costruttore senza dipendenze (non usato direttamente)
    public OrdineServiceImpl() {
    	
            // Inizializza tutte le dipendenze nel costruttore
            this.orderServiceDAO = new OrderServiceDAO();
            this.productServiceDAO = new ProductServiceDAO();
            this.cartServiceDAO = new CartServiceDAO();
            this.userServiceDAO = new UserServiceDAO();
            
        
    }

    // Costruttore con dipendenze iniettate
    public OrdineServiceImpl(OrderServiceDAO orderServiceDAO, ProductServiceDAO productServiceDAO,
                            CartServiceDAO cartServiceDAO, UserServiceDAO userServiceDAO) {
        this.orderServiceDAO = orderServiceDAO;
        this.productServiceDAO = productServiceDAO;
        this.cartServiceDAO = cartServiceDAO;
        this.userServiceDAO = userServiceDAO;
    }

    // Setter methods for each dependency
    public void setOrderServiceDAO(OrderServiceDAO orderServiceDAO) {
        this.orderServiceDAO = orderServiceDAO;
    }

    public void setProductServiceDAO(ProductServiceDAO productServiceDAO) {
        this.productServiceDAO = productServiceDAO;
    }

    public void setCartServiceDAO(CartServiceDAO cartServiceDAO) {
        this.cartServiceDAO = cartServiceDAO;
    }

    public void setUserServiceDAO(UserServiceDAO userServiceDAO) {
        this.userServiceDAO = userServiceDAO;
    }

    @Override
    public String authorizePayment(List<Transaction> product, Payer payer) throws PayPalRESTException {
        Payer payerObj = payer;
        RedirectUrls redirectUrls = getRedirectURLs();
        List<Transaction> listTransaction = product;

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payerObj);
        requestPayment.setIntent("authorize");

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        Payment approvedPayment = requestPayment.create(apiContext);

        return getApprovalLink(approvedPayment);
    }

    private RedirectUrls getRedirectURLs() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/BirdComics/cancel.jsp");
        redirectUrls.setReturnUrl("http://localhost:8080/BirdComics/review_payment");
        return redirectUrls;
    }

    private String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;

        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }

        return approvalLink;
    }

    @Override
    public Payment processaPagamento(String paymentId, String payerId) throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment payment = new Payment().setId(paymentId);

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        return payment.execute(apiContext, paymentExecution);
    }

    @Override
    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return Payment.get(apiContext, paymentId);
    }

    @Override
    public List<OrderBean> getOrdiniNonSpediti() throws SQLException {
        return orderServiceDAO.getAllOrderDetailsNoShipped();  // Chiama il DAO per recuperare gli ordini non spediti
    }

    @Override
    public List<OrderBean> getOrdiniPerUtente(String email) throws SQLException {
        return orderServiceDAO.getAllOrderDetails(email);  // Chiama il DAO per ottenere gli ordini di un utente
    }

    public void creaOrdine(String paymentId, String payerId, String email, HttpSession session) throws SQLException {
        // Ottieni i dettagli dell'utente
        UserBean u = userServiceDAO.getUserDetails(email);

        // Elimina gli articoli dal carrello
        cartServiceDAO.deleteAllCartItems(session, email);

        // Rimuovi il carrello dalla sessione (per assicurarti che non venga visualizzato ancora)
        session.removeAttribute("cart"); // Se il carrello è memorizzato nella sessione

        // Esegui il pagamento tramite PayPal
        Payment payment;
        try {
            payment = this.processaPagamento(paymentId, payerId);
            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0);
            List<Item> items = transaction.getItemList().getItems();

            // Crea la fattura
            FatturaBean fattura = new FatturaBean(22, u.getNome(), u.getCognome(), u.getNumeroTelefono(),
                    u.getIndirizzo().getNomeCitta(), u.getIndirizzo().getVia(), u.getIndirizzo().getNumeroCivico(),
                    u.getIndirizzo().getCap());

            // Crea l'ordine
            OrderBean order = new OrderBean(u.getEmail(), paymentId, "Non Spedito", new java.sql.Date(System.currentTimeMillis()));
            order.setIdFattura(fattura);

            // Aggiungi i fumetti all'ordine
            for (Item item : items) {
                ProductBean product = productServiceDAO.getProductsByID(item.getSku());
                order.addFumetti(product, Integer.valueOf(item.getQuantity()));
            }

            // Aggiungi l'ordine al database
            orderServiceDAO.addOrder(order);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
    }
}
