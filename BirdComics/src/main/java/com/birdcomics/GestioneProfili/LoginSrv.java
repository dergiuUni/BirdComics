package com.birdcomics.GestioneProfili;

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

@WebServlet("/LoginSrv")
public class LoginSrv extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginSrv() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");

        String status = "Login Denied! Invalid Username or password.";

        UserServiceDAO udao = new UserServiceDAO();

        try {
            status = udao.isValidCredential(userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (status.equalsIgnoreCase("valid")) {
            UserBean user = null;
            try {
                user = udao.getUserDetails(userName, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            HttpSession session = request.getSession();
            session.setAttribute("userdata", user);
            session.setAttribute("username", userName);
            session.setAttribute("password", password);

            try {
                List<String> userType = new ArrayList<String>();
                List<RuoloBean> ruolo = udao.getUserType(userName);
                for (RuoloBean ruoloBean : ruolo) {
					userType.add(ruoloBean.toString());	
				} 
                
                session.setAttribute("usertype", userType);
                RequestDispatcher rd = request.getRequestDispatcher("/UserProfileServlet");
                rd.forward(request, response);

      
                
         
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp?message=" + status);
            rd.include(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
