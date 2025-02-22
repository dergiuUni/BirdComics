package com.birdcomics.Utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Dao.CartServiceDAO;

import java.io.IOException;
import java.sql.SQLException;

public class EmptyCartRedirectFilter implements Filter {

    private CartServiceDAO cartService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        cartService = new CartServiceDAO();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String email = (String) httpRequest.getSession().getAttribute("email");

        if (email != null) {
            try {
                int cartItemCount = cartService.getCartCount(email);

                if (cartItemCount == 0) {
                    // Cart is empty, redirect to index.jsp
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.jsp");
                } else {
                    // Cart is not empty, proceed to payment.jsp
                    chain.doFilter(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle SQLException appropriately
                // Redirect to an error page or handle the error condition as needed
            }
        }
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}
