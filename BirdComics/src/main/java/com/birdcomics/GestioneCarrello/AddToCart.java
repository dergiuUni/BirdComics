package com.birdcomics.GestioneCarrello;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.GestioneCatalogo.ProductBean;
import com.birdcomics.GestioneCatalogo.ProductServiceDAO;
import com.birdcomics.GestioneProfili.Cliente.CartServiceDAO;

@WebServlet("/AddToCart")
public class AddToCart extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AddToCart() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
   

        if (userName == null || password == null)  {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again to Continue!");
            return;
        }

        String userId = userName;
        String prodId = request.getParameter("pid");
        int pQty = Integer.parseInt(request.getParameter("pqty"));

        CartServiceDAO cart = new CartServiceDAO();
        ProductServiceDAO productDao = new ProductServiceDAO();
        ProductBean product = null;

        try {
            product = productDao.getProductDetails(prodId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int availableQty = product.getProdQuantity();
        int cartQty = 0;

        try {
            cartQty = cart.getProductCount(userId, prodId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Calcola la quantità totale da aggiungere al carrello
        int totalQtyToAdd = pQty + cartQty;

        // Controlla se la quantità totale supera la disponibilità
        if (totalQtyToAdd > availableQty) {
            // Se la quantità supera la disponibilità, aggiungi solo fino alla disponibilità
            totalQtyToAdd = availableQty;

            try {
                cart.updateProductToCart(userId, prodId, totalQtyToAdd);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String status = "La quantità richiesta di " + product.getProdName() + " non è disponibile al momento. "
                            + "Abbiamo aggiunto solo " + availableQty + " unità al tuo carrello. "
                            + "Ti avviseremo quando il prodotto sarà disponibile nuovamente.";

            // Reindirizza alla pagina del carrello con un messaggio di stato
            response.sendRedirect("cartDetails.jsp?message=" + status);
        } else {
            // Se la quantità totale è disponibile, aggiungi tutto al carrello
            try {
                cart.updateProductToCart(userId, prodId, totalQtyToAdd);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Reindirizza alla pagina del carrello
            response.sendRedirect("CartDetailsServlet");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
