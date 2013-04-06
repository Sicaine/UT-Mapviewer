package net.sicaine.mapviewer.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Client{
    private ServerSocket serverSocket;
	private Socket connection;
	private OutputStream out;
	private BufferedReader in;
	
	Client(Socket verbindung){
		connection = verbindung;
		try {
			in  = new BufferedReader(new InputStreamReader(verbindung.getInputStream()));
			out = verbindung.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(dynarray pTex) throws IOException{
		byte[] text = pTex.a;
		boolean[] txt = pTex.b;
		try{
			char array[];
	  		for(int i = 0; i < text.length; i++){
	  			if( txt[i] == true){
	  				out.write(text[i]);
	  			}
	  		}
			out.flush();
		}catch(IOException e){
			throw new IOException("test");
		}
	}

}