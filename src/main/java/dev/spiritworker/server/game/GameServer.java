package dev.spiritworker.server.game;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.netty.tcp.TcpServer;
import dev.spiritworker.server.world.WorldServer;
import dev.spiritworker.util.ServerData;

public class GameServer extends TcpServer {
	private int id;
	private String name;

	private ServerData authServer;
	
	private List<WorldServer> channels;
	
	private Map<String, GameCharacter> characters;
	
	public GameServer(int id, String name, InetSocketAddress address) {
		super(address);
		this.id = id;
		this.name = name;
		this.authServer = new ServerData(null, SpiritWorker.getConfig().AuthServerIp, SpiritWorker.getConfig().AuthServerPort);
		this.characters = new ConcurrentHashMap<String, GameCharacter>();
		this.channels = new ArrayList<WorldServer>();
		
		SpiritWorker.getLogger().info("Starting SpiritWorker game server...");
		
		this.setServerInitializer(new GameServerInitializer(this));
	}
	
	public ServerData getAuthServer() {
		return this.authServer;
	}
	
	public int getChannelCount() {
		return getChannels().size();
	}

	public List<WorldServer> getChannels() {
		return this.channels;
	}
	
	public WorldServer getChannelByIndex(int id) {
		return getChannels().get(id);
	}
	
	public int getServerId() {
		return this.id;
	}
	
	public String getServerName() {
		return this.name;
	}
	
	public int getCurrentPlayers() {
		return getCharacters().size();
	}
	
	public Map<String, GameCharacter> getCharacters() {
		return this.characters;
	}
	
	public void addCharacter(GameCharacter character) {
		if (character.getSession() != null && character.getSession().isOpen()) {
			characters.put(character.getName().toLowerCase(), character);
		}
	}
	
	public void removeCharacter(GameCharacter character) {
		this.characters.remove(character.getName().toLowerCase());
	}
	
	@Override
	public void onStart() {
		SpiritWorker.getLogger().info("SpiritWorker game server startup completed!");
		
		// Create world server
		WorldServer worldServer = new WorldServer(this, new InetSocketAddress(SpiritWorker.getConfig().WorldServerIp, SpiritWorker.getConfig().WorldServerPort));
		worldServer.start();
	}

	public void registerWorldServer(WorldServer worldServer) {
		worldServer.setChannelId(1);
		getChannels().add(worldServer);
	}
}
