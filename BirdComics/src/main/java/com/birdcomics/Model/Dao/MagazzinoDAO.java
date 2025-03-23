package com.birdcomics.Model.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.birdcomics.Model.Bean.IndirizzoBean;
import com.birdcomics.Model.Bean.MagazzinoBean;
import com.birdcomics.Model.Bean.ScaffaliBean;
import com.birdcomics.Utils.DBUtil;

public class MagazzinoDAO {

    // Ottieni l'istanza singleton di DBUtil
    private DBUtil dbUtil = DBUtil.getInstance();

    public String addMagazzino(MagazzinoBean magazzino) throws SQLException {
        String status = "add magazzino Failed!";
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        if (con != null) {
            System.out.println("Connected Successfully!");
        }

        try {
            IndirizzoDAO in = new IndirizzoDAO(con);
            if (!in.ifExists(magazzino.getIndirizzo().getNomeCitta(), magazzino.getIndirizzo().getVia(), magazzino.getIndirizzo().getNumeroCivico(), magazzino.getIndirizzo().getCap())) {
                ps = con.prepareStatement("INSERT INTO Indirizzo (nomeCitta, via, numeroCivico, cap) VALUES (?, ?, ?, ?)");
                ps.setString(1, magazzino.getIndirizzo().getNomeCitta());
                ps.setString(2, magazzino.getIndirizzo().getVia());
                ps.setString(3, magazzino.getIndirizzo().getNumeroCivico());
                ps.setString(4, magazzino.getIndirizzo().getCap());
                ps.executeUpdate();
            }

            ps = con.prepareStatement("INSERT INTO Magazzino (nome, nomeCitta, via, numeroCivico, cap) VALUES (?, ?, ?, ?, ?)");

            ps.setString(1, magazzino.getNome());
            ps.setString(2, magazzino.getIndirizzo().getNomeCitta());
            ps.setString(3, magazzino.getIndirizzo().getVia());
            ps.setString(4, magazzino.getIndirizzo().getNumeroCivico());
            ps.setString(5, magazzino.getIndirizzo().getCap());

            int k = ps.executeUpdate();

            if (k > 0) {
                status = "magazzino Registered Successfully!";
            }

        } catch (SQLException e) {
            status = "Error sql";
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }
        return status;
    }

    public String removeMagazzino(MagazzinoBean magazzino) throws SQLException {
        String status = "Delete magazzino Failed!";
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        if (con != null) {
            System.out.println("Connected Successfully!");
        }

        try {
            ps = con.prepareStatement("DELETE FROM Magazzino where nome = ?");
            ps.setString(1, magazzino.getNome());
            ps.executeUpdate();

            status = "magazzino cancellato";

        } catch (SQLException e) {
            status = "Error sql";
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return status;
    }

    public ArrayList<MagazzinoBean> getMagazzini() throws SQLException {
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        ArrayList<MagazzinoBean> magazzini = new ArrayList<MagazzinoBean>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("select * from Magazzino");
            rs = ps.executeQuery();

            while (rs.next()) {
                ScaffaleDao sc = new ScaffaleDao();
                IndirizzoBean in = new IndirizzoBean(rs.getString("nomeCitta"), rs.getString("via"), rs.getString("numeroCivico"), rs.getString("cap"));
                MagazzinoBean mag = new MagazzinoBean(rs.getString("nome"), in, sc.getScaffaleMagazzino(rs.getString("nome")));
                magazzini.add(mag);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
            dbUtil.closeConnection(rs); // Chiudi il ResultSet
        }

        return magazzini;
    }
}