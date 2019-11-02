package dev.spiritworker.game.inventory;

public class InventoryEquippable extends BaseInventoryTab {

	public InventoryEquippable(Inventory inventory, InventorySlotType slotType) {
		super(inventory, slotType);
		this.items = new Item[getSlotType().getDefaultCapacity()];
	}
}
