package com.birdcomics.GestioneCatalogo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.GestioneOrdine.OrderBean;
import com.birdcomics.GestioneOrdine.OrderServiceDAO;
import com.birdcomics.GestioneOrdine.TransServiceDAO;
import com.birdcomics.GestioneProfili.UserServiceDAO;

@WebServlet("/ShipmentServlet")
public class ShipmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userType = (String) session.getAttribute("usertype");
        if (userType == null) {
            response.sendRedirect("login.jsp?message=Access Denied, Login Again!!");
            return;
        }

        OrderServiceDAO orderdao = new OrderServiceDAO();
        List<String> ArrayUserId = new ArrayList<>();
        List<String> ArrayUserAddr = new ArrayList<>();
        List<String> ArrayDateTime = new ArrayList<>();
        List<OrderBean> orders = null;
        
        String orderId = request.getParameter("orderid");
        String prodId = request.getParameter("prodid");

        
        if (orderId != null && prodId != null) {
            try {
                orderdao.shipNow(orderId, prodId);
                // Aggiorna la lista degli ordini dopo la spedizione
                orders = orderdao.getAllOrders();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        String search = request.getParameter("search");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        try {
        	 if (search != null && !search.isEmpty()) {
        	        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
        	            // Se entrambe le date sono specificate, utilizza il DAO per ottenere gli ordini per utente e intervallo di date
        	            orders = orderdao.getOrdersByUserIdAndDate(search, startDate, endDate);
        	        } else {
        	            // Se le date non sono specificate, utilizza il DAO per ottenere gli ordini solo per l'utente
        	            orders = orderdao.getOrdersByUserId(search);
        	        }
        	    } else {
        	        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
        	            // Se solo le date sono specificate senza l'utente, utilizza il DAO per ottenere tutti gli ordini nel intervallo di date
        	            orders = orderdao.getOrdersByDate(startDate, endDate);
        	        } else {
        	            // Se né l'utente né le date sono specificate, ottieni tutti gli ordini
        	            orders = orderdao.getAllOrders();
        	        }
        	    }
            // Populate ArrayUserId and ArrayUserAddr
            for (OrderBean order : orders) {
                String transId = order.getTransactionId();
                String userId = new TransServiceDAO().getUserId(transId);
                String userAddr = new UserServiceDAO().getUserAddr(userId).toString();
                String dateTime =  new TransServiceDAO().getDateTime(transId);

                ArrayUserId.add(userId);
                ArrayUserAddr.add(userAddr);
                ArrayDateTime.add(dateTime);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        

        request.setAttribute("orders", orders);
        request.setAttribute("ArrayUserId", ArrayUserId);
        request.setAttribute("ArrayUserAddr", ArrayUserAddr);
        request.setAttribute("ArrayDateTime", ArrayDateTime);
        RequestDispatcher rd = request.getRequestDispatcher("/shippedItems.jsp");
        rd.forward(request, response);
  }


    

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);

    }
}