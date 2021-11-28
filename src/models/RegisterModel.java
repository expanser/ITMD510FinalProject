package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Dao.DBConnect;

public class RegisterModel extends DBConnect {
		
	// check if the user name has been used
	public Boolean checkUserOccupied(String username){
           
        	String query = "SELECT * FROM wei_zhou_users WHERE user_name = ?;";
            try(PreparedStatement stmt = connection.prepareStatement(query)) {
               stmt.setString(1, username);
               ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                	return true;
               	}
             }catch (SQLException e) {
            	e.printStackTrace();   
             }
			return false;
    }
	
	// add new user
	public Boolean addUser(String username, String password){
        
    	String query = "INSERT INTO `wei_zhou_users` (`user_name`, `user_password`) VALUES (?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
           stmt.setString(1, username);
           stmt.setString(2, password);
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
