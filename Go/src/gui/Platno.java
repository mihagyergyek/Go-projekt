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
	protected double razdaljaMedCrtami;
	protected double v;
	protected double s;
	
	private int zacetniX, stariX, klikX;
	private int zacetniY, stariY, klikY;
	
	public Platno(int sirina, int visina) {
		super();
		setPreferredSize(new Dimension(sirina, visina));
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
		razdaljaMedCrtami = (Math.min(sirina, visina) - 2 * PADDING)  / (Igra.N - 1);
		s = sirina;
		v = visina;
	}
	
	public void nastaviPlosco(Polje[][] plosca) {
		this.plosca = plosca;
		repaint();
	}
	
	private void pobarvajCrno(Graphics2D g2, int i, int j) {
		int x = round(i * razdaljaMedCrtami + PADDING - velikostZetonov / 2);
		int y = round(j * razdaljaMedCrtami + PADDING - velikostZetonov / 2);
		g2.setColor(barvaPrvegaIgralca);
		g2.fillOval(x, y, (int)velikostZetonov, (int)velikostZetonov);
		repaint();
	}
	
	private void pobarvajBelo(Graphics2D g2, int i, int j) {
		int x = round(i * razdaljaMedCrtami + PADDING - velikostZetonov / 2);
		int y = round(j * razdaljaMedCrtami + PADDING - velikostZetonov / 2);
		g2.setColor(barvaDrugegaIgralca);
		g2.fillOval(x, y, (int)velikostZetonov, (int)velikostZetonov);
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(debelinaCrt);
		for (int i = 0; i <= Igra.N; i++) {
			g.drawLine(round(PADDING + i * razdaljaMedCrtami), round(PADDING), round(PADDING + i * razdaljaMedCrtami), round(v - PADDING));
			g.drawLine(round(PADDING), round(PADDING + i * razdaljaMedCrtami), round(s - PADDING), round(PADDING + i * razdaljaMedCrtami));
		}
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
	
	private int round(double x) {
		return (int)(x + 0.5);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (plosca == null) return;
		zacetniX = stariX = klikX = e.getX();
		zacetniY = stariY = klikY = e.getY();
		Point najblizjaTocka = null;
		double najmanjsaRazdalja = 20;
		for (int i = 0; i < Igra.N; i++) {
			for (int j = 0; j < Igra.N; j++) {
				double razdalja = Math.sqrt((PADDING + i * razdaljaMedCrtami - klikX) * (PADDING + i * razdaljaMedCrtami - klikX) + (PADDING + j * razdaljaMedCrtami - klikY) * (PADDING + j * razdaljaMedCrtami - klikY));
				if (razdalja < najmanjsaRazdalja) {
					najmanjsaRazdalja = razdalja;
					Point p = new Point(i, j);
					najblizjaTocka = p;
				}
			}
		}
		if (najblizjaTocka != null && plosca[najblizjaTocka.x][najblizjaTocka.y] == Polje.PRAZNO) plosca[najblizjaTocka.x][najblizjaTocka.y] = Polje.CRNO;
		repaint();
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
