package dev.spiritworker.game.map;

import dev.spiritworker.game.data.def.MonsterDef;
import dev.spiritworker.game.data.spawns.MazeData.MazeSpawnData;
import dev.spiritworker.net.packet.util.PacketWriter;
import dev.spiritworker.util.Position;

public class Monster {
	private final int id;
	private final MonsterDef monsterDef;
	
	private int level;
	private float hp;
	private float maxHp;
	private int percentHp;
	
	private final Position position;
	private float angle;
	
	public Monster(Maze maze, MonsterDef monsterDef, Position position) {
		this.id = maze.getNextId();
		this.monsterDef = monsterDef;
		this.position = position;
		this.level = 1;
		this.maxHp = 100;
		this.setHealth(maxHp);
	}

	public Monster(Maze maze, MonsterDef monsterDef, MazeSpawnData spawnData) {
		this.id = maze.getNextId();
		this.monsterDef = monsterDef;
		this.position = spawnData.getPos().clone();
		this.angle = spawnData.getAngle();
		this.level = spawnData.getLevel();
		this.maxHp = spawnData.getHealth();
		this.setHealth(maxHp);
	}

	public int getId() {
		return id;
	}
	
	public int getLevel() {
		return level;
	}

	public float getCurrentHealth() {
		return this.hp;
	}
	
	public float getMaxHealth() {
		return this.maxHp;
	}
	
	public int getPercentHealth() {
		return this.percentHp;
	}
	
	public void setHealth(float newHp) {
		newHp = Math.max(newHp, 0);
		this.hp = newHp;
		this.percentHp = (int) Math.ceil(100 * (newHp / maxHp));
	}
	
	public void takeDamage(int damage) {
		this.hp -= damage;
		this.setHealth(this.getCurrentHealth() - damage);
	}	

	public MonsterDef getDef() {
		return monsterDef;
	}

	public Position getPosition() {
		return position;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	// TODO
	public boolean isBoss() {
		return this.getDef().getId() == 30102003;
	}
	
	public void write(PacketWriter p) {
		p.writeUint32(this.getId());
		p.writeFloat(this.getPosition().getX());
		p.writeFloat(this.getPosition().getY());
		p.writeFloat(this.getPosition().getZ());
		p.writeFloat(this.getAngle());
		p.writeUint32((int) getMaxHealth());
		p.writeUint32(0);
		p.writeUint32(10001);
		p.writeUint8(2); // Level ?
		p.writeUint32(getDef().getId());
		p.writeInt32(-1);
		p.writeUint32(40117);
		p.writeUint8(1);
		p.writeUint32(0);
		p.writeUint32(0);
		p.writeUint32(0);
		// Metadata
		p.writeUint8(5);
		
		p.writeUint8(1);
		p.writeFloat(100f);
		p.writeUint8(3); 
		p.writeFloat(200f);
		
		p.writeUint8(0x0e);
		p.writeFloat(100f);
		p.writeUint8(0x12);
		p.writeFloat(100f);
		p.writeUint8(0x13);
		p.writeFloat(100f);
		
		// Padding
		p.writeUint32(0);
	}
}
