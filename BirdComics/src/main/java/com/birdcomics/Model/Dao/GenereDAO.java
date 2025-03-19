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
	
	 public List<GenereBean> getGeneri() {
	        List<GenereBean> generi = new ArrayList<>();
	        String query = "SELECT * FROM Genere";

	        try (Connection con = DBUtil.getConnection();
	             PreparedStatement ps = con.prepareStatement(query);
	             ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {
	                generi.add(new GenereBean(rs.getString("genere")));
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return generi;
	    }
}
