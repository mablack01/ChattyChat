package client;

import client.networking.ClientAdapter;

public class ClientLauncher {
	
	static ClientAdapter client;

    public static void main(String[] args) throws Exception {
        if (args.length > 1) {
            client = new ClientAdapter(args[0], args[1]);
        } else {
        	client = new ClientAdapter();
        }
        client.start();
    }

}
