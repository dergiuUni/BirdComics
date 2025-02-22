package com.birdcomics.GestioneMagazzino.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Bean.MagazzinoBean;
import com.birdcomics.GestioneMagazzino.Service.MagazzinoService;
import com.birdcomics.GestioneMagazzino.Service.MagazzinoServiceImpl;

@WebServlet("/ListaMagazziniServlet")
public class ListaMagazziniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MagazzinoService magazzinoService;

    public ListaMagazziniServlet() {
        super();
        this.magazzinoService = new MagazzinoServiceImpl();  // Inizializzazione del servizio
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<MagazzinoBean> listaMagazzini = magazzinoService.getAllMagazzini();
            request.setAttribute("listaMagazzini", listaMagazzini);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Errore nel recupero dei magazzini.");
        }
        request.getRequestDispatcher("listaMagazzini.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
