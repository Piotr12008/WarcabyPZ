package network;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class Polaczenie extends Thread {
	
	private String nick = "";
	private boolean jestPolaczony = false;
	private Socket gniazdo;
	private BufferedReader in;
	private PrintWriter out;
	public LinkedList<String> kolejkaWiadomosci;

	public Polaczenie(Socket gniazdo) {
		try {
			in = new BufferedReader(new InputStreamReader(gniazdo.getInputStream()));
			out = new PrintWriter(gniazdo.getOutputStream(), true);
		} catch (IOException ex) {
		}
		kolejkaWiadomosci = new LinkedList<String>();
		this.gniazdo = gniazdo;
	}

	public void wyslijWiadomosc(String s) {
		out.println(s);
	}

	public void run() {
		String s;
		try {
			while ((s = in.readLine()) != null) {
				kolejkaWiadomosci.add(s);
			}
			out.close();
			in.close();
		} catch (IOException ex) {
		}
		try {
			gniazdo.close();
		} catch (Exception ex) {
		}
	}

	public void close() {
		try {
			gniazdo.close();
		} catch (Exception ex) {
		}
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return nick;
	}

	public void setJoined(boolean b) {
		jestPolaczony = b;
	}

	public boolean jestPolaczony() {
		return jestPolaczony;
	}
}