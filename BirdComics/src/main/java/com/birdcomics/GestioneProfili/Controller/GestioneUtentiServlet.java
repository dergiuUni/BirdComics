package com.birdcomics.GestioneProfili.Controller;

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

import com.birdcomics.Model.Bean.RuoloBean;
import com.birdcomics.Model.Bean.UserBean;
import com.birdcomics.Model.Dao.UserServiceDAO;
import com.birdcomics.GestioneProfili.*;
import com.birdcomics.GestioneProfili.Service.ProfileService;
import com.birdcomics.GestioneProfili.Service.ProfileServiceImpl;

import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class manageGestoriMagazzini
 */
@WebServlet("/GestioneUtentiServlet")
public class GestioneUtentiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ProfileService profileService;

    public GestioneUtentiServlet() {
        super();
        this.profileService = new ProfileServiceImpl(); // Usa l'implementazione di ProfileService
    }

    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<RuoloBean> ruoloUtenti = new ArrayList<>();
        List<UserBean> utenti = new ArrayList<>();
        List<String> userTypes = (List<String>) session.getAttribute("usertype"); 
        
        String email = (String) request.getSession().getAttribute("email");
        UserBean u = new UserBean();
        
        try {
            u = profileService.getUserDetails(email);

            if(userTypes.contains(RuoloBean.GestoreGenerale.toString())) {
                ruoloUtenti.add(RuoloBean.GestoreMagazzino);
                utenti = profileService.getUsersByRole(ruoloUtenti, null);
            } else if(userTypes.contains(RuoloBean.GestoreMagazzino.toString())) {
                ruoloUtenti.add(RuoloBean.RisorseUmane);
                utenti = profileService.getUsersByRole(ruoloUtenti, u.getMagazzino().getNome());
            } else if(userTypes.contains(RuoloBean.RisorseUmane.toString())) {
                ruoloUtenti.add(RuoloBean.Assistenza);
                ruoloUtenti.add(RuoloBean.Finanza);
                ruoloUtenti.add(RuoloBean.GestoreCatalogo);
                ruoloUtenti.add(RuoloBean.Magazziniere);
                ruoloUtenti.add(RuoloBean.Spedizioniere);
                utenti = profileService.getUsersByRole(ruoloUtenti, u.getMagazzino().getNome());    
            }
        } catch (SQLException e) {
            request.setAttribute("message", "Errore nel recupero degli utenti.");
            e.printStackTrace();
        }

        request.setAttribute("utenti", utenti);
        request.getRequestDispatcher("manageUsers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}



