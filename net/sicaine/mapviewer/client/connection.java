/*
 * Created on 30.11.2004
 */
package net.sicaine.mapviewer.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.Timer;

import net.sicaine.mapviewer.client.Client;


public class connection implements Runnable{
	private Socket connection;
	private Thread t;
	private Client client;
	private OutputStream out;
	private BufferedReader in;
	private Timer timer;
	private boolean isUpdatel = false;
	private boolean isUpdatef = false;
	
	connection(Client client){
		client = client;
		t = new Thread(this, "Abfragethread (Da er ja nur die Sachen aktualisiert :)");
		t.start();
	}
	
	public void run(){
		try{
			client.connect.setEnabled(false);
			client.connect.setLabel("connecting...");
			connection = new Socket("192.168.100.50", 5000);
			connection.setSoTimeout(10000);
			client.connect.setLabel("connected");
			byte lineStart;
			in  = new BufferedReader(new InputStreamReader(connection.getInputStream(), "ISO-8859-1"));
		  	out = connection.getOutputStream();
			timer = new Timer(500, new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if(isUpdatel){
						client.repaint(0,0,200,600);
						isUpdatel = false;
					}
					if(isUpdatef){
						client.repaint(200,0,600,400);
						isUpdatef = false;
					}
				}		
			});
			timer.start();

			client.getGame().delAllObjects();
			while(true){
				lineStart = (byte)in.read();
				if(lineStart == -1){
					
				}else if(lineStart == 10){
					in.read();
					in.read();
					System.out.println("Update");
				}else{
					processLine(lineStart, in);
					isUpdatef = true;
				}
			}
		}catch(IOException e){
			client.connect.setLabel("Connect");
			client.connect.setEnabled(true);
			System.out.print("Fehler beim verbinden :,(");
			t.stop();
		}catch(NumberFormatException e){
			System.out.print("Falsche Formatierung!");
		}
	}

	void processLine(byte lineStart, BufferedReader in) throws IOException{
		GameObject object = null;
		Integer c;
		int a;
		int id;
		
		byte b = lineStart;
		boolean isS = false; 
	    
		// If the second Bit is zero, then its a player
	    if ((b & 0x40) == 0){
	    	isS = true;
	    }
	    id = (byte) (b & 0x3F);

	    b = (byte) in.read();
		
		//Spielerobjekt zuweisen bzw. neu erstellen!
	    try{
			if(isS){
				object = client.getGame().playerHashMap.get(String.valueOf(id));
			}else{
				object = (GameObject)client.getGame().objectHashMap.get(String.valueOf(id));
			}
	    }catch(NullPointerException e){
	    	if(isS){
	    		object = client.getGame().addGamePlayer(id);
	    	}else{
	    		object = client.getGame().addGameObject(id);
	    	}
	    }
	    
		if ( (b & 0x80) == 0 ) {
			if(isS){
	    		client.getGame().delGamePlayer(id);
	    	}else{
	    		client.getGame().delGameObject(id);
	    	}
			System.out.print("Removebit from Object/Player " + id);
		}else{
		    
		    /*
		    if ((b & 0x40) != 0){
		    	System.out.print(", is spectator");
		    }
		    if ((b & 0x20) != 0){
		    	System.out.print(", is admin");
		    }
		    if ((b & 0x10) != 0){
		    	System.out.print(", is bot");
		    }
		    if ((b & 0x08) != 0){
		    	System.out.print(", has flag/bomb");
		    }
		    if ((b & 0x04) != 0){
		    	System.out.print(", is invisible");
		    }
		    */
		    
		    boolean changedHealth = (b & 0x02) != 0;
		    boolean changedShield = (b & 0x01) != 0;
		    b = (byte) in.read();
		    boolean changedLocX = (b & 0x80)  != 0;
		    boolean changedLocY = (b & 0x40)  != 0;
		    boolean changedLocZ = (b & 0x20)  != 0;
		    boolean changedScore = (b & 0x10) != 0;
		    boolean changedLives = (b & 0x08) != 0;
		    boolean changedTeam = (b & 0x04)  != 0;
		    boolean changedName = (b & 0x02)  != 0;
		    boolean changedWeapon = (b & 0x01) != 0;
		
		    if ( changedHealth ) {
		    	//Health:
		    	a = in.read() << 8 | in.read();
	    		object.setHealth(a);
		    }
		    if ( changedShield ) {
		    	// Set the Shieldvalue:
		    	a = in.read() << 8 | in.read();
		    	if(isS){
		    		((GamePlayer)object).setShield(a);
		    	}else{
		    		// no shieldvalue for objects
		    	}
		    }
		    if ( changedLocX ) {
		    	// Set X:
		    	a = in.read() << 8 | in.read();
	    		object.setX((short) (a));
		    }
		    if ( changedLocY ) {
		    	// Set Y:
		    	a = in.read() << 8 | in.read();
		    	object.setY((short) (a));
		    }
		    if ( changedLocZ ) {
		    	// Set Z:
		    	a = in.read() << 8 | in.read();
		    	object.setZ((short) (a));
		    }
		    if ( changedScore ) {
		        //Set Score:
		    	a = in.read() << 8 | in.read();
		    	if(isS){
		    		((GamePlayer)object).setScore((short) (a));
		    	}else{
		    		// no scorevalue for object
		    	}
		    }
		    if ( changedLives ) {
		    	// Set Health: 
		    	a = in.read() << 8 | in.read();
		    	object.setHealth(a);
		    }
		    if ( changedTeam ) {
		        // Set Teamid:
		    	a = (byte)in.read();
		    	object.setTeam((byte)a);
		    }
		
		    if ( changedName || changedWeapon ) {
		    	String line = in.readLine();
		    	if ( changedName ) {
		    		int i = line.indexOf(9);
		    		if ( i == -1 ){
		    			object.setName(line);
		    		}else{
		    			// Set Nickname:
		    			object.setName(line.substring(0, i));
		    			line = line.substring(i + 1);
		    		}
		    		isUpdatel = true;
		        }
		    
		    	if ( changedWeapon ) {
		    		// Set Weaponname:
		    		if(isS){
		    			((GamePlayer)object).setWeapon(line);
		    		}else{
		    			// No Weaponname for objects
		    		}
		    	}
		    }
		}
	}
}