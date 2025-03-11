package com.birdcomics.GestioneCarrello.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Dao.ProductServiceDAO;
import com.birdcomics.GestioneCarrello.Service.CarrelloServiceImpl;

@WebServlet("/UpdateToCart")
public class UpdateToCart extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateToCart() {
        super();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        if (email == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
            return;
        }

        String userId = email;
        String prodId = request.getParameter("pid");
        int pQty = Integer.parseInt(request.getParameter("pqty"));

        CarrelloServiceImpl cartService = new CarrelloServiceImpl();
        ProductServiceDAO productDao = new ProductServiceDAO();
        ProductBean product = null;

        try {
            product = productDao.getProductsByID(prodId);
            int availableQty = productDao.getAllQuantityProductsById(product);

            if (availableQty < pQty) {
                // Se la quantità richiesta supera la disponibilità, aggiorna al massimo disponibile
                pQty = availableQty;
                String status = "Only " + availableQty + " of " + product.getName()
                        + " are available in the shop! So we are adding only " + availableQty + " products to your cart.";
                session.setAttribute("message", status);
            }

            // Aggiorna il carrello nel database
            cartService.modificaQuantita(session, userId, prodId, pQty);

            // Ricarica il carrello dal database e aggiorna la sessione
            CartBean cartBean = cartService.loadCartFromDB(session, userId);
            session.setAttribute("cart", cartBean);

            // Reindirizza alla pagina del carrello
            response.sendRedirect("CartDetailsServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Error updating cart.");
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }


}
