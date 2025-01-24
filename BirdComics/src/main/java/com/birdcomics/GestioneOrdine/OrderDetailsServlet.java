package com.birdcomics.GestioneOrdine;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/OrderDetailsServlet")
public class OrderDetailsServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = (String) request.getSession().getAttribute("username");
        if (userName != null) {
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
        } else {
            // Handle case where user is not logged in
            response.sendRedirect("login.jsp");
        }
    }
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chiama il metodo doGet per gestire le richieste POST in modo consistente con le richieste GET
        doGet(request, response);
    }
}
