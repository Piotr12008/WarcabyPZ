package network;

import java.net.Socket;

public class Client {
	
	private String host;
	private int port;
	private String IDGracza;
	private Socket gniazdo;
	private Polaczenie polaczenie = null;

	public String getIDGracza() {
		return IDGracza;
	}
	
	public Client(String nick, String host, int port) {
		this.host = host;
		this.port = port;
		this.IDGracza = nick;
	}
	
	public boolean start() {
		try {
			gniazdo = new Socket(host, port);
		} catch (Exception ex) {
			return false;
		}
		polaczenie = new Polaczenie(gniazdo);
		polaczenie.start();
		return true;
	}

	public void stop() {
		if (polaczenie != null)
			polaczenie.close();
	}

	public void sendMessage(Wydarzenia ge) { 
		polaczenie.wyslijWiadomosc(ge.toSend()); //wyslij wiadomosc o tworzeniu klienta
	}

	public Wydarzenia receiveMessage() { 
		if (polaczenie.kolejkaWiadomosci.isEmpty()) { //jesli kolejka wiadomosci jest pusta zwroc null
			return null;
		} else {
			Wydarzenia ge = new Wydarzenia((String) polaczenie.kolejkaWiadomosci.getFirst()); //wez pierwsza
			polaczenie.kolejkaWiadomosci.removeFirst();//usun pierwsza
			return ge;
		}
	}

	public boolean isAlive() { //klient/polaczenie dostepne
		return (polaczenie != null && polaczenie.isAlive());
	}
}