package com.birdcomics.GestioneCatalogo;

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

import com.birdcomics.GestioneCatalogo.ProductServiceDAO;
import com.birdcomics.GestioneProfili.RuoloBean;

/**
 * Servlet implementation class AddProductSrv
 */
@WebServlet("/AddProductSrv")
@MultipartConfig(maxFileSize = 16177215)
public class AddProductSrv extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String status = "Product Registration Failed!";
        String prodName = request.getParameter("name");
        String prodType = request.getParameter("type");
        String prodInfo = request.getParameter("info");
        float prodPrice = Float.parseFloat(request.getParameter("price"));

        Part filePart = request.getPart("image"); // Get the file part with the name "image"

        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName(); // Get the original file name

            // Check file extension to ensure it's an image
            String fileExtension = getFileExtension(fileName);
            if (!fileExtension.equals("jpg") && !fileExtension.equals("jpeg") && !fileExtension.equals("png")) {
                status = "Invalid file type! Only JPG, JPEG, and PNG are allowed.";
                request.setAttribute("message", status);
                RequestDispatcher rd = request.getRequestDispatcher("addProduct.jsp");
                rd.forward(request, response);
                return;
            }

            // Create a unique name for the image to avoid conflicts
            String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;

            // Define the upload path
            String uploadPath = request.getServletContext().getRealPath("uploads/");

            // Ensure the directory exists
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Create the directory if it doesn't exist
            }

            // Write the file to the directory
            String filePath = uploadPath + File.separator + uniqueFileName;
            filePart.write(filePath); 

            // Call the DAO to add the product
            ProductServiceDAO productService = new ProductServiceDAO();
            try {
                status = productService.addProduct(prodName, prodInfo, prodPrice, uniqueFileName);
            } catch (SQLException e) {
                status = "Database error occurred while adding the product.";
                e.printStackTrace();
            }
        } else {
            // If no image is provided, handle it accordingly (optional)
            status = "Please provide an image for the product.";
        }

        // Redirect back to the JSP page with the status message
        request.setAttribute("message", status);
        RequestDispatcher rd = request.getRequestDispatcher("addProduct.jsp");
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	
 
          HttpSession session = request.getSession();

    	
      		List<String> generi =new ArrayList<String>();
    		GenereDAO genereDAO = new GenereDAO();
    		List<GenereBean> generiBeans = genereDAO.getGeneri();
    		for(GenereBean genereBean : generiBeans)
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
