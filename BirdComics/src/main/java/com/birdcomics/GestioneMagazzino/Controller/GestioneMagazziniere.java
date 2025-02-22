package com.birdcomics.GestioneMagazzino.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Bean.ScaffaliBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.Dao.OrderServiceDAO;
import com.birdcomics.Dao.ProductServiceDAO;
import com.birdcomics.Dao.ScaffaleDao;
import com.birdcomics.Dao.UserServiceDAO;
import com.birdcomics.GestioneMagazzino.Service.MagazzinoService;
import com.birdcomics.GestioneMagazzino.Service.MagazzinoServiceImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Bean.ScaffaliBean;
import com.birdcomics.Bean.UserBean;


@WebServlet("/GestioneMagazziniere")
public class GestioneMagazziniere extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MagazzinoService magazzinoService;

    public GestioneMagazziniere() {
        super();
        this.magazzinoService = new MagazzinoServiceImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = (String) request.getSession().getAttribute("email");
        String message = "";

        try {
            UserBean user = new UserBean();
            user.setEmail(email);
            List<ScaffaliBean> products = magazzinoService.getScaffaleMagazzino(user);

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
