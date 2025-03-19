package com.birdcomics.Unit.test_gestioneCarrello;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.birdcomics.Model.Bean.CartBean;
import com.birdcomics.Model.Bean.CartItem;
import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.Model.Dao.CartServiceDAO;
import com.birdcomics.Model.Dao.ProductServiceDAO;
import com.birdcomics.GestioneCarrello.Service.CarrelloServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CarrelloServiceImplTest {

    @Mock
    private CartServiceDAO cartServiceDAO;

    @Mock
    private ProductServiceDAO productServiceDAO;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CarrelloServiceImpl carrelloService;

    private final String userEmail = "user@example.com"; // userId è un'email
    private final String prodId = "1"; // ID del prodotto come stringa
    private final ProductBean product = new ProductBean();
    private final CartItem cartItem = new CartItem();
    private final CartBean cartBean = new CartBean(userEmail);

    @Before
    public void setUp() {
        product.setId(Integer.parseInt(prodId)); // ID del prodotto come intero (se necessario)
        product.setName("Test Product");
        product.setPrice(10.0f);

        cartItem.setProdId(prodId); // ID del prodotto come stringa
        cartItem.setQuantity(2);

        cartBean.setCartItems(new ArrayList<>(Arrays.asList(cartItem)));
    }

    @Test
    public void testAddToCart_SufficientQuantity() throws SQLException {
        when(productServiceDAO.getProductsByID(prodId)).thenReturn(product); // prodId è una stringa
        when(productServiceDAO.getAllQuantityProductsById(product)).thenReturn(10); // Quantità disponibile

        carrelloService.aggiungiFumetto(session, userEmail, prodId, 3);

        verify(cartServiceDAO).addProductToCart(session, userEmail, prodId, 3);
    }

    @Test
    public void testAddToCart_ZeroAvailableQuantity() throws SQLException {
        when(productServiceDAO.getProductsByID(prodId)).thenReturn(product); // prodId è una stringa
        when(productServiceDAO.getAllQuantityProductsById(product)).thenReturn(0); // Quantità esaurita

        carrelloService.aggiungiFumetto(session, userEmail, prodId, 3);

        verify(cartServiceDAO, never()).addProductToCart(any(), any(), any(), anyInt());
    }

    @Test
    public void testRemoveFromCart() throws SQLException {
        carrelloService.rimuoviFumetto(session, userEmail, prodId);
        verify(cartServiceDAO).removeProductFromCart(session, userEmail, prodId);
    }

    @Test
    public void testGetCartItems() throws SQLException {
        when(cartServiceDAO.getCartFromSession(session, userEmail)).thenReturn(cartBean);

        List<CartItem> result = carrelloService.visualizzaCarrello(session, userEmail);
        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));
    }

    @Test
    public void testGetProductsFromCart() throws SQLException {
        List<CartItem> cartItems = Arrays.asList(cartItem);
        when(productServiceDAO.getProductsByID(prodId)).thenReturn(product); // prodId è una stringa

        List<ProductBean> products = carrelloService.visualizzaProdottiCarrello(cartItems);
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    public void testCalculateTotalAmount() throws SQLException {
        List<CartItem> cartItems = Arrays.asList(cartItem);
        when(productServiceDAO.getProductsByID(prodId)).thenReturn(product); // prodId è una stringa

        float total = carrelloService.calculateTotalAmount(cartItems);
        assertEquals(20.0f, total, 0.001); // 2 (quantità) * 10.0f (prezzo) = 20.0f
    }

    @Test
    public void testEmptyCart() throws SQLException {
        carrelloService.svuotaCarrello(session, userEmail);
        verify(cartServiceDAO).deleteAllCartItems(session, userEmail);
    }

    @Test
    public void testUpdateProductInCart() throws SQLException {
        when(cartServiceDAO.updateProductToCart(userEmail, prodId, 5)).thenReturn("success");

        String result = carrelloService.modificaQuantita(session, userEmail, prodId, 5);
        assertEquals("success", result);
        verify(cartServiceDAO).updateProductToCart(userEmail, prodId, 5);
    }

    @Test
    public void testLoadCartFromDB() throws SQLException {
        when(cartServiceDAO.loadCartFromDB(session, userEmail)).thenReturn(cartBean);

        CartBean result = carrelloService.loadCartFromDB(session, userEmail);
        assertEquals(cartBean, result);
    }
}