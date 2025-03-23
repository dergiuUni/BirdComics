package com.birdcomics.Model.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

import com.birdcomics.Model.Bean.CartBean;
import com.birdcomics.Model.Bean.CartItem;
import com.birdcomics.Utils.DBUtil;

public class CartServiceDAO {

    // Ottieni l'istanza singleton di DBUtil
    private DBUtil dbUtil = DBUtil.getInstance();

    // 🔹 Metodo per ottenere il carrello dalla sessione
    public CartBean getCartFromSession(HttpSession session, String email) {
        CartBean cart = (CartBean) session.getAttribute("cartBean");
        if (cart == null) {
            cart = new CartBean(email);
            session.setAttribute("cartBean", cart);
        }
        return cart;
    }

    // 🔹 Metodo per aggiungere un prodotto al carrello
    @SuppressWarnings("resource")
    public String addProductToCart(HttpSession session, String email, String prodId, int prodQty) throws SQLException {
        CartBean cart = getCartFromSession(session, email);

        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT quantita FROM CarrelloCliente WHERE id=? AND idFumetto=?");
            ps.setString(1, email);
            ps.setString(2, prodId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int currentQty = rs.getInt("quantita");
                prodQty += currentQty;
                updateProductToCart(email, prodId, prodQty);
            } else {
                ps = con.prepareStatement("INSERT INTO CarrelloCliente (id, idFumetto, quantita) VALUES (?, ?, ?)");
                ps.setString(1, email);
                ps.setString(2, prodId);
                ps.setInt(3, prodQty);
                ps.executeUpdate();
            }

            // Ricarica il carrello dal database
            loadCartFromDB(session, email);  // Aggiungi questa riga per ricaricare il carrello dalla base dati
            return "Product added to cart successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error adding product to cart: " + e.getMessage();
        } finally {
            dbUtil.closeConnection(ps); // Usa l'istanza singleton
            dbUtil.closeConnection(rs); // Usa l'istanza singleton
        }
    }

    // 🔹 Metodo per rimuovere un prodotto dal carrello
    public String removeProductFromCart(HttpSession session, String email, String prodId) throws SQLException {
        CartBean cart = getCartFromSession(session, email);

        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("DELETE FROM CarrelloCliente WHERE id=? AND idFumetto=?");
            ps.setString(1, email);
            ps.setString(2, prodId);
            ps.executeUpdate();

            // Rimuovi il prodotto dal carrello nella sessione
            cart.removeItem(prodId);
            session.setAttribute("cartBean", cart);

            // Ricarica il carrello dal database
            loadCartFromDB(session, email);  // Aggiungi questa riga per ricaricare il carrello dalla base dati
            return "Product removed from cart successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error removing product from cart: " + e.getMessage();
        } finally {
            dbUtil.closeConnection(ps); // Usa l'istanza singleton
        }
    }

    // 🔹 Metodo per aggiornare la quantità di un prodotto nel carrello
    public String updateProductToCart(String email, String prodId, int prodQty) throws SQLException {
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        try {
            if (prodQty > 0) {
                ps = con.prepareStatement("UPDATE CarrelloCliente SET quantita=? WHERE id=? AND idFumetto=?");
                ps.setInt(1, prodQty);
                ps.setString(2, email);
                ps.setString(3, prodId);
                ps.executeUpdate();
            } else {
                ps = con.prepareStatement("DELETE FROM CarrelloCliente WHERE id=? AND idFumetto=?");
                ps.setString(1, email);
                ps.setString(2, prodId);
                ps.executeUpdate();
            }

            return "Product updated in cart successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error updating cart: " + e.getMessage();
        } finally {
            dbUtil.closeConnection(ps); // Usa l'istanza singleton
        }
    }

    // 🔹 Metodo per ottenere tutti gli articoli del carrello dal database
    public CartBean loadCartFromDB(HttpSession session, String email) throws SQLException {
        // Crea un nuovo carrello per l'utente
        CartBean cart = new CartBean(email);

        // Connessione al database
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Prepara la query per ottenere tutti gli articoli del carrello dell'utente
            ps = con.prepareStatement("SELECT idFumetto, quantita FROM CarrelloCliente WHERE id=?");
            ps.setString(1, email);  // Imposta l'ID dell'utente

            rs = ps.executeQuery();

            // Scorri i risultati e aggiungi gli articoli al carrello
            while (rs.next()) {
                String prodId = rs.getString("idFumetto");
                int quantity = rs.getInt("quantita");

                // Aggiungi ogni articolo al carrello
                CartItem item = new CartItem(prodId, quantity);
                cart.addItem(item);
            }

            // Memorizza il carrello nella sessione
            session.setAttribute("cartBean", cart);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Chiudi le risorse
            dbUtil.closeConnection(ps); // Usa l'istanza singleton
            dbUtil.closeConnection(rs); // Usa l'istanza singleton
        }

        // Restituisci il carrello caricato
        return cart;
    }

    public String deleteAllCartItems(HttpSession session, String email) throws SQLException {
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("DELETE FROM CarrelloCliente WHERE id=?");
            ps.setString(1, email);
            ps.executeUpdate();

            // Rimuovi il carrello dalla sessione
            session.removeAttribute("cartBean");

            return "All cart items deleted successfully!";
        } catch (SQLException e) {
            e.printStackTrace(); // Stampa l'errore per debug
            return "Error deleting all cart items: " + e.getMessage(); // Ritorna un messaggio di errore
        } finally {
            dbUtil.closeConnection(ps); // Usa l'istanza singleton
        }
    }

}