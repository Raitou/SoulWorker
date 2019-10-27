package dev.spiritworker.game.character;

import dev.morphia.annotations.Transient;
import dev.spiritworker.game.data.SoulWorker;
import dev.spiritworker.game.data.def.SkillDef;
import dev.spiritworker.net.packet.util.PacketWriter;

public class Skill {
	private int id;
	private int modifier;
	
	@Transient private SkillDef skillDef;
	
	public Skill() {
		
	}
	
	public Skill(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getModifier() {
		return modifier;
	}
	
	public void setModifier(int mod) {
		this.modifier = mod;
	}
	
	public SkillDef getDef() {
		if (this.skillDef == null) {
			this.skillDef = SoulWorker.getSkillDefs().get(this.id);
		}
		
		return this.skillDef;
	}

	public void write(PacketWriter p) {
		p.writeUint32(this.id);
		p.writeUint32(this.modifier);
	}
	
	public String toString() {
		return "[Skill: " + this.id + "]";
	}
}
