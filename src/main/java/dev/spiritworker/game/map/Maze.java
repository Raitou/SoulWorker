package dev.spiritworker.game.map;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.game.character.Skill;
import dev.spiritworker.game.data.SoulWorker;
import dev.spiritworker.game.data.def.MazeDef;
import dev.spiritworker.game.data.def.MonsterDef;
import dev.spiritworker.game.data.def.SkillDef;
import dev.spiritworker.game.data.spawns.MazeData;
import dev.spiritworker.game.data.spawns.MazeData.MazeSpawnData;
import dev.spiritworker.game.data.spawns.MazeData.MazeStageData;
import dev.spiritworker.game.data.spawns.MazeData.MazeWaveData;
import dev.spiritworker.game.managers.MazeManager;
import dev.spiritworker.net.packet.PacketBuilder;
import dev.spiritworker.server.world.WorldServer;
import dev.spiritworker.util.FileUtils;
import dev.spiritworker.util.Position;
import dev.spiritworker.util.Utils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class Maze extends GameMap implements Runnable {
	private final MazeDef def;
	private final MazeManager mazeManager;
	private int lastId = 2000000000;
	private Int2ObjectMap<Monster> monsters;
	private Int2ObjectMap<Monster> bosses;

	private int currentStageCounter = 0;
	private MazeStageData currentStage;
	private int nextWave = 0;

	
	public Maze(MazeManager mazeManager, MazeDef mazeDef) {
		super(mazeDef.getId());
		this.def = mazeDef;
		this.mazeManager = mazeManager;
		this.monsters = new Int2ObjectOpenHashMap<Monster>();
		this.bosses = new Int2ObjectOpenHashMap<Monster>();
		
		if (getData() != null && getData().getStages().size() > 0) {
			this.currentStageCounter = 0;
			currentStage = getData().getStages().get(this.currentStageCounter);
			advanceWave();
		}
	}

	public MazeDef getDef() {
		return def;
	}
	
	public MazeData getData() {
		return def.getData();
	}

	public MazeManager getMazeManager() {
		return mazeManager;
	}
	
	public WorldServer getServer() {
		return getMazeManager().getServer();
	}
	
	public int getNextId() {
		return ++lastId;
	}
	
	public synchronized Int2ObjectMap<Monster> getMonsters() {
		return this.monsters;
	}
	
	public synchronized Int2ObjectMap<Monster> getBosses() {
		return this.bosses;
	}
	
	public synchronized void registerMonster(Monster monster) {
		getMonsters().put(monster.getId(), monster);
	}
	
	public synchronized void registerBoss(Monster monster) {
		getMonsters().put(monster.getId(), monster);
		getBosses().put(monster.getId(), monster);
	}
	
	public synchronized void deregisterMonster(Monster monster) {
		getMonsters().remove(monster.getId());
		if (monster.isBoss()) {
			getBosses().remove(monster.getId());
		}
		
		onMonsterDeath(monster);
	}
	
	private Monster spawnMonster(MazeSpawnData spawnData) {
		MonsterDef def = SoulWorker.getMonsterDefs().get(spawnData.getId());
		
		if (def == null) {
			return null;
		}
		
		Monster monster = new Monster(this, def, spawnData);
		registerMonster(monster);
		
		if (monster.isBoss()) {
			registerBoss(monster);
		}
		
		return monster;
	}

	@Override
	public synchronized void run() {

	}
	
	public void onEnter(GameCharacter character) {

	}
	
	public void onLeave(GameCharacter character) {
		if (getCharacters().size() == 0) {
			getServer().deregisterMaze(this);
		}
	}

	public void onEntered(GameCharacter character) {
		// Client has loaded in
		character.getSession().sendPacket(PacketBuilder.sendClientSpawnMonsters(this.getMonsters().values()));
	}

	// TODO TODO TODO TODO TODO TODO TODO
	public synchronized void onSkillUse(GameCharacter character, float x, float y, float z, float angle, SkillDef skillDef) {
		Position point = new Position(x, y, z);
		for (Monster monster : getMonsters().values()) {
			if (Utils.getFast2dDist(point, monster.getPosition()) <= (500f * 500f)) {
				// Calculate damage
				int damage = Utils.randomRange(character.getStats().getAttackMin().getIntValue(), character.getStats().getAttackMax().getIntValue());
				monster.takeDamage(damage);
				if (monster.getCurrentHealth() <= 0) {
					deregisterMonster(monster);
				}
				// Damage packet
				character.getSession().sendPacket(PacketBuilder.sendClientSkillDamageInfo(
					character, 
					monster, 
					skillDef,
					damage
				));
			}
		}
	}

	public synchronized void onEnterPortal(int portal) {
		int p = portal;
		MazeStageData stage = getData().getStages().stream().filter(s -> s.getId() == p).findFirst().orElse(null);
		
		if (stage == null) {
			return;
		}
		
		for (GameCharacter character : getCharacters()) {
			character.getPosition().set(stage.getPos());
			character.setAngle(stage.getAngle());
			broadcastPacket(PacketBuilder.sendClientUpdatePosition(character));
		}
		// Close old portal
		broadcastPacket(PacketBuilder.sendClientMazePortalUpdate(portal, false, false));
	}
	
	public void onMonsterDeath(Monster monster) {
		if (monster.isBoss() && getBosses().size() == 0) {
			// TODO kill all other mobs
			// Allow the client to leave
			broadcastPacket(PacketBuilder.sendClientFinishMaze(5, 100000));
			broadcastPacket(PacketBuilder.sendClientFinishMazeOpenPortal(10003, true));
			broadcastPacket(PacketBuilder.sendClientMazePortalUpdate(currentStage.getId(), true, true));
		} else if (getMonsters().size() == 0) {
			this.advanceWave();
		}
	}
	
	private void spawnMonstersInWave() {
		MazeWaveData wave = this.currentStage.getWaves().get(this.nextWave);
		Collection<Monster> monsters = new LinkedList<Monster>();
		
		for (MazeSpawnData spawnData : wave.getSpawns()) {
			monsters.add(spawnMonster(spawnData));
		}
		
		if (this.getCharacters().size() > 0) {
			this.broadcastPacketToLoadedCharacters(PacketBuilder.sendClientSpawnMonsters(monsters));
		}
		
		this.nextWave++;
	}
	
	private void advanceStage() {
		// Open portal for players
		broadcastPacket(PacketBuilder.sendClientMazePortalUpdate(currentStage.getId(), true, false));
		// Increment stage
		this.currentStageCounter++;
		currentStage = getData().getStages().get(this.currentStageCounter);
		this.nextWave = 0;
		// Spawn mobs
		advanceWave();
	}

	private void advanceWave() {
		if (this.nextWave < currentStage.getWaves().size()) {
			this.spawnMonstersInWave();
		} else if (getData().getLastStage() != currentStage) {
			// Increment stage
			advanceStage();
		}		
	}
	
	public void broadcastPacketToLoadedCharacters(byte[] packet) {
		for (GameCharacter character : getCharacters()) {
			if (!character.hasLoadedInMap()) return;
			character.getSession().sendPacket(packet);
		}
	}
}
