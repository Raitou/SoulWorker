package dev.spiritworker.game.data.spawns;

import java.util.LinkedList;
import java.util.List;

import dev.spiritworker.util.Position;

public class MazeData {
	private int id;
	private String name;
	private List<MazeStageData> stages;
	private Position pos;
	private float angle;
	
	private int version = 1;
	
	public MazeData() {}
	
	public MazeData(int id) {
		this.id = id;
		this.stages = new LinkedList<MazeStageData>();
		this.pos = new Position();
	}
	
	public int getId() {
		return id;
	}

	public List<MazeStageData> getStages() {
		return stages;
	}
	
	public MazeStageData getLastStage() {
		return getStages().get(getStages().size() - 1);
	}

	public Position getPosition() {
		return pos;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public static class MazeStageData {
		private int stage;
		private List<MazeWaveData> waves;
		private Position pos;
		private float angle;
		
		public MazeStageData() {
			this.waves = new LinkedList<MazeWaveData>();
			this.pos = new Position();
		}
		
		public MazeStageData(int stage) {
			this.setStage(stage);
			this.waves = new LinkedList<MazeWaveData>();
			this.pos = new Position();
		}

		public int getId() {
			return stage;
		}

		public void setStage(int stage) {
			this.stage = stage;
		}

		public List<MazeWaveData> getWaves() {
			return waves;
		}

		public Position getPos() {
			return pos;
		}

		public float getAngle() {
			return angle;
		}

		public void setAngle(float angle) {
			this.angle = angle;
		}
	}
	
	public static class MazeWaveData {
		private List<MazeSpawnData> spawns;
		
		public MazeWaveData() {
			this.spawns = new LinkedList<MazeSpawnData>();
		}

		public List<MazeSpawnData> getSpawns() {
			return spawns;
		}
	}
	
	public static class MazeSpawnData {
		private int id; // Monster id
		private int level;
		private int health;
		private Position pos;
		private float angle;
		
		public MazeSpawnData() {}
		
		public MazeSpawnData(int id) {
			this.setId(id);
			this.pos = new Position();
		}
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public Position getPos() {
			return pos;
		}

		public float getAngle() {
			return angle;
		}

		public void setAngle(float angle) {
			this.angle = angle;
		}

		public int getHealth() {
			return health;
		}

		public void setHealth(int health) {
			this.health = health;
		}

		public void createPos() {
			this.pos = new Position();
		}
		
		public int getLevel() {
			return level;
		}

		public void setLevel(byte b) {
			this.level = b;
		}
	}
}
