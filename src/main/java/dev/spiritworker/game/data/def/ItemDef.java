package dev.spiritworker.game.data.def;

import java.nio.ByteBuffer;

import dev.spiritworker.game.data.ResourceDef;

public class ItemDef extends ResourceDef {
	public String path;
	public String name;
	public String description;
	
	private int classification;	// item type
	private int character;		// character id that can use this item
	
	private String s;
	
	private int upgradesMax; // ?
	private int levelReq;
	private int durability;
	private int damageMin;
	private int damageMax;
	private int defenceMin;
	private int defenceMax;
	private int sellPrice;
	
	private int statId1;
	private int statId2;
	private int statId3;
	private int statId4;
	private int statId5;
	private int statValue1;
	private int statValue2;
	private int statValue3;
	private int statValue4;
	private int statValue5;
	
	public ItemDef(int id) {
		super(id);
	}

	@Override
	public void loadFromRes(ByteBuffer buf) { // 52
		this.classification = buf.getInt();
		buf.get();
		buf.get();
		buf.getShort(); // Same as levelReq but some items have a value of 1
		this.sellPrice = buf.getInt();
		buf.getInt();
		buf.getInt();
		buf.getInt();
		buf.getShort();
		buf.get();
		buf.getInt();
		buf.getInt();
		buf.getInt();
		this.levelReq = buf.getShort();
		this.character = buf.get();
		buf.get();
		buf.get();
		buf.get();
		buf.getInt(); // ??
		s = readString(buf);
	
		this.durability = buf.get();
		buf.get();
		
		this.damageMin = buf.getInt();
		this.damageMax = buf.getInt();
		buf.getInt();
		this.defenceMin = buf.getInt();
		this.defenceMax = buf.getInt();
		buf.getInt();
		buf.get();
		buf.get();
		buf.get();
		buf.get();
		buf.get();
		statId1 = buf.getInt(); // --------- Stats ----------
		statId2 = buf.getInt();
		statId3 = buf.getInt();
		statId4 = buf.getInt();
		statId5 = buf.getInt();
		statValue1 = buf.getInt(); // Signed int
		statValue2 = buf.getInt(); // Signed int
		statValue3 = buf.getInt(); // Signed int
		statValue4 = buf.getInt(); // Signed int
		statValue5 = buf.getInt(); // Signed int
		buf.getInt();
		buf.getInt();
		buf.getInt();
		buf.getShort();
		buf.getInt();
		buf.getInt();
		buf.getInt();
		buf.getShort();
		buf.getInt();
		buf.getShort();
		buf.getInt();
		buf.get();
		buf.getShort();
		buf.getInt();
		buf.getShort();
		buf.get();
		buf.get();
		buf.getInt();
		buf.get();
		buf.getInt();
		buf.get();
		buf.get();
		buf.getInt();
	}

	public int getDamageMin() {
		return damageMin;
	}

	public int getDamageMax() {
		return damageMax;
	}

	public int getDefenceMin() {
		return defenceMin;
	}

	public int getDefenceMax() {
		return defenceMax;
	}

	public int getMaxDurability() {
		return durability;
	}
	
}
