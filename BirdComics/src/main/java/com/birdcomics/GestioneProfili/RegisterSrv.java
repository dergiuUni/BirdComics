package com.birdcomics.GestioneProfili;

import java.io.IOException;
import java.sql.Date;
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
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		String telefono = request.getParameter("telefono");
		String citta = request.getParameter("nomeCitta");
		String via = request.getParameter("via");
		int numeroCivico = Integer.valueOf(request.getParameter("numeroCivico"));
		String cap = request.getParameter("cap");
		
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String dataNascitaStr = request.getParameter("dataNascita");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // Imposta il fuso orario a UTC per evitare offset
		java.util.Date parsedDate;
		java.sql.Date dataNascita = null;
		try {
			parsedDate = sdf.parse(dataNascitaStr);
			dataNascita = new java.sql.Date(parsedDate.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//System.out.println("Data ricevuta: " + dataNascitaStr);
		//System.out.println("Data convertita: " + dataNascita);
		
		
		String status = "";
		if (password != null && password.equals(confirmPassword)) {

			UserServiceDAO dao = new UserServiceDAO();

			try {
				ArrayList<RuoloBean> ruoli = new ArrayList<RuoloBean>();
				ruoli.add(RuoloBean.Cliente);
				status = dao.registerUser(email, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli);
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
