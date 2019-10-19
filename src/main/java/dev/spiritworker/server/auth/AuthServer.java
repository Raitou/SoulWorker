package dev.spiritworker.server.auth;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.netty.tcp.TcpServer;
import dev.spiritworker.server.game.GameServer;
import dev.spiritworker.util.ServerData;

public class AuthServer extends TcpServer {
	private Map<Integer, ServerData> gameServers = new HashMap<Integer, ServerData>();

	public AuthServer() {
		super(new InetSocketAddress(SpiritWorker.getConfig().AuthServerIp, SpiritWorker.getConfig().AuthServerPort));
		SpiritWorker.getLogger().info("Starting SpiritWorker auth server...");
		
		this.registerServer(new ServerData(SpiritWorker.getConfig().GameServerName, SpiritWorker.getConfig().GameServerIp, SpiritWorker.getConfig().GameServerPort));
		
		this.setServerInitializer(new AuthServerInitializer(this));
	}
	
	public Map<Integer, ServerData> getGameServers() {
		return this.gameServers;
	}

	@Override
	public void onStart() {
		SpiritWorker.getLogger().info("SpiritWorker auth server startup completed!");
		
		// Build game server
		if (SpiritWorker.MODE == dev.spiritworker.SpiritWorker.RunMode.BOTH) {
			for (ServerData server : getGameServers().values()) {
				GameServer gameServer = new GameServer(1, server.getName(), server.getAddress());
				gameServer.start();
			}
		}
	}

	public void registerServer(ServerData serverData) {
		this.gameServers.put(serverData.getId(), serverData);
	}

	public ServerData getServerDataById(int serverId) {
		return this.gameServers.get(serverId);
	}
}
