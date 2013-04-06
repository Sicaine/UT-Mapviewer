package net.sicaine.mapviewer.client.configObject;

public class MapConfig extends ConfigObject {
	
	private int x;
	private int y;
	private int z;

	public MapConfig() {
	}
	
	public MapConfig(int gameid, int mapid, int x, int y, int z, String actPictureName, String actMapName) {
		this.id = gameid + mapid * 100;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pictureName = actPictureName;
		this.name = actMapName;
	}

	public ConfigObject createConfigObject(String line) {
//		 1, 1, 2000, 2000, 1024, torlan.jpg, Torlan
		int pos = line.indexOf(", ");
		int gameid = Integer.valueOf(line.substring(0, pos));
		int pos2 = line.indexOf(", ", pos + 1);
		int mapid = Integer.valueOf(line.substring(pos + 2, pos2));
		pos = pos2;
		pos2 = line.indexOf(", ", pos + 1);
		int x = Integer.valueOf(line.substring(pos + 2, pos2));
		pos = pos2;
		pos2 = line.indexOf(", ", pos + 1);
		int y = Integer.valueOf(line.substring(pos + 2, pos2));
		pos = pos2;
		pos2 = line.indexOf(", ", pos + 1);
		int z = Integer.valueOf(line.substring(pos + 2, pos2));
		pos = pos2;
		pos2 = line.indexOf(", ", pos + 1);
		String actPictureName = line.substring(pos + 2, pos2);
		pos = pos2;
		String actMapName = line.substring(pos + 2);
		return new MapConfig(gameid, mapid, x, y, z, actPictureName, actMapName);
	}

}
