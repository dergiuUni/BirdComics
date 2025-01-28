package com.birdcomics.GestioneCarrello;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.GestioneCatalogo.ProductBean;
import com.birdcomics.GestioneCatalogo.ProductServiceDAO;
import com.birdcomics.GestioneProfili.Cliente.CartBean;
import com.birdcomics.GestioneProfili.Cliente.CartServiceDAO;


@WebServlet("/CartDetailsServlet")
public class CartDetailsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");

        // Check user credentials
        String userName = (String) request.getSession().getAttribute("username");
        String password = (String) request.getSession().getAttribute("password");

        if (userName == null || password == null) {
            response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
            return;
        }

        CartServiceDAO cartService = new CartServiceDAO();
        String addS = request.getParameter("add");

        if (addS != null) {
            int add = Integer.parseInt(addS);
            String uid = request.getParameter("uid");
            String pid = request.getParameter("pid");
            int avail = Integer.parseInt(request.getParameter("avail"));
            int cartQty = Integer.parseInt(request.getParameter("qty"));

            if (add == 1) {
                // Add Product into the cart
                cartQty += 1;
                if (cartQty <= avail) {
                    cartService.addProductToCart(uid, pid, 1);
                } else {
                    response.sendRedirect("./AddtoCart?pid=" + pid + "&pqty=" + cartQty);
                    return;
                }
            } else if (add == 0) {
                // Remove Product from the cart
                cartService.removeProductFromCart(uid, pid);
            }
        }

        // Fetch cart items
        List<CartBean> cartItems = cartService.getAllCartItems(userName);
        List<ProductBean> products = new ArrayList<>();


        // Calculate total amount
        double totAmount = 0;
        for (CartBean item : cartItems) {
            String prodId = item.getProdId();
            int prodQuantity = item.getQuantity();
            ProductBean product = new ProductServiceDAO().getProductsByID(prodId);
            products.add(product);
            double currAmount = product.getPrice() * prodQuantity;
            totAmount += currAmount;
            System.out.println("product:" + product);
        }
        
     


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
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
			processRequest(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
