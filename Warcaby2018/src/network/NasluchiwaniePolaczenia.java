package network;

import java.util.Iterator;

public class NasluchiwaniePolaczenia extends Thread {

	private Server server;

	public NasluchiwaniePolaczenia(Server server) {
		this.server = server;
	}

	public void run() {
		while (server.serwerUruchomiony()) {
			for (int i = server.getConnections().size() - 1; i >= 0; --i) {
				Polaczenie polaczenie = (Polaczenie) server.getConnections().get(i);
				if (!polaczenie.isAlive()) {
					polaczenie.close();
					server.getConnections().remove(polaczenie);
				} else {
					Wydarzenia ge;
					while ((ge = receiveMessage(polaczenie)) != null) {
						switch (ge.getType()) {
						case Wydarzenia.C_CHAT_MSG:
							if (ge.getPlayerId() != "") {
								Wydarzenia geOut;
								geOut = new Wydarzenia(Wydarzenia.SB_CHAT_MSG, ge.getMessage());
								geOut.setPlayerId(ge.getPlayerId());
								sendBroadcastMessage(geOut);
							}
							break;
						}
					}
				}
			}

			try {
				Thread.sleep(50);
			} catch (Exception ex) {
			}
		}
	}

	public void sendMessage(Polaczenie polaczenie, Wydarzenia ge) {
		polaczenie.wyslijWiadomosc(ge.toSend());
	}

	public void sendBroadcastMessage(Wydarzenia ge) {
		Iterator<Polaczenie> i = server.getConnections().iterator();
		while (i.hasNext()) {
			Polaczenie polaczenie = (Polaczenie) i.next();
			if (polaczenie.isAlive()) {
				sendMessage(polaczenie, ge);
			}
		}
	}

	public Wydarzenia receiveMessage(Polaczenie polaczenie) {
		if (polaczenie.kolejkaWiadomosci.isEmpty()) {
			return null;
		} else {
			Wydarzenia ge = new Wydarzenia((String) polaczenie.kolejkaWiadomosci.getFirst());
			polaczenie.kolejkaWiadomosci.removeFirst();
			return ge;
		}
	}

	public boolean isPlayerIDUnique(String nick) {
		Iterator<Polaczenie> i = server.getConnections().iterator();
		while (i.hasNext()) {
			Polaczenie polaczenie = (Polaczenie) i.next();
			if (polaczenie.getNick().compareTo(nick) == 0)
				return false;
		}
		return true;
	}
}