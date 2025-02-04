package com.birdcomics.GestioneOrdine;

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

@WebServlet("/GestioneOrdiniNonSpediti")
public class GestioneOrdiniNonSpediti extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                
            OrderServiceDAO oDao = new OrderServiceDAO();
            List<OrderBean> products = new ArrayList<>();
            

            String message = "";

            try {
            	
                    products = oDao.getAllOrderDetailsNoShipped();

                if (products.isEmpty()) {
                    message = "No items found ";        
                }


                request.setAttribute("message", message);
                request.setAttribute("products", products);

            } catch (SQLException e) {
                e.printStackTrace();
                // Gestire l'eccezione in modo appropriato
            }

            request.getRequestDispatcher("/visualizzaOrdiniNonSpediti.jsp").forward(request, response);
        }
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
