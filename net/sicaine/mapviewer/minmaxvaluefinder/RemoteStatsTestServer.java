/*
 * Created on 20.11.2004 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Wormbo TODO To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Style - Code Templates
 */

package net.sicaine.mapviewer.minmaxvaluefinder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RemoteStatsTestServer extends JFrame implements WindowListener{
	JPanel p;
	JLabel jminx;
	JLabel jmaxx;
	JLabel jminy;
	JLabel jmaxy;
	JLabel jminz;
	JLabel jmaxz;
	JLabel map;
	JButton bablage;
	ServerSocket serverSock;

	Socket verbindung;

	BufferedWriter out;

	BufferedReader in;
	int minx = 0;
	int maxx = 0;
	int miny = 0;
	int maxy = 0;
	int minz = 0;
	int maxz = 0;
	boolean isSX = true;
	boolean isSY = true;
	boolean isSZ = true;
	
	RemoteStatsTestServer(int port) throws SocketException, IOException {
		//passiven Socket anlegen
		serverSock = new ServerSocket(port);
	}

	void mainprog() {
		bablage = new JButton("Zwischenablage");
		bablage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				 Toolkit tk = Toolkit.getDefaultToolkit();

				  Clipboard systemClipboard = tk.getSystemClipboard();

				  /*Transferable transferData =
				     systemClipboard.getContents(null);

				  DataFlavor[] dataFlavor =
				      transferData.getTransferDataFlavors();

				  System.out.println( "Wir haben " + dataFlavor.length +
				                      " Eigenschaften" );
*/
				  //DataFlavor flavor = dataFlavor[0];

				 // Object content = transferData.getTransferData( flavor );
				  
				  String s = map.getText() + "\n" +
				             minx + " " + maxx + " " +
				             miny + " " + maxy + " " +
				             minz + " " + maxz+ " ";
				  StringSelection cont =  new StringSelection(s);
				  /*systemClipboard.setContents(cont, new ClipboardOwner() {
					public void lostOwnership(Clipboard arg0,
							Transferable arg1) {
						// TODO Auto-generated method stub

					}
				});*/

				systemClipboard.setContents(cont, null);
				 // System.out.println( "Der Inhalt des Clipboards:\n" + content);
				  }				
		});
		this.addWindowListener(this);
		this.setSize(150,190);
		GridBagLayout gbl = new GridBagLayout();
		
		p = new JPanel(gbl);
		GridBagConstraints cgbl = gbl.getConstraints(p);
		cgbl.gridwidth = GridBagConstraints.REMAINDER;
		p.add(map = new JLabel("Map"), cgbl);
		cgbl.fill = GridBagConstraints.BOTH;
		p.add(new JLabel("X-min:"),cgbl);
		cgbl.gridwidth = GridBagConstraints.REMAINDER;
		p.add(jminx = new JLabel("0"),cgbl);
		cgbl.gridwidth = GridBagConstraints.BOTH;
		p.add(new JLabel("X-max:"),cgbl);
		cgbl.gridwidth = GridBagConstraints.REMAINDER;
		p.add(jmaxx = new JLabel("0"),cgbl);
		cgbl.gridwidth = GridBagConstraints.BOTH;
		p.add(new JLabel("Y-min:"),cgbl);
		cgbl.gridwidth = GridBagConstraints.REMAINDER;
		p.add(jminy = new JLabel("0"),cgbl);
		cgbl.gridwidth = GridBagConstraints.BOTH;
		p.add(new JLabel("Y-max:"),cgbl);
		cgbl.gridwidth = GridBagConstraints.REMAINDER;
		p.add(jmaxy = new JLabel("0"),cgbl);
		cgbl.gridwidth = GridBagConstraints.BOTH;
		p.add(new JLabel("Z-min:"),cgbl);
		cgbl.gridwidth = GridBagConstraints.REMAINDER;
		p.add(jminz = new JLabel("0"),cgbl);
		cgbl.gridwidth = GridBagConstraints.BOTH;
		p.add(new JLabel("Z-max:"),cgbl);
		cgbl.gridwidth = GridBagConstraints.REMAINDER;
		p.add(jmaxz = new JLabel("0"),cgbl);
		cgbl.gridwidth = GridBagConstraints.BOTH;
		
		p.add(bablage,cgbl);

		
		this.getContentPane().add(p);
		this.setLocation(50,50);
		this.setVisible(true);
		
		while (true) {
			// net.sicaine.mapviewer.server-Aktivitaet
			try {
				// auf Verbindungsanforderungen warten
				System.out.println("Warte auf Verbindung...");
				verbindung = serverSock.accept();
				verbindung.setSoTimeout(30000);
				System.out.println("Verbindung hergestellt.");
				// StreamReader/Writer initialisieren
				// Hierbei ist die Angabe des Charsets zumindest beim
				// InputStreamReader wichtig, da sonst UTF-8 angenommen wird!
				in = new BufferedReader(new InputStreamReader(verbindung
						.getInputStream(), "ISO-8859-1"));
				out = new BufferedWriter(new OutputStreamWriter(verbindung
						.getOutputStream(), "ISO-8859-1"));
				System.out.println("Mutator: " + in.readLine());
				System.out.println("Game   : " + in.readLine());
				{
					String []temp;
					String temi = in.readLine();
					temp = temi.split(" ", 4);
					map.setText(temp[0]);
				}
				out.write("200 OK");
				out.newLine();
				out.flush();
				do {
					// erstes Zeichen lesen
					int lineStart = in.read();

					if (lineStart == -1) {
						verbindung.close();
						System.out.println("Verbindung abgebaut.");
						break;
					} else if (lineStart == 10) {
						out.write("200 OK");
						out.newLine();
						out.flush();
					} else {
						processLine(lineStart, in);
					}
				} while (true);
			} catch (SocketException e) {
				System.out.println(e);
			}
			// Reaktion auf Verbindungsabbau durch net.sicaine.mapviewer.client
			catch (EOFException e) {
				System.out.println("Verbindung abgebrochen.");
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	void processLine(int lineStart, BufferedReader in) {
		byte b = (byte) lineStart;
		if ((b & 0x40) == 0)
			System.out.print("Player");
		else
			System.out.print("Object");
		System.out.print(" ID: ");
		System.out.print((byte) (b & 0x3F));

		try {
			b = (byte) in.read();
			if ((b & 0x80) == 0) {
				System.out.print(" disconnected");
			} else {
				if ((b & 0x40) != 0)
					System.out.print(", is spectator");
				if ((b & 0x20) != 0)
					System.out.print(", is admin");
				if ((b & 0x10) != 0)
					System.out.print(", is bot");
				if ((b & 0x08) != 0)
					System.out.print(", has flag/bomb");
				if ((b & 0x04) != 0)
					System.out.print(", is invisible");

				boolean bChangedHealth = (b & 0x02) != 0;
				boolean bChangedShield = (b & 0x01) != 0;
				b = (byte) in.read();
				boolean bChangedLocX = (b & 0x80) != 0;
				boolean bChangedLocY = (b & 0x40) != 0;
				boolean bChangedLocZ = (b & 0x20) != 0;
				boolean bChangedScore = (b & 0x10) != 0;
				boolean bChangedLives = (b & 0x08) != 0;
				boolean bChangedTeam = (b & 0x04) != 0;
				boolean bChangedName = (b & 0x02) != 0;
				boolean bChangedWeapon = (b & 0x01) != 0;

				if (bChangedHealth) {
					System.out.print(", Health: ");
					System.out.print(in.read() << 8 | in.read());
				}
				if (bChangedShield) {
					System.out.print(", Shield: ");
					System.out.print(in.read() << 8 | in.read());
				}
				if (bChangedLocX) {
					System.out.print(", X: ");
					int x = (short) (in.read() << 8 | in.read());
					if(isSX){
						minx = maxx = x;
						isSX = false;
						jminx.setText(String.valueOf(minx));
						jmaxx.setText(String.valueOf(maxx));
						this.repaint();
					}
					if(x < minx){
						minx = x;
						jminx.setText(String.valueOf(minx));
						this.repaint();
					}else if(x > maxx){
						maxx = x;
						jmaxx.setText(String.valueOf(maxx));
						this.repaint();
					}
				}
				if (bChangedLocY) {
					System.out.print(", Y: ");
					int y = (short) (in.read() << 8 | in.read());
					if(isSY){
						miny = maxy = y;
						isSY = false;
						jminy.setText(String.valueOf(miny));
						jmaxy.setText(String.valueOf(maxy));
						this.repaint();
					}
					if(y < miny){
						miny = y;
						jminy.setText(String.valueOf(miny));
						this.repaint();
					}else if(y > maxy){
						maxy = y;
						jmaxy.setText(String.valueOf(maxy));
						this.repaint();
					}
				}
				if (bChangedLocZ) {
					System.out.print(", Z: ");
					int z = (short) (in.read() << 8 | in.read());
					if(isSZ){
						minz = maxz = z;
						isSZ = false;
						jminz.setText(String.valueOf(minz));
						jmaxz.setText(String.valueOf(maxz));
						this.repaint();
					}
					if(z < minz){
						minz = z;
						jminz.setText(String.valueOf(minz));
						this.repaint();
					}else if(z > maxz){
						maxz = z;
						jmaxz.setText(String.valueOf(maxz));
						this.repaint();
					}
				}
				if (bChangedScore) {
					System.out.print(", Score: ");
					System.out.print((short) (in.read() << 8 | in.read()));
				}
				if (bChangedLives) {
					System.out.print(", Lives: ");
					System.out.print(in.read() << 8 | in.read());
				}
				if (bChangedTeam) {
					System.out.print(", Team: ");
					System.out.print((byte) in.read());
				}

				if (bChangedName || bChangedWeapon) {
					String line = in.readLine();

					if (bChangedName) {
						int i = line.indexOf(9);
						System.out.print(", Nick: ");
						if (i == -1)
							System.out.print(line);
						else {
							System.out.print(line.substring(0, i));
							line = line.substring(i + 1);
						}
					}
					if (bChangedWeapon) {
						System.out.print(", Weapon: ");
						System.out.print(line);
					}
				}
			}
			System.out.println();
		} catch (IOException e) {
			System.out.println();
			System.out.println(e);
		}

	}


	public static void main(String args[]) {
		int port;
		if (args.length == 1)
			port = Integer.parseInt(args[0]);
		else
			port = 7770;

		try {
			RemoteStatsTestServer srv = new RemoteStatsTestServer(port);
			srv.mainprog();
		} catch (SocketException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent arg0) {
		System.exit(1);
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}