package vodja;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import gui.Okno;
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
		switch (igra.stanje()) {
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
	
	private static Random random = new Random ();
	
	public static void igrajRacunalnikovoPotezo() {
		Igra zacetekIgre = igra;
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void> () {
			@Override
			protected Void doInBackground() {
				try {TimeUnit.SECONDS.sleep(2);} catch (Exception e) {};	
				return null;
			}
			@Override
			protected void done () {
				if (igra != zacetekIgre) return;
				List<Poteza> moznePoteze = igra.poteze();
				int randomIndex = random.nextInt(moznePoteze.size());
				Poteza poteza = moznePoteze.get(randomIndex);
				igra.odigraj(poteza);
				igramo ();	
			}
		};
		worker.execute();	
	}
	
	public static void igrajClovekovoPotezo(Poteza poteza) {
		if (igra.odigraj(poteza)) {
			clovekNaVrsti = false;
			igramo ();
		}
	}
}
