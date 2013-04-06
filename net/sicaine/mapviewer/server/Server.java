package net.sicaine.mapviewer.server;

public class Server{
	Game game;
	public static void main(String args []){
		new Server();
	}
	
	Server(){
		game = new Game();
		System.out.println("System Start!");
		Clientconnection cconnection = new Clientconnection(5000);
		System.out.println("Starting up server");
		Serverconnection sconnection = new Serverconnection(this, cconnection, 7770);
	}
}
