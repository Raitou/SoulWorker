package dev.spiritworker.util;

public class Position {
	private float x;
	private float z;
	private float y;
	
	public Position() {

	}
	
	public Position(float x, float z) {
		set(x, z);
	}
	
	public Position(float x, float z, float y) {
		set(x, z, y);
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
	
	public Position set(float x, float z) {
		this.x = x;
		this.z = z;
		return this;
	}
	
	public Position set(float x, float z, float y) {
		this.x = x;
		this.z = z;
		this.y = y;
		return this;
	}
	
	public Position add(Position add) {
		this.x += add.getX();
		this.z += add.getZ();
		this.y += add.getY();
		return this;
	}
}
