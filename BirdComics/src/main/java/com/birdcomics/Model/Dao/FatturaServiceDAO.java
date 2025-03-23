package com.birdcomics.Model.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.Model.Bean.FatturaBean;
import com.birdcomics.Utils.DBUtil;

public class FatturaServiceDAO {

    // Ottieni l'istanza singleton di DBUtil
    private DBUtil dbUtil = DBUtil.getInstance();

    public boolean addFattura(FatturaBean fattura) throws SQLException {
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("insert into Fattura(iva, nome, cognome, telefono, nomeCittaCliente, viaCliente, numeroCivicoCliente, capCliente) values(?,?,?,?,?,?,?,?)", java.sql.Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, fattura.getIva());
            ps.setString(2, fattura.getNome());
            ps.setString(3, fattura.getCognome());
            ps.setString(4, fattura.getTelefono());
            ps.setString(5, fattura.getNomeCittaCliente());
            ps.setString(6, fattura.getViaCliente());
            ps.setString(7, fattura.getNumeroCivicoCliente());
            ps.setString(8, fattura.getCapCliente());

            int k = ps.executeUpdate();

            ResultSet rs = null;
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                fattura.setId(rs.getInt(1)); // O getInt(1) se l'ID è un int
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return false;
    }

    public List<FatturaBean> getAllFattura() throws SQLException {
        List<FatturaBean> fatturaList = new ArrayList<>();
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("select * from Fattura");
            rs = ps.executeQuery();

            while (rs.next()) {
                FatturaBean fattura = new FatturaBean(
                    rs.getInt("id"),
                    rs.getInt("iva"),
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("telefono"),
                    rs.getString("nomeCittaCliente"),
                    rs.getString("viaCliente"),
                    rs.getString("numeroCivicoCliente"),
                    rs.getString("capCliente")
                );
                fatturaList.add(fattura);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(rs); // Chiudi il ResultSet
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return fatturaList;
    }

    public FatturaBean getFatturaById(int idFattura) throws SQLException {
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT * FROM Fattura Where id = ?");
            ps.setInt(1, idFattura);
            rs = ps.executeQuery();

            if (rs.next()) {
                FatturaBean fattura = new FatturaBean(
                    rs.getInt("id"),
                    rs.getInt("iva"),
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("telefono"),
                    rs.getString("nomeCittaCliente"),
                    rs.getString("viaCliente"),
                    rs.getString("numeroCivicoCliente"),
                    rs.getString("capCliente")
                );
                return fattura;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(rs); // Chiudi il ResultSet
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return null;
    }
}