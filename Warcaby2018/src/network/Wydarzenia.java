package network;

public class Wydarzenia {

	// C_* (Client) - zdarzenia wysy³ane przez klienta
	// S_* (Server) - zdarzenia wysy³ane przez serwer
	// SB_* (Server broadcast) - zdarzenia wysy³ane przez serwer do wszystkich
	// klientów

	// Próba zalogowania przez klienta
	public static final int C_LOGIN = 1001;
	
	/** Logowanie powiod³o siê - wysy³anie informacji do wszystkich graczy */
	public static final int SB_LOGIN = 1003;
	
	/** Próba do³¹czenia do gry przez klienta */
	public static final int C_JOIN_GAME = 1103;

	// Klient wysy³a wiadomoœæ tekstow¹
	public static final int C_CHAT_MSG = 1201;

	// Swerwer przesy³a wiadomoœæ tekstow¹ do wszystkich graczy
	public static final int SB_CHAT_MSG = 1202;

	/** Gracz strzela do przeciwnika */
	public static final int C_SHOT = 1301;

	
	// -----------------------------------------------------

	// Typ zdarzenia
	private int eventType;

	// ID gracza który przes³a³ wiadomoœæ
	private String playerId = "";

	// treœæ wiadomoœci
	private String message;

	public Wydarzenia(int type) {
		setType(type);
	}

	public void setType(int type) {
		eventType = type;
	}

	public int getType() {
		return eventType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String id) {
		playerId = id;
	}
	
	public Wydarzenia(int type, String message) {
		this(type);
		this.message = message;
	}

	public Wydarzenia(String receivedMessage) {
		String x = receivedMessage;
		int idx1 = x.indexOf('|');
		int idx2 = x.indexOf('|', idx1 + 1);
		String a = x.substring(0, idx1);
		String b = x.substring(idx1 + 1, idx2);
		String c = x.substring(idx2 + 1);
		try {
			setType(Integer.parseInt(a));
		} catch (NumberFormatException ex) {
			setType(-1);
		}
		setPlayerId(b);
		setMessage(c);
	}

	public String toSend() {
		String toSend = eventType + "|" + playerId + "|" + getMessage();
		return toSend;
	}
}