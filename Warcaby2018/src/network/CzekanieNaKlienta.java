package network;

import java.io.IOException;
import java.net.Socket;

public class CzekanieNaKlienta extends Thread {
	
	private Server server;

	public CzekanieNaKlienta(Server server) {
		this.server = server;
	}

	public void run() {
		while (server.serwerUruchomiony()) { 
			Socket gniazdoKlienta;
			try {
				gniazdoKlienta = server.gniazdoSerwera.accept(); //Ods�uchuje po��czenie, kt�re ma by� wykonane w tym gniezdzie i akceptuje je
				// S�uchaj klienta w odr�bnym w�tku
				Polaczenie polaczenie = new Polaczenie(gniazdoKlienta); //nowe polaczenie z gniazdem klienta
				server.getConnections().add(polaczenie); 
				polaczenie.start(); //start polaczenia
			} catch (IOException e) {
			}
		}
	}
}