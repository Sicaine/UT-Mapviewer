package net.sicaine.mapviewer.server;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.Timer;

public class Serverconnection implements Runnable{
	Thread t;
    ServerSocket serverSock;
	Socket verbindung;
	BufferedWriter out;
	BufferedReader in;
	Clientconnection cconnection; 
	Server server;
	int id;
  	int line;
  	dynarray linec;
  	boolean[] linet;
  	Timer timer;
	
	Serverconnection( Server server, Clientconnection cconnection, int port){
		this.server = server;
		this.cconnection = cconnection;
		try{
			serverSock = new ServerSocket(port);
		}catch(IOException e){
			System.out.println("huhu");
		}
		t = new Thread(this, "Serververbindungsthread");
		t.start();
	}
	public void run(){
		timer = new Timer(5000, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
	  			Client p;
	  			byte [] linec = new byte[256];
	  			boolean [] linet = new boolean[256];
	  			for(int i = 0; i < linec.length; i++){
	  				linet[i] = false;
	  			}
	  			linec[0] = 10;
	  			linet[0] = true;
	  			linec[1] = 'a';
	  			linet[1] = true;
	  			linec[2] = (char)9;
	  			linet[2] = true;
	  			dynarray array = new dynarray(linec, linet);
	  			
		  		for(int i = 0; i < cconnection.clients.size(); i++){
		  			//System.out.println("Update die Clients. Nr: " + i);
		  			p = (Client)cconnection.clients.elementAt(i);
		  			try{
		  				p.write(array);
		  			}catch(IOException e){
		  				cconnection.clients.removeElementAt(i);
		  				System.out.println("net.sicaine.mapviewer.client hat disconected");
		  			}
		  		}
			}
			
		});
		timer.start();
		while(true){
		  	System.out.println("Warte auf Verbindung des UT2k4Servers");
            try{
			  	verbindung = serverSock.accept();
			  	verbindung.setSoTimeout(50000);
			  	System.out.println("Verbindung wurde aufgebaut.");
			  	//Streams pipen
			  	in  = new BufferedReader(new InputStreamReader(verbindung.getInputStream(),"ISO-8859-1"));
			  	out = new BufferedWriter(new OutputStreamWriter(verbindung.getOutputStream(),"ISO-8859-1"));

			  	int k = 0;
			  	int schritte = 5;
			  	out.write("200 OK");
			  	out.newLine();
			  	out.flush();
		        //Speichern des Mutators:
			  	{
			  		String line = in.readLine();
			  		server.game.setMapType(line);
			  		System.out.println("Line1: " + line);
			  	}
		        //Speichern des Spiels
		        //server.game.mapname
		        System.out.println("Line2: " + in.readLine());
				//Auftrennung von Map Gametype und Port und speicherung.
		        {
		        	String line = in.readLine();
		        	String[] lines = new String[3];
		        	lines = line.split(" ", 4);
		        	//Setzte Mapname
		        	server.game.setMap(lines[0]);
		        	//Sezte Spieltyp:
		        	//server.game.set
		        	//Setzte Port
		        	System.out.println("Line3: " + line);
		        	try{
		        		server.game.port = Integer.parseInt(lines[2]);
		        	}catch(ArrayIndexOutOfBoundsException e){
		        		System.out.println("Line4: " + in.readLine());
		        		System.out.println("Line5: " + in.readLine());
		        		System.out.println("Line6: " + in.readLine());
		        		System.exit(1);
		        	}
		        }
		        System.out.println("port: " + server.game.port);
			  	do{
			  		line = in.read();
				  	try{
				  		if(line == -1){
				  			System.out.println("Verbindung wurde abgebrochen :(" );
				  			break;
				  		}else if (line == 10) {
				            out.write("200 OK"); 
				            out.newLine(); 
				            out.flush();
				            k = 0;
				  		}else{
						  	linec = processLine(line, in);
						  	//System.out.println("Inhalt der Linie: " + linec);
					  		for(int i = 0; i < cconnection.clients.size(); i++){
					  			//System.out.println("Update die Clients. Nr: " + i);
					  			Client p;
					  			p = (Client)cconnection.clients.elementAt(i);
					  			try{
					  				p.write(linec);
					  			}catch(IOException e){
					  				cconnection.clients.removeElementAt(i);
					  				System.out.println("net.sicaine.mapviewer.client hat disconected");
					  			}
					  		}
				  		}
				  	}catch(NullPointerException e){
				  		System.out.println("Verbindung wurde abgebrochen :( weil: " + e.getMessage());
				  		e.printStackTrace();
				  		break;
				  	}
				  	
			  		k++;
			  	}while(true);
		  	}catch (SocketException e){
		  		System.out.println("Socketproblemchen"+ e.getMessage());
		  		//Verbindung wurde hier dann abgebrochen
		  	}catch (IOException e){
		  		System.out.println("IOproblemchen"+ e.getMessage());
		  		//Weis noch ne dwarum das hier passiert :/
			}
		  	
		  	
		}
	}
	
	dynarray processLine(int lineStart, BufferedReader in){
		byte[] linec = new byte[256];
		boolean[] linet = new boolean[256];
		for(int i = 0; i < linet.length; i++){
			linet[i] = false;
		}
		Player spieler = null;
		GameObject objekt = null;
		Integer c;
		int a;
		//isSpieler? Wenn ja dann true :) 
		boolean isS = false; 
	    byte b = (byte) lineStart;
	    //Wenn das 2te Bit 0 ist, dann ist es ein Spieler ansonsten ein Objekt.
	    if ((b & 0x40) == 0){
	    	isS = true;
	    }
	    id = (byte) (b & 0x3F);
	    //System.out.println("linie: " + id);
	    linec[0] = b;
	    linet[0] = true;

/*	    try{
	    	server.game.spieler.elementAt(id);
	    }catch(ArrayIndexOutOfBoundsException e){
	    	server.game.addSpieler(id);
	    }
		*/
		try {
			b = (byte) in.read();
			linec[1] = b;
			linet[1] = true;
			
			if ( (b & 0x80) == 0 ) {
				System.out.print(" disconnected");
			}else{
				//Spielerobjekt zuweisen bzw. neu erstellen!
			    try{
					if(isS){
						spieler = (Player)server.game.player.get(String.valueOf(id));
					}else{
						objekt = (GameObject)server.game.gameObjects.get(String.valueOf(id));
					}
			    	if(isS){
			    		int i3 = spieler.health;
			    	}else{
			    		int i3 = objekt.health;
			    	}
			    }catch(NullPointerException e){
			    	if(isS){
			    		server.game.addSpieler(id);
			    		spieler = (Player)server.game.player.get(String.valueOf(id));
			    	}else{
			    		server.game.addObject(id);
			    		objekt = (GameObject)server.game.gameObjects.get(String.valueOf(id));
			    	}
			    }
			    
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
			    
			    boolean bChangedHealth = (b & 0x02) != 0;
			    boolean bChangedShield = (b & 0x01) != 0;
			    b = (byte) in.read();
			    linec[2] = b;
			    linet[2] = true;
			    boolean bChangedLocX = (b & 0x80)  != 0;
			    boolean bChangedLocY = (b & 0x40)  != 0;
			    boolean bChangedLocZ = (b & 0x20)  != 0;
			    boolean bChangedScore = (b & 0x10) != 0;
			    boolean bChangedLives = (b & 0x08) != 0;
			    boolean bChangedTeam = (b & 0x04)  != 0;
			    boolean bChangedName = (b & 0x02)  != 0;
			    boolean bChangedWeapon = (b & 0x01) != 0;
			
			    if ( bChangedHealth ) {
			    	//Health:
			    	linec[3] = (byte)in.read();
			    	linec[4] = (byte)in.read();
			    	linet[3] = true;
			    	linet[4] = true;
			    	a = linec[3] << 8 | linec[4];
			    	if(isS){
			    		spieler.setHealth(a);
			    	}else{
			    		objekt.setHealth(a);
			    	}
			    }
			    if ( bChangedShield ) {
			    	//Setze Shield:
			    	linec[5] = (byte)in.read();
			    	linec[6] = (byte)in.read();
			    	linet[5] = true;
			    	linet[6] = true;
			    	a = linec[5] << 8 | linec[6];
			    	if(isS){
			    		spieler.setShield(a);
			    	}else{
			    		//Hier kommt man ned rein. Zumindest bis jetzt ned :P
			    	}
			    }
			    if ( bChangedLocX ) {
			    	//Setzte X:
			    	linec[7] = (byte)in.read();
			    	linec[8] = (byte)in.read();
			    	linet[7] = true;
			    	linet[8] = true;
			    	a = linec[7] << 8 | linec[8];
			    	if(isS){
			    		spieler.setX((short) (a));
			    	}else{
			    		objekt.setX((short) (a));
			    	}
			    }
			    if ( bChangedLocY ) {
			    	//Setze Y:
			    	linec[9] = (byte)in.read();
			    	linec[10] = (byte)in.read();
			    	linet[9] = true;
			    	linet[10] = true;
			    	a = linec[9] << 8 | linec[10];
			    	if(isS){
			    		spieler.setY((short) (a));
			    	}else{
			    		objekt.setY((short) (a));
			    	}
			    }
			    if ( bChangedLocZ ) {
			    	//Setze Z:
			    	linec[11] = (byte)in.read();
			    	linec[12] = (byte)in.read();
			    	linet[11] = true;
			    	linet[12] = true;
			    	a = linec[11] << 8 | linec[12];
			    	if(isS){
			    		spieler.setZ((short) (a));
			    	}else{
			    		objekt.setZ((short) (a));
			    	}
			    }
			    if ( bChangedScore ) {
			        //Setze Score:
			    	linec[13] = (byte)in.read();
			    	linec[14] = (byte)in.read();
			    	linet[13] = true;
			    	linet[14] = true;
			    	a = linec[13] << 8 | linec[14];
			    	if(isS){
			    		spieler.setScore((short) (a));
			    	}else{
			    		//Hier sollte man auch ned hierherkommen können
			    	}
			    }
			    if ( bChangedLives ) {
			    	//Setze Leben: 
			    	linec[15] = (byte)in.read();
			    	linec[16] = (byte)in.read();
			    	linet[15] = true;
			    	linet[16] = true;
			    	a = linec[15] << 8 | linec[16];
			    	if(isS){
			    		spieler.setHealth(a);
			    	}else{
			    		objekt.setHealth(a);
			    	}
			    }
			    if ( bChangedTeam ) {
			        //Setze Team:
			    	linec[17] = (byte)in.read();
			    	linet[17] = true;
			    	a = linec[17];
			    	if(isS){
			    		spieler.setTeam((byte)a);
			    	}else{
			    		objekt.setTeam((byte)a);
			    	}
			    }
			
			    if ( bChangedName || bChangedWeapon ) {
			    	String line = in.readLine();
			    	char[] chars = line.toCharArray();
			    	for(int j = 0; j < chars.length; j++){
			    		linec[18 + j] = (byte)chars[j];
			    		linet[18 + j] = true;
			    	}
			    	linec[18 + chars.length] = (char)13;
			    	linet[18 + chars.length] = true;
			    	if ( bChangedName ) {
			    		int i = line.indexOf(9);
			    		if ( i == -1 ){
			    			if(isS){
			    				spieler.name = line;
			    			}else{
			    				objekt.name = line;
			    			}
			    		}else{
			    			//Setzte Nick:
			    			System.out.print("inhalt vom nickteil halt: " + line.substring(0, i));
			    			line = line.substring(i + 1);
			    		}
			        }
			    
			    	if ( bChangedWeapon ) {
			    		//Setzte Waffenname:
			    		if(isS){
			    			spieler.setWeapon(line);
			    		}else{
			    			//Ne Waffennamen bei nem Objekt?
			    		}
			    	}
			    }
			    if(isS){
			    	System.out.println("Nick:" + spieler.name +
						   " ID: " + id + 
				           " Health: " + spieler.health + 
						   " Shield: " + spieler.shield +
						   " Waffenname: " + spieler.weapon + 
						   " Team: " + spieler.team +
						   " X/Y/Z: " + spieler.x + 
						   "/" + spieler.y +
						   "/" + spieler.z +
						   " Score: " + spieler.score);
			    }else{
			    	System.out.println("OName:" + objekt.name +
							   " ID: " + id + 
					           " Health: " + objekt.health +  
							   " Team: " + objekt.team +
							   " X/Y/Z: " + objekt.x + 
							   "/" + objekt.y +
							   "/" + objekt.z);			    	
			    }
			}		
			System.out.println();
		}catch (IOException e) {
			System.out.println();
			System.out.println(e);
		}
		dynarray lined = new dynarray(linec, linet); 
		return lined;
	}
}