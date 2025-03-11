package com.birdcomics.GestioneCarrello.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.GestioneCarrello.Service.CarrelloService;
import com.birdcomics.GestioneCarrello.Service.CarrelloServiceImpl;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AddToCart")
public class AddToCart extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CarrelloService cartService;

    public AddToCart(CarrelloService cartService) {
        this.cartService = cartService;
    }

    public AddToCart() {
        this.cartService = new CarrelloServiceImpl();
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
        	cartService.aggiungiFumetto(session, userId, prodId, pQty);

            
            // Aggiorna il carrello in sessione
            CartBean cartBean = new CartBean(userId);
            cartBean.setCartItems(cartService.visualizzaCarrello(session,userId));
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

// Se vuoi, possiamo migliorare la gestione degli errori o aggiungere log per il debug! 🚀
