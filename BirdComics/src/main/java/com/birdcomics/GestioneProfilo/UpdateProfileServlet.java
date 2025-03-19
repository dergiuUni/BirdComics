package com.birdcomics.GestioneProfilo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		String email = (String) request.getSession().getAttribute("email");

		// Retrieve user from session

		if (email != null) {
			UserServiceDAO dao = new UserServiceDAO();
			try {
				dao.updateUserDetails(email, password, request.getParameter("fullName"), request.getParameter("cognome"), request.getParameter("telefono"), request.getParameter("nomeCitta"), request.getParameter("via"), Integer.parseInt(request.getParameter("numeroCivico")), request.getParameter("cap"));		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Redirect back to userProfile.jsp
			  request.getRequestDispatcher("UserProfileServlet").forward(request, response);
		}*/
	}
	
}