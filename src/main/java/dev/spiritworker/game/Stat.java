package dev.spiritworker.game;

import dev.spiritworker.net.packet.PacketBuilder;
import dev.spiritworker.net.packet.util.PacketWriter;

public class Stat {
	private final CharacterStats characterStats;
	private final int id;
	private float value;
	private boolean updated;
	
	public Stat(CharacterStats characterStats, int id) {
		this.characterStats = characterStats;
		this.id = id;
	}
	
	public Stat(CharacterStats characterStats, int id, float value) {
		this(characterStats, id);
		this.value = value;
	}
	
	public int getId() {
		return id;
	}
	
	public CharacterStats getCharacterStats() {
		return characterStats;
	}
	
	public float getValue() {
		return value;
	}
	
	public int getIntValue() {
		return (int) this.value;
	}

	public void updateValue(float value) {
		if (this.value == value) {
			return;
		}
		this.value = value;
		this.getCharacterStats().getCharacter().getSession().sendPacket(PacketBuilder.sendClientCharacterUpdate(getCharacterStats().getCharacter(), this));
	}

	public void setValue(float value) {
		if (this.value == value) {
			return;
		}
		this.value = value;
		this.updated = true;
	}
	
	public boolean isUpdated() {
		return this.updated;
	}

	public void write(PacketWriter p) {
		p.writeFloat(getValue());
		p.writeUint16(getId());
		this.updated = false;
	}
}
