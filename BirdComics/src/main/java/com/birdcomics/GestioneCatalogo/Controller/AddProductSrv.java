package com.birdcomics.GestioneCatalogo.Controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.birdcomics.Model.Bean.GenereBean;
import com.birdcomics.Model.Dao.GenereDAO;
import com.birdcomics.GestioneCatalogo.Service.CatalogoService;
import com.birdcomics.GestioneCatalogo.Service.CatalogoServiceImpl;


@WebServlet("/AddProductSrv")
@MultipartConfig(maxFileSize = 16177215)
public class AddProductSrv extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CatalogoService catalogoService;

    public AddProductSrv() {
        super();
        this.catalogoService = new CatalogoServiceImpl();  // Inizializziamo il servizio
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String status = "Product Registration Failed!";
        String prodName = request.getParameter("name");
        String[] selectedGenres = request.getParameterValues("genres");
        String prodInfo = request.getParameter("info");
        float prodPrice = Float.parseFloat(request.getParameter("price"));

        Part filePart = request.getPart("image"); // Ottieni il file caricato

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName(); // Ottieni il nome originale del file

            // Verifica l'estensione del file
            String fileExtension = getFileExtension(fileName);
            if (!fileExtension.equals("jpg") && !fileExtension.equals("jpeg") && !fileExtension.equals("png")) {
                status = "Invalid file type! Only JPG, JPEG, and PNG are allowed.";
                request.setAttribute("message", status);
                RequestDispatcher rd = request.getRequestDispatcher("addProduct.jsp");
                rd.forward(request, response);
                return;
            }

            // Salva il file con un nome temporaneo
            String uploadPath = request.getServletContext().getRealPath("uploads/");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String tempFileName = "temp_" + UUID.randomUUID().toString() + "." + fileExtension;
            String tempFilePath = uploadPath + File.separator + tempFileName;
            filePart.write(tempFilePath);

            try {
                // Usa il CatalogoService per aggiungere il prodotto
                status = catalogoService.addFumetto(prodName, prodInfo, prodPrice, tempFilePath, selectedGenres);
            } catch (SQLException e) {
                status = "Database error occurred while adding the product.";
                e.printStackTrace();
            }
        } else {
            status = "Please provide an image for the product.";
        }

        // Reindirizza alla pagina JSP con il messaggio di stato
        request.setAttribute("message", status);
        RequestDispatcher rd = request.getRequestDispatcher("addProduct.jsp");
        rd.forward(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        List<String> generi = new ArrayList<String>();
        GenereDAO genereDAO = new GenereDAO();
        List<GenereBean> generiBeans = genereDAO.getGeneri();
        for (GenereBean genereBean : generiBeans)
            generi.add(genereBean.toString());

        request.setAttribute("genres", generi);
        request.getRequestDispatcher("addProduct.jsp").forward(request, response);
    }

    // Utility method to get the file extension
    private String getFileExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1).toLowerCase();
        }
        return extension;
    }
}
