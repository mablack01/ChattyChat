package server;

import server.networking.ServerAdapter;
    
public class ServerLauncher {
	
	static ServerAdapter server;

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            server = new ServerAdapter(args[0]);
        } else {
        	server = new ServerAdapter();
        }
        server.start();
    }
}