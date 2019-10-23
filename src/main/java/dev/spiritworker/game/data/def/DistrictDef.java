package dev.spiritworker.game.data.def;

import java.nio.ByteBuffer;

import dev.spiritworker.game.data.ResourceDef;

public class DistrictDef extends ResourceDef {
	private String name;
	private int i1;
	private int i2;

	public DistrictDef(int id) {
		super(id);
	}
	
	public String getName() {
		return name;
	}

	@Override
	public void loadFromRes(ByteBuffer buf) {
		buf.getShort();
		buf.getShort();
		buf.getShort();
		readString(buf);
		this.name = readString(buf);
		buf.getShort();
		this.i1 = buf.getInt();
		this.i2 = buf.getInt();
		readString(buf);
		readString(buf);
		buf.get();
		buf.get();
		
		this.readString(buf);
		buf.get();
		buf.get();
	}
	
	public float getUnk1() {
		return this.i1;
	}
	
	public float getUnk2() {
		return this.i2;
	}
	
	@Override
	public String toString() {
		return "[" + this.getId() + "] " + this.name + " (" + this.i1 + " , " + this.i2 + ")";
	}
}
