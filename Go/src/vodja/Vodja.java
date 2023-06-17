package vodja;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.SwingWorker;

import gui.Okno;
import inteligenca.Inteligenca;
import logika.Igra;
import logika.Igralec;
import logika.Polje;
import splosno.Poteza;

public class Vodja {
	
	public static Map<Igralec,VrstaIgralca> vrstaIgralca;
	
	public static Okno okno;
	
	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
	
	public static void novaIgra () {
		igra = new Igra ();
		igramo ();
	}
	
	public static void shrani(String izhod) {
		//Kopija trenutne igre
		try {
			PrintWriter dat = new PrintWriter(new FileWriter(izhod));
			dat.println(Igra.N);
			dat.println(Vodja.vrstaIgralca.get(Igralec.CRNI));
			dat.println(Vodja.vrstaIgralca.get(Igralec.BELI));
			dat.println(igra.naPotezi());
			dat.println(igra.skips());
			for (int i = 0; i < Igra.N; i++) {
				for (int j = 0; j < Igra.N; j++) {
					dat.println(igra.getPlosca()[i][j]);
				}
			}
			dat.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void odpriIgro(String vhod) {
		try {
			BufferedReader dat = new BufferedReader(new FileReader(vhod));
			Igra.N = Integer.parseInt(dat.readLine().strip());
			igra = new Igra();
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			//Kdo je igral
			switch (dat.readLine().strip()) {
			case "človek":
				Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.C);
				break;
			case "računalnik":
				Vodja.vrstaIgralca.put(Igralec.CRNI, VrstaIgralca.R);
				break;
			}
			switch (dat.readLine().strip()) {
			case "človek":
				Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.C);
				break;
			case "računalnik":
				Vodja.vrstaIgralca.put(Igralec.BELI, VrstaIgralca.R);
				break;
			}
			//Kdo je bil na potezi
			switch (dat.readLine().strip()) {
			case "Crni":
				Vodja.igra.setNaPotezi("CRNI");
				break;
			case "Beli":
				Vodja.igra.setNaPotezi("BELI");
				break;
			}
			//Kakšna je bila plošča
			igra.setSkips(Integer.parseInt(dat.readLine().strip()));
			for (int i = 0; i < Igra.N; i++) {
				for (int j = 0; j < Igra.N; j++) {
					switch (dat.readLine().strip()) {
					case "PRAZNO":
						Vodja.igra.getPlosca()[i][j] = Polje.PRAZNO;
						break;
					case "CRNO":
						Vodja.igra.getPlosca()[i][j] = Polje.CRNO;
						break;
					case "BELO":
						Vodja.igra.getPlosca()[i][j] = Polje.BELO;
						break;
					}
				}
			}
			dat.close();
			Vodja.igra = new Igra(igra);
			igramo();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void igramo () {
		shrani("trenutna_igra.txt");
		okno.osveziGUI();
		switch (igra.stanjeGo()) {
		case ZMAGA_CRNI: 
		case ZMAGA_BELI: 
		case NEODLOCENO: 
			return; // odhajamo iz metode igramo
		case V_TEKU: 
			Igralec igralec = igra.naPotezi();
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case C: 
				clovekNaVrsti = true;
				break;
			case R:
				igrajRacunalnikovoPotezo ();
				break;
			}
		}
	}
	
	public static Inteligenca racunalnikovaInteligenca = new Inteligenca();
	
	public static void igrajRacunalnikovoPotezo() {
		Igra zacetkaIgra = igra;
		SwingWorker<Poteza, Void> worker = new SwingWorker<Poteza, Void> () {
			@Override
			protected Poteza doInBackground() {
				Poteza poteza = racunalnikovaInteligenca.izberiPotezo(igra);
				return poteza;
			}
			@Override
			protected void done () {
				Poteza poteza = null;
				try {poteza = get();} catch (Exception e) {};
				if (igra == zacetkaIgra) {
					igra.odigrajGo(poteza);
					igramo ();
				}
			}
		};
		worker.execute();
	}
	
	public static void igrajClovekovoPotezo(Poteza poteza) {
		if (igra.odigrajGo(poteza)) {
			clovekNaVrsti = false;
			igramo ();
		}
	}
}
