package dev.spiritworker.game;

import java.util.HashSet;
import java.util.Set;

import dev.spiritworker.game.inventory.Item;
import dev.spiritworker.net.packet.PacketBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class CharacterStats {
	private final static Float ZERO = Float.valueOf(0);
	private final GameCharacter character;
	
	private final Int2ObjectMap<Stat> statsMap;
	private final Set<Stat> updatedStats;
	
	private Stat hp;
	private Stat hpMax;
	private Stat staminaPercent;
	private Stat stamina;
	private Stat staminaRegen;
	private Stat moveSpeed;
	private Stat attackSpeed;
	private Stat attackMin;
	private Stat attackMax;
	private Stat defence;
	private Stat accuracy;
	private Stat critChance;
	private Stat critResist;
	private Stat critValue;
	private Stat damageReduction;
	private Stat evasion;
	
	public CharacterStats(GameCharacter character) {
		this.character = character;
		this.statsMap = new Int2ObjectOpenHashMap<Stat>();
		this.updatedStats = new HashSet<Stat>();
		
		this.hp = createStat(1);
		this.hpMax = createStat(10);
		this.staminaPercent = createStat(12);
		this.stamina = createStat(14);
		this.staminaRegen = createStat(15);
		this.moveSpeed = createStat(15);
		this.attackSpeed = createStat(19);
		this.attackMin = createStat(20);
		this.attackMax = createStat(21);
		this.defence = createStat(24);
		this.accuracy = createStat(26);
		this.critChance = createStat(29);
		this.critResist = createStat(31);
		this.critValue = createStat(35);
		this.damageReduction = createStat(38);
		this.evasion = createStat(43);
	}
	
	public GameCharacter getCharacter() {
		return character;
	}

	public Int2ObjectMap<Stat> getMap() {
		return statsMap;
	}

	public Set<Stat> getUpdatedStats() {
		return updatedStats;
	}
	
	private Stat createStat(int statId) {
		Stat stat = new Stat(this, statId);
		getMap().put(statId, stat);
		return stat;
	}
	
	public void recalc() {
		float hpMax = 850 + (character.getLevel() * 300);
		float hp = Math.min(this.hp.getValue(), hpMax);
		float defence = 100;
		float stamina = 100;
		float attackMin = 10; // Filler
		float attackMax = 20; // Filler
		float attackSpeed = 100;
		float moveSpeed = 100;
		
		// Add item stats here
		for (int slot = 0; slot < getCharacter().getInventory().getEquippedItems().getItems().length; slot++) {
			Item item = getCharacter().getInventory().getEquippedItems().getItemAt(slot);
			if (item == null) {
				continue;
			}
			defence += item.getDefence();
			attackMin += item.getWeaponDamage();
			attackMax += item.getWeaponDamage();
		}
		
		if (getCharacter().getMap() != null && getCharacter().getMap() instanceof District) {
			hp = hpMax;
		}
		
		// Update
		this.hp.setValue(hp);
		this.hpMax.setValue(hpMax);
		this.defence.setValue(defence);
		this.stamina.setValue(stamina);
		this.attackMin.setValue(attackMin);
		this.attackMax.setValue(attackMax);
		this.attackSpeed.setValue(attackSpeed);
		this.moveSpeed.setValue(moveSpeed);
		
		// Sync to client
		this.getCharacter().getSession().sendPacket(PacketBuilder.sendClientCharacterUpdate(this));
	}

	public void onUpdate(Stat stat) {
		
	}

	public Stat getHp() {
		return hp;
	}

	public Stat getMaxHp() {
		return hpMax;
	}

	public Stat getStaminaPercent() {
		return staminaPercent;
	}

	public Stat getStamina() {
		return stamina;
	}

	public Stat getStaminaRegen() {
		return staminaRegen;
	}

	public Stat getMoveSpeed() {
		return moveSpeed;
	}

	public Stat getAttackSpeed() {
		return attackSpeed;
	}

	public Stat getAttackMin() {
		return attackMin;
	}

	public Stat getAttackMax() {
		return attackMax;
	}

	public Stat getDefence() {
		return defence;
	}

	public Stat getAccuracy() {
		return accuracy;
	}

	public Stat getCritChance() {
		return critChance;
	}

	public Stat getCritResist() {
		return critResist;
	}

	public Stat getCritValue() {
		return critValue;
	}

	public Stat getDamageReduction() {
		return damageReduction;
	}

	public Stat getEvasion() {
		return evasion;
	}
}
