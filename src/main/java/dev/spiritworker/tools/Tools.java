package dev.spiritworker.tools;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.spiritworker.SpiritWorker;
import dev.spiritworker.game.data.def.ItemDef;

public class Tools {
	public static void main(String[] args) {
		dumpItemScripts();
		//dumpMazeInfo();
	}
	
	public static void dumpMazeInfo() {
		File file = new File(SpiritWorker.getConfig().RESOURCE_FOLDER + "Maze_Info.res");
		
		try (FileInputStream fis = new FileInputStream(file); DataInputStream dis = new DataInputStream(fis)) {
			byte[] bytes = new byte[(int) file.length()];
			dis.readFully(bytes);
			
			ByteBuffer buf = ByteBuffer.wrap(bytes);
			buf.order(ByteOrder.LITTLE_ENDIAN);
			
			int count = buf.getInt();
			
			SpiritWorker.getLogger().info("Maze Count: " + count);
			
			for (int i = 0; i < count; i++) {
				Maze maze = new Maze();
				maze.id = buf.getShort();
				SpiritWorker.getLogger().info("Maze id : " + maze.id);
				buf.get();
				buf.getShort();
				buf.get();
				buf.get();
				buf.getShort();
				buf.get();
				buf.get();
				buf.get();
				buf.get();
				buf.get();
				buf.getShort();
				buf.getInt();
				buf.getInt();
				buf.getInt();
				buf.getShort();
				buf.getShort();
				maze.folder = readString(buf);
				maze.name = readString(buf);
				maze.path1 = readString(buf);
				maze.s1 = readString(buf);
				maze.s2 = readString(buf);
				maze.s3 = readString(buf);
				buf.getInt();
				buf.getShort();
				buf.get();
				maze.path2 = readString(buf);
				maze.icon = readString(buf);	
				buf.getShort();
				buf.getShort();
				buf.getShort();
				buf.getShort();
				SpiritWorker.getLogger().info(maze.path1 + " : " + maze.s1);
				// Skip for now
				buf.position(buf.position() + 26);
				readString(buf);
				buf.get();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void dumpItemScripts() {
		File fileItemScript = new File(SpiritWorker.getConfig().RESOURCE_FOLDER + "tb_item_script_eng.res");
		File fileItemData = new File(SpiritWorker.getConfig().RESOURCE_FOLDER + "tb_Item.res");

		//List<ItemDef> itemList = new LinkedList<ItemDef>();
		Map<Integer, ItemDef> items = new HashMap<Integer, ItemDef>();
		
		try (FileInputStream fis = new FileInputStream(fileItemScript); DataInputStream dis = new DataInputStream(fis)) {
			byte[] bytes = new byte[(int) fileItemScript.length()];
			dis.readFully(bytes);
			
			ByteBuffer buf = ByteBuffer.wrap(bytes);
			buf.order(ByteOrder.LITTLE_ENDIAN);
			
			int count = buf.getInt();
			
			SpiritWorker.getLogger().info("Item (Script) Count: " + count);
			
			for (int i = 0; i < count; i++) {
				ItemDef item = new ItemDef(buf.getInt());
				item.path = readString(buf);
				readString(buf);
				readString(buf);
				readString(buf);
				readString(buf);
				readString(buf);
				buf.get();
				buf.get();
				buf.get();
				buf.get();
				buf.get();
				item.name = readString(buf);
				item.description = readString(buf);
				
				items.put(item.getId(), item);
				//itemList.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try (FileInputStream fis = new FileInputStream(fileItemData); DataInputStream dis = new DataInputStream(fis)) {
			byte[] bytes = new byte[(int) fileItemData.length()];
			dis.readFully(bytes);
			
			ByteBuffer buf = ByteBuffer.wrap(bytes);
			buf.order(ByteOrder.LITTLE_ENDIAN);
			
			int count = buf.getInt();
			
			SpiritWorker.getLogger().info("Item Count: " + count);
			
			for (int i = 0; i < count; i++) {
				int id = buf.getInt();
				ItemDef item = items.computeIfAbsent(id, it -> new ItemDef(id));
				item.loadFromRes(buf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File save = new File("./items.json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try (FileWriter wr = new FileWriter(save)) {
			gson.toJson(items.values(), wr);
			
			SpiritWorker.getLogger().info("Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		save = new File("./Soul Worker GM Handbook.txt");
		try (FileWriter fw = new FileWriter(save); PrintWriter wr = new PrintWriter(fw)) {
			wr.println("// Soul Worker GM Handbook");
			wr.println();
			wr.println();
			wr.println("[Items] (id : name)");
			itemList.forEach(item -> {
				wr.println(item.id + " : " + item.name);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}
	
	public static String readString(ByteBuffer packet) {
		int len = packet.getShort();
		StringBuffer sb = new StringBuffer(len);
		int i;
        for (int j = 0; j < len; j++) {
            i = packet.getShort();
            if (i == 0) { // End of string
            	break;
            } else {
            	sb.append((char) i);
            }
        }
        return sb.toString();
	}
	
	private static class Maze {
		public int id;
		public String folder;
		public String name;
		public String path1;
		public String path2;
		public String icon;
		public String s1;
		public String s2;
		public String s3;
	}
}
