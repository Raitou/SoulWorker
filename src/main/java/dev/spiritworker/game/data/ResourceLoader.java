package dev.spiritworker.game.data;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.game.data.def.*;

public class ResourceLoader {
	
	public static void loadDefinitions() {
		loadFromResource("tb_Item.res", ItemDef.class, SoulWorker.getItemDefs());
		loadFromResource("tb_Item_Package.res", PackageDef.class, SoulWorker.getPackageDefs());
		loadFromResource("tb_Skill.res", SkillDef.class, SoulWorker.getSkillDefs());
		loadFromResourceShort("tb_district.res", DistrictDef.class, SoulWorker.getDistrictDefs());
		loadFromResourceShort("tb_Maze_Info.res", MazeDef.class, SoulWorker.getMazeDefs());
	}
	
	@SuppressWarnings("rawtypes")
	protected static void loadFromResource(String fileName, Class<?> c, Int2ObjectMap map) {
		File file = new File(SpiritWorker.getConfig().RESOURCE_FOLDER + fileName);
		try (FileInputStream fis = new FileInputStream(file); DataInputStream dis = new DataInputStream(fis)) {
			byte[] bytes = new byte[(int) file.length()];
			dis.readFully(bytes);
			
			ByteBuffer buf = ByteBuffer.wrap(bytes);
			buf.order(ByteOrder.LITTLE_ENDIAN);
			
			int count = buf.getInt();
			
			Constructor<?> constructor = c.getConstructor(Integer.TYPE);
			
			for (int i = 0; i < count; i++) {
				int id = buf.getInt();
				ResourceDef def = (ResourceDef) constructor.newInstance(id);
				def.loadFromRes(buf);
				map.put(id, def);
			}
			
			SpiritWorker.getLogger().info("Loaded " + count + " " + c.getSimpleName() + "s.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected static void loadFromResourceShort(String fileName, Class<?> c, Int2ObjectMap map) {
		File file = new File(SpiritWorker.getConfig().RESOURCE_FOLDER + fileName);
		try (FileInputStream fis = new FileInputStream(file); DataInputStream dis = new DataInputStream(fis)) {
			byte[] bytes = new byte[(int) file.length()];
			dis.readFully(bytes);
			
			ByteBuffer buf = ByteBuffer.wrap(bytes);
			buf.order(ByteOrder.LITTLE_ENDIAN);
			
			int count = buf.getInt();
			
			Constructor<?> constructor = c.getConstructor(Integer.TYPE);
			
			for (int i = 0; i < count; i++) {
				int id = buf.getShort();
				ResourceDef def = (ResourceDef) constructor.newInstance(id);
				def.loadFromRes(buf);
				map.put(id, def);
			}
			
			SpiritWorker.getLogger().info("Loaded " + count + " " + c.getSimpleName() + "s.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadItems() {
		loadFromResource("tb_Item.res", ItemDef.class, SoulWorker.getItemDefs());
	}
	
	public static void loadPackages() {
		loadFromResource("tb_Item_Package.res", PackageDef.class, SoulWorker.getPackageDefs());
	}
}
