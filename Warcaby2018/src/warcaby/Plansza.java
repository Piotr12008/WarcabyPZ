package warcaby;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

public class Plansza extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int[][] plansza =   {{ 0, 2, 0, 2, 0, 2, 0, 2 }, 
								{ 2, 0, 2, 0, 2, 0, 2, 0 }, 
								{ 0, 2, 0, 2, 0, 2, 0, 2 },
								{ 1, 0, 1, 0, 1, 0, 1, 0 }, 
								{ 0, 1, 0, 1, 0, 1, 0, 1 }, 
								{ 3, 0, 3, 0, 3, 0, 3, 0 },
								{ 0, 3, 0, 3, 0, 3, 0, 3 }, 
								{ 3, 0, 3, 0, 3, 0, 3, 0 }};

	public int[][] planszaT =  {{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
								{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
								{ 0, 0, 0, 0, 0, 0, 0, 0 },
								{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
								{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
								{ 0, 0, 0, 0, 0, 0, 0, 0 },
								{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
								{ 0, 0, 0, 0, 0, 0, 0, 0 }};
	
	
	public Plansza() {
		
//.......................................
		Dimension rozmiar = new Dimension(460, 460);
		setSize(rozmiar);
	}
	
	
	public void paint(Graphics g) {
		rysujPlansze(g);
	}
	
	public void rysujLinie(Graphics2D g2) {
		for (int j = 0; j < 9; j++) {
			g2.setColor(Color.red);
			g2.fillRect(50, 50, 407, 407);
		}
	}
	
	public void rysujPlansze(Graphics g) {
		Image img = createImage(getSize().width, getSize().height);
		Graphics2D g2 = (Graphics2D) img.getGraphics();
		rysujLinie(g2);

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				switch (plansza[i][j]) {
				case 0:
					g2.setColor(Color.white);
					g2.fillRect(j + 50 + j * 50, i + 50 + i * 50, 50, 50);
					break;
				case 1:
					g2.setColor(Color.black);

					if (planszaT[i][j] == 2)
						g2.setColor(Color.pink);
					if (planszaT[i][j] == 3)
						g2.setColor(Color.yellow);

					g2.fillRect(j + 50 + j * 50, i + 50 + i * 50, 50, 50);
					break;
				case 2:
					g2.setColor(Color.black);
					g2.fillRect(j + 50 + j * 50, i + 50 + i * 50, 50, 50);
					g2.setColor(Color.red);

					if (planszaT[i][j] == 1)
						g2.setColor(Color.pink);

					g2.fillOval(j + 53 + j * 50, i + 53 + i * 50, 44, 44);

					break;
				case 3:
					g2.setColor(Color.black);
					g2.fillRect(j + 50 + j * 50, i + 50 + i * 50, 50, 50);
					g2.setColor(Color.LIGHT_GRAY);

					if (planszaT[i][j] == 4)
						g2.setColor(Color.yellow);

					g2.fillOval(j + 53 + j * 50, i + 53 + i * 50, 44, 44);

					break;
				}
			}
		}

		g.drawImage(img, 0, 0, this);
	}
}
