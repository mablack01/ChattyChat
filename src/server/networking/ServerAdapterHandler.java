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
import server.object.Command;
import server.object.User;
import server.object.UserMessage;

public class ServerAdapterHandler extends SimpleChannelInboundHandler<Object> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // Once session is secured, send a greeting and register the channel to the global channel
        // list so the channel received the messages from others.
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception {
                        UserMessage reply = new UserMessage("Welcome to " + InetAddress.getLocalHost().getHostName() + " secure chat service!\nYour session is protected by " +
                                        ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +
                                        " cipher suite.\n");
                    	ctx.writeAndFlush(reply);
                        channels.add(ctx.channel());                     
                    }
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    	UserMessage message = ((UserMessage) msg);
    	Command messageType = message.getCommand();
    	switch(messageType){
    		case MESSAGE:
    	       // Send the received message to all channels but the current one.
    	       for (Channel c: channels) {
    	    	   if (c != ctx.channel()) {
    	    		   UserMessage reply = new UserMessage("(" + ctx.channel().remoteAddress() + ") " + message.message + '\n');
    	    		   c.writeAndFlush(reply);
    	    	            } 
    	    	   else {
    	    		  UserMessage reply = new UserMessage("(you) " + message.message + '\n');
    	    	      c.writeAndFlush(reply);
    	    	   }
    	       }
    	     break;
    		case LOGIN:
    			UserMessage reply = new UserMessage("Welcome to the server, " + message.userName + "!\n");
    			ctx.writeAndFlush(reply);
        		User user = new User(message.userName, message.password);
        		break;
    	}

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}