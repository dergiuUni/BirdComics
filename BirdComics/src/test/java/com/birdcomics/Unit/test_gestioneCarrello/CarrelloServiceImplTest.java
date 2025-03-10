package com.birdcomics.Unit.test_gestioneCarrello;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.birdcomics.Bean.CartItem;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Dao.CartServiceDAO;
import com.birdcomics.Dao.ProductServiceDAO;
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

    @InjectMocks
    private CarrelloServiceImpl carelloService;

    private final String userId = "user123";
    private final String prodId = "prod456";
    private final ProductBean product = new ProductBean();
    private final CartItem cartItem = new CartItem();

    @Before
    public void setUp() {
        product.setId(1); // Assuming ID is used as prodId
        product.setName("Test Product");
        product.setPrice(10.0f);

        cartItem.setProdId(prodId);
        cartItem.setQuantity(2);
    }

    @Test
    public void testAddToCart_SufficientQuantity() throws SQLException {
        when(productServiceDAO.getProductsByID(prodId)).thenReturn(product);
        when(productServiceDAO.getAllQuantityProductsById(product)).thenReturn(10); // Available quantity
        when(cartServiceDAO.getProductCount(userId, prodId)).thenReturn(2);

        carelloService.addToCart(userId, prodId, 3);

        verify(cartServiceDAO).updateProductToCart(userId, prodId, 5); // 2 (existing) + 3 (new) = 5
    }

    @Test
    public void testAddToCart_ExceedAvailableQuantity() throws SQLException {
        when(productServiceDAO.getProductsByID(prodId)).thenReturn(product);
        when(productServiceDAO.getAllQuantityProductsById(product)).thenReturn(5); // Available quantity
        when(cartServiceDAO.getProductCount(userId, prodId)).thenReturn(3);

        carelloService.addToCart(userId, prodId, 3);

        verify(cartServiceDAO).updateProductToCart(userId, prodId, 5); // Cannot exceed available quantity
    }

    @Test
    public void testAddToCart_ZeroAvailableQuantity() throws SQLException {
        when(productServiceDAO.getProductsByID(prodId)).thenReturn(product);
        when(productServiceDAO.getAllQuantityProductsById(product)).thenReturn(0); // No available quantity

        carelloService.addToCart(userId, prodId, 3);

        verify(cartServiceDAO, never()).updateProductToCart(any(), any(), anyInt()); // No update should happen
    }

    @Test
    public void testRemoveFromCart() throws SQLException {
        carelloService.removeFromCart(userId, prodId);
        verify(cartServiceDAO).removeProductFromCart(userId, prodId);
    }

    @Test
    public void testGetCartItems() throws SQLException {
        List<CartItem> expected = Arrays.asList(cartItem);
        when(cartServiceDAO.getAllCartItems(userId)).thenReturn(expected);

        List<CartItem> result = carelloService.getCartItems(userId);
        assertEquals(expected, result);
    }

    @Test
    public void testGetProductsFromCart() throws SQLException {
        List<CartItem> cartItems = Arrays.asList(cartItem);
        when(productServiceDAO.getProductsByID(prodId)).thenReturn(product);

        List<ProductBean> products = carelloService.getProductsFromCart(cartItems);
        assertEquals(1, products.size());
        assertEquals(product, products.get(0));
    }

    @Test
    public void testCalculateTotalAmount() throws SQLException {
        List<CartItem> cartItems = Arrays.asList(cartItem);
        when(productServiceDAO.getProductsByID(prodId)).thenReturn(product);

        float total = carelloService.calculateTotalAmount(cartItems);
        assertEquals(20.0f, total, 0.001); // 2 (quantity) * 10.0f (price) = 20.0f
    }

    @Test
    public void testEmptyCart() throws SQLException {
        carelloService.emptyCart(userId);
        verify(cartServiceDAO).deleteAllCartItems(userId);
    }

    @Test
    public void testUpdateProductInCart() throws SQLException {
        when(cartServiceDAO.updateProductToCart(userId, prodId, 5)).thenReturn("success");

        String result = carelloService.updateProductInCart(userId, prodId, 5);
        assertEquals("success", result);
        verify(cartServiceDAO).updateProductToCart(userId, prodId, 5);
    }
}