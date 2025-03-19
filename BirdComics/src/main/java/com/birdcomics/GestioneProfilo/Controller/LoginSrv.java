package com.birdcomics.GestioneProfilo.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.birdcomics.Model.Bean.CartBean;
import com.birdcomics.Model.Bean.RuoloBean;
import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.GestioneCarrello.Service.*;
import com.birdcomics.GestioneProfilo.Service.ProfileService;
import com.birdcomics.GestioneProfilo.Service.ProfileServiceImpl;

@WebServlet("/LoginSrv")
public class LoginSrv extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ProfileService profileService;
    private CarrelloService cartService; // Dichiara una variabile di istanza per CarrelloService

    public LoginSrv() {
        super();
        this.profileService = new ProfileServiceImpl(); // Inizializza il servizio di profilo
        this.cartService = new CarrelloServiceImpl(); // Inizializza il servizio per il carrello
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Usa il ProfileService per validare le credenziali e ottenere i dettagli dell'utente
        String status = "Login Denied! Invalid email or password.";
        try {
            status = profileService.login(email, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if ("valid".equalsIgnoreCase(status)) {
            try {
                // Ottieni i dettagli dell'utente e il ruolo tramite il servizio
                UserBean user = profileService.getUserDetails(email);
                List<String> userType = profileService.getUserTypes(email);

                // Gestisci la sessione
                HttpSession session = request.getSession();
                session.setAttribute("userdata", user);
                session.setAttribute("email", email);
                session.setAttribute("usertype", userType);

                if (userType.contains(RuoloBean.Cliente.toString())) {
                    // Verifica se il carrello esiste già nella sessione
                    CartBean cart = (CartBean) session.getAttribute("cartBean");

                    if (cart == null) {
                        // Se il carrello non esiste nella sessione, caricalo dal database
                        cart = cartService.loadCartFromDB(session, email);  // Carica il carrello dal database
                        session.setAttribute("cart", cart);  // Memorizza il carrello nella sessione
                    }

                    // Se l'utente è un cliente, reindirizza a index.jsp
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                } else {
                    // Se l'utente non è un cliente, reindirizza a UserProfileServlet
                    response.sendRedirect(request.getContextPath() + "/UserProfileServlet");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Gestisci eventuali errori SQL durante il recupero dell'utente
                RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
                rd.forward(request, response);
            }
        } else {
            // Se il login non è valido, invia un messaggio di errore
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp?message=" + status);
            rd.include(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
