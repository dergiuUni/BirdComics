package com.birdcomics.GestioneOrdine;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("username");


		double paidAmount = Double.parseDouble(request.getParameter("amount"));
		String status = null;
		try {
			status = new OrderServiceDAO().paymentSuccess(userName, paidAmount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PrintWriter pw = response.getWriter();
		response.setContentType("text/html");
		 OrderServiceDAO dao = new OrderServiceDAO();
		 List<OrderBean> orders;
			try {
				orders = dao.getAllOrderDetails(userName);
				 request.setAttribute("orders", orders);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
         request.getRequestDispatcher("/orderDetails.jsp").forward(request, response);


	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
