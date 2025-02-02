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

	/*
	public String paymentSuccess(String userName, double paidAmount) throws SQLException {
		String status = "Order Placement Failed!";

		List<CartBean> cartItems = new ArrayList<CartBean>();
		cartItems = new CartServiceDAO().getAllCartItems(userName);

		if (cartItems.size() == 0)
			return status;

		TransactionBean transaction = new TransactionBean(userName, paidAmount);
		boolean ordered = false;

		String transactionId = transaction.getTransactionId();

		// System.out.println("Transaction: "+transaction.getTransactionId()+"
		// "+transaction.getTransAmount()+" "+transaction.getUserName()+"
		// "+transaction.getTransDateTime());

		for (CartBean item : cartItems) {

			double amount = new ProductServiceDAO().getProductPrice(item.getProdId()) * item.getQuantity();

			OrderBean order = new OrderBean(transactionId, item.getProdId(), item.getQuantity(), amount);

			ordered = addOrder(order);
			if (!ordered)
				break;
			else {
				ordered = new CartServiceDAO().removeAProduct(item.getUserId(), item.getProdId());
			}

			if (!ordered)
				break;
			else
				ordered = new ProductServiceDAO().sellNProduct(item.getProdId(), item.getQuantity());

			if (!ordered)
				break;
		}

		if (ordered) {
			ordered = new OrderServiceDAO().addTransaction(transaction);
			if (ordered) {
				status = "Order Placed Successfully!";
			}
		}

		return status;
	}

	*/
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
			ps.setBoolean(3, order.isShipped());
			ps.setDate(4, order.getDataEffettuato());
			ps.setInt(5, order.getIdFattura().getId());
			
			ResultSet rs = null;
        	rs = ps.getGeneratedKeys();
        	
        	if (rs.next()) {
                order.setId(rs.getInt(1)); // O getInt(1) se l'ID è un int
                
                
                
                order.getFumetti().forEach((key, value) -> {
                	try {
                		PreparedStatement psscaf;
                		PreparedStatement psFumetto = con.prepareStatement("select Magazzino.nome, Scaffali.id, Scaffali.quantita from Magazzino, Scaffali, MagazzinoScaffali where Magazzino.nome = MagazzinoScaffali.idMagazzino and MagazzinoScaffali.idScaffale = Scaffale.id and Scaffali.idFumetto = ? order by Scaffali.quantita DESC");
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
                            	psscaf.executeQuery();
                            }
                            
                            if ((value-quantita) < rsFumetto.getInt("Scaffali.quantita")) {
                            	
                            	psFumetto.setInt(1, (value - quantita));
                                	// update scaffale rsFumetto.getInt("Scaffali.quantita") - (value - quantita)
                            	quantita = value;
                            	psscaf = con.prepareStatement("UPDATE Scaffali SET quantita = ? WHERE id = ?");
                            	psscaf.setInt(1, rsFumetto.getInt("Scaffali.quantita") - (value - quantita));
                            	psscaf.setInt(2, rsFumetto.getInt("Scaffali.id"));
                            	psscaf.executeQuery();
                            }
                            psFumetto.executeQuery();   
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
/*
	
	public boolean addTransaction(TransactionBean transaction) throws SQLException {
		boolean flag = false;

		Connection con = DBUtil.getConnection();

		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into transactions values(?,?,?,?)");

			ps.setString(1, transaction.getTransactionId());
			ps.setString(2, transaction.getUserName());
			ps.setTimestamp(3, transaction.getTransDateTime());
			ps.setDouble(4, transaction.getTransAmount());

			int k = ps.executeUpdate();

			if (k > 0)
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}

	
	public int countSoldItem(String prodId) throws SQLException {
		int count = 0;

		Connection con =  DBUtil.getConnection();
		PreparedStatement ps = null;

		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select sum(quantity) from orders where prodid=?");

			ps.setString(1, prodId);

			rs = ps.executeQuery();

			if (rs.next())
				count = rs.getInt(1);

		} catch (SQLException e) {
			count = 0;
			e.printStackTrace();
		}


		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return count;
	}

	
	public List<OrderBean> getAllOrders() throws SQLException {
		List<OrderBean> orderList = new ArrayList<OrderBean>();

		Connection con =  DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement("select * from orders");

			rs = ps.executeQuery();

			while (rs.next()) {

				OrderBean order = new OrderBean(rs.getString("orderid"), rs.getString("prodid"), rs.getInt("quantity"),
						rs.getDouble("amount"), rs.getInt("shipped"));

				orderList.add(order);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return orderList;
	}


	public List<OrderBean> getOrdersByUserId(String emailId) throws SQLException {
		List<OrderBean> orderList = new ArrayList<OrderBean>();

		Connection con =  DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement(
				    "SELECT o.*, t.username\r\n"
				    + "FROM `shopping-cart`.`orders` o\r\n"
				    + "JOIN `shopping-cart`.`transactions` t ON o.orderid = t.transid\r\n"
				    + "WHERE t.username = ?");
				ps.setString(1, emailId);
			ps.setString(1, emailId);
			rs = ps.executeQuery();

			while (rs.next()) {

				OrderBean order = new OrderBean(rs.getString("orderid"), rs.getString("prodid"), rs.getInt("quantity"),
						rs.getDouble("amount"), rs.getInt("shipped"));

				orderList.add(order);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return orderList;
	}
	
	
	public List<OrderBean> getOrdersByDate(String startDate, String endDate) throws SQLException {
	    List<OrderBean> orderList = new ArrayList<>();

	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    
        startDate = startDate + " 00:00:00";
        endDate = endDate + " 23:59:59";

	    try {
	        con = DBUtil.getConnection();
	        String query = "SELECT " +
	                       "    o.orderid, " +
	                       "    o.prodid, " +
	                       "    o.quantity AS order_quantity, " +
	                       "    o.amount AS order_amount, " +
	                       "    o.shipped, " +
	                       "    t.transid, " +
	                       "    t.username, " +
	                       "    t.time AS transaction_time, " +
	                       "    t.amount AS transaction_amount " +
	                       "FROM " +
	                       "    `shopping-cart`.`orders` o " +
	                       "JOIN " +
	                       "    `shopping-cart`.`transactions` t ON o.orderid = t.transid " +
	                       "WHERE " +
	                       "    t.time BETWEEN ? AND ?";
	        
	        ps = con.prepareStatement(query);
	        ps.setString(1, startDate);
	        ps.setString(2, endDate);

	        rs = ps.executeQuery();

	        while (rs.next()) {
	            OrderBean order = new OrderBean(
	                rs.getString("orderid"),
	                rs.getString("prodid"),
	                rs.getInt("order_quantity"),
	                rs.getDouble("order_amount"),
	                rs.getInt("shipped")
	            );
	            orderList.add(order);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        // Chiudere tutte le risorse aperte
	        if (rs != null) {
	            rs.close();
	        }
	        if (ps != null) {
	            ps.close();
	        }
	        if (con != null) {
	            con.close();
	        }
	    }

	    return orderList;
	}

	

	
	public List<OrderBean> getOrdersByUserIdAndDate(String search, String startDate, String endDate) throws SQLException {
		List<OrderBean> orderList = new ArrayList<OrderBean>();

		Connection con =  DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;
		
		startDate = startDate + " 00:00:00";
        endDate = endDate + " 23:59:59";
		 

		try {

			ps = con.prepareStatement("SELECT " +
                    "    o.orderid, " +
                    "    o.prodid, " +
                    "    o.quantity AS order_quantity, " +
                    "    o.amount AS order_amount, " +
                    "    o.shipped, " +
                    "    t.transid, " +
                    "    t.username, " +
                    "    t.time AS transaction_time, " +
                    "    t.amount AS transaction_amount " +
                    "FROM " +
                    "    `shopping-cart`.`orders` o " +
                    "JOIN " +
                    "    `shopping-cart`.`transactions` t ON o.orderid = t.transid " +
                    "WHERE " +
                    "    t.time BETWEEN ? AND ? AND t.username=?");
		    
			ps.setString(1, startDate);
            ps.setString(2, endDate);
            ps.setString(3, search);

			rs = ps.executeQuery();
		 

			while (rs.next()) {

				OrderBean order = new OrderBean(rs.getString("o.orderid"), rs.getString("o.prodid"), rs.getInt("order_quantity"),
						rs.getDouble("order_amount"), rs.getInt("o.shipped"));

				orderList.add(order);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return orderList;
	}
	*/


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
				order.setShipped(rs.getBoolean("shipped"));
				order.setDataEffettuato(rs.getDate("dataEffettuato"));
				orderList.add(order);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return orderList;
	}
	
	

	/*
	public String shipNow(String orderId, String prodId) throws SQLException {
		String status = "FAILURE";

		Connection con =  DBUtil.getConnection();

		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("update orders set shipped=1 where orderid=? and prodid=? and shipped=0");

			ps.setString(1, orderId);
			ps.setString(2, prodId);

			int k = ps.executeUpdate();

			if (k > 0) {
				status = "Order Has been shipped successfully!!";
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);

		return status;
	}

	*/

}
