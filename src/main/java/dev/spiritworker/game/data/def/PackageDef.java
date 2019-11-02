package dev.spiritworker.game.data.def;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import dev.spiritworker.game.data.ResourceDef;

public class PackageDef extends ResourceDef {
	private List<PackageItemData> items;
	
	public PackageDef(int id) {
		super(id);
		this.items = new LinkedList<PackageItemData>();
	}
	
	public List<PackageItemData> getItems() {
		return this.items;
	}

	@Override
	public void loadFromRes(ByteBuffer buf) {
		// Skip for now
		buf.position(buf.position() + 12);
		int count = 14; // 15 slots, minus 1 initally
		
		for (int item = buf.getInt(); item != 0 && count > 0; item = buf.getInt()) {
			PackageItemData data = new PackageItemData(item);
			getItems().add(data);
			count--;
		}
		
		// Skip the rest
		buf.position(buf.position() + (count * 4));
		
		// Get item package count
		for (PackageItemData data : getItems()) {
			int itemCount = buf.getInt();
			data.setCount(itemCount);
		}
		
		// Skip the rest
		buf.position(buf.position() + ((15 - getItems().size()) * 4));
	}

	public static class PackageItemData {
		private final int itemId;
		private int count;
		
		PackageItemData(int itemId) {
			this.itemId = itemId;
		}

		public int getItemId() {
			return itemId;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
	}
}
