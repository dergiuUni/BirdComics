package com.birdcomics.GestioneMagazzino;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.birdcomics.Bean.MagazzinoBean;
import com.birdcomics.Dao.MagazzinoDao;

/**
 * Servlet per ottenere la lista dei magazzini e passarla alla JSP
 */
@WebServlet("/ListaMagazziniServlet")
public class ListaMagazziniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public ListaMagazziniServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MagazzinoDao magazzinoDao = new MagazzinoDao();
        ArrayList<MagazzinoBean> listaMagazzini = new ArrayList<>();

        try {
            listaMagazzini = magazzinoDao.getMagazzini();
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "Errore nel recupero dei magazzini.");
        }

        request.setAttribute("listaMagazzini", listaMagazzini);
        request.getRequestDispatcher("listaMagazzini.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
