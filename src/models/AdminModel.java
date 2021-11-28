package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import Dao.DBConnect;

public class AdminModel extends DBConnect {
		
	// get user list by user name
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
	
	// get media list by title
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
	
	//delete media by id
	public boolean deleteMedia(int mediaId) {	
		try {
			//need to make sure that all reviews relate to this media are deleted first,then delete the media itself
			connection.setAutoCommit(false);
			
			String queryComment = "DELETE FROM `wei_zhou_comments` WHERE media_id = ?;";
			PreparedStatement stmtComment = connection.prepareStatement(queryComment);
			stmtComment.setInt(1, mediaId);
			stmtComment.executeUpdate();
		    
		    String queryMedia = "DELETE FROM `wei_zhou_media` WHERE id = ?;";
		    PreparedStatement stmtMedia = connection.prepareStatement(queryMedia);
		    stmtMedia.setInt(1, mediaId);
		    int res = stmtMedia.executeUpdate();
		    
	    	connection.commit();
	    	if(res == 1) return true;
         }catch (SQLException e) {
        	e.printStackTrace();   
        	try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
         }
		return false;
	}
	
}
