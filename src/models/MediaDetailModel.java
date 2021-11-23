package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import Dao.DBConnect;

public class MediaDetailModel extends DBConnect {
	
	public ArrayList<Object> getMediaDetail(int id) {
		
    	String query = "SELECT * FROM wei_zhou_media WHERE id = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
        	ResultSet rs = stmt.executeQuery();
        	
   		 	ResultSetMetaData metaData = rs.getMetaData();
   		 	int columns = metaData.getColumnCount();
			if (rs.next()) {
				ArrayList<Object> row = new ArrayList<Object>(columns);
			   for (int i = 1; i <= columns; i++) 
				   row.add(rs.getObject(i));
			   return row;
			}
			return null;
         }catch (SQLException e) {
        	e.printStackTrace();   
         }
		return null;
	}
	
	public ArrayList<ArrayList<Object>> getComments(int media_id) {
		
    	String query = "SELECT wei_zhou_users.user_name,wei_zhou_comments.content,wei_zhou_comments.create_time FROM wei_zhou_users,wei_zhou_comments WHERE wei_zhou_comments.media_id = ? and wei_zhou_users.id = wei_zhou_comments.user_id;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, media_id);
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
	
	public Boolean postComment(int user_id, int media_id, String comment){
        
    	String query = "INSERT INTO `wei_zhou_comments` (`content`, `user_id`, `media_id`) VALUES (?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
           stmt.setString(1, comment);
           stmt.setInt(2, user_id);
           stmt.setInt(3, media_id);
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
