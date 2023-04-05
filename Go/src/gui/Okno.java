package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener {
	
	protected Platno platno;
	
	private JMenuItem menuClovekClovek, menuClovekRacunalnik, menuRacunalnikClovek, menuRacunalnikRacunalnik;
	private JMenuItem menuBarvaPlosce, menuBarvaCrt, menuBarvaPrvegaIgralca, menuBarvaDrugegaIgralca;
	private JMenuItem menuDebelinaCrt, menuVelikostZetonov;
	
	public Okno() {
		super();
		setTitle("Igra Capture GO");
		//platno = new Platno(800, 800);
		add(platno);
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		JMenu menuNovaIgra = dodajMenu(menubar, "Nova igra");
		JMenu menuNastavitve = dodajMenu(menubar, "Nastavitve");
		
		menuClovekClovek = dodajMenuItem(menuNovaIgra, "Človek proti človeku");
		menuClovekRacunalnik = dodajMenuItem(menuNovaIgra, "Človek proti računalniku");
		menuRacunalnikClovek = dodajMenuItem(menuNovaIgra, "Računalnik proti človeku");
		menuRacunalnikRacunalnik = dodajMenuItem(menuNovaIgra, "Računalnik proti računalniku");
		menuBarvaPrvegaIgralca = dodajMenuItem(menuNastavitve, "Barva prvega igralca ...");
		menuBarvaDrugegaIgralca = dodajMenuItem(menuNastavitve, "Barva drugega igralca ...");
		menuNastavitve.addSeparator();
		menuBarvaPlosce = dodajMenuItem(menuNastavitve, "Barva plošče ...");
		menuBarvaCrt = dodajMenuItem(menuNastavitve, "Barva črt ...");
		menuDebelinaCrt = dodajMenuItem(menuNastavitve, "Debelina crt ...");
		menuVelikostZetonov = dodajMenuItem(menuNastavitve, "Velikost žetonov ...");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private JMenu dodajMenu(JMenuBar menubar, String naslov) {
		JMenu menu = new JMenu(naslov);
		menubar.add(menu);
		return menu;
	}
	
	private JMenuItem dodajMenuItem(JMenu menu, String naslov) {
		JMenuItem menuitem = new JMenuItem(naslov);
		menu.add(menuitem);
		menuitem.addActionListener(this);
		return menuitem;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object objekt = e.getSource();
		if (objekt == menuClovekClovek) {
			
		}
		
		else if (objekt == menuClovekRacunalnik) {
			
		}
		
		else if (objekt == menuRacunalnikClovek) {
			
		}
		
		else if (objekt == menuRacunalnikRacunalnik) {
			
		}
		
		else if (objekt == menuBarvaPrvegaIgralca) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo prvega igralca", platno.barvaPrvegaIgralca);
			if (barva != null) {
				platno.barvaPrvegaIgralca = barva;
				repaint();
			}
		}
		
		else if (objekt == menuBarvaDrugegaIgralca) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo drugega igralca", platno.barvaDrugegaIgralca);
			if (barva != null) {
				platno.barvaDrugegaIgralca = barva;
				repaint();
			}
		}
		
		else if (objekt == menuBarvaPlosce) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo plošče", platno.barvaPlosce);
			if (barva != null) {
				platno.barvaPlosce = barva;
				repaint();
			}
		}
		
		else if (objekt == menuBarvaCrt) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo črt", platno.barvaCrt);
			if (barva != null) {
				platno.barvaCrt = barva;
				repaint();
			}
		}
		
		else if (objekt == menuDebelinaCrt) {
			String debelinaCrt = JOptionPane.showInputDialog(this, "Debelina črt:");
			if (debelinaCrt != null && debelinaCrt.matches("\\d+")) {
				platno.debelinaCrt = new BasicStroke(Integer.parseInt(debelinaCrt));
				repaint();
			}
		}
		
		else if (objekt == menuVelikostZetonov) {
			String velikostZetonov = JOptionPane.showInputDialog(this, "Velikost žetonov:");
			if (velikostZetonov != null && velikostZetonov.matches("\\d+")) {
				platno.velikostZetonov = Integer.parseInt(velikostZetonov);
				repaint();
			}
		}
	}
}