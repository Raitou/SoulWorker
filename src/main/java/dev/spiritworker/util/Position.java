package dev.spiritworker.util;

public class Position {
	private float x;
	private float y;
	private float z;
	
	public Position() {

	}
	
	public Position(float x, float y) {
		set(x, y);
	}
	
	public Position(float x, float y, float z) {
		set(x, y, z);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public Position set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Position set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Position add(Position add) {
		this.x += add.getX();
		this.y += add.getY();
		this.z += add.getZ();
		return this;
	}
}
