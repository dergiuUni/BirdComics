package com.birdcomics.GestioneOrdine;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.birdcomics.DatabaseImplementator.DBUtil;

public class TransServiceDAO  {


	public String getUserId(String transId) throws SQLException {
		String userId = "";

		Connection con = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement("select username from transactions where transid=?");

			ps.setString(1, transId);

			rs = ps.executeQuery();

			if (rs.next())
				userId = rs.getString(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userId;
	}

	public String getDateTime(String transId) throws SQLException {
		String dateTime = "";

		Connection con = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement("select time from transactions where transid=?");

			ps.setString(1, transId);

			rs = ps.executeQuery();

			if (rs.next())
				dateTime = rs.getString(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dateTime;
	}

}
