package client.networking;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import server.object.UserMessage;

public class ClientAdapterInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;
    private final ClientAdapter client;

    public ClientAdapterInitializer(SslContext sslCtx, ClientAdapter client) {
        this.sslCtx = sslCtx;
        this.client = client;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        pipeline.addLast(sslCtx.newHandler(ch.alloc(), client.getHost(), client.getPort()));

        // On top of the SSL handler, add the text line codec.
        //pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new ObjectDecoder(ClassResolvers.softCachingResolver(ClassLoader.getSystemClassLoader())));
        pipeline.addLast(new ObjectEncoder());

        // and then business logic.
        pipeline.addLast(new ClientAdapterHandler());
    }

}