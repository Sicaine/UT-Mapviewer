package net.sicaine.mapviewer.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

public class Clientconnection implements Runnable{
	Thread t;
    ServerSocket serverSock;
	Socket verbindung;
	public Vector clients;  
	Clientconnection(int port){
		clients = new Vector(10, 5);
		try{
			serverSock = new ServerSocket(port);
		}catch(IOException e){
			System.out.println("huhu");
		}
		t = new Thread(this, "Serververbindungsthread");
		t.start();
	}
	public void run(){
		while(true){
		  	System.out.println("Warte auf Verbindung von einem net.sicaine.mapviewer.client");
            try{
			  	verbindung = serverSock.accept();
			  	verbindung.setSoTimeout(1000);
			  	System.out.println("Verbindung wurde aufgebaut.");
			  	//Thread erzeugen
			  	clients.addElement(new Client(verbindung));
			  	
			  	System.out.println("Aktuelle Clientanz: " + clients.size());
		  	}catch (SocketException e){
		  		System.out.println("Verbindung verloren. Anzahl: " + clients.size());
		  		//Verbindung wurde hier dann abgebrochen
		  	}catch (IOException e){
		  		System.out.println("Verbindung verloren. Anzahl: " + clients.size());
		  		//Weis noch ne dwarum das hier passiert :/
			}
		}
	}

}
