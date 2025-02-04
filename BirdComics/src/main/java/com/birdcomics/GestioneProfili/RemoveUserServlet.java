package com.birdcomics.GestioneProfili;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RemoveUserServlet
 */
@WebServlet("/RemoveUserServlet")
public class RemoveUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


        String userEmail = request.getParameter("userId");
        String status = "";

        if (userEmail != null && !userEmail.trim().isEmpty()) {
            UserServiceDAO dao = new UserServiceDAO();
            try {
                status = dao.deleteUser(userEmail);
            } catch (SQLException e) {
                status = "Errore di database: " + e.getMessage();
                e.printStackTrace();
            }
        }

        request.setAttribute("message", status);
        RequestDispatcher rd = request.getRequestDispatcher("manageUsers.jsp");
        rd.forward(request, response);
		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}