package com.birdcomics.GestioneCarrello.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.GestioneCarrello.Service.CarrelloService;
import com.birdcomics.GestioneCarrello.Service.CarrelloServiceImpl;
import com.birdcomics.Model.Bean.*;
import com.birdcomics.Model.Dao.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AddToCart")
public class AddToCart extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CarrelloService cartService;
    private ProductServiceDAO productDao;

    public AddToCart(CarrelloService cartService) {
        this.cartService = cartService;
        this.productDao = new ProductServiceDAO();
    }

    public AddToCart() {
        this.cartService = new CarrelloServiceImpl();
        this.productDao = new ProductServiceDAO();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        if (email == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again to Continue!");
            return;
        }

        String userId = email;
        String prodId = request.getParameter("pid");
        int pQty = Integer.parseInt(request.getParameter("pqty"));

        try {
            // Ottieni il prodotto e la quantità disponibile
            ProductBean product = productDao.getProductsByID(prodId);
            int availableQty = productDao.getAllQuantityProductsById(product);

            if (availableQty < pQty) {
                // Se la quantità richiesta supera la disponibilità, aggiorna al massimo disponibile
                pQty = availableQty;
                String status = "Only " + availableQty + " of " + product.getName()
                        + " are available in the shop! So we are adding only " + availableQty + " products to your cart.";
                session.setAttribute("message", status);
            }

            // Aggiungi il fumetto al carrello
            cartService.aggiungiFumetto(session, email, prodId, pQty);

            // Aggiorna il carrello in sessione
            CartBean cartBean = new CartBean(userId);
            cartBean.setCartItems(cartService.visualizzaCarrello(session, email));
            session.setAttribute("cart", cartBean);

            response.sendRedirect("CartDetailsServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Error adding product to cart.");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
