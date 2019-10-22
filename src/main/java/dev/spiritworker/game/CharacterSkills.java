package dev.spiritworker.game;

import dev.morphia.annotations.Id;
import dev.morphia.annotations.Transient;
import dev.spiritworker.Constants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

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
	
	public void upgradeSkill() {
		
	}

	public void addSkill(Skill skill) {
		this.getMap().put(skill.getId(), skill);
	}
}
