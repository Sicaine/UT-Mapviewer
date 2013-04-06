package net.sicaine.mapviewer.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import net.sicaine.mapviewer.client.configObject.ConfigObject;
import net.sicaine.mapviewer.client.configObject.GameTypeConfig;
import net.sicaine.mapviewer.client.configObject.MapConfig;
import net.sicaine.mapviewer.client.configObject.ObjectConfig;
import net.sicaine.mapviewer.client.configObject.WeaponConfig;

public class Game {
	int mapId;
	int gameId;
	int port;

	HashMap<Integer, GamePlayer> playerHashMap;
	HashMap<Integer, GameObject> objectHashMap;
	
	HashMap<Integer, GameTypeConfig> gameTypeConfigHashMap;
	HashMap<Integer, MapConfig> mapConfigHashMap;
	HashMap<Integer, WeaponConfig> weaponConfigHashMap;
	HashMap<Integer, ObjectConfig> objectConfigHashMap;
	
	Game(URL codeBase){
		playerHashMap = new HashMap<Integer, GamePlayer>();
		objectHashMap = new HashMap<Integer, GameObject>();
		
		gameTypeConfigHashMap = new HashMap<Integer, GameTypeConfig>();
		mapConfigHashMap = new HashMap<Integer, MapConfig>();
		weaponConfigHashMap = new HashMap<Integer, WeaponConfig>();
		objectConfigHashMap = new HashMap<Integer, ObjectConfig>();
			
		File test = new File(codeBase.toString());
	
		try {
			initConfigFile(new URL(codeBase, "games.txt"), gameTypeConfigHashMap, new GameTypeConfig());
			initConfigFile(new URL(codeBase, "net/sicaine/mapviewer/client/config/maps.txt"), mapConfigHashMap, new MapConfig());
			initConfigFile(new URL(codeBase, "net/sicaine/mapviewer/client/config/weapons.txt"), weaponConfigHashMap, new WeaponConfig());
			initConfigFile(new URL(codeBase, "net/sicaine/mapviewer/client/config/objects.txt"), objectConfigHashMap, new ObjectConfig());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initConfigFile(URL url, HashMap configObjectHashMap, ConfigObject co) {
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = "";
			ConfigObject actConfigObject = null;
			
			while ((line = br.readLine()) != null) {
				actConfigObject = co.createConfigObject(line);
				configObjectHashMap.put(actConfigObject.getId(), actConfigObject);
			}

			br.close();
		} catch (Exception e) {
			System.err.println("Error by reading Configfile " + url + e);
		}
	}
	public void addObject(GamePlayer o){
		
	}
	
	public GamePlayer addGamePlayer(int id){
		return playerHashMap.put(id, new GamePlayer());
	}
	
	public void delGamePlayer(int id) {
		playerHashMap.remove(id);
	}

	public GameObject addGameObject(int id){
		return objectHashMap.put(id, new GameObject());
	}
	
	public void setMapTyp(){
		
	}
	
	public void delGameObject(int id){
		objectHashMap.remove(id);
	}
	
	public void delAllObjects(){
	    playerHashMap.clear();
	    objectHashMap.clear();		
	}

	public HashMap getObjectHashMap() {
		return objectHashMap;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public HashMap<Integer, GamePlayer> getPlayerHashMap() {
		return playerHashMap;
	}

	public void setPlayerHashMap(HashMap<Integer, GamePlayer> spieler) {
		this.playerHashMap = spieler;
	}

	public HashMap<Integer, GameTypeConfig> getGameTypeConfigHashMap() {
		return gameTypeConfigHashMap;
	}

	public HashMap<Integer, MapConfig> getMapConfigHashMap() {
		return mapConfigHashMap;
	}

	public HashMap<Integer, ObjectConfig> getObjectConfigHashMap() {
		return objectConfigHashMap;
	}

	public HashMap<Integer, WeaponConfig> getWeaponConfigHashMap() {
		return weaponConfigHashMap;
	}
}
