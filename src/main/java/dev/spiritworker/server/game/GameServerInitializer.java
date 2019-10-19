package dev.spiritworker.server.game;

import dev.spiritworker.netty.SoulWorkerPacketDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class GameServerInitializer extends ChannelInitializer<SocketChannel> {
	private GameServer server;
	
	public GameServerInitializer(GameServer server) {
		this.server = server;
	}
	
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SoulWorkerPacketDecoder());
        pipeline.addLast(new GameSession(server));
    }
}
