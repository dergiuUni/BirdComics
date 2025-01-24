package com.birdcomics.GestioneProfili.Gestore.GestoreCatalogo;

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

@WebServlet("/adminStock")
public class adminStock extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductServiceDAO prodDao = new ProductServiceDAO();
        OrderServiceDAO orderDao = new OrderServiceDAO();
        List<ProductBean> products = new ArrayList<>();
        List<Integer> soldQuantities = new ArrayList<>(); // Array di interi per soldQty

        String search = request.getParameter("search");
        String type = request.getParameter("type");
        String userType = request.getParameter("userType");
        String message = "";

        try {
        	
            if (search != null) {
                products = prodDao.searchAllProducts(search);
                message = "Showing Results for '" + search + "'";
            } else if (type != null) {
                products = prodDao.getAllProductsByType(type);
                message = "Showing Results for '" + type + "'";
            } else {
                products = prodDao.getAllProducts();
            }

            if (products.isEmpty()) {
                message = "No items found for the search '" + (search != null ? search : type) + "'";        
            }

            // Per ciascun prodotto, ottenere il numero di articoli venduti dal DAO
            for (ProductBean product : products) {
                int soldQty = orderDao.countSoldItem(product.getProdId());
                soldQuantities.add(soldQty); // Aggiungi soldQty all'array di interi
            }

            request.setAttribute("userType", userType);
            request.setAttribute("message", message);
            request.setAttribute("products", products);
            request.setAttribute("soldQuantities", soldQuantities); // Passa l'array di soldQty come attributo

        } catch (SQLException e) {
            e.printStackTrace();
            // Gestire l'eccezione in modo appropriato
        }

        request.getRequestDispatcher("/adminStock.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
