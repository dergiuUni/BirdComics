package com.birdcomics.GestioneCarrello.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.GestioneCarrello.Service.CartService;
import com.birdcomics.GestioneCarrello.Service.CartServiceImpl;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AddToCart")
public class AddToCart extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CartService cartService;

    // Costruttore per iniezione di dipendenze (usato nel test)
    public AddToCart(CartService cartService) {
        this.cartService = cartService;
    }

    public AddToCart() {
        this.cartService = new CartServiceImpl();  // Inizializza con l'implementazione di default
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");

        if (userName == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again to Continue!");
            return;
        }

        String userId = userName;
        String prodId = request.getParameter("pid");
        int pQty = Integer.parseInt(request.getParameter("pqty"));

        try {
            cartService.addToCart(userId, prodId, pQty);
            response.sendRedirect("CartDetailsServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
