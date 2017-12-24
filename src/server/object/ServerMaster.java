package server.object;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

public class ServerMaster {
	ArrayList<User> usersOnline;
	
	public ServerMaster(){
		usersOnline = new ArrayList<User>();
	}
	
	public void updateOnlineUsers(){
		
	}
	
	//Logs current user connected to server from the array of users, usersOnline and writes to usersOnline.txt
	public void logUsers() throws UnsupportedEncodingException, FileNotFoundException, IOException{
			
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream("usersOnline.txt"), "utf-8"))) {
			writer.write("Current Users: ");
			this.updateOnlineUsers();
			for(User user : usersOnline){
				StringBuilder sb = new StringBuilder();
				sb.append("User: ");
				sb.append(user.getUserName());
				sb.append(", Password: ");
				sb.append(user.getPassword());
				sb.append("   IP: ");
				sb.append(user.getIP());
				writer.write(sb.toString());
			}
		}
		
	}
	
}
