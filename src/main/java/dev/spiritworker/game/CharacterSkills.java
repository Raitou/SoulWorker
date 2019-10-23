package dev.spiritworker.game;

import java.util.ArrayList;
import java.util.List;

import dev.morphia.annotations.Id;
import dev.morphia.annotations.Transient;
import dev.spiritworker.Constants;
import dev.spiritworker.SpiritWorker;
import dev.spiritworker.game.data.SoulWorker;
import dev.spiritworker.game.data.def.SkillDef;
import dev.spiritworker.net.packet.PacketBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class CharacterSkills {
	@Id private int id;
	@Transient private GameCharacter character;
	
	private Int2ObjectMap<Skill> skills;
	private int[][] loadout;
	
	// For morphia
	public CharacterSkills() {
		this.skills = new Int2ObjectOpenHashMap<Skill>();
		this.loadout = new int[Constants.LOADOUT_COLUMNS][Constants.LOADOUT_COLUMN_SIZE];
	}
	
	public CharacterSkills(GameCharacter character) {
		this();
		this.character = character;
		this.id = character.getId();
	}
	
	public GameCharacter getCharacter() {
		return character;
	}
	
	public void setCharacter(GameCharacter character) {
		this.character = character;
	}
	
	public Int2ObjectMap<Skill> getMap() {
		return skills;
	}

	public int[][] getLoadout() {
		return loadout;
	}
	
	public Skill getSkillById(int id) {
		return getMap().get(id);
	}
	
	public Skill getSkillInLoadout(int column, int i) {
		return getSkillById(this.loadout[column][i]);
	}
	
	public void setSkillInLoadout(int column, int i, int skillId) {
		try {
			this.loadout[column][i] = skillId;
		} catch (Exception e) {
			
		}
	}
	
	public void upgradeSkill(int skillId, int unk1, int unk2) {
		// Sanity check
		SkillDef skillDef = SoulWorker.getSkillDefs().get(skillId);
		if (skillDef == null) {
			return;
		}
		
		int cost = 1; // TODO get skill point cost of skill and check if player has enough
		if (getCharacter().getSkillPoints() < cost) {
			return;
		}
		
		IntSet updatedColumns = null;
		Skill newSkill = null;
		
		// Check if the player has this skill already
		if (getMap().containsKey(skillDef.getPrevSkill()) && skillDef.getPrevSkill() != 0) {
			// Player has a lower level version of this skill, lets upgrade
			updatedColumns = replaceSkill(skillDef.getPrevSkill(), skillId);
			newSkill = addSkill(new Skill(skillId));
		} else if (getCharacter().getType() == skillDef.getCharacterType()) {
			// Player does not have this skill, lets add it
			newSkill = addSkill(new Skill(skillId));
		} else {
			return;
		}
		
		// Update skill points
		getCharacter().setSkillPoints(getCharacter().getSkillPoints() - cost);
		getCharacter().setUsedSkillPoints(getCharacter().getUsedSkillPoints() + cost);
	
		// Send packets
		if (newSkill != null) {
			getCharacter().getSession().sendPacket(PacketBuilder.sendClientUpdateSkill(newSkill, unk1, unk2));
		}
		if (updatedColumns != null && updatedColumns.size() > 0) {
			getCharacter().getSession().sendPacket(PacketBuilder.sendClientUpdateSkillLoadout(character, updatedColumns));
		}
		getCharacter().getSession().sendPacket(PacketBuilder.sendClientUpdateSkillPoints(character));
	}

	public Skill addSkill(Skill skill) {
		getMap().put(skill.getId(), skill);
		return skill;
	}
	
	public void removeSkill(int skill) {
		this.getMap().remove(skill);
	}
	
	public IntSet replaceSkill(int skill, int newSkill) {
		// Create list of loadouts that need to be updated
		IntSet list = new IntOpenHashSet();
		
		// Remove skill
		removeSkill(skill);
		
		// Scan for any skills that need to be updated
		for (int column = 0; column < this.loadout.length; column++) {
			for (int i = 0; i < this.loadout[column].length; i++) {
				if (this.loadout[column][i] == skill) {
					this.loadout[column][i] = newSkill;
					list.add(column);
				}
			}
		}
		
		return list;
	}
}
