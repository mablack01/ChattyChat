package server.object;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Handles all the global data of the server.
 * @author Miles Black & Paul Mikulskis
 * @date Dec 24, 2017
 */
public class ServerMaster {
	
	private static ArrayList<User> usersOnline;
	
	/**
	 * Constructs a Server Master object.
	 */
	public ServerMaster(){
		usersOnline = new ArrayList<User>();
	}
	
	public void updateOnlineUsers(){
		
	}
	
	//Logs current user connected to server from the array of users, usersOnline and writes to usersOnline.txt
	/**
	 * Logs the data of all the users connected to the server 
	 * onto the text file usersOnline.txt
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
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
	
	/**
	 * Gets the current user instances online.
	 * @return usersOnline
	 */
	public static ArrayList<User> getUsersOnline() {
		return usersOnline;
	}
	
}
