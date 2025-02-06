package com.birdcomics.GestioneProfili;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Dao.UserServiceDAO;

/**
 * Servlet implementation class RemoveUserServlet
 */
@WebServlet("/RemoveUserServlet")
public class RemoveUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userEmail = request.getParameter("userId");
        String status = null;

        if (userEmail != null && !userEmail.trim().isEmpty()) {
            UserServiceDAO dao = new UserServiceDAO();
            try {
                dao.deleteUser(userEmail);
                // Fetch the updated list of users after deletion
                
                //List<UserBean> updatedUserList = dao.getUsersByRole("GestoreMagazzino"); // Assuming you have a method like this in your DAO
                //request.setAttribute("gestoriMagazzino", updatedUserList); // Set the updated list
            } catch (SQLException e) {
                status = "Errore di database: " + e.getMessage();
                e.printStackTrace();
            }
        }

        RequestDispatcher rd = request.getRequestDispatcher("/GestioneUtentiServlet");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

