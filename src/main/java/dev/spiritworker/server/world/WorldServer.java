package dev.spiritworker.server.world;

import java.net.InetSocketAddress;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.game.District;
import dev.spiritworker.game.data.SoulWorker;
import dev.spiritworker.game.data.def.DistrictDef;
import dev.spiritworker.netty.tcp.TcpServer;
import dev.spiritworker.server.game.GameServer;
import dev.spiritworker.server.world.handlers.ChatManager;
import dev.spiritworker.server.world.handlers.ItemManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class WorldServer extends TcpServer {
	private int channelId;
	private GameServer gameServer;
	
	private final ChatManager chatHandler;
	private final ItemManager itemHandler;
	
	private final Int2ObjectMap<District> districts;
	private District defaultDistrict;
	
	public WorldServer(GameServer gameServer, InetSocketAddress address) {
		super(address);
		this.gameServer = gameServer;
		
		this.chatHandler = new ChatManager(this);
		this.itemHandler = new ItemManager(this);
		this.districts = new Int2ObjectOpenHashMap<District>();
		
		this.setupDistricts();
		
		this.setServerInitializer(new WorldServerInitializer(this));
	}

	public int getChannelId() {
		return this.channelId;
	}
	
	public void setChannelId(int i) {
		this.channelId = i;
	}
	
	public GameServer getGameServer() {
		return this.gameServer;
	}

	public ChatManager getChatManager() {
		return chatHandler;
	}
	
	public ItemManager getItemManager() {
		return itemHandler;
	}
	
	public District getDistrictById(int id) {
		return districts.get(id);
	}
	
	public District getDefaultDistrict() {
		return this.defaultDistrict;
	}
	
	private void setupDistricts() {
		for (DistrictDef districtDef : SoulWorker.getDistrictDefs().values()) {
			District district = new District(districtDef);
			this.districts.put(district.getMapId(), district);
		}
		this.defaultDistrict = this.getDistrictById(10003);
	}

	@Override
	public void onStart() {
		this.gameServer.registerWorldServer(this);
		SpiritWorker.getLogger().info("Channel " + this.getChannelId() + " registered on port " + this.getAddress().getPort());
	}

	public void onTick() {
		
	}
}
