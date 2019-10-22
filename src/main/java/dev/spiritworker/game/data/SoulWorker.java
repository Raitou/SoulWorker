package dev.spiritworker.game.data;

import dev.spiritworker.game.data.def.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class SoulWorker {
	private static Int2ObjectMap<ItemDef> itemDefs = new Int2ObjectOpenHashMap<ItemDef>();
	private static Int2ObjectMap<PackageDef> packageDefs = new Int2ObjectOpenHashMap<PackageDef>();
	private static Int2ObjectMap<DistrictDef> districtDefs = new Int2ObjectOpenHashMap<DistrictDef>();
	private static Int2ObjectMap<SkillDef> skillDefs = new Int2ObjectOpenHashMap<SkillDef>();
	
	public static Int2ObjectMap<ItemDef> getItemDefs() {
		return itemDefs;
	}
	
	public static boolean isValidItemId(int id) {
		return itemDefs.containsKey(id);
	}
	
	public static ItemDef getItemDefById(int itemId) {
		return getItemDefs().get(itemId);
	}
	
	public static Int2ObjectMap<PackageDef> getPackageDefs() {
		return packageDefs;
	}

	public static Int2ObjectMap<DistrictDef> getDistrictDefs() {
		return districtDefs;
	}

	public static Int2ObjectMap<SkillDef> getSkillDefs() {
		return skillDefs;
	}
}
