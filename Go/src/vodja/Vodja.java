package vodja;

import java.util.Map;
import javax.swing.SwingWorker;

import gui.Okno;
import inteligenca.Inteligenca;
import logika.Igra;
import logika.Igralec;
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
	
	public static void igramo () {
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
