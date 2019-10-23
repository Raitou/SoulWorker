package dev.spiritworker.game;

import dev.spiritworker.server.world.handlers.MazeManager;

public class Maze extends GameMap implements Runnable {
	private final MazeManager mazeManager;
	private boolean spawned = false;
	
	public Maze(MazeManager mazeManager, int id) {
		super(id);
		this.mazeManager = mazeManager;
	}

	public MazeManager getMazeManager() {
		return mazeManager;
	}

	@Override
	public synchronized void run() {
		
	}

}
