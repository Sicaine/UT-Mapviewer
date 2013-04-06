package net.sicaine.mapviewer.client.configObject;

public class GameTypeConfig extends ConfigObject{

	public GameTypeConfig() {
		// TODO Auto-generated constructor stub
	}

	public GameTypeConfig(int id, String pictureName, String mapName) {
		this.id = id;
		this.pictureName = pictureName;
		this.name = mapName;
	}

	public ConfigObject createConfigObject(String line) {
		// 1, ut2k4.jpg, Unreal Tournament 2004 v3399
		int pos = line.indexOf(", ");
		int id = Integer.valueOf(line.substring(0, pos));
		int pos2 = line.indexOf(", ", pos + 1);
		String pictureName = line.substring(pos + 2, pos2);
		String actMapName = line.substring(pos2 + 2);
		return new GameTypeConfig(id, pictureName, actMapName);
	}

}
