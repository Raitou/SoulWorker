package dev.spiritworker.server.world;

public class WorldServerLoop extends Thread {
	public final static int TICK_RATE = 10;
	private final WorldServer server;
	private boolean running = true;
	
	public WorldServerLoop(WorldServer server) {
		this.server = server;
	}
	
	public WorldServer getServer() {
		return server;
	}

	@Override
	public void run() {
		// Thread variables
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / TICK_RATE;
		
		long now = 0;
		double delta = 0;
		
		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			if (delta >= 1) {
				// Tick
				server.onTick();
				// Math
				delta -= 1;
			}

			// Wait 1 millisecond
	    	try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
