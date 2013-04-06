package net.sicaine.mapviewer.server;

public class Player {
	String name;
	int health;
	int shield;
	int score;
	int x;
	int y;
	int z;
	int team;
	int status;
	String weapon;
	String art[] = {"Spectator", "Admin", "Bot"};
	String stat [] = {"Flag", "Bomb", "Unsichtbar"};
	int statuse;
	
	Player(String name, int team){
		
		this.name = name;
		health = 0;
		shield = 0;
		score  = 0;
		weapon = "";
		this.team = team;
		x = 0;
		y = 0;
		z = 0;
	} 
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public void setShield(int shield){
		if(shield > 150){
			this.shield = 150;
		}else if(shield < 0){
			this.shield = 0;
		}else{
			this.shield = shield;
		}
	}
	
	public void setScore(int score){
		this.score = score;
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
	
	public void setStatus(int status){
		this.statuse = status;
	}
	
	public void setTeam(int team){
		this.team = team;
	}
	
	public void setWeapon(String weapon){
		this.weapon = weapon;
	}
}
