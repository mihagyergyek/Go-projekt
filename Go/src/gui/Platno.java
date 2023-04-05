package gui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Platno extends JPanel{
	
	protected Color barvaPlosce;
	protected Color barvaCrt;
	protected Color barvaPrvegaIgralca;
	protected Color barvaDrugegaIgralca;
	protected Stroke debelinaCrt;
	protected double velikostZetonov;
	
	public Platno() {
		super();
		barvaPlosce = Color.YELLOW;
		barvaCrt = Color.BLACK;
		barvaPrvegaIgralca = Color.BLACK;
		barvaDrugegaIgralca = Color.WHITE;
		debelinaCrt = new BasicStroke(2);
		velikostZetonov = 10;
		setFocusable(true);
	}

}
