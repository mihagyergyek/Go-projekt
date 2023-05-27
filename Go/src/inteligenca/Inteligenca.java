package inteligenca;

import java.util.List;

import logika.Igra;
import logika.Igralec;
import splosno.KdoIgra;
import splosno.Poteza;

public class Inteligenca extends KdoIgra{
	
	private static final int ZMAGA = 1000000000; // vrednost zmage
	private static final int PORAZ = -ZMAGA;  // vrednost poraza
	private static final int NEODLOC = 0;  // vrednost neodločene igre	
	
	private int globina = 4; //globina pregledovanja
	
	public Inteligenca () {
		super("Miha in Tiana");
	}
	
	public Poteza izberiPotezo(Igra igra) {
		Poteza poteza = alphabetaPoteze(igra, globina, PORAZ, ZMAGA, igra.naPotezi()).poteza;
		return poteza;
	}
	
	public OcenjenaPoteza alphabetaPoteze(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		int ocena;
		// Če sem računalnik, maksimiramo oceno z začetno oceno PORAZ
		// Če sem pa človek, minimiziramo oceno z začetno oceno ZMAGA
		if (igra.naPotezi() == jaz) {ocena = PORAZ;} else {ocena = ZMAGA;}
		List<Poteza> moznePoteze = igra.poteze();
		Poteza kandidat = moznePoteze.get(0);
		for (Poteza p: moznePoteze) {
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigrajGo (p);
			int ocenap;
			switch (kopijaIgre.stanjeGo()) {
			case ZMAGA_CRNI: ocenap = (jaz == Igralec.CRNI ? ZMAGA : PORAZ); break;
			case ZMAGA_BELI: ocenap = (jaz == Igralec.BELI ? ZMAGA : PORAZ); break;
			case NEODLOCENO: ocenap = NEODLOC; break;
			default:
				// Nekdo je na potezi
				if (globina == 1) ocenap = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
				else ocenap = alphabetaPoteze (kopijaIgre, globina-1, alpha, beta, jaz).ocena;
			}
			if (igra.naPotezi() == jaz) { // Maksimiramo oceno
				if (ocenap > ocena) { // mora biti > namesto >=
					ocena = ocenap;
					kandidat = p;
					alpha = Math.max(alpha,ocena);
				}
			} else { // igra.naPotezi() != jaz, torej minimiziramo oceno
				if (ocenap < ocena) { // mora biti < namesto <=
					ocena = ocenap;
					kandidat = p;
					beta = Math.min(beta, ocena);					
				}	
			}
			if (alpha >= beta) // Izstopimo iz "for loop", saj ostale poteze ne pomagajo
				break;
		}
		return new OcenjenaPoteza (kandidat, ocena);
	}
	
}
