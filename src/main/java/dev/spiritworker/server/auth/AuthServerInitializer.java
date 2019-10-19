package dev.spiritworker.server.auth;

import dev.spiritworker.netty.SoulWorkerPacketDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class AuthServerInitializer extends ChannelInitializer<SocketChannel> {
	private AuthServer server;
	
	public AuthServerInitializer(AuthServer server) {
		this.server = server;
	}
	
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SoulWorkerPacketDecoder());
        pipeline.addLast(new AuthSession(server));
    }
}
