package dev.spiritworker.server.world;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.game.data.SoulWorker;
import dev.spiritworker.game.data.def.DistrictDef;
import dev.spiritworker.game.managers.ChatManager;
import dev.spiritworker.game.managers.ItemManager;
import dev.spiritworker.game.managers.MazeManager;
import dev.spiritworker.game.map.District;
import dev.spiritworker.game.map.Maze;
import dev.spiritworker.netty.tcp.TcpServer;
import dev.spiritworker.server.game.GameServer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class WorldServer extends TcpServer {
	private int channelId;
	private GameServer gameServer;
	
	private final ChatManager chatHandler;
	private final ItemManager itemHandler;
	private final MazeManager mazeManager;
	
	private final Int2ObjectMap<District> districts;
	private final Set<Maze> mazes;
	private District defaultDistrict;
	
	private WorldServerLoop serverLoop;
	private final ExecutorService pool;
	
	public WorldServer(GameServer gameServer, InetSocketAddress address) {
		super(address);
		this.gameServer = gameServer;
		
		this.chatHandler = new ChatManager(this);
		this.itemHandler = new ItemManager(this);
		this.mazeManager = new MazeManager(this);
		
		this.districts = new Int2ObjectOpenHashMap<District>();
		this.setupDistricts();
		
		this.mazes = new HashSet<Maze>();
		
		this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		this.serverLoop = new WorldServerLoop(this);
		
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
	
	public MazeManager getMazeManager() {
		return mazeManager;
	}
	
	public synchronized void registerMaze(Maze maze) {
		this.mazes.add(maze);
	}
	
	public synchronized void deregisterMaze(Maze maze) {
		this.mazes.remove(maze);
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
		this.serverLoop.start();
		SpiritWorker.getLogger().info("Channel " + this.getChannelId() + " registered on port " + this.getAddress().getPort());
	}

	public synchronized void onTick() {
		for (Maze maze : this.mazes) {
			maze.run();
		}
	}

}
