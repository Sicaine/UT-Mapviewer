/*
 * Created on 29.11.2004
 */
package net.sicaine.mapviewer.server;

import java.util.HashMap;
import java.util.Vector;

public class Game {
	String mapname;
	String maptyp;
	String Sversion;
	HashMap<Integer, Player> player;
	HashMap gameObjects;
	int port;
	Vector team;
	
	
	
	Game(){
		player = new HashMap<Integer, Player>();
		gameObjects = new HashMap();
	}
	
	public void setMap(String map){
		this.mapname = map;
	}
	
	public void setMapType(String maptype){
		this.maptyp = maptype;
	}
	
	public void setUTVersion(String version){
		this.Sversion = version;
	}
	
	public void addObject(Player o){
		
	}
	
	public void addSpieler(int id){
		player.put(id, new Player("", 0));
	}

	public void addObject(int id){
		gameObjects.put(id, new GameObject());
	}
	
	public void setMapTyp(){
		
	}
	
	public void addObject(){
		
	}
	
	public void delObject(){
		
	}
	
	public void delAllObjects(){
		
	}
}
