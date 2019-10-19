package dev.spiritworker.game.inventory;

public enum ItemEnhanceResult {
	SUCCESS	(1),
	FAILURE	(2),
	BREAK (3);
	
	private final int value;

	private ItemEnhanceResult(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}
}
