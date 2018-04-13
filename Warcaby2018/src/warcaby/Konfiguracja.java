package warcaby;

import java.io.*;

public class Konfiguracja {
	
	private static Konfiguracja instance = null; //instancja
	private String host = "localhost"; //nazwa hostu do polaczenia
	private int port = 4545; //port 
	private String fileName = "warcaby.ini"; //nazwa pliku konfiguracyjnego
	
	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public static Konfiguracja getInstance() {
		if (instance == null) 
			instance = new Konfiguracja(); //jeœli instancja nie istnieje stwórz j¹
		return instance;
	}
	
	public void zapisz() {
		try {
			PrintWriter writer;
			writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
			writer.print("host=");
			writer.println(getHost());
			writer.print("port=");
			writer.println(getPort());
			writer.close();
		} catch (IOException e) {
		}
	}
}