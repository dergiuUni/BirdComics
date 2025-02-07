package com.birdcomics.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.Bean.CartBean;
import com.birdcomics.Bean.ProductBean;
import com.birdcomics.Utils.DBUtil;

public class CartServiceDAO {

    public String addProductToCart(String userId, String prodId, int prodQty) throws SQLException {
        String status = "Failed to Add into Cart";

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT * FROM CarrelloCliente WHERE id=? AND idFumetto=?");
            ps.setString(1, userId);
            ps.setString(2, prodId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int cartQuantity = rs.getInt("quantity");
                ProductBean product = new ProductServiceDAO().getProductsByID(prodId);

                // Verifica se il prodotto è attivo
                /*
                if (!product.isActive()) {
                    return "Cannot add inactive product to cart.";
                }
*/
                //int availableQty = product.getProdQuantity();
                int availableQty = 500;
                prodQty += cartQuantity;

                if (availableQty < prodQty) {
                    status = updateProductToCart(userId, prodId, availableQty);
                    status = "Only " + availableQty + " units of " + product.getName() +
                             " are available in the shop. Added maximum available units to your cart.";
                } else {
                    status = updateProductToCart(userId, prodId, prodQty);
                }
            } else {
            	 /*
                // Se il prodotto non è nel carrello, controlla se è attivo prima di aggiungerlo
                ProductBean product = new ProductServiceDAO().getProductsByID(prodId);
               
                if (!product.isActive()) {
                    return "Cannot add inactive product to cart.";
                }
*/
                ps = con.prepareStatement("INSERT INTO CarrelloCliente VALUES (?, ?, ?)");
                ps.setString(1, userId);
                ps.setString(2, prodId);
                ps.setInt(3, prodQty);

                int k = ps.executeUpdate();
                if (k > 0) {
                    status = "Product Successfully added to Cart!";
                }
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
        }

        return status;
    }

    public List<CartBean> getAllCartItems(String userId) throws SQLException {
        List<CartBean> items = new ArrayList<>();

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
        	/*
            ps = con.prepareStatement("SELECT uc.id, uc.idFumetto, uc.quantita, p.active FROM CarrelloCliente uc \r\n"
            		+ "JOIN Fumetto p ON uc.idFumetto = p.id WHERE uc.id=? AND p.active = 1;");
            		*/
        	 ps = con.prepareStatement("SELECT uc.id, uc.idFumetto, uc.quantita FROM CarrelloCliente uc \r\n"
             		+ "JOIN Fumetto p ON uc.idFumetto = p.id WHERE uc.id=? ;");
            //qui bisogna controllare quantita > 0  and p.quantita > 0
            ps.setString(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                CartBean cart = new CartBean();
                cart.setUserId(rs.getString("id"));
                cart.setProdId(rs.getString("idFumetto"));
                cart.setQuantity(rs.getInt("quantita"));
                items.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
        }

        return items;
    }

    public int getCartCount(String userId) throws SQLException {
        int count = 0;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT SUM(quantita) FROM CarrelloCliente WHERE id=?");
            ps.setString(1, userId);
            rs = ps.executeQuery();

            if (rs.next() && !rs.wasNull()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
 
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
        }

        return count;
    }

    public String removeProductFromCart(String userId, String prodId) throws SQLException {
        String status = "Product Removal Failed";

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT * FROM CarrelloCliente WHERE id=? AND idFumetto=?");
            ps.setString(1, userId);
            ps.setString(2, prodId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int prodQuantity = rs.getInt("quantita");

                // Controllo se il prodotto è ancora attivo
                ProductBean product = new ProductServiceDAO().getProductsByID(prodId);
               /*
                if (!product.isActive()) {
                    return "Cannot remove inactive product from cart.";
                }
*/
                prodQuantity -= 1;

                if (prodQuantity > 0) {
                    ps2 = con.prepareStatement("UPDATE CarrelloCliente SET quantita=? WHERE id=? AND idFumetto=?");
                    ps2.setInt(1, prodQuantity);
                    ps2.setString(2, userId);
                    ps2.setString(3, prodId);

                    int k = ps2.executeUpdate();
                    if (k > 0) {
                        status = "Product Successfully removed from the Cart!";
                    }
                } else if (prodQuantity <= 0) {
                    ps2 = con.prepareStatement("DELETE FROM CarrelloCliente WHERE id=? AND idFumetto=?");
                    ps2.setString(1, userId);
                    ps2.setString(2, prodId);

                    int k = ps2.executeUpdate();
                    if (k > 0) {
                        status = "Product Successfully removed from the Cart!";
                    }
                }
            } else {
                status = "Product Not Available in the cart!";
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
            DBUtil.closeConnection(ps2);
        }

        return status;
    }

    public boolean removeAProduct(String userId, String prodId) throws SQLException {
        boolean flag = false;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("DELETE FROM CarrelloCliente WHERE id=? AND idFumetto=?");
            ps.setString(1, userId);
            ps.setString(2, prodId);

            int k = ps.executeUpdate();
            if (k > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            flag = false;
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
        }

        return flag;
    }

    public String updateProductToCart(String userId, String prodId, int prodQty) throws SQLException {
        String status = "Failed to Add into Cart";

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT * FROM CarrelloCliente WHERE id=? AND idFumetto=?");
            ps.setString(1, userId);
            ps.setString(2, prodId);
            rs = ps.executeQuery();

            if (rs.next()) {
                if (prodQty > 0) {
                    ps2 = con.prepareStatement("UPDATE CarrelloCliente SET quantita=? WHERE id=? AND idFumetto=?");
                    ps2.setInt(1, prodQty);
                    ps2.setString(2, userId);
                    ps2.setString(3, prodId);

                    int k = ps2.executeUpdate();
                    if (k > 0) {
                        status = "Product Successfully Updated to Cart!";
                    }
                } else if (prodQty == 0) {
                    ps2 = con.prepareStatement("DELETE FROM CarrelloCliente WHERE id=? AND idFumetto=?");
                    ps2.setString(1, userId);
                    ps2.setString(2, prodId);

                    int k = ps2.executeUpdate();
                    if (k > 0) {
                        status = "Product Successfully Updated in Cart!";
                    }
                }
            } else {
                ps2 = con.prepareStatement("INSERT INTO CarrelloCliente VALUES (?, ?, ?)");
                ps2.setString(1, userId);
                ps2.setString(2, prodId);
                ps2.setInt(3, prodQty);

                int k = ps2.executeUpdate();
                if (k > 0) {
                    status = "Product Successfully Updated to Cart!";
                }
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
            DBUtil.closeConnection(ps2);
        }

        return status;
    }

    public int getProductCount(String userId, String prodId) throws SQLException {
        int count = 0;

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT SUM(quantita) FROM CarrelloCliente WHERE id=? AND idFumetto=?");
            ps.setString(1, userId);
            ps.setString(2, prodId);
            rs = ps.executeQuery();

            if (rs.next() && !rs.wasNull()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
        }

        return count;
    }

    public int getCartItemCount(String userId, String itemId) throws SQLException {
        int count = 0;
        if (userId == null || itemId == null)
            return 0;
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT quantita FROM CarrelloCliente WHERE id=? AND idFumetto=?");
            ps.setString(1, userId);
            ps.setString(2, itemId);

            rs = ps.executeQuery();

            if (rs.next() && !rs.wasNull())
                count = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(ps);
            DBUtil.closeConnection(rs);
        }

        return count;
    }

	public void deleteAllCartItems(String userName) throws SQLException {

	        Connection con = DBUtil.getConnection();
	        PreparedStatement ps = null;

	        try {
	            ps = con.prepareStatement("DELETE FROM CarrelloCliente WHERE id=?");
	            ps.setString(1, userName);

	            ps.executeUpdate();

	        } catch (SQLException e) {
	        	e.printStackTrace();
	        } finally {
	            DBUtil.closeConnection(ps);
	        }

	    
	}
}

