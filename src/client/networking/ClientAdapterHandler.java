package client.networking;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import server.object.UserMessage;

public class ClientAdapterHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    	UserMessage message = ((UserMessage) msg);
        System.out.println("The type of message sent is: " + message.getCommand());
        System.out.println("The content from the message sent is:\n" + message.message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}