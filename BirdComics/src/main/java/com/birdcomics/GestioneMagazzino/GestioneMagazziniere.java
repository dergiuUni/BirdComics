package com.birdcomics.GestioneMagazzino;

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
import com.birdcomics.GestioneProfili.UserBean;
import com.birdcomics.GestioneProfili.UserServiceDAO;

@WebServlet("/GestioneMagazziniere")
public class GestioneMagazziniere extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ScaffaleDao prodDao = new ScaffaleDao();
        List<ScaffaliBean> products = new ArrayList<>();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String username = (String) httpRequest.getSession().getAttribute("username");
        String message = "";

        try {
        	UserBean u = new UserBean();
        	u.setEmail(username);
        	UserServiceDAO us = new UserServiceDAO();
        	u = us.getUserDetails(u.getEmail());
        	
            products = prodDao.getScaffaleMagazzino(u.getMagazzino().getNome());
            message = "Showing Results ";
            
            if (products.isEmpty()) {
                message = "No items found for the search ";        
            }


            request.setAttribute("message", message);
            request.setAttribute("listaScaffali", products);

        } catch (SQLException e) {
            e.printStackTrace();
            // Gestire l'eccezione in modo appropriato
        }

        request.getRequestDispatcher("listaScaffali.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
