package com.birdcomics.Utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class AuthenticationFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro, se necessario
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false); // Non crea una nuova sessione se non esiste

        // Verifica se l'utente è autenticato
        if (session == null || session.getAttribute("email") == null || session.getAttribute("password") == null) {
            // Utente non autenticato, reindirizza al login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?message=Session Expired, Login Again!!");
        } else {
            // Utente autenticato, permetti l'accesso alla risorsa richiesta
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
        // Pulizia delle risorse, se necessario
    }
}
