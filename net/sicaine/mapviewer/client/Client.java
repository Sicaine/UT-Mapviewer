package net.sicaine.mapviewer.client;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JTable;

public class Client extends Applet implements ActionListener{
	private boolean picerror = false;
	private Game game;
	private Panel p;
	private  TextField user;
	private TextField pw;
	private  TextField host;
	private  TextField port;
	protected Label tuser;
	protected Label tpw;
	protected Label thost;
	protected Label tport;
	protected Label terror;
	protected JTable tlist;
	private List actAppletUserList;
	protected Button connect;
	private  Image pic;
	private InetAddress hosti;
	private int startx = 201;
	private int starty = 0;
	private int endx = 800;
	private int endy = 400;
	private int minx = 0;
	private int miny = 0;
	private int minz = 0;
	private int maxx = 0;
	private int maxy = 0;
	private int maxz = 50;
	
	public void init(){

		this.setSize(800,400);
		game = new Game(getCodeBase());
		System.out.println(getDocumentBase());				
		setPic(this.getImage(getCodeBase(), "maptorlanoben1.jpg"));
		//pic.getWidth();
		
	}
	
	public void start(){	
		p     = new Panel();
		p.setSize(400,100);
		p.setLayout(null);
		
		/*
		//Erzeugen des Passwortfeldes
		tpw = new Label("Passwort:");
		tpw.setLocation(5,30);
		tpw.setSize(50,20);
		p.add(tpw);
		pw    = new TextField();
		pw.setColumns(8);
		pw.setLocation(100,30);
		pw.setSize(50,20);
		p.add(pw);
		
		//Erzeugen des Portfeldes. Das ganze wird dadurch sehr dynamisch :). 
		//Standardwerte werden auch hier gesetzt
		tport = new Label("Port:");
		tport.setLocation(5,80);
		tport.setSize(50,20);
		p.add(tport);
		port  = new TextField();
		port.setLocation(100,80);
		port.setSize(100,20);
		p.add(port);*/

		//Erzeugen des Verbindungsbuttons. Er wird später auch als Disconnectbutton misbraucht ;-)
		connect = new Button("Connect");
		connect.setLocation(20,10);
		connect.setSize(160,20);
		connect.addActionListener(this);
		p.add(connect);

		tlist = new JTable();
		tlist.setLocation(10,70);
		tlist.setSize(180,340);
		p.add(tlist);
		//Erzeugen der Liste und zuweisen der Werte
		actAppletUserList = new List(12);
		actAppletUserList.setLocation(10,40);
		actAppletUserList.setSize(180,340);
		p.add(actAppletUserList);
	
		p.setLocation(0,0);
		p.setBackground(Color.BLUE);
		add(p);
	}
	
	public void stop(){
		
	}
	
	public void destroy(){
		
	}
	
	public void paint(Graphics g){
		p.setSize(200,600);
		p.setLocation(0,0);
		p.setBackground(Color.BLUE);

		
		p.show(true);
		
		if(isPicerror()){
			System.out.println("Fehler beim pici: ");
		}else{
			g.setClip(201,0,800,400);
			g.drawImage(getPic(), 201, 0, this);
			
			g.setColor(Color.YELLOW);
			int x = 50;
			int y = 50;
			g.drawOval(x,y,1,1);
			g.fillOval(x,y,1,1);
		}

		
		
		/*
		 * Anzeigen der User:
		 */
		//Liste löschen:
		actAppletUserList.removeAll();
		
		//Set erzeugen der UserHashtable
		Set set = game.playerHashMap.entrySet();
		
		//Iterator erzeugen:
		Iterator I = set.iterator();
		
		//Elemente durchgehen:
		while( I.hasNext() ){
			Map.Entry me = (Map.Entry) I.next();
			actAppletUserList.add(((GamePlayer)me.getValue()).getName());
			//System.out.println(((Spieler)me.getValue()).name);
			//Min und Maxwerte überprüfen
			if(((GamePlayer)me.getValue()).getX()  < getMinX()){
				setMinX(((GamePlayer)me.getValue()).getX());
			}
			if(((GamePlayer)me.getValue()).getX() > getMaxX()){
				setMaxX(((GamePlayer)me.getValue()).getX());
			}
			if(((GamePlayer)me.getValue()).getY()  < getMiny()){
				setMiny(((GamePlayer)me.getValue()).getY());
			}
			if(((GamePlayer)me.getValue()).getY()  > getMaxy()){
				setMaxy(((GamePlayer)me.getValue()).getY());
			}
			if(((GamePlayer)me.getValue()).getZ()  < getMinz()){
				setMinz(((GamePlayer)me.getValue()).getZ());
			}
			if(((GamePlayer)me.getValue()).getZ()  > getMaxz()){
				setMaxz(((GamePlayer)me.getValue()).getZ());
			}
			//Punkte setzten
			switch(((GamePlayer)me.getValue()).getTeam()){
				case -1:
					g.setColor(Color.WHITE);
					break;
				case 0:
					g.setColor(Color.RED);
					break;
				case 1:
					g.setColor(Color.BLUE);
					break;
				case 2:
					g.setColor(Color.GREEN);
					break;
				case 3:
					g.setColor(Color.YELLOW);
					break;
				case 4:
					g.setColor(Color.MAGENTA);
					break;
				default:
					g.setColor(Color.BLACK);
					break;	
			}

			g.drawOval(((((GamePlayer)me.getValue()).getX() * ((getEndx() - getStartx())/(getMaxX() - getMinX()))) + (getStartx() - (getMinX() * ((getEndx() - getStartx())/(getMaxX() - getMinX()))))),
				       (int)(((float)((GamePlayer)me.getValue()).getY() * (((float)getEndy() - (float)getStarty())/((float)getMaxy() - (float)getMiny()))) + ((float)getStarty() - ((float)getMiny() * (((float)getEndy() - (float)getStarty())/((float)getMaxy() - (float)getMiny()))))),
					   10,
					   10);
			g.fillOval(((((GamePlayer)me.getValue()).getX() * ((getEndx() - getStartx())/(getMaxX() - getMinX()))) + (getStartx() - (getMinX() * ((getEndx() - getStartx())/(getMaxX() - getMinX()))))),
					(int)(((float)((GamePlayer)me.getValue()).getY() * (((float)getEndy() - (float)getStarty())/((float)getMaxy() - (float)getMiny()))) + ((float)getStarty() - ((float)getMiny() * (((float)getEndy() - (float)getStarty())/((float)getMaxy() - (float)getMiny()))))),
					   10,
					   10);	
			/*System.out.println("Zeichne PunktX!" + ((((Spieler)me.getValue()).x * ((endx - startx)/(maxx - minx))) + (startx - (minx * ((endx - startx)/(maxx - minx))))) +
					" x: " + ((Spieler)me.getValue()).x +
					" minx: " + minx +
					" maxx: " + maxx +
					" startx: " + startx +
					" endx: " + endx);
			System.out.println("Zeichne PunktY!" + (int)(((float)((Spieler)me.getValue()).y * (((float)endy - (float)starty)/((float)maxy - (float)miny))) + ((float)starty - ((float)miny * (((float)endy - (float)starty)/((float)maxy - (float)miny))))) +
					" y: " + ((Spieler)me.getValue()).y +
					" miny: " + miny +
					" maxy: " + maxy +
					" starty: " + starty +
					" endy: " + endy);
					*/
		}
		
		
		/*
		 * Anzeigen der Objekte:
		 */
		//Set erzeugen der UserHashtable
		Set seto = game.objectHashMap.entrySet();
		
		//Iterator erzeugen:
		Iterator Io = seto.iterator();

		//Elemente durchgehen:
		while( Io.hasNext() ){
			Map.Entry me = (Map.Entry) Io.next();
			if(((GameObject)me.getValue()).getName().matches("PowerNode")){
			  
			}else if(((GameObject)me.getValue()).getName().matches("PowerCore")){
				  
			}else if(((GameObject)me.getValue()).getName().matches("")){
				  
			}else if(((GameObject)me.getValue()).getName().matches("")){
				  
			}else if(((GameObject)me.getValue()).getName().matches("")){
				  
			}else if(((GameObject)me.getValue()).getName().matches("")){
				  
			}else if(((GameObject)me.getValue()).getName().matches("")){
				  
			}else if(((GameObject)me.getValue()).getName().matches("")){
				  
			}else if(((GameObject)me.getValue()).getName().matches("")){
				  
			}else if(((GameObject)me.getValue()).getName().matches("")){
				  
			}			   
		}
		
	}

	public void actionPerformed(ActionEvent e) {
		String com = e.getActionCommand();
		if(com.equals("Connect")){
			connection mdf = new connection(this);
		}
	}
	
	public void connect(String url, int port){
		
		Socket verbindung;
		
		try{
            verbindung = new Socket(url, port);
            
		}catch(IOException e){
			System.out.println("huhu");
		}
	}
	
	public boolean imageUpdate(Image pic, 
			                   int flags, 
							   int x, 
							   int y, 
							   int w, 
							   int h){
		if((flags & SOMEBITS) != 0){
			repaint(x, y, w, h);
		}else if((flags & ABORT) != 0){
			setPicerror(true);
			repaint();
		}
		return (flags & (ALLBITS | ABORT)) == 0 ;
		
	}

	public List getActAppletUserList() {
		return actAppletUserList;
	}

	public void setActAppletUserList(List actAppletUserList) {
		this.actAppletUserList = actAppletUserList;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setPicerror(boolean picerror) {
		this.picerror = picerror;
	}

	public boolean isPicerror() {
		return picerror;
	}

	public void setPic(Image pic) {
		this.pic = pic;
	}

	public Image getPic() {
		return pic;
	}

	public void setMinX(int minx) {
		this.minx = minx;
	}

	public int getMinX() {
		return minx;
	}

	public void setMaxX(int maxx) {
		this.maxx = maxx;
	}

	public int getMaxX() {
		return maxx;
	}

	public void setMiny(int miny) {
		this.miny = miny;
	}

	public int getMiny() {
		return miny;
	}

	public void setMaxy(int maxy) {
		this.maxy = maxy;
	}

	public int getMaxy() {
		return maxy;
	}

	public void setMinz(int minz) {
		this.minz = minz;
	}

	public int getMinz() {
		return minz;
	}

	public void setMaxz(int maxz) {
		this.maxz = maxz;
	}

	public int getMaxz() {
		return maxz;
	}

	public void setEndx(int endx) {
		this.endx = endx;
	}

	public int getEndx() {
		return endx;
	}

	public void setStarty(int starty) {
		this.starty = starty;
	}

	public int getStarty() {
		return starty;
	}

	public void setStartx(int startx) {
		this.startx = startx;
	}

	public int getStartx() {
		return startx;
	}

	public void setEndy(int endy) {
		this.endy = endy;
	}

	public int getEndy() {
		return endy;
	}
}
