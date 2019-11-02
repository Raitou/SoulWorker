package dev.spiritworker.util;

import java.util.Random;

public class Utils {
	public static final Random random = new Random();
	
	public static int randomRange(int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}
	
	public static String bytesToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for (byte b: a) {
			sb.append(String.format("%02x", b));
	   		sb.append(" ");
		}
		return sb.toString();
	}

	public static int getAppearanceType(int appearance) {
		return (int) Math.floor(appearance / 1000f);
	}

	public static float getFast2dDist(Position pos1, Position pos2) {
		float xs = pos1.getX() - pos2.getX();
		float ys = pos1.getY() - pos2.getY();
	    return (xs * xs) + (ys * ys);
	}
}
