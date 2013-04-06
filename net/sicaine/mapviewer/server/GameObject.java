package net.sicaine.mapviewer.server;

public class GameObject {
	String name;
	int health;
	int x;
	int y;
	int z;
	int team;
	
	GameObject(){
		
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setZ(int z){
		this.z = z;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setTeam(int team){
		this.team = team;
	}
}
