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

import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.Model.Bean.ScaffaliBean;
import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.Model.Dao.OrderServiceDAO;
import com.birdcomics.Model.Dao.ProductServiceDAO;
import com.birdcomics.Model.Dao.ScaffaleDao;
import com.birdcomics.Model.Dao.UserServiceDAO;
import com.birdcomics.GestioneMagazzino.Service.MagazzinoService;
import com.birdcomics.GestioneMagazzino.Service.MagazzinoServiceImpl;

import com.birdcomics.Model.Bean.ScaffaliBean;
import com.birdcomics.Model.Bean.UserBean;


@WebServlet("/GestioneMagazziniere")
public class GestioneMagazziniere extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public MagazzinoService magazzinoService;

    public GestioneMagazziniere() {
        super();
        this.magazzinoService = new MagazzinoServiceImpl();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = (String) request.getSession().getAttribute("email");
        String message = "";

        try {
            List<ScaffaliBean> products = magazzinoService.getScaffaleMagazzino(email);

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

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
