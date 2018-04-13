package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class Server {
	
	private int port;
	public ServerSocket gniazdoSerwera;
	private LinkedList<Polaczenie> polaczenie;
	public NasluchiwaniePolaczenia nasluchiwaniePolaczenia;
	private CzekanieNaKlienta czekanieNaKlienta;
	private boolean serwerUruchomiony; 

	public Server(int port) {
		this.port = port;
		serwerUruchomiony = false;
	}

	public LinkedList<Polaczenie> getConnections() {
		return polaczenie;
	}
	
	public int getConnectionsCount() {
		return polaczenie.size();
	}
	
	public boolean start() { //start serwera
		try {
			gniazdoSerwera = new ServerSocket(port); //tworzy gniazdo serwera
		} catch (Exception ex) {
			return false;
		}
		serwerUruchomiony = true;
		polaczenie = new LinkedList<Polaczenie>();
		nasluchiwaniePolaczenia = new NasluchiwaniePolaczenia(this);
		nasluchiwaniePolaczenia.start();
		czekanieNaKlienta = new CzekanieNaKlienta(this);
		czekanieNaKlienta.start();
		return true;
	}

	public boolean serwerUruchomiony() {
		return serwerUruchomiony;
	}

	public void stop() {
		serwerUruchomiony = false;
		nasluchiwaniePolaczenia.interrupt(); //przerwanie nas³uchiwania po³¹czenia
		czekanieNaKlienta.interrupt(); //przerwanie czekanie na klienta
		try {
			for (int i = 0; i < polaczenie.size(); i++) { //zamyka istniej¹ce po³¹czenia
				Polaczenie connection = (Polaczenie) polaczenie.get(i);
				connection.close();
			}
			polaczenie.clear();
			gniazdoSerwera.close();
		} catch (IOException ex) {
		}
	}
}
