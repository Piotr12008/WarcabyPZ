package warcaby;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import network.Client;
import network.Wydarzenia;
import network.Server;

public class Warcaby extends JFrame {
	/**
	 * Niby jakas zmiana, sprawdzam gita
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel Panel = null;	
	private JButton graczVsGracz = null;
	private JButton graczVsKomputer = null;
	private JButton start = null;
	private JButton polacz = null;
	private JButton poziomLatwy = null;
	private JButton poziomTrudny = null;
	private JButton wroc = null;
	private JButton zapisz = null;
	private JButton wczytaj = null;
	private JButton rozpocznijGre = null;
	private JLabel napisWarcaby = null;
	private JLabel napisPoziom = null;
	private JLabel napisChat = null;
	private JScrollPane poleInformacyjneGraczVsKomputer = null;
	private JScrollPane poleDoWyswietlaniaWiadomosci = null;
	private JScrollPane poleInformacyjneGraczVsGracz = null;
	private JTextField poleDoWpisaniaAdresu = null;
	private JTextField poleDoPisaniaWiadomosci = null;
	private JTextArea statusGryZKomputerem = null;
	private JTextArea statusGryZGraczem = null;
	private JRadioButton przyciskKlient = null;
	private JRadioButton przyciskSerwer = null;

	private Client client = null;
	private JTextArea czatOdbierz = null;
	private boolean clientStarted = false;
	private Server server = null; // status serwera
	private int port;
	
	private Plansza plansza = null;
	

	public Warcaby() {
		super();
		inicjalizacja();
		port = Konfiguracja.getInstance().getPort();
		poleDoWpisaniaAdresu.setText(Konfiguracja.getInstance().getHost());
	}

	
	private void inicjalizacja() {
		this.setSize(650, 700);
		this.setLocation(200, 5);
		this.setContentPane(getPanel());
		this.setResizable(false);
		this.setTitle("Warcaby");
		
		this.addWindowListener(new java.awt.event.WindowAdapter() { // nas³uchiwanie interfejsu do odbierania zdarzeñ okiennych. 
			public void windowClosing(java.awt.event.WindowEvent e) { // Wywo³ywane, gdy u¿ytkownik próbuje zamkn¹æ okno z menu systemowego okna.
				Konfiguracja.getInstance().setHost(poleDoWpisaniaAdresu.getText());
				Konfiguracja.getInstance().zapisz(); //zapisanie do ppoleliki ini aktualnej konfiguracji
			}
		});
		
		new Thread() { //aalokacja nowego watku
			@Override
			public void run() {
				while (true) { // jesli ten watek jest uruchomiony
					if (client != null && client.isAlive()) {
						processMessages(); //funkcja obsugujaca wiadomosci
					} else if (clientStarted && client != null) {
						client.stop();
						client = null;
						zerwanePolaczenie(); //funckja oblugujaca gdy polaczenie zostanie zerwane
					}
					try {
						Thread.sleep(20); // usypia watek na 20 ms
					} catch (InterruptedException ex) {
					}
				}
			}
		}.start(); //powoduje wyknywanie watku
		
		
	}

	
	private JPanel getPanel() {
		if (Panel == null) {
			Panel = new JPanel();
			Panel.setLayout(null);
			//Panel.setBackground(Color.BLUE);
			Panel.add(getGraczVsGracz(), null);
			Panel.add(getGraczVsKomputer(), null);
			Panel.add(getNapisWarcaby(), null);
			Panel.add(getNapisPoziom(), null);
			Panel.add(getPoziomLatwy(), null);
			Panel.add(getPoziomTrudny(), null);
			Panel.add(getWroc(), null);
			Panel.add(getZapisz(), null);
			Panel.add(getWczytaj(), null);
			Panel.add(getNapisChat(), null);
			Panel.add(getpoleDoPisaniaWiadomosci(), null);
			Panel.add(getPoleInformacyjneGraczVsKomputer(), null);
			Panel.add(getpoleDoWyswietlaniaWiadomosci(), null);
			Panel.add(getPoleInformacyjneGraczVsGracz(), null);
			Panel.add(getPrzyciskKlient(), null);
			Panel.add(getPlansza(), null);
			Panel.add(getPrzyciskSerwer(), null);
			Panel.add(getStart(), null);
			Panel.add(getPolacz(), null);
			Panel.add(getPoleDoWpisaniaAdresu(), null);
			Panel.add(getRozpocznijGre(), null);
			ButtonGroup grupa = new ButtonGroup();
			grupa.add(przyciskKlient);
			grupa.add(przyciskSerwer);
			
		}
		
		return Panel;
	}

	
	private JLabel getNapisWarcaby() {
		if (napisWarcaby == null) {
			napisWarcaby = new JLabel();
			// setLayout(null);
			Font font = new Font("Helvetica", Font.BOLD, 80);
			napisWarcaby.setFont(font);
			napisWarcaby.setForeground(Color.RED);
			napisWarcaby.setBounds(new Rectangle(120, 60, 600, 70));
			napisWarcaby.setText("WARCABY");
		}
		
		return napisWarcaby;
	}

	
	private JLabel getNapisPoziom() {
		if (napisPoziom == null) {
			napisPoziom = new JLabel();
			Font font = new Font("Helvetica", Font.BOLD, 80);
			napisPoziom.setFont(font);
			napisPoziom.setForeground(Color.RED);
			napisPoziom.setBounds(new Rectangle(165, 80, 500, 70));
			napisPoziom.setText("POZIOM");
			napisPoziom.setVisible(false);
		}
		
		return napisPoziom;
	}

	
	private JButton getGraczVsGracz() {
		if (graczVsGracz == null) {
			graczVsGracz = new JButton();
			Font font = new Font("Helvetica", Font.BOLD, 25);
			graczVsGracz.setFont(font);
			graczVsGracz.setBackground(Color.RED);
			graczVsGracz.setLocation(170, 340);
			graczVsGracz.setSize(300, 80);
			graczVsGracz.setText("Gracz vs Gracz");
			graczVsGracz.setEnabled(true);
			graczVsGracz.setVisible(true);
			graczVsGracz.addActionListener(new java.awt.event.ActionListener() {	
				public void actionPerformed(java.awt.event.ActionEvent e) {
					graczVsGracz.setVisible(false);
					graczVsKomputer.setVisible(false);
					napisWarcaby.setVisible(false);
					napisPoziom.setVisible(false);
					poziomLatwy.setVisible(false);
					poziomTrudny.setVisible(false);
					Font font = new Font("Helvetica", Font.BOLD, 12);
					wroc.setFont(font);
					wroc.setLocation(178, 510);
					wroc.setSize(130, 25);
					wroc.setBackground(null);
					wroc.setEnabled(true);
					wroc.setVisible(true);
					zapisz.setVisible(false);
					napisChat.setVisible(true);
					poleDoPisaniaWiadomosci.setVisible(true);
					poleDoWyswietlaniaWiadomosci.setVisible(true);
					poleInformacyjneGraczVsGracz.setVisible(true);
					przyciskKlient.setVisible(true);
					przyciskSerwer.setVisible(true);
					start.setVisible(true);
					rozpocznijGre.setVisible(true);
					wczytaj.setVisible(false);
					plansza.setLocation(70,0);
					plansza.setVisible(true);
				}
			});
		}
		
		return graczVsGracz;
	}

	
	private JButton getGraczVsKomputer() {
		if (graczVsKomputer == null) {
			graczVsKomputer = new JButton();
			Font font = new Font("Helvetica", Font.BOLD, 25);
			graczVsKomputer.setFont(font);
			graczVsKomputer.setBackground(Color.RED);
			graczVsKomputer.setLocation(170, 210);
			graczVsKomputer.setSize(300, 80);
			graczVsKomputer.setText("Gracz vs Komputer");
			graczVsKomputer.setEnabled(true);
			graczVsKomputer.setVisible(true);
			graczVsKomputer.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Font fontwroc = new Font("Helvetica", Font.BOLD, 25);
					wroc.setFont(fontwroc);
					wroc.setBackground(Color.RED);
					wroc.setLocation(170, 490);
					wroc.setSize(300, 80);
					wroc.setEnabled(true);
					wroc.setVisible(true);
					napisWarcaby.setVisible(false);
					graczVsGracz.setVisible(false);
					graczVsKomputer.setVisible(false);
					napisPoziom.setVisible(true);
					poziomTrudny.setVisible(true);
					poziomLatwy.setVisible(true);
					wczytaj.setVisible(false);
				}
			});
		}
		
		return graczVsKomputer;
	}

	
	private JButton getPoziomLatwy() {
		if (poziomLatwy == null) {
			poziomLatwy = new JButton();
			Font font = new Font("Helvetica", Font.BOLD, 25);
			poziomLatwy.setFont(font);
			poziomLatwy.setBackground(Color.RED);
			poziomLatwy.setLocation(170, 250);
			poziomLatwy.setSize(300, 80);
			poziomLatwy.setText("£atwy");
			poziomLatwy.setEnabled(true);
			poziomLatwy.setVisible(false);
			poziomLatwy.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Font fontwroc = new Font("Helvetica", Font.BOLD, 15);
					wroc.setFont(fontwroc);
					wroc.setSize(150, 50);
					wroc.setBackground(null);
					wroc.setLocation(470, 600);
					wroc.setVisible(true);
					napisPoziom.setVisible(false);
					poziomTrudny.setVisible(false);
					poziomLatwy.setVisible(false);
					zapisz.setVisible(true);
					poleInformacyjneGraczVsKomputer.setVisible(true);
					plansza.setLocation(70,40);
					plansza.setVisible(true);
				}
			});
		}
		
		return poziomLatwy;
	}

	
	private JButton getPoziomTrudny() {
		if (poziomTrudny == null) {
			poziomTrudny = new JButton();
			Font font = new Font("Helvetica", Font.BOLD, 25);
			poziomTrudny.setFont(font);
			poziomTrudny.setBackground(Color.RED);
			poziomTrudny.setLocation(170, 370);
			poziomTrudny.setSize(300, 80);
			poziomTrudny.setText("Trudny");
			poziomTrudny.setEnabled(true);
			poziomTrudny.setVisible(false);
			poziomTrudny.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Font fontwroc = new Font("Helvetica", Font.BOLD, 15);
					wroc.setFont(fontwroc);
					wroc.setSize(150, 50);
					wroc.setLocation(470, 600);
					wroc.setBackground(null);
					wroc.setVisible(true);
					napisPoziom.setVisible(false);
					poziomTrudny.setVisible(false);
					poziomLatwy.setVisible(false);
					zapisz.setVisible(true);
					poleInformacyjneGraczVsKomputer.setVisible(true);
					plansza.setLocation(70,40);
					plansza.setVisible(true);
				}
			});
		}
		
		return poziomTrudny;
	}

	
	private JButton getWroc() {
		if (wroc == null) {
			wroc = new JButton();
			Font font = new Font("Helvetica", Font.BOLD, 25);
			wroc.setFont(font);
			wroc.setBackground(Color.RED);
			wroc.setText("Wróæ");
			wroc.setEnabled(true);
			wroc.setVisible(false);
			wroc.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					napisWarcaby.setVisible(true);
					graczVsGracz.setVisible(true);
					graczVsKomputer.setVisible(true);
					napisPoziom.setVisible(false);
					poziomTrudny.setVisible(false);
					poziomLatwy.setVisible(false);
					wroc.setVisible(false);
					zapisz.setVisible(false);
					poleInformacyjneGraczVsKomputer.setVisible(false);
					poleInformacyjneGraczVsGracz.setVisible(false);
					napisChat.setVisible(false);
					poleDoPisaniaWiadomosci.setVisible(false);
					poleDoWyswietlaniaWiadomosci.setVisible(false);
					poleInformacyjneGraczVsGracz.setVisible(false);
					przyciskKlient.setVisible(false);
					przyciskSerwer.setVisible(false);
					start.setVisible(false);
					rozpocznijGre.setVisible(false);
					wczytaj.setVisible(true);
					poleDoWpisaniaAdresu.setVisible(false);
					polacz.setVisible(false);
					plansza.setVisible(false);
				}
			});
		}
		
		return wroc;
	}

	
	private JButton getZapisz() {
		if (zapisz == null) {
			zapisz = new JButton();
			Font font = new Font("Helvetica", Font.BOLD, 15);
			zapisz.setFont(font);
			//zapisz.setBackground(Color.RED);
			zapisz.setSize(150, 50);
			zapisz.setLocation(25, 600);
			zapisz.setText("Zapisz");
			zapisz.setEnabled(true);
			zapisz.setVisible(false);
			zapisz.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					
					
				}
			});
		}
		
		return zapisz;
	}

	
	private JButton getWczytaj() {
		if (wczytaj == null) {
			wczytaj = new JButton();
			Font font = new Font("Helvetica", Font.BOLD, 25);
			wczytaj.setFont(font);
			wczytaj.setBackground(Color.RED);
			wczytaj.setLocation(170, 470);
			wczytaj.setSize(300, 80);
			wczytaj.setText("Wczytaj");
			wczytaj.setEnabled(true);
			wczytaj.setVisible(true);
			wczytaj.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
		
					
					
				}
			});
		}
		
		return wczytaj;
	}
	
	
	private void zmienStatusGryZKomputerem(String wiadomosc) {
		Color color;
		color = new Color(255, 255, 196);
		statusGryZKomputerem.setBackground(color);
		statusGryZKomputerem.setText("");
		statusGryZKomputerem.append(wiadomosc);
	}

	
	private JTextArea getStatusGryZKomputerem() {
		if (statusGryZKomputerem == null) {
			statusGryZKomputerem = new JTextArea();
			statusGryZKomputerem.setEnabled(true);
			statusGryZKomputerem.setEditable(false);
			statusGryZKomputerem.setLineWrap(true);
			statusGryZKomputerem.setWrapStyleWord(true);
			statusGryZKomputerem.setFont(new Font("Dialog", Font.BOLD, 12));
			zmienStatusGryZKomputerem(
					"Zagrajmy w Warcaby, o ile ktoœ napisze logike xD Jest to Panel scrollowany wiêc jak bêdzie d³ugi tekst to powinno siê daæ przewin¹æ.");
		}
		
		return statusGryZKomputerem;
	}

	
	private JScrollPane getPoleInformacyjneGraczVsKomputer() {
		if (poleInformacyjneGraczVsKomputer == null) {
			poleInformacyjneGraczVsKomputer = new JScrollPane();
			poleInformacyjneGraczVsKomputer.setLocation(223, 600);
			poleInformacyjneGraczVsKomputer.setEnabled(true);
			poleInformacyjneGraczVsKomputer.setVisible(false);
			poleInformacyjneGraczVsKomputer.setViewportView(getStatusGryZKomputerem());
			poleInformacyjneGraczVsKomputer.setSize(200, 50);
		}
		
		return poleInformacyjneGraczVsKomputer;
	}

	
	private JLabel getNapisChat() {
		if (napisChat == null) {
			napisChat = new JLabel();
			napisChat.setBounds(new Rectangle(330, 510, 72, 16));
			napisChat.setText("Czat:");
			napisChat.setVisible(false);
		}
		
		return napisChat;
	}

	
	private JTextField getpoleDoPisaniaWiadomosci() {
		if (poleDoPisaniaWiadomosci == null) {
			poleDoPisaniaWiadomosci = new JTextField();
			poleDoPisaniaWiadomosci.setLocation(330, 540);
			poleDoPisaniaWiadomosci.setEnabled(false);
			poleDoPisaniaWiadomosci.setSize(300, 20);
			poleDoPisaniaWiadomosci.setVisible(false);
			poleDoPisaniaWiadomosci.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (!poleDoPisaniaWiadomosci.getText().trim().isEmpty()) {
						Wydarzenia ge = new Wydarzenia(Wydarzenia.C_CHAT_MSG);
						ge.setMessage(poleDoPisaniaWiadomosci.getText().trim());
						sendMessage(ge);
						poleDoPisaniaWiadomosci.setText("");
					}
				}
			});
		}
		
		return poleDoPisaniaWiadomosci;
	}

	
	private JScrollPane getpoleDoWyswietlaniaWiadomosci() {
		if (poleDoWyswietlaniaWiadomosci == null) {
			poleDoWyswietlaniaWiadomosci = new JScrollPane();
			poleDoWyswietlaniaWiadomosci.setLocation(330, 570);	
			poleDoWyswietlaniaWiadomosci.setViewportView(getCzatOdbierz()); 
			poleDoWyswietlaniaWiadomosci.setSize(300, 83);
			poleDoWyswietlaniaWiadomosci.setVisible(false);
		}
		
		return poleDoWyswietlaniaWiadomosci;
	}

	
	private JScrollPane getPoleInformacyjneGraczVsGracz() {
		if (poleInformacyjneGraczVsGracz == null) {
			poleInformacyjneGraczVsGracz = new JScrollPane();
			poleInformacyjneGraczVsGracz.setLocation(20, 545);
			poleInformacyjneGraczVsGracz.setEnabled(true);
			poleInformacyjneGraczVsGracz.setViewportView(getStatusGryZGraczem());
			poleInformacyjneGraczVsGracz.setSize(290, 70);
			poleInformacyjneGraczVsGracz.setVisible(false);
		}
		
		return poleInformacyjneGraczVsGracz;
	}

	
	private JTextArea getStatusGryZGraczem() {
		if (statusGryZGraczem == null) {
			statusGryZGraczem = new JTextArea();
			statusGryZGraczem.setEnabled(true);
			statusGryZGraczem.setEditable(false);
			statusGryZGraczem.setLineWrap(true);
			statusGryZGraczem.setWrapStyleWord(true);
			statusGryZGraczem.setFont(new Font("Dialog", Font.BOLD, 12));
			zmienStatusGryZGraczem("Status gry gracz vs gracz");
		}
		
		return statusGryZGraczem;
	}

	
	private void zmienStatusGryZGraczem(String wiadomosc) {
		Color color;
		color = new Color(255, 255, 196);
		statusGryZGraczem.setBackground(color);
		statusGryZGraczem.setText("");
		statusGryZGraczem.append(wiadomosc);
	}


	private JRadioButton getPrzyciskKlient() {
		if (przyciskKlient == null) {
			przyciskKlient = new JRadioButton();
			przyciskKlient.setLocation(20, 620);
			przyciskKlient.setText("klient");
			przyciskKlient.setSize(70, 20);
			przyciskKlient.setVisible(false);
			przyciskKlient.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					start.setVisible(false);
					polacz.setVisible(true);
					poleDoWpisaniaAdresu.setVisible(true);
				}
			});
		}
		
		return przyciskKlient;
	}


	private JRadioButton getPrzyciskSerwer() {
		if (przyciskSerwer == null) {
			przyciskSerwer = new JRadioButton();
			przyciskSerwer.setLocation(20, 640);
			przyciskSerwer.setSelected(true);
			przyciskSerwer.setText("serwer");
			przyciskSerwer.setVisible(false);
			przyciskSerwer.setSize(70, 20);
			przyciskSerwer.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					start.setVisible(true);
					polacz.setVisible(false);
					poleDoWpisaniaAdresu.setVisible(false);
				}
			});
		}
		
		return przyciskSerwer;
	}


	private JButton getStart() {
		if (start == null) {
			start = new JButton();
			start.setBounds(new Rectangle(120, 628, 80, 25));
			start.setText("Start");
			start.setVisible(false);
			start.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					start.setEnabled(false);
					if (server == null || !server.serwerUruchomiony()) {
						przyciskSerwer.setEnabled(false);
						przyciskKlient.setEnabled(false);
						String host = "localhost";
						server = new Server(port);
						if (server.start()) {
							client = new Client(getID(), host, port);
							if (client.start()) {
								Wydarzenia ge = new Wydarzenia(Wydarzenia.C_LOGIN);
								sendMessage(ge);
							}
							zmienStatusGryZGraczem("Serwer pomyœlnie uruchomiony!");
							poleDoPisaniaWiadomosci.setEnabled(true);
							wroc.setEnabled(false);
							start.setText("Stop");
						} else {
							przyciskSerwer.setEnabled(true);
							przyciskKlient.setEnabled(true);
							zmienStatusGryZGraczem("Nie uda³o sie uruchoniæ serwera!\n");
						}
					} else {
						zmienStatusGryZGraczem("Serwer zatrzymany!\n");
						if (server != null)
							server.stop();
						server = null;
						ustawOdNowa();
					}
					start.setEnabled(true);
					
					
				}
			});
		}
		
		return start;
	}

	
	private JButton getPolacz() {
		if (polacz == null) {
			polacz = new JButton();
			polacz.setBounds(new Rectangle(230, 630, 80, 25));
			polacz.setText("Po³¹cz");
			polacz.setVisible(false);
			polacz.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					polacz.setEnabled(false);
					poleDoWpisaniaAdresu.setEnabled(false);
					if (client == null || !client.isAlive()) {
						przyciskSerwer.setEnabled(false);
						przyciskKlient.setEnabled(false);
						String host = poleDoWpisaniaAdresu.getText().trim();
						client = new Client(getID(), host, port);
						if (client.start()) {
							Wydarzenia ge = new Wydarzenia(Wydarzenia.C_LOGIN);
							sendMessage(ge);
							zmienStatusGryZGraczem("Pomyœlnie po³¹czono siê z serwerem!");
							poleDoPisaniaWiadomosci.setEnabled(true);
							polacz.setText("Roz³¹cz");
							wroc.setEnabled(false);
							clientStarted = true;
						} else {
							clientStarted = false;
							przyciskSerwer.setEnabled(true);
							przyciskKlient.setEnabled(true);
							poleDoWpisaniaAdresu.setEnabled(true);
							zmienStatusGryZGraczem("Nie uda³o sie po³¹czyæ z serwerem!");
						}
					} else {
						zmienStatusGryZGraczem("Po³¹czenie przerwane!\n");
						if (client != null) {
							client.stop();
							clientStarted = false;
						}
						client = null;
						ustawOdNowa();
					}
					polacz.setEnabled(true);	
				}
			});
		}
		
		return polacz;
	}

	
	private JTextField getPoleDoWpisaniaAdresu() {
		if (poleDoWpisaniaAdresu == null) {
			poleDoWpisaniaAdresu = new JTextField();
			poleDoWpisaniaAdresu.setLocation(100, 630);
			poleDoWpisaniaAdresu.setSize(120, 25);
			poleDoWpisaniaAdresu.setText("localhost");
			poleDoWpisaniaAdresu.setVisible(false);
		}
		
		return poleDoWpisaniaAdresu;
	}


	private JButton getRozpocznijGre() {
		if (rozpocznijGre == null) {
			rozpocznijGre = new JButton();
			rozpocznijGre.setLocation(20, 510);
			rozpocznijGre.setEnabled(false);
			rozpocznijGre.setText("Rozpocznij grê");
			rozpocznijGre.setVisible(false);
			rozpocznijGre.setSize(130, 25);
			rozpocznijGre.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					
					
				}
			});
		}
		
		return rozpocznijGre;
	}


	
/* Do po³¹czenie klient serwer*/
// Oprócz tego poni¿ej zmiany w przycisku Start, Po³¹cz
	private String getID() {
		return (przyciskSerwer.isSelected()) ? "ID_SERVER" : "ID_CLIENT";
	}
	
	private JTextArea getCzatOdbierz() {
		if (czatOdbierz == null) {
			czatOdbierz = new JTextArea();
			czatOdbierz.setEnabled(true);
			czatOdbierz.setLineWrap(true);
			czatOdbierz.setWrapStyleWord(true);
			czatOdbierz.setEditable(false);
		}
		return czatOdbierz;
	}
	
	public void scrollChatBox() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				czatOdbierz.setCaretPosition(czatOdbierz.getText().length());
			}
		});
	}
	
	private void processMessages() {
		Wydarzenia ge;
		while (client != null && client.isAlive() && (ge = client.receiveMessage()) != null) {
			switch (ge.getType()) {
			case Wydarzenia.SB_CHAT_MSG:
				if (getID().compareTo(ge.getPlayerId()) == 0) {
					czatOdbierz.append("TY > ");
				} else {
					czatOdbierz.append("PRZECIWNIK > ");
				}
				czatOdbierz.append(ge.getMessage() + "\n");
				scrollChatBox();
				break;
				
			case Wydarzenia.SB_LOGIN:
				if (getID().compareTo(ge.getMessage()) != 0) {
					zmienStatusGryZGraczem("Przy³aczy³ siê drugi gracz!");
				}
				break;
			}
		}
	}
	
	private void ustawOdNowa() {
		rozpocznijGre.setText("Rozpocznij grê");
		start.setText("Start");
		polacz.setText("Po³¹cz");
		poleDoPisaniaWiadomosci.setEnabled(false);
		rozpocznijGre.setEnabled(false);
		przyciskSerwer.setEnabled(true);
		przyciskKlient.setEnabled(true);
		poleDoWpisaniaAdresu.setEnabled(true);
		wroc.setEnabled(true);
	}
	
	private void zerwanePolaczenie() {
		if (przyciskKlient.isSelected()) {
			clientStarted = false;
			ustawOdNowa();
			polacz.setEnabled(true);
		} //else {
			//if (!losuj.isEnabled()) {
			//	resetujPlansze();
			//}
			//nowaGra.setText("Rozpocznij grê");
			//nowaGra.setEnabled(false);
			//losuj.setEnabled(true);
		//}
		zmienStatusGryZGraczem("Po³¹czenie zosta³o przerwane!");
	}
	
	
	public boolean sendMessage(Wydarzenia ge) {
		if (client != null && client.isAlive()) {
			ge.setPlayerId(getID());
			client.sendMessage(ge);
			return true;
		} else {
			return false;
		}
	}
	
	//............................PLANSZA.........................
	
	private Plansza getPlansza() {
		if (plansza == null) {
			plansza = new Plansza();
			plansza.setLocation(70,0);
			plansza.setVisible(false);
		}
		return plansza;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() { // invokeLater - wykonywanie asynchronicznie
			public void run() {
				Warcaby warcaby = new Warcaby();
				warcaby.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				warcaby.setVisible(true);
			}
		});
	}
}