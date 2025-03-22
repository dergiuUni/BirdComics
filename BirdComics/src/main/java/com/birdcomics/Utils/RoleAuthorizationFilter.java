package com.birdcomics.Utils;

import com.birdcomics.Model.Bean.RuoloBean;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class RoleAuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro (opzionale)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Cast degli oggetti request e response
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Recupera la sessione
        HttpSession session = httpRequest.getSession(false);

        // Recupera i ruoli dell'utente dalla sessione
        List<String> userRoles = (session != null && session.getAttribute("usertype") != null)
                ? (List<String>) session.getAttribute("usertype")
                : null;

        // Recupera l'URL richiesto
        String requestURI = httpRequest.getRequestURI();

        // Se l'utente è autenticato e cerca di accedere a /login.jsp, reindirizzalo in base al ruolo
        if (requestURI.contains("/login.jsp") && userRoles != null && !userRoles.isEmpty()) {
            if (userRoles.contains(RuoloBean.Cliente.toString())) {
                // Cliente: reindirizza a index.jsp
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.jsp");
            } else {
                // Altri ruoli: reindirizza a UserProfileServlet
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/UserProfileServlet");
            }
            return;
        }

        // Se l'utente è autenticato e cerca di accedere a /RegisterSrv, reindirizzalo a UserProfileServlet
        if (requestURI.contains("/RegisterSrv") && userRoles != null && !userRoles.isEmpty()) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/UserProfileServlet");
            return;
        }

        // Verifica se l'utente è autorizzato
        if (isAuthorized(userRoles, requestURI)) {
            // Utente autorizzato, continua con la richiesta
            chain.doFilter(request, response);
        } else {
            // Utente non autorizzato, reindirizza alla pagina di accesso negato
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/accessDenied.jsp");
        }
    }

    private boolean isAuthorized(List<String> userRoles, String requestURI) {
        // Accesso consentito a /login.jsp sia agli ospiti che agli utenti autenticati
        if (requestURI.contains("/login.jsp")) {
            return true; // Tutti possono accedere
        }

        // Accesso consentito a /RegisterSrv solo se l'utente non è autenticato (ospite)
        if (requestURI.contains("/RegisterSrv")) {
            return userRoles == null || userRoles.isEmpty(); // Solo ospiti possono accedere
        }

        // Accesso consentito solo a /AddToCart se l'utente è un Cliente
        if (requestURI.contains("/AddToCart")) {
            return userRoles != null && userRoles.contains(RuoloBean.Cliente.toString());
        }

        // Accesso consentito solo a /CartDetailsServlet se l'utente è un Cliente
        if (requestURI.contains("/CartDetailsServlet")) {
            return userRoles != null && userRoles.contains(RuoloBean.Cliente.toString());
        }

        // Accesso consentito solo a /UpdateToCart se l'utente è un Cliente
        if (requestURI.contains("/UpdateToCart")) {
            return userRoles != null && userRoles.contains(RuoloBean.Cliente.toString());
        }

        // Pagine comuni accessibili a tutti gli utenti autenticati
        return requestURI.contains("/index.jsp") || requestURI.contains("/UserProfileServlet");
    }

    @Override
    public void destroy() {
        // Pulizia del filtro (opzionale)
    }
}
