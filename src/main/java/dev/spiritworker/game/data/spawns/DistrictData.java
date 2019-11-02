package dev.spiritworker.game.data.spawns;

import java.util.LinkedList;
import java.util.List;

import dev.spiritworker.util.Position;

public class DistrictData {
	private int id;
	private List<DistrictNpcData> npcs;
	
	private int version = 1;
	
	public DistrictData() {}
	
	public DistrictData(int id) {
		this.id = id;
		this.npcs = new LinkedList<DistrictNpcData>();
	}
	
	public int getId() {
		return id;
	}

	public List<DistrictNpcData> getNpcList() {
		return npcs;
	}

	public static class DistrictNpcData {
		private int id;
		private Position pos;
		private float angle;
		private int tableId;
		
		public DistrictNpcData () {}
		
		public DistrictNpcData(int id) {
			this.id = id;
			this.pos = new Position();
		}

		public int getId() {
			return id;
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

		public int getTableId() {
			return tableId;
		}

		public void setTableId(int tableId) {
			this.tableId = tableId;
		}
	}
	
}
