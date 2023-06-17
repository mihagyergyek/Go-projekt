package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import logika.Igra;
import logika.Igralec;
import vodja.Vodja;
import vodja.VrstaIgralca;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener {
	
	public Platno platno;
	
	
	private JLabel status;
	private JMenuItem menuClovekClovek, menuClovekRacunalnik, menuRacunalnikClovek, menuRacunalnikRacunalnik;
	private JMenuItem menuBarvaPlosce, menuBarvaCrt, menuBarvaPrvegaIgralca, menuBarvaDrugegaIgralca;
	private JMenuItem menuDebelinaCrt, menuVelikostZetonov, menuVelikostPolja, menuOdpri, menuShrani, menuPreskoci;
	
	public Okno() {
		super();
		this.setTitle("Igra GO");
		platno = new Platno();
		
		JMenuBar menubar = new JMenuBar();
		this.setJMenuBar(menubar);
		
		JMenu menuNovaIgra = dodajMenu(menubar, "Nova igra");
		JMenu menuNastavitve = dodajMenu(menubar, "Nastavitve");
		JMenu menuIgra = dodajMenu(menubar, "Igra");
		JMenu menuPoteza = dodajMenu(menubar, "Poteza");
		
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
		menuVelikostPolja = dodajMenuItem(menuNastavitve, "Velikost polja...");
		menuOdpri = dodajMenuItem(menuIgra, "Odpri...");
		menuShrani = dodajMenuItem(menuIgra, "Shrani...");
		menuPreskoci = dodajMenuItem(menuPoteza, "Preskoči potezo");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints platno_layout = new GridBagConstraints();
		platno_layout.gridx = 0;
		platno_layout.gridy = 0;
		platno_layout.fill = GridBagConstraints.BOTH;
		platno_layout.weightx = 1.0;
		platno_layout.weighty = 1.0;
		getContentPane().add(platno, platno_layout);
		
		// statusna vrstica za sporočila
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
							    status.getFont().getStyle(),
							    20));
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		getContentPane().add(status, status_layout);
		
		status.setText("Izberite igro!");
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
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.C); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
			Vodja.novaIgra();
		}
		
		else if (objekt == menuClovekRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.C); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
			Vodja.novaIgra();
		}
		
		else if (objekt == menuRacunalnikClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.R); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
			Vodja.novaIgra();
		}
		
		else if (objekt == menuRacunalnikRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.R); 
			Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
			Vodja.novaIgra();
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
				platno.setBackground(barva);
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
		else if (objekt == menuVelikostPolja) {
			String velikostPolja = JOptionPane.showInputDialog(this, "Velikost polja:");
			if (velikostPolja != null && velikostPolja.matches("\\d+")) {
				Vodja.igra = null;
				Igra.N = Integer.parseInt(velikostPolja);
				osveziGUI();
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
		else if (objekt == menuOdpri) {
			JFileChooser dialog = new JFileChooser();
			int izbira = dialog.showOpenDialog(this);
			if (izbira == JFileChooser.APPROVE_OPTION) {
				String ime = dialog.getSelectedFile().getPath();
				Vodja.odpriIgro(ime);
			}
		}
		else if (objekt == menuShrani) {
			JFileChooser dialog = new JFileChooser();
			int izbira = dialog.showOpenDialog(this);
			if (izbira == JFileChooser.APPROVE_OPTION) {
				String ime = dialog.getSelectedFile().getPath();
				Vodja.shrani(ime);
			}
		}
		else if (objekt == menuPreskoci) {
			if (Vodja.igra != null && Vodja.clovekNaVrsti == true) {
				Vodja.igrajClovekovoPotezo(null);
			}
		}
	}
	public void osveziGUI() {
		if (Vodja.igra == null) {
			status.setText("Igra ni v teku.");
		}
		else {
			switch(Vodja.igra.stanjeGo()) {
			case NEODLOCENO: 
				status.setText("Neodločeno!" + " (Črni " + Vodja.igra.rezultat()[0] + " : " + Vodja.igra.rezultat()[1] + " Beli)"); 
				break;
			case V_TEKU: 
				status.setText("Na potezi je " + Vodja.igra.naPotezi() + 
						" - " + Vodja.vrstaIgralca.get(Vodja.igra.naPotezi())); 
				break;
			case ZMAGA_BELI: 
				status.setText("Zmagal je beli igralec! " + 
						"(Črni " + Vodja.igra.rezultat()[0] + " : " + Vodja.igra.rezultat()[1] + " Beli)");
				break;
			case ZMAGA_CRNI: 
				status.setText("Zmagal je črni igralec! " +
						"(Črni " + Vodja.igra.rezultat()[0] + " : " + Vodja.igra.rezultat()[1] + " Beli)");
				break;
			}
		}
		platno.repaint();
	}
}
