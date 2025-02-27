package com.birdcomics.Dao;

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
	  

	public String addProduct(String name, String description, float price, String image, String[] selectedGenres) throws SQLException {
	    String status = "Product Registration Failed!";

	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	    ResultSet generatedKeys = null;

	    try {
	        // Inserisci il fumetto
	    	/*
	        ps = con.prepareStatement("insert into Fumetto (nome, descrizione, prezzo, immagine, active) values(?,?,?,?,?);", 
	                                  Statement.RETURN_GENERATED_KEYS);
	                                  */
	    	ps = con.prepareStatement("insert into Fumetto (nome, descrizione, prezzo, immagine) values(?,?,?,?);", 
                    Statement.RETURN_GENERATED_KEYS);
	        ps.setString(1, name);
	        ps.setString(2, description);
	        ps.setFloat(3, price);
	        ps.setString(4, image);
	       // ps.setBoolean(5, true);
	        
	        int k = ps.executeUpdate();

	        if (k > 0) {
	            // Recupera l'ID del fumetto appena inserito
	            generatedKeys = ps.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int fumettoId = generatedKeys.getInt(1);

	                // Inserisci i generi nella tabella Genere_Fumetto
	                for (String genre : selectedGenres) {
	                    PreparedStatement genrePs = con.prepareStatement("insert into Genere_Fumetto (genere, idFumetto) values(?,?)");
	                    genrePs.setString(1, genre);
	                    genrePs.setInt(2, fumettoId);
	                    genrePs.executeUpdate();
	                    genrePs.close();
	                }

	                status = "Product Added Successfully";
	            }
	        }

	    } catch (SQLException e) {
	        status = "Error: " + e.getMessage();
	        e.printStackTrace();
	    } finally {
	        // Chiudi tutte le risorse
	        DBUtil.closeConnection(ps);
	        if (generatedKeys != null) {
	            try {
	                generatedKeys.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    return status;
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
                    product.setImage(rs.getString("immagine"));
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
                    product.setImage(rs.getString("immagine"));
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
                    product.setImage(rs.getString("immagine"));
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
                    product.setImage(rs.getString("immagine"));
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
        
        int idfumetto = -1; // Valore di default

        if (idString != null) {
            try {
            	idfumetto = Integer.parseInt(idString); // Tenta di fare il parsing
            } catch (NumberFormatException e) {
                // Gestisci l'errore se il parametro non è un numero valido
                System.out.println("Errore nel parsing del parametro pid: " + e.getMessage());
                return null; 
            }
        }


        
        try {
            //ps = con.prepareStatement("SELECT * FROM Fumetto WHERE id=? AND active = 1");
            ps = con.prepareStatement("select * from Fumetto, Genere_Fumetto where id=idFumetto and id=?");
            ps.setInt(1,idfumetto );
            rs = ps.executeQuery();

            if (rs.next()) {
                product = new ProductBean();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("nome"));
                product.setDescription(rs.getString("descrizione"));
                product.setPrice(rs.getFloat("prezzo"));
                product.setImage(rs.getString("immagine"));
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
       
