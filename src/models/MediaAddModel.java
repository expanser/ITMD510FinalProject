package models;

import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import Dao.DBConnect;

public class MediaAddModel extends DBConnect {
		
	//update media by id
	public Boolean UpdateMedia(int id, String title, String genre, String media_type, String director, String release_date){
           
        	String query = "UPDATE wei_zhou_media SET title = ?,genre = ?,media_type = ?,director = ?,release_date = ? WHERE id = ?;";
            try(PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, title);
                stmt.setString(2, genre);
                stmt.setString(3, media_type);
                stmt.setString(4, director);
                stmt.setString(5, release_date);
                stmt.setInt(6, id);
                int res = stmt.executeUpdate();
                if(res == 1) {
                	return true;
               	}
             }catch (SQLException e) {
            	e.printStackTrace();   
             }
			return false;
    }
	
	// add new media
	public Boolean addMedia(String title, String genre, String media_type, String director, String release_date){
        
    	String query = "INSERT INTO `wei_zhou_media` (`title`, `genre`, `media_type`, `director`, `release_date`) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
           stmt.setString(1, title);
           stmt.setString(2, genre);
           stmt.setString(3, media_type);
           stmt.setString(4, director);
           stmt.setString(5, release_date);
           int res = stmt.executeUpdate();
            if(res == 1) {
            	return true;
           	}
         }catch (SQLException e) {
        	e.printStackTrace();   
         }
		return false;
	}
	
	// get media detail by id
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
	
}
