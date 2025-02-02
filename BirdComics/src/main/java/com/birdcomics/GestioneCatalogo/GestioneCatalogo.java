package com.birdcomics.GestioneCatalogo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.GestioneCatalogo.ProductBean;
import com.birdcomics.GestioneCatalogo.ProductServiceDAO;
import com.birdcomics.GestioneOrdine.OrderServiceDAO;

@WebServlet("/GestioneCatalogo")
public class GestioneCatalogo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductServiceDAO prodDao = new ProductServiceDAO();
        List<ProductBean> products = new ArrayList<>();
        

        String search = request.getParameter("search");
        String type = request.getParameter("type");
        String userType = request.getParameter("userType");
        String message = "";

        try {
        	
            if (search != null) {
                products = prodDao.searchAllProductsGestore(search);
                message = "Showing Results for '" + search + "'";
            } else {
                products = prodDao.getAllProducts();
            }

            if (products.isEmpty()) {
                message = "No items found for the search '" + (search != null ? search : type) + "'";        
            }


            request.setAttribute("userType", userType);
            request.setAttribute("message", message);
            request.setAttribute("products", products);

        } catch (SQLException e) {
            e.printStackTrace();
            // Gestire l'eccezione in modo appropriato
        }

        request.getRequestDispatcher("/visualizzaCatalogo.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
