package server.object;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import static java.lang.System.out;

public class User {

	public String userName;
	public String password;
	public String ip;
	
	//Constructors
	public User(String u, String p) throws SocketException{
		userName = u;
		password = p;
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

	public User(){
		
	}
	
	//Getters
	public String getUserName(){
		return this.userName;
	}
	public String getPassword(){
		return this.password;
	}
	public String getIP(){
		return this.ip;
	}
}
	

