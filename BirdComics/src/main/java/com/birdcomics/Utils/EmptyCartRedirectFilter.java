package com.birdcomics.Utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.birdcomics.Bean.CartBean;
import com.birdcomics.Dao.CartServiceDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EmptyCartRedirectFilter implements Filter {

    private CartServiceDAO cartService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializza il DAO per il carrello
        cartService = new CartServiceDAO();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        // Cast degli oggetti request e response
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Recupera l'email e i ruoli dalla sessione
        String email = (String) httpRequest.getSession().getAttribute("email");
        List<String> userTypes = (List<String>) httpRequest.getSession().getAttribute("usertype");

        if (email != null && userTypes != null && !userTypes.isEmpty()) {
            // Verifica se l'utente è un cliente
			boolean isCliente = userTypes.contains("Cliente");

			if (isCliente) {
			    // Ottieni il carrello dell'utente
			    CartBean cart = cartService.getCartFromSession(httpRequest.getSession(), email);

			    // Verifica se il carrello è vuoto
			    boolean isCartEmpty = cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty();

			    if (isCartEmpty) {
			        // Se il carrello è vuoto, reindirizza alla pagina principale (index.jsp)
			        httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.jsp");
			        return;
			    }
			} else {
			    // Se l'utente non è un cliente, reindirizza alla pagina di profilo
			    httpResponse.sendRedirect(httpRequest.getContextPath() + "/UserProfileServlet");
			    return;
			}
        } else {
            // Se l'email o i ruoli non sono presenti nella sessione, reindirizza alla pagina di login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }

        // Se tutto è OK, procedi con la richiesta
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup delle risorse se necessario
    }
}
