package server.object;

import static java.lang.System.out;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import io.netty.channel.ChannelHandlerContext;

/**
 * An object that represents a connected user.
 * @author Miles Black & Paul Mikulskis
 * @date Dec 24, 2017
 */
public class User {

	public String userName;
	public String password;
	public String ip;
	private ChannelHandlerContext channel;
	
	/**
	 * Constructs a new user with the indicated
	 * username and password.
	 * @param u the username for the user.
	 * @param p the password for the user.
	 * @param ch the channel context of the current session.
	 * @throws SocketException
	 */
	public User(String u, String p, ChannelHandlerContext ch) throws SocketException {
		this.userName = u;
		this.password = p;
		this.channel = ch;
		Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
		
		for (NetworkInterface netint : Collections.list(n)){
			if(netint.getDisplayName().contains("Wireless".toString())){
				Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
				for (InetAddress inetAddress : Collections.list(inetAddresses)) {
		            if(inetAddress.toString().length() > 0){
		            String ip = inetAddress.toString();
		            ip = ip.substring(1);
		            this.ip = ip;
					out.printf("New user created with ip: %s\n", this.ip);
		            }
		        }
			}
		}
	}
	
	/**
	 * Gets the username of the user.
	 * @return username
	 */
	public String getUserName(){
		return userName;
	}
	
	/**
	 * Gets the password of the user.
	 * @return password
	 */
	public String getPassword(){
		return password;
	}
	
	/**
	 * Gets the ip address of the user.
	 * @return ip
	 */
	public String getIP(){
		return ip;
	}
	
	/**
	 * Logs a user out from the current session.
	 */
	public void logout() {
		ServerMaster.getUsersOnline().remove(this);
		channel.channel().disconnect();
	}
}
	

