package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import Dao.DBConnect;

public class AdminModel extends DBConnect {
		
	public ArrayList<ArrayList<Object>> searchUser(String keyword) {
		
    	String query = "SELECT * FROM wei_zhou_users WHERE user_name like ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
        	ResultSet rs = stmt.executeQuery();
        	
        	ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
   		 	ResultSetMetaData metaData = rs.getMetaData();
   		 	int columns = metaData.getColumnCount();
			while (rs.next()) {
				ArrayList<Object> row = new ArrayList<Object>(columns);
			   for (int i = 1; i <= columns; i++) 
				   row.add(rs.getObject(i));
			   data.add(row);
			}
			return data;
         }catch (SQLException e) {
        	e.printStackTrace();   
         }
		return null;
	}
	
	public ArrayList<ArrayList<Object>> searchMedia(String keyword) {
		
    	String query = "SELECT * FROM wei_zhou_media WHERE title like ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
        	ResultSet rs = stmt.executeQuery();
        	
        	ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
   		 	ResultSetMetaData metaData = rs.getMetaData();
   		 	int columns = metaData.getColumnCount();
			while (rs.next()) {
				ArrayList<Object> row = new ArrayList<Object>(columns);
			   for (int i = 1; i <= columns; i++) 
				   row.add(rs.getObject(i));
			   data.add(row);
			}
			return data;
         }catch (SQLException e) {
        	e.printStackTrace();   
         }
		return null;
	}
	
	public boolean deleteMedia(int mediaId) {
    	String query = "DELETE FROM `wei_zhou_media` WHERE id = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
           stmt.setInt(1, mediaId);
           int res = stmt.executeUpdate();
	        if(res == 1) {
	        	return true;
	       	}
         }catch (SQLException e) {
        	e.printStackTrace();   
         }
		return false;
	}
	
}
