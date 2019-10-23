package dev.spiritworker.game.character;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum CharacterClass {
	HARU (
		1, 
		new int[] {12040011, 12070011, 12000211, 12100011, 111, 11160111, 11, 12090011, 12080011, 12110011, 12000411, 12001211}, 
		new int[][] {
			{12000211, 12000411, 12001211},
			{12000411, 0, 0},
			{12001211, 0, 0},
			{0, 0, 0},
			{0, 0, 0},
			{0, 0, 0}
		}
	),
	ERWIN (
		2, 
		new int[] {22110011, 112, 12, 22040011, 22070011, 22000211, 22100011, 21160111, 22000111, 22090011, 22080011, 22000511}, 
		new int[][] {
			{22000111, 22000211, 22000511},
			{22000211, 0, 0},
			{22000511, 0, 0},
			{0, 0, 0},
			{0, 0, 0},
			{0, 0, 0}
		}
	),
	LILY (
		3, 
		new int[] {113, 32080011, 13, 32110011, 32000311, 32040011, 32070011, 32000211, 32100011, 31160111, 32000111, 32090011}, 
		new int[][] {
			{32000111, 32000211, 32000311},
			{32000211, 0, 0},
			{32000311, 0, 0},
			{0, 0, 0},
			{0, 0, 0},
			{0, 0, 0}
		}
	),
	JIN (
		4, 
		new int[] {42090011, 114, 14, 42080011, 42110011, 42000411, 42000311, 42040011, 42070011, 42000211, 42100011, 41160111}, 
		new int[][] {
			{42000211, 42000311, 42000411},
			{42000311, 0, 0},
			{42000411, 0, 0},
			{0, 0, 0},
			{0, 0, 0},
			{0, 0, 0}
		}
	),
	STELLA (
		5, 
		new int[] {52040011, 52070011, 52000211, 52100011, 51160111, 52000111, 115, 15, 52090011, 52080011, 52110011, 52000311}, 
		new int[][] {
			{52000111, 52000211, 52000311},
			{52000211, 0, 0},
			{52000311, 0, 0},
			{0, 0, 0},
			{0, 0, 0},
			{0, 0, 0}
		}
	),
	IRIS (
		6, 
		new int[] {62110011, 62000311, 116, 62040011, 62070011, 62000211, 16, 62100011, 61160111, 62000111, 62090011, 62080011}, 
		new int[][] {
			{62000111, 62000211, 62000311},
			{62000211, 0, 0},
			{62000311, 0, 0},
			{0, 0, 0},
			{0, 0, 0},
			{0, 0, 0}
		}
	),
	CHII (
		7, 
		new int[] {72080011, 117, 72110011, 17, 76000111, 72000311, 72040011, 72070011, 72000211, 72100011, 71160111, 72000111, 72090011}, 
		new int[][] {
			{72000111, 72000211, 72000311},
			{72000211, 0, 0},
			{72000311, 0, 0},
			{0, 0, 0},
			{0, 0, 0},
			{0, 0, 0}
		}
	);
	
	private final int id;
	private final int[] skills;
	private final int[][] loadout;
	
	private static Int2ObjectMap<CharacterClass> map;
	
	static {
		map = new Int2ObjectOpenHashMap<CharacterClass>();
		for (CharacterClass c : CharacterClass.values()) {
			map.put(c.getId(), c);
		}
	}
	
	CharacterClass(int id, int[] startingSkills, int[][] startingLoadout) {
		this.id = id;
		this.skills = startingSkills;
		this.loadout = startingLoadout;
	}

	public int getId() {
		return id;
	}

	public int[] getSkills() {
		return skills;
	}

	public int[][] getLoadout() {
		return loadout;
	}

	public static CharacterClass getCharacterClass(GameCharacter character) {
		return CharacterClass.map.get(character.getType());
	}
}
