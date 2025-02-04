package com.birdcomics.GestioneCarrello;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.GestioneCatalogo.ProductBean;
import com.birdcomics.GestioneCatalogo.ProductServiceDAO;

/**
 * Servlet implementation class UpdateToCart
 */
@WebServlet("/UpdateToCart")
public class UpdateToCart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UpdateToCart() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");

		if (userName == null || password == null) {

			response.sendRedirect("login.jsp?message=Session Expired, Login Again!!");
		}

		// login Check Successfull

		String userId = userName;
		String prodId = request.getParameter("pid");
		int pQty = Integer.parseInt(request.getParameter("pqty"));

		CartServiceDAO cart = new CartServiceDAO();

		ProductServiceDAO productDao = new ProductServiceDAO();

		ProductBean product = null;
		try {
			product = productDao.getProductsByID(prodId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		//int availableQty = product.getProdQuantity();
		 ProductServiceDAO pr = new ProductServiceDAO();
		int availableQty = 0;
		try {
			availableQty = pr.getAllQuantityProductsById(product);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PrintWriter pw = response.getWriter();

		response.setContentType("text/html");

		if (availableQty < pQty) {

			String status;
			try {
				status = cart.updateProductToCart(userId, prodId, availableQty);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			status = "Only " + availableQty + " no of " + product.getName()
					+ " are available in the shop! So we are adding only " + availableQty + " products into Your Cart"
					+ "";

			RequestDispatcher rd = request.getRequestDispatcher("cartDetails.jsp");

			rd.include(request, response);

			pw.println("<script>document.getElementById('message').innerHTML='" + status + "'</script>");

		} else {
			String status = null;
			try {
				status = cart.updateProductToCart(userId, prodId, pQty);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			RequestDispatcher rd = request.getRequestDispatcher("CartDetailsServlet");

			rd.include(request, response);

			pw.println("<script>document.getElementById('message').innerHTML='" + status + "'</script>");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
