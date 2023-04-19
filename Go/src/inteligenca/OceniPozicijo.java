package inteligenca;

import java.awt.Point;
import java.util.Set;

import logika.Igra;
import logika.Igralec;

public class OceniPozicijo {
	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		int ocena = 0;
		ocena = oceniSteviloSkupinJaz(igra,jaz) + oceniSteviloLibertiesJaz(igra, jaz) + oceniPovprecnoLibertiesJaz(igra, jaz) +
				oceniSteviloSkupinNasprotnik(igra, jaz) + oceniSteviloLibertiesNasprotnik(igra, jaz);
		return ocena;	
	}
	
	public static int oceniSteviloSkupinJaz(Igra igra, Igralec jaz) {
		int stevilo = 0;
		for (Set<Point> skupina : igra.skupineNaPlosci()) {
			if (igra.cigavaSkupina(skupina) == jaz) stevilo += 1;
		}
		return 3 * stevilo;
	}
	
	public static int oceniSteviloLibertiesJaz(Igra igra, Igralec jaz) {
		int stevilo = 0;
		for (Set<Point> skupina : igra.skupineNaPlosci()) {
			if (igra.cigavaSkupina(skupina) == jaz) stevilo += igra.skupinaLiberties(skupina).size();
		}
		return 11 * stevilo;
	}
	
	public static int oceniPovprecnoLibertiesJaz(Igra igra, Igralec jaz) {
		int steviloSkupin = 0;
		int steviloLiberties = 0;
		for (Set<Point> skupina : igra.skupineNaPlosci()) {
			if (igra.cigavaSkupina(skupina) == jaz) {
				steviloLiberties += igra.skupinaLiberties(skupina).size();
				steviloSkupin += 1;
			}
		}
		return 12 * (int)(steviloLiberties / steviloSkupin);
	}
	
	public static int oceniSteviloSkupinNasprotnik(Igra igra, Igralec jaz) {
		int stevilo = 0;
		for (Set<Point> skupina : igra.skupineNaPlosci()) {
			if (igra.cigavaSkupina(skupina) != jaz) stevilo += 1;
		}
		return -3 * stevilo;
	}
	
	public static int oceniSteviloLibertiesNasprotnik(Igra igra, Igralec jaz) {
		int stevilo = 0;
		for (Set<Point> skupina : igra.skupineNaPlosci()) {
			if (igra.cigavaSkupina(skupina) != jaz) stevilo += igra.skupinaLiberties(skupina).size();
		}
		return -10 * stevilo;
	}
	
	public static int oceniPovprecnoLibertiesNasprotnik(Igra igra, Igralec jaz) {
		int steviloSkupin = 0;
		int steviloLiberties = 0;
		for (Set<Point> skupina : igra.skupineNaPlosci()) {
			if (igra.cigavaSkupina(skupina) != jaz) {
				steviloLiberties += igra.skupinaLiberties(skupina).size();
				steviloSkupin += 1;
			}
		}
		return -10 * (int)(steviloLiberties / steviloSkupin);
	}

}
