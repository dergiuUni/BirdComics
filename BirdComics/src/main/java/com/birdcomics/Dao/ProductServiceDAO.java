package com.birdcomics.Dao;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Utils.DBUtil;

public class ProductServiceDAO {
	  

	public String addProduct(String name, String description, float price, String image, String[] selectedGenres) {
	    Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    PreparedStatement genrePs = null; // Aggiungi questa dichiarazione

	    try {
	        connection = DBUtil.getConnection();
	        String query = "insert into Fumetto(nome, descrizione, prezzo, immagine) values(?,?,?,?)";
	        ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	        ps.setString(1, name);
	        ps.setString(2, description);
	        ps.setFloat(3, price);
	        ps.setString(4, image);
	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                int id = rs.getInt(1);

	                // Inserisci i generi
	                String genreQuery = "insert into Genere_Fumetto (genere, idFumetto) values(?,?)";
	                genrePs = connection.prepareStatement(genreQuery); // Crea il PreparedStatement per i generi
	                for (String genre : selectedGenres) {
	                    genrePs.setString(1, genre);
	                    genrePs.setInt(2, id);
	                    genrePs.executeUpdate();
	                }
	            }
	        }
	        return "Product Added Successfully";
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Error Adding Product";
	    } finally {
	        // Chiudi le risorse
	        DBUtil.closeConnection(rs);
	        DBUtil.closeConnection(ps);
	        DBUtil.closeConnection(genrePs); // Chiudi anche il PreparedStatement dei generi
	        DBUtil.closeConnection(connection);
	    }
	}
 
    public String removeProduct(String prodId) throws SQLException {
        String status = "Product Removal Failed!";

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;

        try {
    
        	ps = con.prepareStatement("DELETE FROM Fumetto WHERE id = ?");
            ps.setString(1, prodId);

            int k = ps.executeUpdate();

            if (k > 0) {
                status = "Fumetto Cancellato!";
            }

        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
        }

        return status;
    }

    public List<ProductBean> getAllProducts() throws SQLException {
        List<ProductBean> products = new ArrayList<>();
        Map<Integer, ProductBean> productMap = new HashMap<Integer, ProductBean>();
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
        	/*
            ps = con.prepareStatement("SELECT * FROM Fumetto WHERE active = 1");
            */
        	 ps = con.prepareStatement("select * from Fumetto, Genere_Fumetto where id=idFumetto");
            rs = ps.executeQuery();
            
            
            while (rs.next()) {
                int productId = rs.getInt("id");
                ProductBean product = productMap.get(productId);

                if (product == null) {
                    product = new ProductBean();
                    product.setId(productId);
                    product.setName(rs.getString("nome"));
                    product.setDescription(rs.getString("descrizione"));
                    product.setPrice(rs.getFloat("prezzo"));
                    product.setImage(product.getId() + ".jpg");
                    productMap.put(productId, product);
                }

                product.addGenere(rs.getString("genere"));
            }

            products.addAll(productMap.values());
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
            DBUtil.closeConnection(con);
        }

        return products;
    }
    public int getAllQuantityProductsById(ProductBean p) throws SQLException {
        Connection con = DBUtil.getConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
        	ps = con.prepareStatement("select SUM(Scaffali.quantita) from Scaffali where Scaffali.idFumetto = ?");
            ps.setInt(1, p.getId());
            rs = ps.executeQuery();

            if(rs.next()) {
            	System.out.println("somma scaffali " + rs.getInt(1));
            	return rs.getInt(1);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBUtil.closeConnection(ps);
        DBUtil.closeConnection(rs);
        DBUtil.closeConnection(con);

        return 0;
    }
    
    public List<ProductBean> getAllProductsByType(String type) throws SQLException {
        List<ProductBean> products = new ArrayList<>();

        Connection con = DBUtil.getConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
        	//ps = con.prepareStatement("select * from Fumetto, Genere_Fumetto where Fumetto.id = Genere_Fumetto.idFumetto and Genere_Fumetto.genere = ? and Fumetto.active = 1");
        	ps = con.prepareStatement("select * from Fumetto, Genere_Fumetto where Fumetto.id = Genere_Fumetto.idFumetto and Genere_Fumetto.genere = ?");
            ps.setString(1, type);
            rs = ps.executeQuery();

            Map<Integer, ProductBean> productMap = new HashMap<Integer, ProductBean>();
            while (rs.next()) {
                int productId = rs.getInt("id");
                ProductBean product = productMap.get(productId);

                if (product == null) {
                    product = new ProductBean();
                    product.setId(productId);
                    product.setName(rs.getString("nome"));
                    product.setDescription(rs.getString("descrizione"));
                    product.setPrice(rs.getFloat("prezzo"));
                    product.setImage(product.getId() + ".jpg");
                    productMap.put(productId, product);
                }

                product.addGenere(rs.getString("genere"));
            }

            products.addAll(productMap.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBUtil.closeConnection(ps);
        DBUtil.closeConnection(rs);
        DBUtil.closeConnection(con);

        return products;
    }
  
    public List<ProductBean> searchAllProducts(String search) throws SQLException {
        List<ProductBean> products = new ArrayList<>();

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
        	
        	ps = con.prepareStatement(
        		    "SELECT Fumetto.id, Fumetto.nome, Fumetto.descrizione, Fumetto.prezzo, Fumetto.immagine, Genere_Fumetto.genere "
        		    + "FROM Fumetto "
        		    + "JOIN Genere_Fumetto ON Fumetto.id = Genere_Fumetto.idFumetto "
        		    + "WHERE LOWER(Genere_Fumetto.genere) LIKE ? OR LOWER(Fumetto.nome) LIKE ? "
        		    + "GROUP BY Fumetto.id, Genere_Fumetto.genere");

            search = "%" + search.toLowerCase() + "%";
            ps.setString(1, search);
            ps.setString(2, search);

            rs = ps.executeQuery();

            
            Map<Integer, ProductBean> productMap = new HashMap<Integer, ProductBean>();
            while (rs.next()) {
                int productId = rs.getInt("id");
                ProductBean product = productMap.get(productId);

                if (product == null) {
                    product = new ProductBean();
                    product.setId(productId);
                    product.setName(rs.getString("nome"));
                    product.setDescription(rs.getString("descrizione"));
                    product.setPrice(rs.getFloat("prezzo"));
                    product.setImage(product.getId() + ".jpg");
                    productMap.put(productId, product);
                }

                product.addGenere(rs.getString("genere"));
            }

            products.addAll(productMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
            DBUtil.closeConnection(con);
        }

        return products;
    }
    
    
    public List<ProductBean> searchAllProductsGestore(String search) throws SQLException {
        List<ProductBean> products = new ArrayList<>();

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
        	ps = con.prepareStatement(
        		    "SELECT * FROM Fumetto, Genere_Fumetto " +
        		    "WHERE Fumetto.id = Genere_Fumetto.idFumetto " +
        		    "AND (LOWER(Genere_Fumetto.genere) LIKE ? OR LOWER(Fumetto.nome) LIKE ? OR Fumetto.id = ?) " +
        		    "GROUP BY Fumetto.id, Genere_Fumetto.genere"
        		);

            String search1 = "%" + search.toLowerCase() + "%";
            ps.setString(1, search1);
            ps.setString(2, search1);
            ps.setString(3, search);

            rs = ps.executeQuery();

            
            Map<Integer, ProductBean> productMap = new HashMap<Integer, ProductBean>();
            while (rs.next()) {
                int productId = rs.getInt("id");
                ProductBean product = productMap.get(productId);

                if (product == null) {
                    product = new ProductBean();
                    product.setId(productId);
                    product.setName(rs.getString("nome"));
                    product.setDescription(rs.getString("descrizione"));
                    product.setPrice(rs.getFloat("prezzo"));
                    product.setImage(product.getId() + ".jpg");
                    productMap.put(productId, product);
                }

                product.addGenere(rs.getString("genere"));
            }

            products.addAll(productMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
            DBUtil.closeConnection(con);
        }

        return products;
    }


    public ProductBean getProductsByID(String idString) throws SQLException {
        ProductBean product = null;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        int idfumetto = -1;

        if (idString != null) {
            try {
                idfumetto = Integer.parseInt(idString);
            } catch (NumberFormatException e) {
                System.out.println("Errore nel parsing del parametro pid: " + e.getMessage());
                return null;
            }
        }

        try {
            ps = con.prepareStatement("SELECT * FROM Fumetto, Genere_Fumetto WHERE id = idFumetto AND id = ?");
            ps.setInt(1, idfumetto);
            rs = ps.executeQuery();

            if (rs.next()) {
                product = new ProductBean();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("nome"));
                product.setDescription(rs.getString("descrizione"));
                product.setPrice(rs.getFloat("prezzo"));
                // Genera il percorso dell'immagine dinamicamente
                product.setImage(product.getId() + ".jpg");
                product.addGenere(rs.getString("genere"));
            }
            while (rs.next()) {
                product.addGenere(rs.getString("genere"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
            DBUtil.closeConnection(con);
        }

        return product;
    }

}
       
