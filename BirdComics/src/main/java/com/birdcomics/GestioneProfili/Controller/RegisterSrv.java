package com.birdcomics.GestioneProfili.Controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Bean.RuoloBean;
import com.birdcomics.Bean.UserBean;
import com.birdcomics.GestioneProfili.Service.ProfileService;
import com.birdcomics.GestioneProfili.Service.ProfileServiceImpl;

@WebServlet("/RegisterSrv")
public class RegisterSrv extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ProfileService profileService;

    public RegisterSrv() {
        super();
        this.profileService = new ProfileServiceImpl(); // Usa ProfileService per la logica
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String emailP = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String citta = request.getParameter("nomeCitta");
        String via = request.getParameter("via");
        int numeroCivico = Integer.valueOf(request.getParameter("numeroCivico"));
        String cap = request.getParameter("cap");
        
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String dataNascitaStr = request.getParameter("dataNascita");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date parsedDate;
        java.sql.Date dataNascita = null;
        try {
            parsedDate = sdf.parse(dataNascitaStr);
            dataNascita = new java.sql.Date(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        String status = "";
        if (password != null && password.equals(confirmPassword)) {

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String email = (String) httpRequest.getSession().getAttribute("email");
            ArrayList<RuoloBean> ruoli = new ArrayList<RuoloBean>();

            try {
                if (email == null || email.equals("null")) {
                    ruoli.add(RuoloBean.Cliente);
                    status = profileService.registerUser(emailP, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli);
                } else {
                    UserBean at = profileService.getUserDetails(emailP);
                    if (at.isRuolo(RuoloBean.GestoreGenerale)) {
                        ruoli.add(RuoloBean.GestoreMagazzino);
                        status = profileService.registerUser(emailP, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli);
                    }
                    if (at.isRuolo(RuoloBean.GestoreMagazzino)) {
                        ruoli.add(RuoloBean.RisorseUmane);
                        status = profileService.registerUser(emailP, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli);
                    }
                    if (at.isRuolo(RuoloBean.RisorseUmane)) {
                        String[] ruoliSelezionati = request.getParameterValues("ruoli");
                        if (ruoliSelezionati != null) {
                            for (String ruolo : ruoliSelezionati) {
                                ruoli.add(RuoloBean.fromString(ruolo));
                            }
                            status = profileService.registerUser(emailP, password, nome, cognome, telefono, dataNascita, citta, via, numeroCivico, cap, ruoli);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            status = "Password not matching!";
        }

        RequestDispatcher rd = request.getRequestDispatcher("register.jsp?message=" + status);
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
