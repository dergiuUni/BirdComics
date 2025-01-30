package com.birdcomics.GestioneProfili;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.GestioneIndirizzo.IndirizzoBean;


/**
 * Servlet implementation class RegisterSrv
 */
@WebServlet("/RegisterSrv")
public class RegisterSrv extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		String userName = request.getParameter("username");
		Long mobileNo = Long.parseLong(request.getParameter("mobile"));
		String emailId = request.getParameter("email");
		String address = request.getParameter("address");
		int pinCode = Integer.parseInt(request.getParameter("pincode"));
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String status = "";
		if (password != null && password.equals(confirmPassword)) {

			UserServiceDAO dao = new UserServiceDAO();

			try {
				ArrayList<RuoloBean> r = new ArrayList<RuoloBean>();
				r.add(RuoloBean.Cliente);
				status = dao.registerUser(request.getParameter("email"), request.getParameter("password"), request.getParameter("username"), request.getParameter("cognome"), request.getParameter("mobile"), java.sql.Date.valueOf(request.getParameter("dataNascita")), request.getParameter("nomeCitta"), request.getParameter("via"), Integer.valueOf(request.getParameter("numeroCivico")), request.getParameter("cvc"), r);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			status = "Password not matching!";
		}

		RequestDispatcher rd = request.getRequestDispatcher("register.jsp?message=" + status);

		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
