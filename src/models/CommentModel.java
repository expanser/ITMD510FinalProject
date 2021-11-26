package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import Dao.DBConnect;

public class CommentModel extends DBConnect {
	
	public ArrayList<ArrayList<Object>> getComments(int user_id) {
		
    	String query = "SELECT wei_zhou_comments.id,wei_zhou_media.title,wei_zhou_comments.content,wei_zhou_comments.create_time FROM wei_zhou_media,wei_zhou_comments WHERE wei_zhou_comments.user_id = ? and wei_zhou_comments.media_id = wei_zhou_media.id;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user_id);
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
	
	public boolean deleteReview(int commentId) {
    	String query = "DELETE FROM `wei_zhou_comments` WHERE id = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
           stmt.setInt(1, commentId);
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
