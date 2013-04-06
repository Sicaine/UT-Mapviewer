package net.sicaine.mapviewer.client.configObject;

public class WeaponConfig extends ConfigObject {
	
	public WeaponConfig() {
	}
	
	public WeaponConfig(int id, String pictureName, String actMapName) {
		this.id = id;
		this.pictureName = pictureName;
		this.name = actMapName;
	}

	public ConfigObject createConfigObject(String line) {
		int pos = line.indexOf(", ");
		int id = Integer.valueOf(line.substring(0, pos));
		int pos2 = line.indexOf(", ", pos + 1);
		String pictureName = line.substring(pos + 2, pos2);
		String actMapName = line.substring(pos2 + 2);
		return new WeaponConfig(id, pictureName, actMapName);
	}

}
