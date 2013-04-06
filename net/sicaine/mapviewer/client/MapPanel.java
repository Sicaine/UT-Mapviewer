/*
 * Created on 11.01.2005
 */
package net.sicaine.mapviewer.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

/**
 * @author skiermayer
 */
public class MapPanel extends JPanel{
	private Client client;
	MapPanel(Client client){
		this.client = client;
	}
	
	public void paint(Graphics e){
		Graphics g = super.getGraphics();
		if(client.isPicerror()){
			System.out.println("Error on picture ");
		}else{
			g.setClip(201,0,800,400);
			g.drawImage(client.getPic(), 201, 0, this);
			
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
		client.getActAppletUserList().removeAll();
		
		//Set erzeugen der UserHashtable
		Set set = client.getGame().playerHashMap.entrySet();
		
		//Iterator erzeugen:
		Iterator I = set.iterator();
		
		//Elemente durchgehen:
		while( I.hasNext() ){
			Map.Entry<String, GamePlayer> playerEntry = (Map.Entry<String, GamePlayer>) I.next();
			client.getActAppletUserList().add(((GamePlayer)playerEntry.getValue()).getName());
			System.out.println(((GamePlayer)playerEntry.getValue()).getName());
			//Min und Maxwerte überprüfen
			if((playerEntry.getValue()).getX()  < client.getMinX()){
				client.setMinX((playerEntry.getValue()).getX());
			}
			if((playerEntry.getValue()).getX() > client.getMaxX()){
				client.setMaxX((playerEntry.getValue()).getX());
			}
			if((playerEntry.getValue()).getY()  < client.getMiny()){
				client.setMiny((playerEntry.getValue()).getY());
			}
			if((playerEntry.getValue()).getY()  > client.getMaxy()){
				client.setMaxy((playerEntry.getValue()).getY());
			}
			if((playerEntry.getValue()).getZ()  < client.getMinz()){
				client.setMinz((playerEntry.getValue()).getZ());
			}
			if((playerEntry.getValue()).getZ()  > client.getMaxz()){
				client.setMaxz((playerEntry.getValue()).getZ());
			}
			//Punkte setzten
			switch((playerEntry.getValue()).getTeam()){
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

			g.drawOval(((((GamePlayer)playerEntry.getValue()).getX() * ((client.getEndx() - client.getStartx())/(client.getMaxX() - client.getMinX()))) + (client.getStartx() - (client.getMinX() * ((client.getEndx() - client.getStartx())/(client.getMaxX() - client.getMinX()))))),
				       (int)(((float)((GamePlayer)playerEntry.getValue()).getY() * (((float)client.getEndy() - (float)client.getStarty())/((float)client.getMaxy() - (float)client.getMiny()))) + ((float)client.getStarty() - ((float)client.getMiny() * (((float)client.getEndy() - (float)client.getStarty())/((float)client.getMaxy() - (float)client.getMiny()))))),
					   10,
					   10);
			g.fillOval(((((GamePlayer)playerEntry.getValue()).getX() * ((client.getEndx() - client.getStartx())/(client.getMaxX() - client.getMinX()))) + (client.getStartx() - (client.getMinX() * ((client.getEndx() - client.getStartx())/(client.getMaxX() - client.getMinX()))))),
					(int)(((float)((GamePlayer)playerEntry.getValue()).getY() * (((float)client.getEndy() - (float)client.getStarty())/((float)client.getMaxy() - (float)client.getMiny()))) + ((float)client.getStarty() - ((float)client.getMiny() * (((float)client.getEndy() - (float)client.getStarty())/((float)client.getMaxy() - (float)client.getMiny()))))),
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
					" endy: " + endy);*/
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
			client.setPicerror(true);
			repaint();
		}
		return (flags & (ALLBITS | ABORT)) == 0 ;
		
	}

}
