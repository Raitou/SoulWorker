package dev.spiritworker.game.map;

import dev.spiritworker.game.managers.MazeManager;

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
