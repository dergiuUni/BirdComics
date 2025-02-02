package com.birdcomics.GestioneCatalogo;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/RemoveProductSrv")
public class RemoveProductSrv extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RemoveProductSrv() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String prodId = request.getParameter("prodid");

		ProductServiceDAO product = new ProductServiceDAO();

		String status = null;
		try {
			status = product.removeProduct(prodId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RequestDispatcher rd = request.getRequestDispatcher("./GestioneCatalogo?message=" + status);

		rd.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
