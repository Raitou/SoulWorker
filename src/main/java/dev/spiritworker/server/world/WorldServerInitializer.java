package dev.spiritworker.server.world;

import dev.spiritworker.netty.SoulWorkerPacketDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class WorldServerInitializer extends ChannelInitializer<SocketChannel> {
	private WorldServer server;
	
	public WorldServerInitializer(WorldServer server) {
		this.server = server;
	}
	
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SoulWorkerPacketDecoder());
        pipeline.addLast(new WorldSession(server));
    }
}
