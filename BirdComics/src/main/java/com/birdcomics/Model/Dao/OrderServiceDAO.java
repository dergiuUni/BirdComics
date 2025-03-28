package com.birdcomics.Model.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.birdcomics.Model.Bean.FatturaBean;
import com.birdcomics.Model.Bean.OrderBean;
import com.birdcomics.Model.Bean.ScaffaliBean;
import com.birdcomics.Utils.DBUtil;

public class OrderServiceDAO {

    // Ottieni l'istanza singleton di DBUtil
    private DBUtil dbUtil = DBUtil.getInstance();

    public boolean addOrder(OrderBean order) throws SQLException {
        boolean flag = true;

        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("insert into Ordine(emailUtente, idpaypal, shipped, dataEffettuato, idFattura) values(?,?,?,?,?)", java.sql.Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, order.getIdUtente());
            ps.setString(2, order.getIdPaypal());
            ps.setString(3, order.getShipped());
            ps.setDate(4, order.getDataEffettuato());
            FatturaServiceDAO ft = new FatturaServiceDAO();
            ft.addFattura(order.getIdFattura());
            ps.setInt(5, order.getIdFattura().getId());
            ps.executeUpdate();

            ResultSet rs = null;
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                order.setId(rs.getInt(1)); // O getInt(1) se l'ID è un int

                order.getFumetti().forEach((key, value) -> {
                    try {
                        PreparedStatement psscaf;
                        PreparedStatement psFumetto, psFumetto2 = con.prepareStatement("select Magazzino.nome, Scaffali.id, Scaffali.quantita from Magazzino, Scaffali, MagazzinoScaffali where Magazzino.nome = MagazzinoScaffali.idMagazzino and MagazzinoScaffali.idScaffale = Scaffali.id and Scaffali.idFumetto = ? order by Scaffali.quantita DESC");
                        psFumetto2.setInt(1, key.getId());

                        ResultSet rsFumetto = psFumetto2.executeQuery();

                        int quantita = 0, i;

                        while (quantita < value && rsFumetto.next()) {
                            ScaffaliBean s = new ScaffaliBean();
                            s.setId(rsFumetto.getInt("Scaffali.id"));
                            System.out.println("scaffale id " + rsFumetto.getInt("Scaffali.id"));
                            ScaffaleDao sc = new ScaffaleDao();

                            psFumetto = con.prepareStatement("insert into Ordine_Magazzino(idOrdine, nomeMagazzino, idFumetto, nome, descrizione, prezzo, quantita, idScaffale) values(?,?,?,?,?,?,?,?)");
                            psFumetto.setInt(1, order.getId());
                            psFumetto.setString(2, rsFumetto.getString("Magazzino.nome"));
                            psFumetto.setInt(3, key.getId());
                            psFumetto.setString(4, key.getName());
                            psFumetto.setString(5, key.getDescription());
                            psFumetto.setFloat(6, key.getPrice());
                            psFumetto.setInt(8, s.getId());

                            int quantitaScaffale = rsFumetto.getInt("Scaffali.quantita");

                            if ((value - quantita) == quantitaScaffale || (value - quantita) > quantitaScaffale) {
                                psFumetto.setInt(7, quantitaScaffale);
                                psFumetto.executeUpdate();
                                quantita += quantitaScaffale;
                                s.setQuantitaOccupata(0);
                                sc.modifyQuantityFumetto(s);
                                quantitaScaffale = 0;
                            }
                            if ((value - quantita) < quantitaScaffale) {
                                psFumetto.setInt(7, (value - quantita));
                                psFumetto.executeUpdate();
                                s.setQuantitaOccupata(quantitaScaffale - (value - quantita));
                                quantita = value;
                                sc.modifyQuantityFumetto(s);
                            }
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                flag = true;
            }

        } catch (SQLException e) {
            flag = false;
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return flag;
    }

    public List<OrderBean> getAllOrderDetails(String userEmailId) throws SQLException {
        List<OrderBean> orderList = new ArrayList<OrderBean>();

        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT * FROM Ordine WHERE emailUtente = ?");
            ps.setString(1, userEmailId);
            rs = ps.executeQuery();

            while (rs.next()) {
                OrderBean order = new OrderBean();
                order.setIdUtente(rs.getString("emailUtente"));
                order.setId(rs.getInt("id"));
                FatturaBean f = new FatturaBean();
                FatturaServiceDAO fd = new FatturaServiceDAO();
                f = fd.getFatturaById(rs.getInt("idFattura"));
                order.setIdFattura(f);
                order.setIdPaypal(rs.getString("idpaypal"));
                order.setShipped(rs.getString("shipped"));
                order.setDataEffettuato(rs.getDate("dataEffettuato"));
                orderList.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(rs); // Chiudi il ResultSet
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return orderList;
    }

    public boolean updateShipped(OrderBean order) throws SQLException {
        boolean flag = false;
        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement("UPDATE Ordine SET shipped = ? WHERE id = ?");
            ps.setString(1, order.getShipped());
            ps.setInt(2, order.getId());
            ps.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return flag;
    }

    public List<OrderBean> getAllOrderDetailsNoShipped() throws SQLException {
        List<OrderBean> orderList = new ArrayList<OrderBean>();

        Connection con = dbUtil.getConnection(); // Usa l'istanza singleton
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT * FROM Ordine WHERE shipped = ?");
            ps.setString(1, "ORDER PLACED");
            rs = ps.executeQuery();

            while (rs.next()) {
                OrderBean order = new OrderBean();
                order.setIdUtente(rs.getString("emailUtente"));
                order.setId(rs.getInt("id"));
                FatturaBean f = new FatturaBean();
                FatturaServiceDAO fd = new FatturaServiceDAO();
                f = fd.getFatturaById(rs.getInt("idFattura"));
                order.setIdFattura(f);
                order.setIdPaypal(rs.getString("idpaypal"));
                order.setShipped(rs.getString("shipped"));
                order.setDataEffettuato(rs.getDate("dataEffettuato"));
                orderList.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(rs); // Chiudi il ResultSet
            dbUtil.closeConnection(ps); // Chiudi il PreparedStatement
        }

        return orderList;
    }
}