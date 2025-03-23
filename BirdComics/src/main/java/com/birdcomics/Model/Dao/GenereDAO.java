package com.birdcomics.Model.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.Model.Bean.GenereBean;
import com.birdcomics.Utils.DBUtil;

public class GenereDAO {

    // Ottieni l'istanza singleton di DBUtil
    private DBUtil dbUtil = DBUtil.getInstance();

    public List<GenereBean> getGeneri() {
        List<GenereBean> generi = new ArrayList<>();
        String query = "SELECT * FROM Genere";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Ottieni una connessione dal singleton DBUtil
            con = dbUtil.getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                generi.add(new GenereBean(rs.getString("genere")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Chiudi le risorse utilizzando il singleton DBUtil
            dbUtil.closeConnection(rs);
            dbUtil.closeConnection(ps);
            dbUtil.closeConnection(con);
        }

        return generi;
    }
}