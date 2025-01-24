package com.birdcomics.GestioneCatalogo;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/SingleProductServlet")
public class SingleProductServlet extends HttpServlet {
	public SingleProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pid = request.getParameter("pid");

        if (pid != null) {
        	ProductServiceDAO dao = new ProductServiceDAO();
        	try {
				ProductBean product = dao.getProductsByID(pid);
				request.setAttribute("product", product);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
        	
            request.getRequestDispatcher("/singleProduct.jsp").forward(request, response);
        } 
    }
}


