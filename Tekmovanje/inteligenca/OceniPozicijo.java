package inteligenca;

import java.awt.Point;
import java.util.Map;
import java.util.Set;

import logika.Igra;
import logika.Igralec;

public class OceniPozicijo {
	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		Map<Igralec, Integer> ocesa = igra.ocesa();
		int steviloSkupinJaz = 0;
		int steviloOgrozenihJaz = 0;
		int steviloLibertiesJaz = 0;
		int povprecnoLibertiesJaz = 0;
		int steviloSkupinNasprotnik = 0;
		int steviloOgrozenihNasprotnik = 0;
		int steviloLibertiesNasprotnik = 0;
		int povprecnoLibertiesNasprotnik = 0;
		Map<Point, Set<Point>> liberties = igra.skupineNaPlosciVseLiberties();
		for (Point p : liberties.keySet()) {
			Set<Point> opazovanaSkupina = liberties.get(p);
			if (igra.cigavaSkupina(p) == jaz) {
				steviloSkupinJaz += 1;
				steviloLibertiesJaz += opazovanaSkupina.size();
				if (opazovanaSkupina.size() <= 3) steviloOgrozenihJaz += 1;
			}
			else if (igra.cigavaSkupina(p) != jaz) {
				steviloSkupinNasprotnik += 1;
				steviloLibertiesNasprotnik += opazovanaSkupina.size();
				if (opazovanaSkupina.size() <= 3) steviloOgrozenihNasprotnik += 1;
			}
		}
		povprecnoLibertiesJaz = steviloLibertiesJaz / steviloSkupinJaz;
		povprecnoLibertiesNasprotnik = steviloLibertiesNasprotnik / steviloSkupinNasprotnik;
		return -35 * steviloSkupinJaz -3900 * steviloOgrozenihJaz + 200 * steviloLibertiesJaz + 250 * povprecnoLibertiesJaz + 1200 * ocesa.get(jaz)
				+ 13 * steviloSkupinNasprotnik + 3300 * steviloOgrozenihNasprotnik -220 * steviloLibertiesNasprotnik -290 * povprecnoLibertiesNasprotnik - 1200 * ocesa.get(jaz.nasprotnik()); 
	}	
	
}
