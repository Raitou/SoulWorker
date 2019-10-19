package dev.spiritworker.netty.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

    public TcpServerInitializer(TcpServer server) {
    	
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
    }
}
