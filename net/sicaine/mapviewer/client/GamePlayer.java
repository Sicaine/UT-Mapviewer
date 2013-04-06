package net.sicaine.mapviewer.client;

public class GamePlayer extends GameObject{
	private String name;
	private int health;
	private int shield;
	private int score;
	private int x;
	private int y;
	private int z;
	private int team;
	private int status;
	private String weapon;
	private String art[] = {"Spectator", "Admin", "Bot"};
	private String stat [] = {"Flag", "Bomb", "Unsichtbar"};
	
	GamePlayer(){
		name = "";
		health = 0;
		shield = 0;
		score  = 0;
		weapon = "";
		team = 0;
		x = 0;
		y = 0;
		z = 0;
	}
	
	GamePlayer(String name, int team){
		
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
	
	public void setTeam(int team){
		this.team = team;
	}
	
	public void setWeapon(String weapon){
		this.weapon = weapon;
	}

	public String[] getArt() {
		return art;
	}

	public void setArt(String[] art) {
		this.art = art;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getStat() {
		return stat;
	}

	public void setStat(String[] stat) {
		this.stat = stat;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getHealth() {
		return health;
	}

	public int getScore() {
		return score;
	}

	public int getShield() {
		return shield;
	}

	public int getTeam() {
		return team;
	}

	public String getWeapon() {
		return weapon;
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
