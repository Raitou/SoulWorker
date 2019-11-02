package dev.spiritworker.game.managers;

import java.lang.reflect.Modifier;
import java.util.HashMap;

import dev.spiritworker.game.character.GameCharacter;
import dev.spiritworker.game.character.Stat;
import dev.spiritworker.game.data.SoulWorker;
import dev.spiritworker.game.inventory.InventorySlotType;
import dev.spiritworker.game.inventory.InventoryTab;
import dev.spiritworker.game.inventory.Item;
import dev.spiritworker.net.packet.PacketBuilder;

public class CommandHandler {
	private static HashMap<String, PlayerCommand> list = new HashMap<String, PlayerCommand>();
	
	static {
		try {
			// Look for classes
			for (Class<?> cls : CommandHandler.class.getDeclaredClasses()) {
				// Get non abstract classes
			    if (!Modifier.isAbstract(cls.getModifiers())) {
			    	String commandName = "!" + cls.getSimpleName().toLowerCase();
			    	list.put(commandName, (PlayerCommand) cls.newInstance());
			    }
		
			}
		} catch (Exception e) {
			
		}
	}
	
	public static void handle(GameCharacter character, String msg) {
		String[] split = msg.split(" ");
		
		// End if invalid
		if (split.length == 0) {
			return;
		}
		
		//
		String first = split[0].toLowerCase();
		PlayerCommand c = CommandHandler.list.get(first);
		
		if (c != null) {
			// Level check
			if (character.getGmLevel() < c.level) {
				return;
			}
			// Execute
			int len = Math.min(first.length() + 1, msg.length());
			c.execute(character, msg.substring(len));
		}
	}
	
	public static abstract class PlayerCommand {
		// Level required to use this command
		public int level;
		
		public void setLevel(int minLevel) {
			this.level = minLevel;
		}
		
		// Main
		public abstract void execute(GameCharacter character, String raw);
	}
	
	// ================ Commands ================
	
	public static class Give extends PlayerCommand {
		
		public Give() {
			this.setLevel(0);
		}

		@Override
		public void execute(GameCharacter character, String raw) {
			String[] split = raw.split(" ");
			int itemId = 0, count = 1;
			
			try {
				itemId = Integer.parseInt(split[0]);
			} catch (Exception e) {
				itemId = 0;
			}
			
			try {
				count = Math.min(Integer.parseInt(split[1]), Short.MAX_VALUE);
			} catch (Exception e) {
				count = 1;
			}
			
			if (!SoulWorker.isValidItemId(itemId)) {
				return;
			}
			
			Item item = new Item(itemId);
			item.setCount(count);
			character.getInventory().addItem(item);
		}
	}
	
	public static class ClearInventory extends PlayerCommand {
		
		public ClearInventory() {
			this.setLevel(0);
		}

		@Override
		public void execute(GameCharacter character, String raw) {
			InventoryTab tab = character.getInventory().getInventoryTabByType(InventorySlotType.NORMAL);
			for (Item item : character.getInventory().getTabByType(InventorySlotType.NORMAL).getItems()) {
				if (item != null) {
					tab.deleteItem(item.getSlot(), item.getCount());
				}
			}
		}
	}
	
	public static class StarterPack extends PlayerCommand {
		
		public StarterPack() {
			this.setLevel(0);
		}

		@Override
		public void execute(GameCharacter character, String raw) {
			// Gear
			character.getInventory().addItem(new Item(510014351));
			character.getInventory().addItem(new Item(520014351));
			character.getInventory().addItem(new Item(530014351));
			character.getInventory().addItem(new Item(540014351));
			// Rings
			character.getInventory().addItem(new Item(410015401));
			character.getInventory().addItem(new Item(430015401));
			character.getInventory().addItem(new Item(440015401));
			character.getInventory().addItem(new Item(440015401));
		}
	}

	public static class ChangeStat extends PlayerCommand {
		
		public ChangeStat() {
			this.setLevel(0);
		}

		@Override
		public void execute(GameCharacter character, String raw) {
			String[] split = raw.split(" ");
			int type = 0;
			float value = 1.0f;
			
			try {
				type = Integer.parseInt(split[0]);
			} catch (Exception e) {
				type = 0;
			}
			
			try {
				value = Float.parseFloat(split[1]);
			} catch (Exception e) {
				value = 1;
			}
			
			Stat stat = character.getStats().getMap().get(type);
			if (stat != null) {
				stat.set(value);
				character.getMap().broadcastPacket(PacketBuilder.sendClientCharacterUpdate(character, stat));
			}
			
		}
	}

}
