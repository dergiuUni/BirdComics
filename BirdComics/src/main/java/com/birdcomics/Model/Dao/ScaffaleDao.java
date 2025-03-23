package com.birdcomics.Model.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.birdcomics.Model.Bean.ProductBean;
import com.birdcomics.Model.Bean.ScaffaliBean;
import com.birdcomics.Utils.DBUtil;

public class ScaffaleDao {

    // Ottieni l'istanza singleton di DBUtil
    private DBUtil dbUtil = DBUtil.getInstance();

    public ScaffaliBean addScaffale(int quantitaMassima) throws SQLException {
        String status = "Scaffale Registration Failed!";
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        if (con != null) {
            System.out.println("Connected Successfully!");
        }

        try {
            ps = con.prepareStatement("INSERT INTO Scaffali (quantitaMassima) VALUES (?)", java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, quantitaMassima);

            int k = ps.executeUpdate();

            if (k > 0) {
                ResultSet rs = null;
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    ScaffaliBean scaffale = new ScaffaliBean();
                    scaffale.setId(rs.getInt(1)); // O getInt(1) se l'ID è un int
                    status = "Scaffale Registered Successfully!";
                    return scaffale;
                }
            }

        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return null;
    }

    public String removeScaffale(ProductBean fumetto) throws SQLException {
        String status = "Scaffale Removal Failed!";
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        if (con != null) {
            System.out.println("Connected Successfully!");
        }

        try {
            ps = con.prepareStatement("DELETE FROM Scaffali where id = ?");
            ps.setInt(1, fumetto.getId());
            int k = ps.executeUpdate();

            if (k > 0) {
                status = "Scaffale Removed Successfully!";
            }

        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return status;
    }

    public String addFumetto(ScaffaliBean scaffale) throws SQLException {
        String status = "Fumetto Addition to Scaffale Failed!";
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        if (con != null) {
            System.out.println("Connected Successfully!");
        }

        try {
            ps = con.prepareStatement("INSERT INTO Scaffali (quantita, idFumetto) VALUES (?, ?)");
            ps.setInt(1, scaffale.getQuantitaOccupata());
            ps.setInt(2, scaffale.getFumetto().getId());

            int k = ps.executeUpdate();

            if (k > 0) {
                status = "Fumetto Added to Scaffale Successfully!";
            }

        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return status;
    }

    public String modifyQuantityFumetto(ScaffaliBean scaffale) throws SQLException {
        String status = "Fumetto Quantity Modification Failed!";
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        if (con != null) {
            System.out.println("Connected Successfully!");
        }

        try {
            ps = con.prepareStatement("UPDATE Scaffali SET quantita = ? where id = ?");
            ps.setInt(1, scaffale.getQuantitaOccupata());
            ps.setInt(2, scaffale.getId());

            int k = ps.executeUpdate();

            if (k > 0) {
                status = "Fumetto Quantity Modified Successfully!";
            }

        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return status;
    }

    public String modifyFumetto(ScaffaliBean scaffale) throws SQLException {
        String status = "Fumetto Modification Failed!";
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        if (con != null) {
            System.out.println("Connected Successfully!");
        }

        try {
            ps = con.prepareStatement("UPDATE Scaffali SET quantita = ?, idFumetto = ? where id = ?");
            ps.setInt(1, scaffale.getQuantitaOccupata());
            ps.setInt(2, scaffale.getFumetto().getId());
            ps.setInt(3, scaffale.getId());

            int k = ps.executeUpdate();

            if (k > 0) {
                status = "Fumetto Modified Successfully!";
            }

        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return status;
    }

    public String deleteFumetto(ScaffaliBean scaffale) throws SQLException {
        String status = "Fumetto Deletion Failed!";
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        if (con != null) {
            System.out.println("Connected Successfully!");
        }

        try {
            ps = con.prepareStatement("UPDATE Scaffali SET quantita = null, idFumetto = null where id = ?");
            ps.setInt(1, scaffale.getId());

            int k = ps.executeUpdate();

            if (k > 0) {
                status = "Fumetto Deleted Successfully!";
            }

        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return status;
    }

    public ArrayList<ScaffaliBean> getScaffaleMagazzino(String nomeMagazzino) throws SQLException {
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        ArrayList<ScaffaliBean> scaffali = new ArrayList<ScaffaliBean>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("select idScaffale, quantita, quantitaMassima, idFumetto from MagazzinoScaffali, Scaffali where idMagazzino=? and idScaffale = id");
            ps.setString(1, nomeMagazzino);
            rs = ps.executeQuery();

            while (rs.next()) {
                ProductBean fumetto = new ProductBean();
                ProductServiceDAO ser = new ProductServiceDAO();
                fumetto = ser.getProductsByID(String.valueOf(rs.getInt("idFumetto")));

                ScaffaliBean scaffale = new ScaffaliBean(rs.getInt("idScaffale"), fumetto, rs.getInt("quantita"), rs.getInt("quantitaMassima"));
                scaffali.add(scaffale);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
            dbUtil.closeConnection(rs); // Chiudi il ResultSet
        }

        return scaffali;
    }
}
