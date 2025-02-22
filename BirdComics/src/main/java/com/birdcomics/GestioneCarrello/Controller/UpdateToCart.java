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

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Dao.ProductServiceDAO;
import com.birdcomics.GestioneCarrello.Service.CarelloServiceImpl;

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

        CarelloServiceImpl cartService = new CarelloServiceImpl(); // Usa il servizio CartService

        ProductServiceDAO productDao = new ProductServiceDAO();
        ProductBean product = null;
        try {
            product = productDao.getProductsByID(prodId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int availableQty = 0;
        try {
            availableQty = productDao.getAllQuantityProductsById(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter pw = response.getWriter();
        response.setContentType("text/html");

        if (availableQty < pQty) {
            String status;
            try {
                status = cartService.updateProductInCart(userId, prodId, availableQty);  // Usa il metodo nel servizio
                
            } catch (SQLException e) {
                e.printStackTrace();
            }

            status = "Only " + availableQty + " of " + product.getName()
                    + " are available in the shop! So we are adding only " + availableQty + " products to your cart.";

            RequestDispatcher rd = request.getRequestDispatcher("CartDetailsServlet");
            rd.include(request, response);

            pw.println("<script>document.getElementById('message').innerHTML='" + status + "'</script>");

        } else {
            String status = null;
            try {
                status = cartService.updateProductInCart(userId, prodId, pQty);  // Usa il metodo nel servizio
                
            } catch (SQLException e) {
                e.printStackTrace();
            }

            RequestDispatcher rd = request.getRequestDispatcher("CartDetailsServlet");
            rd.include(request, response);

            pw.println("<script>document.getElementById('message').innerHTML='" + status + "'</script>");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
