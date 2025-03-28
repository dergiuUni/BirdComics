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
import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.Model.Dao.GenereDAO;
import com.birdcomics.Model.Dao.ProductServiceDAO;

@WebServlet("/UpdateProductSrv")
public class UpdateProductSrv extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UpdateProductSrv() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String prodid = request.getParameter("prodid");

		if (prodid != null && !prodid.isEmpty()) {
			try {
				ProductBean product = new ProductServiceDAO().getProductsByID(prodid);


				// Se il prodotto esiste, imposto l'attributo nella richiesta e reindirizzo
				
				
				List<String> generi =new ArrayList<String>();
	    		GenereDAO genereDAO = new GenereDAO();
	    		List<GenereBean> generiBeans = genereDAO.getGeneri();
	    		for(GenereBean genereBean : generiBeans)
	    			generi.add(genereBean.toString());

	    		request.setAttribute("genres", generi);
				
				
				
				request.setAttribute("product", product);
				RequestDispatcher rd = request.getRequestDispatcher("updateProduct.jsp");
				rd.forward(request, response);
			}

			catch (SQLException e) {
				e.printStackTrace(); // Gestire l'eccezione in modo appropriato
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 String status = "Product Update Failed!";
	        String prodName = request.getParameter("name");
	        String[] selectedGenres = request.getParameterValues("genres"); // Recupera i generi selezionati
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
	            status = productService.addProduct(prodName, prodInfo, prodPrice, uniqueFileName, selectedGenres);
	        } else {
	            // If no image is provided, handle it accordingly (optional)
	            status = "Please provide an image for the product.";
	        }

	        // Redirect back to the JSP page with the status message
	        request.setAttribute("message", status);
	        RequestDispatcher rd = request.getRequestDispatcher("./GestioneCatalogo");
	        rd.forward(request, response);
	}
	
    private String getFileExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1).toLowerCase();
        }
        return extension;
    }
}
