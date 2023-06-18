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
	protected final double PADDING = 60;
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
		velikostZetonov = 0.95;
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
	
	private double velikostZetonov() {
		return velikostZetonov * razdaljaMedCrtami();
	}
	
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(700, 700);
	}
	
	private void pobarvaj(Graphics2D g2, int i, int j, Polje color) {

	    int offsetX = (getWidth() - (int)velikostPolja()) / 2;
	    int offsetY = (getHeight() - (int)velikostPolja()) / 2;

	    int x = offsetX + round(PADDING + i * razdaljaMedCrtami() - velikostZetonov() / 2);
	    int y = offsetY + round(PADDING + j * razdaljaMedCrtami() - velikostZetonov() / 2);


	    switch (color) {
	    case CRNO :
	    	g2.setColor(barvaPrvegaIgralca);
	    	g2.fillOval(x, y, (int)velikostZetonov(), (int)velikostZetonov());
	    	break;
	    case BELO :
	    	g2.setColor(barvaDrugegaIgralca);
	    	g2.fillOval(x, y, (int)velikostZetonov(), (int)velikostZetonov());
	    	break;
	    case PRAZNO :
	    	break;
	    }

	    if (Vodja.igra.nadzorujeBeli.contains(new Point(i, j)) || color == Polje.BELO) {
	        g2.setColor(Color.GREEN);
	        g2.setStroke(new BasicStroke(1));
	        g2.drawOval(x, y, (int)velikostZetonov(), (int)velikostZetonov());
	    }
	    
	    if (Vodja.igra.nadzorujeCrni.contains(new Point(i, j)) || color == Polje.CRNO) {
	        g2.setColor(Color.RED);
	        g2.setStroke(new BasicStroke(1));
	        g2.drawOval(x, y, (int)velikostZetonov(), (int)velikostZetonov());
	    }

	    repaint();
	}


	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setStroke(debelinaCrt);

	    int offsetX = (getWidth() - round(velikostPolja())) / 2;
	    int offsetY = (getHeight() - round(velikostPolja())) / 2;

	    for (int i = 0; i < Igra.N; i++) {
	        g.drawLine(round(offsetX + PADDING + i * razdaljaMedCrtami()), round(offsetY + PADDING),
	                round(offsetX + PADDING + i * razdaljaMedCrtami()), round(offsetY + velikostPolja() - PADDING));
	        g.drawLine(round(offsetX + PADDING), round(offsetY + PADDING + i * razdaljaMedCrtami()),
	                round(offsetX + velikostPolja() - PADDING), round(offsetY + PADDING + i * razdaljaMedCrtami()));
	    }

	    Polje[][] plosca;
	    if (Vodja.igra != null) {
	        plosca = Vodja.igra.getPlosca();
	        for (int i = 0; i < Igra.N; i++) {
	            for (int j = 0; j < Igra.N; j++) {
	            	pobarvaj(g2, i, j, plosca[i][j]);
	            }
	        }
	    }
	}
	
	

	private int round(double x) {
		return (int)(x + 0.5);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	    if (Vodja.clovekNaVrsti) {
	        Polje[][] plosca = Vodja.igra.getPlosca();

	        int offsetX = (getWidth() - (int)velikostPolja()) / 2;
	        int offsetY = (getHeight() - (int)velikostPolja()) / 2;

	        klikX = e.getX() - offsetX;
	        klikY = e.getY() - offsetY;

	        int i = (klikX - round(PADDING) + (int)razdaljaMedCrtami() / 2) / (int)razdaljaMedCrtami();
	        int j = (klikY - round(PADDING) + (int)razdaljaMedCrtami() / 2) / (int)razdaljaMedCrtami();

	        if (i >= 0 && i < Igra.N && j >= 0 && j < Igra.N && plosca[i][j] == Polje.PRAZNO && !Vodja.igra.ilegalnaPoteza(new Point(i,j)) && !Vodja.igra.ponoviStanje(new Point(i,j))) {
	        	Vodja.igrajClovekovoPotezo(new Poteza(i, j));; 
	        }

	        repaint();
	    }
	}
	

	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (Vodja.igra == null) return;
		char tipka = e.getKeyChar();
		if (tipka == ' ' && Vodja.clovekNaVrsti) {
			Vodja.igrajClovekovoPotezo(null);
		}
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