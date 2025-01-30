package com.birdcomics.GestioneCatalogo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import com.google.gson.Gson;

@WebServlet("/ProductListServlet")
public class ProductListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private ProductServiceDAO prodDao;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            prodDao = new ProductServiceDAO();
            String type = request.getParameter("type");
            String search = request.getParameter("search");
            List<ProductBean> products;
            String message;

            if (type != null && !type.isEmpty()) {
                products = prodDao.getAllProductsByType(type);
                message = "Products in " + type;
            } else if (search != null && !search.isEmpty()) {
                products = prodDao.searchAllProducts(search);
                message = "Search results for '" + search + "'";
            } else {
                products = prodDao.getAllProducts();
                message = "All Products";
            }

            if (products.isEmpty()) {
                message = "No products found.";
            }

            // Set attributes on the request to make them available in the JSP
            request.setAttribute("products", products);
            request.setAttribute("message", message);

            // Check if it's an AJAX request
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                // Set content type and write JSON response
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                
                // Create a JSON object or array to send back
                // Here, assuming JSON response format
                Gson gson = new Gson();
                String jsonProducts = gson.toJson(products);
                
                // Write JSON response to the client
                PrintWriter out = response.getWriter();
                out.print(jsonProducts);
                out.flush();
            } else {
                // Forward to your index.jsp for non-AJAX requests
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void destroy() {
        // Close resources like DAO when the servlet is destroyed
    }
}
