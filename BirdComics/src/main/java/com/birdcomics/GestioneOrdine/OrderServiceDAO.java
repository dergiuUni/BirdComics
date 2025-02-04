package com.birdcomics.GestioneOrdine;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.DatabaseImplementator.DBUtil;
import com.birdcomics.GestioneCatalogo.ProductBean;
import com.birdcomics.GestioneCatalogo.ProductServiceDAO;
//import com.birdcomics.GestioneProfili.Cliente.CartBean;
//import com.birdcomics.GestioneProfili.Cliente.CartServiceDAO;
import com.birdcomics.GestioneMagazzino.MagazzinoBean;
import com.birdcomics.GestioneMagazzino.MagazzinoDao;
import com.birdcomics.GestioneMagazzino.ScaffaliBean;


public class OrderServiceDAO {


	public boolean addOrder(OrderBean order) throws SQLException {
		boolean flag = false;

		Connection con = DBUtil.getConnection();

		PreparedStatement ps = null;

		try {
			//Ordine_Fumetto, Ordine_Magazzino
			// vedere la quantita che ordina, prendo il magazzino con quantita maggiore. se non basta procedo con il prossimo e faccio piu ordini ognuno per il magazzino selezionato finche non raggiungo la somma scelta. In un modo o nell'altro la raggiungo altrimenti la index mi blocca
			//quindi creo l'ordine ed inserisco i dati generali
			//inserisco nelle tabelle ordine_fumetto ed ordine_magazzino rispettivamente i dati richiesti dove magazzino puo essere associati a molti
						
			ps = con.prepareStatement("insert into Ordine(emailUtente, idpaypal, shipped, dataEffettuato, idFattura) values(?,?,?,?,?)", java.sql.Statement.RETURN_GENERATED_KEYS );

			ps.setString(1, order.getEmailUtente());
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
                		PreparedStatement psFumetto = con.prepareStatement("select Magazzino.nome, Scaffali.id, Scaffali.quantita from Magazzino, Scaffali, MagazzinoScaffali where Magazzino.nome = MagazzinoScaffali.idMagazzino and MagazzinoScaffali.idScaffale = Scaffali.id and Scaffali.idFumetto = ? order by Scaffali.quantita DESC");
                        psFumetto.setInt(1, key.getId());

                        ResultSet rsFumetto = psFumetto.executeQuery();
                        
                    	//rsFumetto.getString("Magazzino.nome");
                        //rsFumetto.getInt("Scaffali.id");
                    	int quantita = 0;
                    	
                    	while(quantita < value && rsFumetto.next()) {
                			psFumetto = con.prepareStatement("insert into Ordine_Magazzino(idOrdine, nomeMagazzino, idFumetto, nome, descrizione, prezzo, quantità) values(?,?,?,?,?,?,?)");
                            psFumetto.setInt(1, order.getId());
                            psFumetto.setString(2, rsFumetto.getString("Magazzino.nome"));
                            psFumetto.setInt(3, key.getId());
                            psFumetto.setString(4, key.getName());
                            psFumetto.setString(5, key.getDescription());
                            psFumetto.setFloat(6, key.getPrice());
                            
                            if((value-quantita) == rsFumetto.getInt("Scaffali.quantita") || (value-quantita) > rsFumetto.getInt("Scaffali.quantita")) {
                            	psFumetto.setInt(7, value);
                            	quantita += value;
                            	// update scaffale
                            	psscaf = con.prepareStatement("UPDATE Scaffali SET quantita = 0 WHERE id = ?");
                            	psscaf.setInt(1, rsFumetto.getInt("Scaffali.id"));
                            	psscaf.executeUpdate();
                            }
                            
                            if ((value-quantita) < rsFumetto.getInt("Scaffali.quantita")) {
                            	
                            	psFumetto.setInt(1, (value - quantita));
                                	// update scaffale rsFumetto.getInt("Scaffali.quantita") - (value - quantita)
                            	quantita = value;
                            	psscaf = con.prepareStatement("UPDATE Scaffali SET quantita = ? WHERE id = ?");
                            	psscaf.setInt(1, rsFumetto.getInt("Scaffali.quantita") - (value - quantita));
                            	psscaf.setInt(2, rsFumetto.getInt("Scaffali.id"));
                            	psscaf.executeUpdate();
                            }
                    	}
                        	  
                	} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                             	
                });
       
        	}
                

		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
		}

		return flag;
	}


	public List<OrderBean> getAllOrderDetails(String userEmailId) throws SQLException {
		List<OrderBean> orderList = new ArrayList<OrderBean>();

		Connection con =  DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {	
			ps = con.prepareStatement("SELECT * FROM Ordine WHERE emailUtente = ?");
			ps.setString(1, userEmailId);
			rs = ps.executeQuery();

			while (rs.next()) {

				OrderBean order = new OrderBean();
				order.setEmailUtente(rs.getString("emailUtente"));
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
		}

		return orderList;
	}
	
	
	public boolean updateShipped(OrderBean order) throws SQLException {
		boolean flag = false;
		Connection con = DBUtil.getConnection();
		PreparedStatement ps = null;

    	try {
	    	ps = con.prepareStatement("UPDATE Ordine SET shipped = ? WHERE id = ?");
	    	ps.setString(1, order.getShipped());
	    	ps.setInt(2, order.getId());
	    	ps.executeUpdate();
                        	  
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}
	
	public List<OrderBean> getAllOrderDetailsNoShipped() throws SQLException {
		List<OrderBean> orderList = new ArrayList<OrderBean>();

		Connection con =  DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {	
			ps = con.prepareStatement("SELECT * FROM Ordine WHERE shipped = ? ");
			ps.setString(1, "Non Spedito");
			rs = ps.executeQuery();

			while (rs.next()) {

				OrderBean order = new OrderBean();
				order.setEmailUtente(rs.getString("emailUtente"));
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
		}

		return orderList;
	}

}
