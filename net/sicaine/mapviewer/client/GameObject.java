package net.sicaine.mapviewer.client;

public class GameObject {
	private String name;
	private int health;
	private int x;
	private int y;
	private int z;
	private int team;
	private boolean visible;
	
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

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getHealth() {
		return health;
	}

	public String getName() {
		return name;
	}

	public int getTeam() {
		return team;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
}
