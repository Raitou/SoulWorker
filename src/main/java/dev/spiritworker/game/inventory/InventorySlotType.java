package dev.spiritworker.game.inventory;

public enum InventorySlotType {
	COSMETIC		(0x00, 19, 0),
	EQUIPPED		(0x01, 10, 0),
	NORMAL			(0x02, 36, 29),
	FASHION			(0x04, 48, 28),
	PREMIUM			(0x0d, 384, 0),
	BANK_NORMAL		(0x10, 24, 30),
	BANK_FASHION	(0x11, 48, 28),
	BANK_PREMIUM	(0x12, 384, 0);
	
	private final byte id;
	private final int defaultCapacity;
	private final int maxUpgrades;
	private static final InventorySlotType[] tabsTypes = new InventorySlotType[] {NORMAL, FASHION, PREMIUM, BANK_NORMAL, BANK_FASHION, BANK_PREMIUM};
	
	private InventorySlotType (int id, int defaultCapacity, int maxUpgrades) {
		this.id = (byte) id;
		this.defaultCapacity = defaultCapacity;
		this.maxUpgrades = maxUpgrades;
	}
	
	public byte getValue() {
		return this.id;
	}

	public int getDefaultCapacity() {
		return defaultCapacity;
	}

	public int getMaxUpgrades() {
		return maxUpgrades;
	}

	public static InventorySlotType[] getInventoryTabTypes() {
		return tabsTypes;
	}

	public boolean isBankTab() {
		return this.id >= 0x10;
	}
}
