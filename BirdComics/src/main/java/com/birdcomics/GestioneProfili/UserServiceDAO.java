package com.birdcomics.GestioneProfili;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.birdcomics.DatabaseImplementator.DBUtil;


public class UserServiceDAO {


	public String registerUser(String userName, Long mobileNo, String emailId, String address, int pinCode,
			String password, String usertype) throws SQLException {

		UserBean user = new UserBean(userName, mobileNo, emailId, address, pinCode, password, usertype);

		String status = registerUser(user);

		return status;
	}

	
	public String registerUser(UserBean user) throws SQLException {

	    String status = "User Registration Failed!";

	    boolean isRegtd = isRegistered(user.getEmail());

	    if (isRegtd) {
	        status = "Email Id Already Registered!";
	        return status;
	    }

	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;

	    if (con != null) {
	        System.out.println("Connected Successfully!");
	    }

	    try {
	        // Modifica della query per includere il campo usertype
	        ps = con.prepareStatement("INSERT INTO user (email, name, mobile, address, pincode, password, usertype) VALUES (?, ?, ?, ?, ?, ?, ?)");

	        ps.setString(1, user.getEmail());
	        ps.setString(2, user.getName());
	        ps.setLong(3, user.getMobile());
	        ps.setString(4, user.getAddress());
	        ps.setInt(5, user.getPinCode());
	        ps.setString(6, user.getPassword());
	        ps.setString(7, user.getUserType()); // Assumendo che getUserType() ritorni "guest"

	        int k = ps.executeUpdate();

	        if (k > 0) {
	            status = "User Registered Successfully!";
	        }

	    } catch (SQLException e) {
	        status = "Error: " + e.getMessage();
	        e.printStackTrace();
	    } finally {
	        DBUtil.closeConnection(ps);
	    }

	    return status;
	}


	
	public boolean isRegistered(String emailId) throws SQLException {
		boolean flag = false;

		Connection con = DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select * from user where email=?");

			ps.setString(1, emailId);

			rs = ps.executeQuery();

			if (rs.next())
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return flag;
	}

	
	public String isValidCredential(String emailId, String password) throws SQLException {
		String status = "Login Denied! Incorrect Username or Password";

		Connection con = DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement("select * from user where email=? and password=?");

			ps.setString(1, emailId);
			ps.setString(2, password);

			rs = ps.executeQuery();

			if (rs.next())
				status = "valid";

		} catch (SQLException e) {
			status = "Error: " + e.getMessage();
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);
		return status;
	}

	
	public UserBean getUserDetails(String emailId, String password) throws SQLException {

		UserBean user = null;

		Connection con = DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select * from user where email=? and password=?");
			ps.setString(1, emailId);
			ps.setString(2, password);
			rs = ps.executeQuery();

			if (rs.next()) {
				user = new UserBean();
				user.setName(rs.getString("name"));
				user.setMobile(rs.getLong("mobile"));
				user.setEmail(rs.getString("email"));
				user.setAddress(rs.getString("address"));
				user.setPinCode(rs.getInt("pincode"));
				user.setPassword(rs.getString("password"));

				return user;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return user;
	}


	public String getFName(String emailId) throws SQLException {
		String fname = "";

		Connection con = DBUtil.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select name from user where email=?");
			ps.setString(1, emailId);

			rs = ps.executeQuery();

			if (rs.next()) {
				fname = rs.getString(1);

				fname = fname.split(" ")[0];

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return fname;
	}

	
	public String getUserAddr(String userId) throws SQLException {
		String userAddr = "";

		Connection con = DBUtil.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select address from user where email=?");

			ps.setString(1, userId);

			rs = ps.executeQuery();

			if (rs.next())
				userAddr = rs.getString(1);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return userAddr;
	}
	
	public String getUserType(String emailId) throws SQLException {
	    String userType = "";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement("select usertype from user where email=?")) {

	        ps.setString(1, emailId);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                userType = rs.getString("usertype");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return userType;
	}



	public void updateUserDetails(String userName, String password, String fullName, long phone, String address,
			int pinCode) throws SQLException {
	    Connection con = DBUtil.getConnection();
	    PreparedStatement ps = null;
	 

	        // Prepare the SQL update statement
	        String updateSQL = "UPDATE user SET name = ?, mobile = ?, address = ?, pincode = ? WHERE email = ? AND password = ?";
	        ps = con.prepareStatement(updateSQL);

	        // Set the parameters for the update statement
	        ps.setString(1, fullName);
	        ps.setLong(2, phone);
	        ps.setString(3, address);
	        ps.setInt(4, pinCode);
	        ps.setString(5, userName);
	        ps.setString(6, password);

	        // Execute the update statement
	        ps.executeUpdate();
	   
	     
	    }
		
	



}
