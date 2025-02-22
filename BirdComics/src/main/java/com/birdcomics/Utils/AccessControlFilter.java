package com.birdcomics.Utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "/AccessControlFilter", urlPatterns = "/*")
public class AccessControlFilter implements Filter {

    public void init(FilterConfig fConfig) throws ServletException {
        // Inizializzazione se necessaria
    }

    public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Recupera la sessione dell'utente
        HttpSession session = httpServletRequest.getSession();
        List<String> userType = (List<String>) session.getAttribute("usertype");  // La lista di ruoli dell'utente
        String path = httpServletRequest.getRequestURI();  // Recupera il path dell'URL richiesto

        // Verifica se l'URL contiene "/Cliente/"
        if (path.contains("/Cliente/")) {
            // Controlla se "Cliente" è presente nei tipi di ruolo dell'utente
            if (userType == null || !userType.contains("Cliente")) {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login.jsp?message=Permission Denied");
                return;
            }
        }

        // Continua la catena dei filtri se il controllo è passato
        chain.doFilter(request, response);
    }

    public void destroy() {
        // Cleanup se necessario
    }
}
