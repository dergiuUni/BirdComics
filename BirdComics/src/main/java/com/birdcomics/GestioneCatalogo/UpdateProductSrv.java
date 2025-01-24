package com.birdcomics.GestioneCatalogo;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
                ProductBean product = new ProductServiceDAO().getProductDetails(prodid);

                if (product == null) {
                    // Se il prodotto non esiste, reindirizzo con un messaggio di errore
                    response.sendRedirect("updateProductById.jsp?message=Please Enter a valid product Id");
                } else {
                    // Se il prodotto esiste, imposto l'attributo nella richiesta e reindirizzo
                    request.setAttribute("product1", product);
                    RequestDispatcher rd = request.getRequestDispatcher("updateProduct.jsp");
                    rd.forward(request, response);
                }

            } catch (SQLException e) {
                e.printStackTrace(); // Gestire l'eccezione in modo appropriato
            }
        } else {
            // Gestione del caso in cui il parametro prodid non è presente nella richiesta
            response.sendRedirect("updateProductById.jsp?message=Please Enter a valid product Id");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String prodId = request.getParameter("pid");
        String prodName = request.getParameter("name");
        String prodType = request.getParameter("type");
        String prodInfo = request.getParameter("info");
        double prodPrice = Double.parseDouble(request.getParameter("price"));
        int prodQuantity = Integer.parseInt(request.getParameter("quantity"));

        // Creazione dell'oggetto ProductBean con i dati ricevuti dal form
        ProductBean product = new ProductBean();
        product.setProdId(prodId);
        product.setProdName(prodName);
        product.setProdInfo(prodInfo);
        product.setProdPrice(prodPrice);
        product.setProdQuantity(prodQuantity);
        product.setProdType(prodType);

        // Chiamata al DAO per aggiornare il prodotto nel database
        ProductServiceDAO dao = new ProductServiceDAO();
        String status;
        try {
            status = dao.updateProductWithoutImage(prodId, product);
            // Reindirizzamento alla pagina di visualizzazione del prodotto aggiornato con un messaggio di stato
            response.sendRedirect("updateProductById.jsp?message=" + status);

        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione dell'errore, reindirizzamento con messaggio di errore
            response.sendRedirect("updateProductByd.jsp?message=Error updating product: " + e.getMessage());
        }
    }
}
