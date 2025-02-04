package com.birdcomics.GestioneCarrello.Controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.GestioneCarrello.CartBean;
import com.birdcomics.GestioneCarrello.Service.CartService;
import com.birdcomics.GestioneCarrello.Service.CartServiceImpl;
import com.birdcomics.GestioneCatalogo.ProductBean;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/CartDetailsServlet")
public class CartDetailsServlet extends HttpServlet {
    private CartService cartService;

    // Costruttore per iniezione di dipendenze (usato nei test)
    public CartDetailsServlet(CartService cartService) {
        this.cartService = cartService;
    }

    public CartDetailsServlet() {
        this.cartService = new CartServiceImpl();  // Inizializza con l'implementazione di default
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");

        // Verifica le credenziali dell'utente
        String userName = (String) request.getSession().getAttribute("username");
        String password = (String) request.getSession().getAttribute("password");

        if (userName == null || password == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
            return;
        }

        String addS = request.getParameter("add");

        if (addS != null) {
            int add = Integer.parseInt(addS);
            String uid = request.getParameter("uid");
            String pid = request.getParameter("pid");
            int avail = Integer.parseInt(request.getParameter("avail"));
            int cartQty = Integer.parseInt(request.getParameter("qty"));

            if (add == 1) {
                // Aggiungi prodotto al carrello
                cartQty += 1;
                if (cartQty <= avail) {
                    cartService.addToCart(uid, pid, 1);
                } else {
                    response.sendRedirect("./AddtoCart?pid=" + pid + "&pqty=" + cartQty);
                    return;
                }
            } else if (add == 0) {
                // Rimuovi prodotto dal carrello
                cartService.removeFromCart(uid, pid);
            }
        }

        // Ottieni gli articoli del carrello
        List<CartBean> cartItems = cartService.getCartItems(userName);
        List<ProductBean> products = cartService.getProductsFromCart(cartItems);

        // Calcola il totale
        float totAmount = cartService.calculateTotalAmount(cartItems);

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("productItems", products);
        request.setAttribute("totAmount", totAmount);
        request.getRequestDispatcher("/cartDetails.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
