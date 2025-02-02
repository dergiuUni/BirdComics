package com.birdcomics.GestioneOrdine;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.birdcomics.DatabaseImplementator.DBUtil;
import com.birdcomics.GestioneCatalogo.ProductServiceDAO;
//import com.birdcomics.GestioneProfili.Cliente.CartBean;
//import com.birdcomics.GestioneProfili.Cliente.CartServiceDAO;
import com.birdcomics.GestioneMagazzino.ScaffaliBean;


public class FatturaServiceDAO {

	public boolean addFattura(FatturaBean fattura) throws SQLException {

		Connection con = DBUtil.getConnection();

		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into Fattura(iva, nome, cognome, telefono, nomeCittaCliente, viaCliente, numeroCivicoCliente, capCliente, nomeCittaMagazzino, viaMagazzino, numeroCivicoMagazzino, capMagazzino, nomeMagazzino) values(?,?,?,?,?,?,?,?,?,?,?,?,?)", java.sql.Statement.RETURN_GENERATED_KEYS );

			ps.setInt(1, fattura.getIva());
			ps.setString(2, fattura.getNome());
			ps.setString(3, fattura.getCognome());
			ps.setString(4, fattura.getTelefono());
			ps.setString(5, fattura.getNomeCittaCliente());
			ps.setString(6, fattura.getViaCliente());
			ps.setString(7, fattura.getNumeroCivicoCliente());
			ps.setString(8, fattura.getCapCliente());
			ps.setString(9, fattura.getNomeCittaMagazzino());
			ps.setString(10, fattura.getViaMagazzino());
			ps.setString(11, fattura.getNumeroCivicoMagazzino());
			ps.setString(12, fattura.getCapMagazzino());
			ps.setString(13, fattura.getNomeMagazzino());
				

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
		}

		return false;
	}

	
	
	
	public List<FatturaBean> getAllFattura() throws SQLException {
		List<FatturaBean> fatturaList = new ArrayList<>();

		Connection con =  DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement("select * from Fattura");

			rs = ps.executeQuery();

			while (rs.next()) {

				FatturaBean fattura = new FatturaBean(rs.getInt("id"), rs.getInt("iva"), rs.getString("nome"), rs.getString("cognome"), rs.getString("telefono"), rs.getString("nomeCittaCliente"), rs.getString("viaCliente"), rs.getString("numeroCivicoCliente"), rs.getString("capCliente"), rs.getString("nomeCittaMagazzino"), rs.getString("viaMagazzino"), rs.getString("numeroCivicoMagazzino"), rs.getString("capMagazzino"), rs.getString("nomeMagazzino"));

				 fatturaList.add(fattura);

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return fatturaList;
	}


	public FatturaBean getFatturaById(int idFattura) throws SQLException {

		Connection con =  DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement("SELECT * FROM Fattura Where id = ?");
			
			ps.setInt(1, idFattura);
			rs = ps.executeQuery();

			while (rs.next()) {

				FatturaBean fattura = new FatturaBean(rs.getInt("id"), rs.getInt("iva"), rs.getString("nome"), rs.getString("cognome"), rs.getString("telefono"), rs.getString("nomeCittaCliente"), rs.getString("viaCliente"), rs.getString("numeroCivicoCliente"), rs.getString("capCliente"), rs.getString("nomeCittaMagazzino"), rs.getString("viaMagazzino"), rs.getString("numeroCivicoMagazzino"), rs.getString("capMagazzino"), rs.getString("nomeMagazzino"));

				return fattura;

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}
	
	



}
