package com.birdcomics.GestioneProfili;

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
		String userName = (String) request.getSession().getAttribute("username");
		String password = (String) request.getSession().getAttribute("password");

		String fullName = request.getParameter("fullName");
		long phone = Long.parseLong(request.getParameter("phone")); // Convert to long
		String address = request.getParameter("address");
		int pinCode = Integer.parseInt(request.getParameter("pinCode")); // Convert to int

		// Retrieve user from session

		if (userName != null) {
			UserServiceDAO dao = new UserServiceDAO();
			try {
				dao.updateUserDetails(userName, password, fullName, phone, address, pinCode);		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Redirect back to userProfile.jsp
			  request.getRequestDispatcher("UserProfileServlet").forward(request, response);
		}
	}
}