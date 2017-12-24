package server.networking;

import java.net.InetAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import server.object.User;

public class ServerAdapterHandler extends SimpleChannelInboundHandler<String> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // Once session is secured, send a greeting and register the channel to the global channel
        // list so the channel received the messages from others.
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception {
                        ctx.writeAndFlush(
                                "Welcome to " + InetAddress.getLocalHost().getHostName() + " secure chat service!\n");
                        ctx.writeAndFlush(
                                "Your session is protected by " +
                                        ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +
                                        " cipher suite.\n");
                        ctx.writeAndFlush(
                                "Please input your information in the way of: [username,password]\n...include the brackets HOE\n");

                        channels.add(ctx.channel());
                        
                        
                    }
        });
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        
    	//Checks if the input is a username and password
    	if(msg.charAt(0) == '[' && msg.charAt(msg.length()-1) == ']'){
    		String userName = msg.substring(1, msg.indexOf(",", 1));
    		String password = msg.substring(msg.indexOf(",", 1), msg.length()-2);
    		User user = new User(userName, password, ctx);
    		ctx.writeAndFlush(
                    "Welcome to the server, " + userName + "\n");
    	}
    	
    	else{
    	// Send the received message to all channels but the current one.
	        for (Channel c: channels) {
	            if (c != ctx.channel()) {
	                c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] " + msg + '\n');
	            } else {
	                c.writeAndFlush("[you] " + msg + '\n');
	            }
	        }
	
	        // Close the connection if the client has sent 'bye'.
	        if ("bye".equals(msg.toLowerCase())) {
	            ctx.close();
	        }
    	}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}