package com.birdcomics.GestioneCarrello.Controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Model.Bean.CartBean;
import com.birdcomics.Model.Bean.CartItem;
import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.GestioneCarrello.Service.CarrelloService;
import com.birdcomics.GestioneCarrello.Service.CarrelloServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/CartDetailsServlet")
public class CartDetailsServlet extends HttpServlet {
    private CarrelloService cartService;

    // Costruttore predefinito
    public CartDetailsServlet() {
        this.cartService = new CarrelloServiceImpl(); // Inizializza con l'implementazione predefinita
    }

    // Costruttore con iniezione di dipendenze
    public CartDetailsServlet(CarrelloService cartService) {
        this.cartService = cartService;
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();

        // Recupera l'email dell'utente dalla sessione
        String email = (String) session.getAttribute("email");

        if (email == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
            return;
        }

        // Verifica se il carrello è già presente nella sessione
        CartBean cartBean = (CartBean) session.getAttribute("cart");

        if (cartBean == null) {
            // Se il carrello non esiste nella sessione, caricalo dal database
            cartBean = cartService.loadCartFromDB(session, email);
            if (cartBean == null) {
                // Se il carrello è ancora null, crea un nuovo carrello
                cartBean = new CartBean(email);
            }
            session.setAttribute("cart", cartBean);  // Memorizza il carrello nella sessione
        }

        try {
            String addS = request.getParameter("add");

            if (addS != null) {
                int add = Integer.parseInt(addS);
                String pid = request.getParameter("pid");
                int avail = Integer.parseInt(request.getParameter("avail"));
                int cartQty = Integer.parseInt(request.getParameter("qty"));

                if (add == 1) {
                    cartQty += 1;
                    if (cartQty <= avail) {
                        cartService.aggiungiFumetto(session, email, pid, 1);
                    } else {
                        response.sendRedirect("./AddtoCart?pid=" + pid + "&pqty=" + cartQty);
                        return;
                    }
                }
            }

            List<CartItem> cartItems = cartBean.getCartItems();
            List<ProductBean> products = cartService.visualizzaProdottiCarrello(cartItems);
            float totAmount = cartService.calculateTotalAmount(cartItems);

            // Imposta gli attributi per la visualizzazione nella JSP
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("productItems", products);
            request.setAttribute("totAmount", totAmount);
            request.getRequestDispatcher("/cartDetails.jsp").forward(request, response);
        } catch (SQLException e) {
            // Gestisci l'eccezione SQL
            response.sendRedirect("error.jsp");
        }
    }

    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException e) {
            response.sendRedirect("error.jsp");
        }
    }

    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException e) {
            response.sendRedirect("error.jsp");
        }
    
}

}
