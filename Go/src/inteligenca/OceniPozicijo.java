package inteligenca;

import java.awt.Point;
import java.util.Map;
import java.util.Set;

import logika.Igra;
import logika.Igralec;

public class OceniPozicijo {
	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		Map<Igralec, Integer> ocesa = igra.ocesa();
		int vpliv = (int) igra.trenutniVpliv();
		if (jaz == Igralec.BELI) vpliv *= -1;
		int steviloSkupinJaz = 0;
		int steviloOgrozenihJaz = 0;
		int steviloLibertiesJaz = 0;
		int povprecnoLibertiesJaz = 0;
		int steviloSkupinNasprotnik = 0;
		int steviloOgrozenihNasprotnik = 0;
		int steviloLibertiesNasprotnik = 0;
		int povprecnoLibertiesNasprotnik = 0;
		for (Point p : igra.skupineNaPlosciVseLiberties().keySet()) {
			Set<Point> opazovanaSkupina = igra.skupineNaPlosciVseLiberties().get(p);
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
		return -1500 * steviloSkupinJaz -100 * steviloOgrozenihJaz + 200 * steviloLibertiesJaz + 250 * povprecnoLibertiesJaz + 1200 * ocesa.get(jaz)
				+ 1300 * steviloSkupinNasprotnik + 100 * steviloOgrozenihNasprotnik -200 * steviloLibertiesNasprotnik -200 * povprecnoLibertiesNasprotnik - 800 * ocesa.get(jaz.nasprotnik())
				+ 800 * vpliv; 
	}	
	
}