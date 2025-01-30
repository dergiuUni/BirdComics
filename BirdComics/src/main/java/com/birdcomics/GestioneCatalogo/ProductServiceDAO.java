package com.birdcomics.GestioneCatalogo;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.DatabaseImplementator.DBUtil;
import com.birdcomics.DatabaseImplementator.IDUtil;

public class ProductServiceDAO {
	/*    public String addProduct(String prodName, String prodType, String prodInfo, double prodPrice, int prodQuantity,
            String prodImage) throws SQLException {
        String status = null;
        String prodId = IDUtil.generateId();

        ProductBean product = new ProductBean(prodId, prodName, prodType, prodInfo, prodPrice, prodQuantity, prodImage);

        status = addProduct(product);

        return status;
    }

    public String addProduct(ProductBean product) throws SQLException {
        String status = "Product Registration Failed!";

        if (product.getProdId() == null)
            product.setProdId(IDUtil.generateId());

        Connection con = DBUtil.getConnection();

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("insert into product values(?,?,?,?,?,?,?, 1);");
            ps.setString(1, product.getProdId());
            ps.setString(2, product.getProdName());
            ps.setString(3, product.getProdType());
            ps.setString(4, product.getProdInfo());
            ps.setDouble(5, product.getProdPrice());
            ps.setInt(6, product.getProdQuantity());
            ps.setString(7, product.getProdImage());

            int k = ps.executeUpdate();

            if (k > 0) {

                status = "Product Added Successfully with Product Id: " + product.getProdId();

            } else {

                status = "Product Updation Failed!";
            }

        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        }

        DBUtil.closeConnection(ps);

        return status;
    }

    public String removeProduct(String prodId) throws SQLException {
        String status = "Product Removal Failed!";

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("UPDATE product SET active = 0 WHERE pid = ?");
            ps.setString(1, prodId);

            int k = ps.executeUpdate();

            if (k > 0) {
                status = "Product Set as Inactive Successfully!";
            }

        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
        }

        return status;
    }

    public String updateProduct(ProductBean prevProduct, ProductBean updatedProduct) throws SQLException {
        String status = "Product Updation Failed!";

        if (!prevProduct.getProdId().equals(updatedProduct.getProdId())) {

            status = "Both Products are Different, Updation Failed!";

            return status;
        }

        Connection con = DBUtil.getConnection();

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(
                    "update product set pname=?,ptype=?,pinfo=?,pprice=?,pquantity=?,image=? where pid=?");

            ps.setString(1, updatedProduct.getProdName());
            ps.setString(2, updatedProduct.getProdType());
            ps.setString(3, updatedProduct.getProdInfo());
            ps.setDouble(4, updatedProduct.getProdPrice());
            ps.setInt(5, updatedProduct.getProdQuantity());
            ps.setString(6, updatedProduct.getProdImage());
            ps.setString(7, prevProduct.getProdId());

            int k = ps.executeUpdate();

            if (k > 0)
                status = "Product Updated Successfully!";

        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBUtil.closeConnection(ps);

        return status;
    }

    public String updateProductPrice(String prodId, double updatedPrice) throws SQLException {
        String status = "Price Updation Failed!";

        Connection con = DBUtil.getConnection();

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("update product set pprice=? where pid=?");

            ps.setDouble(1, updatedPrice);
            ps.setString(2, prodId);

            int k = ps.executeUpdate();

            if (k > 0)
                status = "Price Updated Successfully!";
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        }

        DBUtil.closeConnection(ps);

        return status;
    }
*/
    public List<ProductBean> getAllProducts() throws SQLException {
        List<ProductBean> products = new ArrayList<>();

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT * FROM Fumetto WHERE active = 1");
            rs = ps.executeQuery();

            while (rs.next()) {
                ProductBean product = new ProductBean();
                product.setId(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setDescription(rs.getString(3));
                product.setPrice(rs.getFloat(4));
                //ps.setString(7, product.getImage());
                // Assuming prodImage is stored as Blob or InputStream in the database
                // product.setProdImage(rs.getAsciiStream(7)); // Uncomment and adjust if needed

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
            DBUtil.closeConnection(con);
        }

        return products;
    }

    public List<ProductBean> getAllProductsByType(String type) throws SQLException {
        List<ProductBean> products = new ArrayList<>();

        Connection con = DBUtil.getConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
        	ps = con.prepareStatement("select * from Fumetto, Genere_Fumetto where Fumetto.id = Genere_Fumetto.idFumetto and Genere_Fumetto.genere = ? and Fumetto.active = 1");
            ps.setString(1, type);
            rs = ps.executeQuery();

            while (rs.next()) {

                ProductBean product = new ProductBean();

                product.setId(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setDescription(rs.getString(3));
                product.setPrice(rs.getFloat(4));
                // Assuming prodImage is stored as Blob or InputStream in the database
                // product.setProdImage(rs.getAsciiStream(7)); // Uncomment and adjust if needed

                products.add(product);

            }

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
                "SELECT * FROM Fumetto, Genere_Fumetto  WHERE Fumetto.id = Genere_Fumetto.idFumetto"
                + " and (lower( Genere_Fumetto.genere) LIKE ? OR lower(Fumetto.nome) LIKE ?)  AND Fumetto.active = 1 GROUP BY Fumetto.id");

            search = "%" + search.toLowerCase() + "%";
            ps.setString(1, search);
            ps.setString(2, search);

            rs = ps.executeQuery();

            while (rs.next()) {
                ProductBean product = new ProductBean();
                product.setId(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setDescription(rs.getString(3));
                product.setPrice(rs.getFloat(4));
                // Assuming prodImage is stored as Blob or InputStream in the database
                // product.setProdImage(rs.getAsciiStream("image")); // Uncomment and adjust if needed

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
            DBUtil.closeConnection(con);
        }

        return products;
    }

   
/*
    public String updateProductWithoutImage(String prevProductId, ProductBean updatedProduct) throws SQLException {
        String status = "Product Updation Failed!";

        if (!prevProductId.equals(updatedProduct.getId())) {

            status = "Both Products are Different, Update Failed!";

            return status;
        }

        Connection con = DBUtil.getConnection();

        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("update product set pname=?,ptype=?,pinfo=?,pprice=?,pquantity=? where pid=? AND active = 1");

            ps.setString(1, updatedProduct.getProdName());
            ps.setString(2, updatedProduct.getProdType());
            ps.setString(3, updatedProduct.getProdInfo());
            ps.setDouble(4, updatedProduct.getProdPrice());
            ps.setInt(5, updatedProduct.getProdQuantity());
            ps.setString(6, prevProductId);

            int k = ps.executeUpdate();

            if (k > 0)
                status = "Product Updated Successfully!";

        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBUtil.closeConnection(ps);

        return status;
    }

    public double getProductPrice(String prodId) throws SQLException {
        double price = 0;

        Connection con = DBUtil.getConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("select * from product where pid=? AND active = 1");

            ps.setString(1, prodId);
            rs = ps.executeQuery();

            if (rs.next()) {
                price = rs.getDouble("pprice");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
   
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
        }

        return price;
    }

    public boolean sellNProduct(String prodId, int n) throws SQLException {
        boolean flag = false;

        Connection con = DBUtil.getConnection();

        PreparedStatement ps = null;

        try {

            ps = con.prepareStatement("update product set pquantity=(pquantity - ?) where pid=? AND active = 1");

            ps.setInt(1, n);

            ps.setString(2, prodId);

            int k = ps.executeUpdate();

            if (k > 0)
                flag = true;
        } catch (SQLException e) {
            flag = false;
            e.printStackTrace();
        }

  
        DBUtil.closeConnection(ps);

        return flag;
    }

    public int getProductQuantity(String prodId) throws SQLException {

        int quantity = 0;

        Connection con = DBUtil.getConnection();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("select * from product where pid=? AND active = 1");

            ps.setString(1, prodId);
            rs = ps.executeQuery();

            if (rs.next()) {
                quantity = rs.getInt("pquantity");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
  
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
        }

        return quantity;
    }
    
    public List<ProductBean> getProductsByName(String name) throws SQLException {
        List<ProductBean> products = new ArrayList<>();

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT * FROM product WHERE lower(pname) LIKE ? AND active = 1");
            ps.setString(1, "%" + name.toLowerCase() + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                ProductBean product = new ProductBean();
                product.setProdId(rs.getString("pid"));
                product.setProdName(rs.getString("pname"));
                product.setProdType(rs.getString("ptype"));
                product.setProdInfo(rs.getString("pinfo"));
                product.setProdPrice(rs.getDouble("pprice"));
                product.setProdQuantity(rs.getInt("pquantity"));
                product.setProdImage(rs.getString("image"));
                // Assuming prodImage is stored as Blob or InputStream in the database
                // product.setProdImage(rs.getAsciiStream("image")); // Uncomment and adjust if needed

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
        }

        return products;
    }
*/
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
            ps = con.prepareStatement("SELECT * FROM Fumetto WHERE id=? AND active = 1");
            ps.setInt(1,idfumetto );
            rs = ps.executeQuery();

            if (rs.next()) {
                product = new ProductBean();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString(2));
                product.setDescription(rs.getString(3));
                product.setPrice(rs.getFloat(4));
                // Assuming prodImage is stored as Blob or InputStream in the database
                // product.setProdImage(rs.getAsciiStream("image")); // Uncomment and adjust if needed
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
       
