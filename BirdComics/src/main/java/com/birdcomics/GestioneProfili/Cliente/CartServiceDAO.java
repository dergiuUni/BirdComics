package com.birdcomics.GestioneProfili.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.DatabaseImplementator.DBUtil;
import com.birdcomics.GestioneCatalogo.ProductBean;
import com.birdcomics.GestioneCatalogo.ProductServiceDAO;

public class CartServiceDAO {

    public String addProductToCart(String userId, String prodId, int prodQty) throws SQLException {
        String status = "Failed to Add into Cart";

        Connection con = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT * FROM usercart WHERE username=? AND prodid=?");
            ps.setString(1, userId);
            ps.setString(2, prodId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int cartQuantity = rs.getInt("quantity");
                ProductBean product = new ProductServiceDAO().getProductDetails(prodId);

                // Verifica se il prodotto è attivo
                if (!product.isActive()) {
                    return "Cannot add inactive product to cart.";
                }

                int availableQty = product.getProdQuantity();
                prodQty += cartQuantity;

                if (availableQty < prodQty) {
                    status = updateProductToCart(userId, prodId, availableQty);
                    status = "Only " + availableQty + " units of " + product.getProdName() +
                             " are available in the shop. Added maximum available units to your cart.";
                } else {
                    status = updateProductToCart(userId, prodId, prodQty);
                }
            } else {
                // Se il prodotto non è nel carrello, controlla se è attivo prima di aggiungerlo
                ProductBean product = new ProductServiceDAO().getProductDetails(prodId);
                if (!product.isActive()) {
                    return "Cannot add inactive product to cart.";
                }

                ps = con.prepareStatement("INSERT INTO usercart VALUES (?, ?, ?)");
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
            ps = con.prepareStatement("SELECT uc.username, uc.prodid, uc.quantity, p.active " +
                                       "FROM usercart uc " +
                                       "JOIN product p ON uc.prodid = p.pid " +
                                       "WHERE uc.username=? AND p.active = 1 and p.pquantity > 0");
            ps.setString(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                CartBean cart = new CartBean();
                cart.setUserId(rs.getString("username"));
                cart.setProdId(rs.getString("prodid"));
                cart.setQuantity(rs.getInt("quantity"));
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
            ps = con.prepareStatement("SELECT SUM(quantity) FROM usercart WHERE username=?");
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
            ps = con.prepareStatement("SELECT * FROM usercart WHERE username=? AND prodid=?");
            ps.setString(1, userId);
            ps.setString(2, prodId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int prodQuantity = rs.getInt("quantity");

                // Controllo se il prodotto è ancora attivo
                ProductBean product = new ProductServiceDAO().getProductDetails(prodId);
                if (!product.isActive()) {
                    return "Cannot remove inactive product from cart.";
                }

                prodQuantity -= 1;

                if (prodQuantity > 0) {
                    ps2 = con.prepareStatement("UPDATE usercart SET quantity=? WHERE username=? AND prodid=?");
                    ps2.setInt(1, prodQuantity);
                    ps2.setString(2, userId);
                    ps2.setString(3, prodId);

                    int k = ps2.executeUpdate();
                    if (k > 0) {
                        status = "Product Successfully removed from the Cart!";
                    }
                } else if (prodQuantity <= 0) {
                    ps2 = con.prepareStatement("DELETE FROM usercart WHERE username=? AND prodid=?");
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
            ps = con.prepareStatement("DELETE FROM usercart WHERE username=? AND prodid=?");
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
            ps = con.prepareStatement("SELECT * FROM usercart WHERE username=? AND prodid=?");
            ps.setString(1, userId);
            ps.setString(2, prodId);
            rs = ps.executeQuery();

            if (rs.next()) {
                if (prodQty > 0) {
                    ps2 = con.prepareStatement("UPDATE usercart SET quantity=? WHERE username=? AND prodid=?");
                    ps2.setInt(1, prodQty);
                    ps2.setString(2, userId);
                    ps2.setString(3, prodId);

                    int k = ps2.executeUpdate();
                    if (k > 0) {
                        status = "Product Successfully Updated to Cart!";
                    }
                } else if (prodQty == 0) {
                    ps2 = con.prepareStatement("DELETE FROM usercart WHERE username=? AND prodid=?");
                    ps2.setString(1, userId);
                    ps2.setString(2, prodId);

                    int k = ps2.executeUpdate();
                    if (k > 0) {
                        status = "Product Successfully Updated in Cart!";
                    }
                }
            } else {
                ps2 = con.prepareStatement("INSERT INTO usercart VALUES (?, ?, ?)");
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
            ps = con.prepareStatement("SELECT SUM(quantity) FROM usercart WHERE username=? AND prodid=?");
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
            ps = con.prepareStatement("SELECT quantity FROM usercart WHERE username=? AND prodid=?");
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
	            ps = con.prepareStatement("DELETE FROM usercart WHERE username=?");
	            ps.setString(1, userName);

	            ps.executeUpdate();

	        } catch (SQLException e) {
	        	e.printStackTrace();
	        } finally {
	            DBUtil.closeConnection(ps);
	        }

	    
	}
}

