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
	
	public ItemDef(int id) {
		super(id);
	}

	@Override
	public void loadFromRes(ByteBuffer buf) { // 52
		this.classification = buf.getShort() & 0xffff;
		buf.get();
		buf.get();
		buf.get();
		buf.get();
		buf.getShort(); // Same as levelReq but some items have a value of 1
		buf.getInt();
		buf.getInt();
		buf.getShort();
		buf.get();
		buf.getInt();
		buf.getInt();
		buf.getInt();
		buf.get();
		buf.get();
		buf.get();
		buf.get();
		buf.getInt(); // ??
		this.levelReq = buf.getShort();
		this.character = buf.get();
		buf.get();
		buf.getShort();
		buf.getInt();
		s = readString(buf);
	
		this.durability = buf.get();
		buf.get();
		
		this.damageMin = buf.getInt();
		this.damageMax = buf.getInt();
		buf.getInt();
		this.defenceMin = buf.getInt();
		this.defenceMax = buf.getInt();
		
		// Skip for now
		buf.position(buf.position() + 113);
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
