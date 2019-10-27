package dev.spiritworker.game.character;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum CharacterClass {
	HARU (
		1, 
		new BaseStats()
			.setHealth(750).setHealthPerLevel(400)
			.setDefence(2.5f).setDefencePerLevel(5f)
			.setSuperArmor(262.5f).setSuperArmorPerLevel(140f)
			.setAttackMin(28.8f).setAttackMinPerLevel(14.4f)
			.setAttackMax(36f).setAttackMaxPerLevel(18f)
			.setCritDamage(28.8f).setCritDamagePerLevel(14.4f)
			.setCritChance(1f).setCritChancePerLevel(0)
			.setAccuracy(801.25f).setAccuracyPerLevel(2.5f)
			.setEvasion(.25f).setEvasionPerLevel(.5f),
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
		new BaseStats()
			.setHealth(750).setHealthPerLevel(350)
			.setDefence(5f).setDefencePerLevel(2.5f)
			.setSuperArmor(262.5f).setSuperArmorPerLevel(122.5f)
			.setAttackMin(28.8f).setAttackMinPerLevel(14.4f)
			.setAttackMax(36f).setAttackMaxPerLevel(18f)
			.setCritDamage(28.8f).setCritDamagePerLevel(14.4f)
			.setCritChance(1f).setCritChancePerLevel(0)
			.setAccuracy(800f).setAccuracyPerLevel(4.21875f)
			.setEvasion(0).setEvasionPerLevel(.75f),
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
		new BaseStats() // TODO
			.setHealth(750).setHealthPerLevel(400)
			.setDefence(2.5f).setDefencePerLevel(5f)
			.setSuperArmor(262.5f).setSuperArmorPerLevel(140f)
			.setAttackMin(28.8f).setAttackMinPerLevel(14.4f)
			.setAttackMax(36f).setAttackMaxPerLevel(18f)
			.setCritDamage(28.8f).setCritDamagePerLevel(14.4f)
			.setCritChance(1f).setCritChancePerLevel(0)
			.setAccuracy(801.25f).setAccuracyPerLevel(2.5f)
			.setEvasion(.25f).setEvasionPerLevel(.5f),
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
		new BaseStats()
			.setHealth(700).setHealthPerLevel(450)
			.setDefence(-5f).setDefencePerLevel(12.5f)
			.setSuperArmor(245f).setSuperArmorPerLevel(157.5f)
			.setAttackMin(14.4f).setAttackMinPerLevel(14.4f)
			.setAttackMax(18f).setAttackMaxPerLevel(18f)
			.setCritDamage(14.4f).setCritDamagePerLevel(14.4f)
			.setCritChance(1f).setCritChancePerLevel(0)
			.setAccuracy(800f).setAccuracyPerLevel(2.5f)
			.setEvasion(-.25f).setEvasionPerLevel(.75f),
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
		new BaseStats()
			.setHealth(800).setHealthPerLevel(300)
			.setDefence(0f).setDefencePerLevel(5f)
			.setSuperArmor(280f).setSuperArmorPerLevel(105f)
			.setAttackMin(28.8f).setAttackMinPerLevel(14.4f)
			.setAttackMax(36f).setAttackMaxPerLevel(18f)
			.setCritDamage(14.4f).setCritDamagePerLevel(14.4f)
			.setCritChance(1f).setCritChancePerLevel(0)
			.setAccuracy(801.25f).setAccuracyPerLevel(2.5f)
			.setEvasion(0).setEvasionPerLevel(.75f),
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
		new BaseStats() // TODO
			.setHealth(750).setHealthPerLevel(250)
			.setDefence(2.5f).setDefencePerLevel(5f)
			.setSuperArmor(262.5f).setSuperArmorPerLevel(140f)
			.setAttackMin(28.8f).setAttackMinPerLevel(14.4f)
			.setAttackMax(36f).setAttackMaxPerLevel(18f)
			.setCritDamage(28.8f).setCritDamagePerLevel(14.4f)
			.setCritChance(1f).setCritChancePerLevel(0)
			.setAccuracy(801.25f).setAccuracyPerLevel(2.5f)
			.setEvasion(.25f).setEvasionPerLevel(.5f),
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
		new BaseStats() // TODO
			.setHealth(700).setHealthPerLevel(400)
			.setDefence(2.5f).setDefencePerLevel(5f)
			.setSuperArmor(262.5f).setSuperArmorPerLevel(140f)
			.setAttackMin(28.8f).setAttackMinPerLevel(14.4f)
			.setAttackMax(36f).setAttackMaxPerLevel(18f)
			.setCritDamage(28.8f).setCritDamagePerLevel(14.4f)
			.setCritChance(1f).setCritChancePerLevel(0)
			.setAccuracy(801.25f).setAccuracyPerLevel(2.5f)
			.setEvasion(.25f).setEvasionPerLevel(.5f),
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
	private final BaseStats baseStats;
	
	private static Int2ObjectMap<CharacterClass> map;
	
	static {
		map = new Int2ObjectOpenHashMap<CharacterClass>();
		for (CharacterClass c : CharacterClass.values()) {
			map.put(c.getId(), c);
		}
	}
	
	CharacterClass(int id, BaseStats stats, int[] startingSkills, int[][] startingLoadout) {
		this.id = id;
		this.skills = startingSkills;
		this.loadout = startingLoadout;
		this.baseStats = stats;
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

	public BaseStats getBaseStats() {
		return baseStats;
	}

	public static CharacterClass getCharacterClass(GameCharacter character) {
		return CharacterClass.map.get(character.getType());
	}
	
	public static class BaseStats {
		private float health;
		private float defence;
		private float superArmor;
		private float attackMin;
		private float attackMax;
		private float critDamage;
		private float critChance;
		private float accuracy;
		private float evasion;
		private float healthPerLevel;
		private float defencePerLevel;
		private float superArmorPerLevel;
		private float attackMinPerLevel;
		private float attackMaxPerLevel;
		private float critDamagePerLevel;
		private float critChancePerLevel;
		private float accuracyPerLevel;
		private float evasionPerLevel;
		
		public float getHealth() {
			return health;
		}
		
		public BaseStats setHealth(float health) {
			this.health = health;
			return this;
		}
		
		public float getDefence() {
			return defence;
		}
		
		public BaseStats setDefence(float defence) {
			this.defence = defence;;
			return this;
		}
		
		public float getSuperArmor() {
			return superArmor;
		}
		
		public BaseStats setSuperArmor(float superArmor) {
			this.superArmor = superArmor;
			return this;
		}
		
		public float getAttackMin() {
			return attackMin;
		}
		
		public BaseStats setAttackMin(float attackMin) {
			this.attackMin = attackMin;
			return this;
		}
		
		public float getAttackMax() {
			return attackMax;
		}
		
		public BaseStats setAttackMax(float attackMax) {
			this.attackMax = attackMax;
			return this;
		}
		
		public float getCritDamage() {
			return critDamage;
		}
		
		public BaseStats setCritDamage(float critDamage) {
			this.critDamage = critDamage;
			return this;
		}
		
		public float getCritChance() {
			return critChance;
		}
		
		public BaseStats setCritChance(float critChance) {
			this.critChance = critChance;
			return this;
		}
		
		public float getAccuracy() {
			return accuracy;
		}
		
		public BaseStats setAccuracy(float accuracy) {
			this.accuracy = accuracy;
			return this;
		}
		
		public float getEvasion() {
			return evasion;
		}
		
		public BaseStats setEvasion(float evade) {
			this.evasion = evade;
			return this;
		}
		
		public float getHealthPerLevel() {
			return healthPerLevel;
		}
		
		public BaseStats setHealthPerLevel(float healthPerLevel) {
			this.healthPerLevel = healthPerLevel;
			return this;
		}
		
		public float getDefencePerLevel() {
			return defencePerLevel;
		}
		
		public BaseStats setDefencePerLevel(float defencePerLevel) {
			this.defencePerLevel = defencePerLevel;
			return this;
		}
		
		public float getSuperArmorPerLevel() {
			return superArmorPerLevel;
		}
		
		public BaseStats setSuperArmorPerLevel(float superArmorPerLevel) {
			this.superArmorPerLevel = superArmorPerLevel;
			return this;
		}
		
		public float getAttackMinPerLevel() {
			return attackMinPerLevel;
		}
		
		public BaseStats setAttackMinPerLevel(float attackMinPerLevel) {
			this.attackMinPerLevel = attackMinPerLevel;
			return this;
		}
		
		public float getAttackMaxPerLevel() {
			return attackMaxPerLevel;
		}
		
		public BaseStats setAttackMaxPerLevel(float attackMaxPerLevel) {
			this.attackMaxPerLevel = attackMaxPerLevel;
			return this;
		}
		
		public float getCritDamagePerLevel() {
			return critDamagePerLevel;
		}
		
		public BaseStats setCritDamagePerLevel(float critDamagePerLevel) {
			this.critDamagePerLevel = critDamagePerLevel;
			return this;
		}
		
		public float getCritChancePerLevel() {
			return critChancePerLevel;
		}
		
		public BaseStats setCritChancePerLevel(float critChancePerLevel) {
			this.critChancePerLevel = critChancePerLevel;
			return this;
		}
		
		public float getAccuracyPerLevel() {
			return accuracyPerLevel;
		}
		
		public BaseStats setAccuracyPerLevel(float accuracyPerLevel) {
			this.accuracyPerLevel = accuracyPerLevel;
			return this;
		}
		
		public float getEvasionPerLevel() {
			return evasionPerLevel;
		}
		
		public BaseStats setEvasionPerLevel(float evadePerLevel) {
			this.evasionPerLevel = evadePerLevel;
			return this;
		}
	}
}
