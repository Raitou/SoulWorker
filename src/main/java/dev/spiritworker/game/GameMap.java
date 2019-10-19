package dev.spiritworker.game;

import java.util.HashSet;
import java.util.Set;

import dev.spiritworker.net.packet.PacketBuilder;

public class GameMap {
	private final int id;
	private final Set<GameCharacter> characters;
	
	public GameMap(int id) {
		this.id = id;
		this.characters = new HashSet<GameCharacter>();
	}

	public int getMapId() {
		return id;
	}
	
	public Set<GameCharacter> getCharacters() {
		return characters;
	}

	public synchronized void addCharacter(GameCharacter character) {
		// Already in this map
		if (character.getMap() == this) {
			return;
		} else if (character.getMap() != null) {
			character.getMap().removeCharacter(character);
		}
		
		character.setMap(this);
		getCharacters().add(character);
		
		// Broadcast
		if (getCharacters().size() > 1) {
			broadcastPacketFrom(character, PacketBuilder.sendClientPlayerSpawn(character));
		}
	}
	
	public synchronized void removeCharacter(GameCharacter character) {
		// Not in this map
		if (character.getMap() != this) {
			return;
		}
		
		// Remove from map
		if (getCharacters().contains(character)) {
			character.setMap(null);
			getCharacters().remove(character);
			
			// Broadcast
			if (getCharacters().size() > 0) {
				broadcastPacketFrom(character, PacketBuilder.sendClientPlayerRemove(character));
			}
		}
	}
	
	public void broadcastPacket(byte[] packet) {
		for (GameCharacter character : getCharacters()) {
			character.getSession().sendPacket(packet);
		}
	}
	
	public void broadcastPacketFrom(GameCharacter sender, byte[] packet) {
		for (GameCharacter character : getCharacters()) {
			if (sender != character) {
				character.getSession().sendPacket(packet);
			}
		}
	}

	public void getPlayersInfo(GameCharacter character) {
		// Send players info if character isnt the only one in the map
		if (getCharacters().size() > 1) {
			character.getSession().sendPacket(PacketBuilder.sendClientPlayersInfo(character, this));
		}
	}
}
