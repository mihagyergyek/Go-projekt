package gui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import logika.Igra;
import logika.Polje;
import splosno.Poteza;
import vodja.Vodja;

@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener, MouseMotionListener, KeyListener{
	
	protected Polje[][] plosca;
	
	protected Color barvaPlosce;
	protected Color barvaCrt;
	protected Color barvaPrvegaIgralca;
	protected Color barvaDrugegaIgralca;
	protected Stroke debelinaCrt;
	protected double velikostZetonov;
	protected final double PADDING = 50;
	public static final Color BEIGE = new Color(217,179,130);
	
	private int klikX;
	private int klikY;
	
	public Platno() {
		super();
		setBackground(BEIGE);
		barvaPlosce = BEIGE;
		barvaCrt = Color.BLACK;
		barvaPrvegaIgralca = Color.BLACK;
		barvaDrugegaIgralca = Color.WHITE;
		debelinaCrt = new BasicStroke(2);
		velikostZetonov = 70;
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}
	
	private double razdaljaMedCrtami() {
		return (Math.min(getHeight(), getWidth()) - 2 * PADDING)  / (Igra.N - 1);
	}
	
	private double velikostPolja() {
		return Math.min(getHeight(), getWidth());
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(700, 700);
	}
	
	private void pobarvajCrno(Graphics2D g2, int i, int j) {
		int x = round(i * razdaljaMedCrtami() + PADDING - velikostZetonov / 2);
		int y = round(j * razdaljaMedCrtami() + PADDING - velikostZetonov / 2);
		g2.setColor(barvaPrvegaIgralca);
		g2.fillOval(x, y, (int)velikostZetonov, (int)velikostZetonov);
		repaint();
	}
	
	private void pobarvajBelo(Graphics2D g2, int i, int j) {
		int x = round(i * razdaljaMedCrtami() + PADDING - velikostZetonov / 2);
		int y = round(j * razdaljaMedCrtami() + PADDING - velikostZetonov / 2);
		g2.setColor(barvaDrugegaIgralca);
		g2.fillOval(x, y, (int)velikostZetonov, (int)velikostZetonov);
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(debelinaCrt);
		for (int i = 0; i < Igra.N; i++) {
			g.drawLine(round(PADDING + i * razdaljaMedCrtami()), round(PADDING), round(PADDING + i * razdaljaMedCrtami()), round(velikostPolja() - PADDING));
			g.drawLine(round(PADDING), round(PADDING + i * razdaljaMedCrtami()), round(velikostPolja() - PADDING), round(PADDING + i * razdaljaMedCrtami()));
		}
		Polje[][] plosca;;
		if (Vodja.igra != null) {
			plosca = Vodja.igra.getPlosca();
			for (int i = 0; i < Igra.N; i++) {
				for (int j = 0; j < Igra.N; j++) {
					switch(plosca[i][j]) {
					case CRNO: pobarvajCrno(g2, i, j); break;
					case BELO: pobarvajBelo(g2, i, j); break;
					default: break;
					}
				}
			}
		}
	}
	
	private int round(double x) {
		return (int)(x + 0.5);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Polje[][] plosca;;
		if (Vodja.clovekNaVrsti) {
			plosca = Vodja.igra.getPlosca();
			klikX = e.getX();
			klikY = e.getY();
			Point najblizjaTocka = null;
			double najmanjsaRazdalja = 20;
			for (int i = 0; i < Igra.N; i++) {
				for (int j = 0; j < Igra.N; j++) {
					double razdalja = Math.sqrt((PADDING + i * razdaljaMedCrtami() - klikX) * (PADDING + i * razdaljaMedCrtami() - klikX) + (PADDING + j * razdaljaMedCrtami() - klikY) * (PADDING + j * razdaljaMedCrtami() - klikY));
					if (razdalja < najmanjsaRazdalja) {
						najmanjsaRazdalja = razdalja;
						Point p = new Point(i, j);
						najblizjaTocka = p;
					}
				}
			}
			if (najblizjaTocka != null && plosca[najblizjaTocka.x][najblizjaTocka.y] == Polje.PRAZNO) {
					Vodja.igrajClovekovoPotezo(new Poteza(najblizjaTocka.x, najblizjaTocka.y));
				}
			repaint();
		}
	}
	

	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {	
	}

}
