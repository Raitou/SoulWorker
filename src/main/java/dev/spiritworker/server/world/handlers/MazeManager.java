package dev.spiritworker.server.world.handlers;

import dev.spiritworker.game.GameCharacter;
import dev.spiritworker.game.Maze;
import dev.spiritworker.game.data.SoulWorker;
import dev.spiritworker.game.data.def.MazeDef;
import dev.spiritworker.net.packet.PacketBuilder;
import dev.spiritworker.server.world.WorldServer;
import dev.spiritworker.server.world.WorldSession;

public class MazeManager {
	private final WorldServer server;
	
	public MazeManager(WorldServer server) {
		this.server = server;
	}
	
	public WorldServer getServer() {
		return server;
	}
	
	public Maze createMaze(int mazeId, GameCharacter character) {
		MazeDef mazeDef = SoulWorker.getMazeDefs().get(mazeId);
		if (mazeDef == null) {
			return null;
		}
		
		// Create maze and register it to the gameserver
		Maze maze = new Maze(this, mazeId);
		getServer().registerMaze(maze);
		
		// Add characters TODO add characters from party
		maze.addCharacter(character);
		character.getSession().sendPacket(PacketBuilder.sendClientEnterMaze(character, maze));
		
		return maze;
	}
}
