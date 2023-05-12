package logika;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import splosno.Poteza;

public class Igra {
	
	public static final int N = 9;
	private Polje[][] plosca;
	private Igralec naPotezi;
	private DisjointSets<Point> skupine;
	private List<Point> odigrani;
	
	public Igra() {
		// Zacetek nove igre
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		naPotezi = Igralec.CRNI;
		skupine = new FastSets<Point> ();
		odigrani = new LinkedList<Point>();
	}
	
	public Igra(Igra igra) {
		//Kopija igre
		this.plosca = new Polje[N][N];
		this.skupine = new FastSets<Point> ();
		this.odigrani = new LinkedList<Point>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.plosca[i][j] = igra.plosca[i][j];
				if (this.plosca[i][j] != Polje.PRAZNO) {
					Point zeton = new Point(i,j);
					this.odigrani.add(zeton);
					this.skupine.add(zeton);
				}
			}
		}
		this.naPotezi = igra.naPotezi;
		for (Point zeton : this.odigrani) {
			List<Point> sosedi = sosedi(zeton);
			for (Point p : sosedi) {
				if (this.plosca[p.x][p.y] == this.plosca[zeton.x][zeton.y]) this.skupine.union(zeton, p);
			}
		}
	}
	
	
	/**
	 * @param zeton
	 * @return vsi sosedi žetona na plošči
	 */
	public List<Point> sosedi(Point zeton){
		List<Point> sosedi = new LinkedList<Point>();
		int x = zeton.x;
		int y = zeton.y;
		if (x-1 > -1) {
			Point p = new Point(x-1,y);
			sosedi.add(p);
		}
		if (x+1 < N) {
			Point p = new Point(x+1,y);
			sosedi.add(p);
		}
		if (y-1 > -1) {
			Point p = new Point(x,y-1);
			sosedi.add(p);
		}
		if (y+1 < N) {
			Point p = new Point(x,y+1);
			sosedi.add(p);
		}
		return sosedi;
	}
	
	/**
	 * @param zeton
	 * @return vsa prosta polja žetona
	 */
	public Set<Point> liberties(Point zeton){
		//Poiščemo prosta polja žetona
		List<Point> sosedi = sosedi(zeton);
		Set<Point> liberties = new HashSet<Point>();
		for (Point p : sosedi) {
			if (plosca[p.x][p.y] == Polje.PRAZNO) liberties.add(p);
		}
		return liberties;
	}
	
	/**
	 * Žeton doda v pripadajočo skupino
	 * @param zeton
	 */
	public void dodajZetonVSkupino(Point zeton) {
		//Nov žeton dodamo v skupino, ki ji pripada. Po potrebi združimo prej različne skupine
		List<Point> sosedi = sosedi(zeton);
		DisjointSets<Point> stareSkupine = skupine();
		stareSkupine.add(zeton);
		for (Point p : sosedi) {
			if (plosca[p.x][p.y] == plosca[zeton.x][zeton.y]) {
				stareSkupine.union(zeton, p);
			}
		}
		skupine = stareSkupine;
	}
	
	public DisjointSets<Point> skupine() {
		return skupine;
	}
	
	/**
	 * @return slovar vseh skupin, opisanih s predstavnikom in množico njihovih prostih polj
	 */
	public Map<Point, Set<Point>> skupineNaPlosciVseLiberties(){
		Map<Point,Set<Point>> skupineNaPlosci = new HashMap<Point,Set<Point>>();
		for (Point p : odigrani) {
			Point key = skupine.find(p);
			if (!skupineNaPlosci.containsKey(key)) skupineNaPlosci.put(key, liberties(p));
			else {
				Set<Point> skupina = skupineNaPlosci.get(key);
				skupina.addAll(liberties(p));
				skupineNaPlosci.put(key, skupina);
			}
		}
		return skupineNaPlosci;
	}
	
	public Map<Igralec, Integer> ocesa() {
		Map<Igralec, Integer> ocesa = new HashMap<Igralec, Integer>();
		ocesa.put(Igralec.BELI, 0);
		ocesa.put(Igralec.CRNI, 0);
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					Set<Polje> razlicniSosedi = new HashSet<Polje>();
					for (Point p : sosedi(new Point(i, j))) razlicniSosedi.add(plosca[p.x][p.y]);
					if (razlicniSosedi.size() == 1) {
						if (razlicniSosedi.iterator().next() == Polje.BELO) {
							int stevilo = ocesa.get(Igralec.BELI);
							stevilo += 1;
							ocesa.put(Igralec.BELI, stevilo);
						}
						else if (razlicniSosedi.iterator().next() == Polje.CRNO) {
							int stevilo = ocesa.get(Igralec.CRNI);
							stevilo += 1;
							ocesa.put(Igralec.CRNI, stevilo);
						}
					}
				}
			}
		}
		return ocesa;
	}
	
	/**
	 * @param predstavnik skupine
	 * @return lastnik skupine na plošči
	 */
	public Igralec cigavaSkupina(Point predstavnik) {
		if (plosca[predstavnik.x][predstavnik.y] == Polje.BELO || plosca[predstavnik.x][predstavnik.y] == Polje.UJET_BELO) return Igralec.BELI;
		else return Igralec.CRNI;
	}
	
	public void oznaciSkupino(Point predstavnik, Igralec barva) {
		for (Point p : odigrani) {
			if (skupine.find(p) == predstavnik) {
				plosca[p.x][p.y] =  barva == Igralec.CRNI ? Polje.UJET_CRNO : Polje.UJET_BELO;
			}
		}
	}
	
	/**
	 * @return igralca na potezi
	 */
	public Igralec naPotezi () {
		return naPotezi;
	}
	
	/**
	 * @return igralna plosca
	 */
	public Polje[][] getPlosca () {
		return plosca;
	}
	
	/**
	 * @return seznam možnih potez
	 */
	public List<Poteza> poteze() {
		int[][] pregledani = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				pregledani[i][j] = 0;
			}
		}
		LinkedList<Poteza> ps = new LinkedList<Poteza>();
		Map<Point,Set<Point>> skupineNaPlosci = skupineNaPlosciVseLiberties();
		for (Point p : skupineNaPlosci.keySet()) {
			for (Point q : skupineNaPlosci.get(p)) {
				if (pregledani[q.x][q.y] == 0 ) {
					ps.add(new Poteza(q.x,q.y));
					pregledani[q.x][q.y] = 1;
				}
			}
		}
		Collections.shuffle(ps);
		LinkedList<Poteza> ps2 = new LinkedList<Poteza>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO && pregledani[i][j] == 0) {
					ps2.add(new Poteza(i, j));
					pregledani[i][j] = 1;
				}
			}
		}
		Collections.shuffle(ps2);
		ps.addAll(ps2);
		return ps;
	}
	
	/**
	 * @return množico igralcev z ujetimi skupinami
	 */
	public Set<Point> ujeteSkupine() {
		Map<Point, Set<Point>> skupine = skupineNaPlosciVseLiberties();
		Set<Point> ujeteSkupine = new HashSet<Point>();
		for (Point p : skupine.keySet()) {
			if (skupine.get(p).size() == 0) {
				ujeteSkupine.add(p);
			}
		}
		return ujeteSkupine;
	}
	
	/**
	 * @param teritorij
	 * @return lastnika in velikost teritorija
	 */
	public int[] razresiTeritorij(Set<Point> teritorij) {
		int[] resitevTeritorija = new int[2];
		int minX = 9;
		int minY = 9;
		int maxX = 0;
		int maxY = 0;
		Set<Polje> sosediTeritorija = new HashSet<Polje>();
		Set<Point> potencialniUjetniki = new HashSet<Point>();
		//Določimo skrajne meje teritorija
		for (Point p : teritorij) {
			if (p.x < minX) minX = p.x;
			if (p.y < minY) minY = p.y;
			if (p.x > maxX) maxX = p.x;
			if (p.y > maxY) maxY = p.y;
		}
		//Za posamezen stolpec in vrstico določimo žetone, ki teritorij omejujejo
		for (int i = minX; i <= maxX; i++) {
			int minStolpecY = 9;
			int maxStolpecY = -1;
			for (int j = minY; j <= maxY; j++) {
				Point p = new Point(i, j);
				if (teritorij.contains(p)) {
					if (minStolpecY == 9 || j < minStolpecY) minStolpecY = j;
					if (maxStolpecY == -1 || j > maxStolpecY) maxStolpecY = j;
				}
				else potencialniUjetniki.add(p);
			}
			if (minStolpecY > 0) sosediTeritorija.add(plosca[i][minStolpecY - 1]);
			if (maxStolpecY < 8) sosediTeritorija.add(plosca[i][maxStolpecY + 1]);
		}
		for (int j = minY; j <= maxY; j++) {
			int minVrsticaX = 9;
			int maxVrsticaX = -1;
			for (int i = minX; i <= maxX; i++) {
				Point p = new Point(i, j);
				if (teritorij.contains(p)) {
					if (minVrsticaX == 9 || i < minVrsticaX) minVrsticaX = i;
					if (maxVrsticaX == -1 || i > maxVrsticaX) maxVrsticaX = i;
				}
				else potencialniUjetniki.add(p);
			}
			if (minVrsticaX > 0) sosediTeritorija.add(plosca[minVrsticaX - 1][j]);
			if (maxVrsticaX < 8) sosediTeritorija.add(plosca[maxVrsticaX + 1][j]);
		}
		//Če teritorij omejujejo samo žetoni ene barve, ima lastnika in velikost. Nasprotnikove žetone štejemo kot ujetnike
		if (sosediTeritorija.size() == 1) {
			if (sosediTeritorija.iterator().next() == Polje.CRNO) resitevTeritorija[0] = 1;
			else resitevTeritorija[0] = -1;
			int steviloUjetnikov = 0;
			for (Point p : potencialniUjetniki) {
				if (plosca[p.x][p.y] != sosediTeritorija.iterator().next()) {
					odigrani.remove(p);
					steviloUjetnikov += 1;
				}
			}
			resitevTeritorija[1] = teritorij.size() + steviloUjetnikov;
			return resitevTeritorija;
		}
		//Sicer teritorij nima lastnika
		else return new int[] {0, 0};
	}
	
	public Stanje razdelitevTeritorija() {
		DisjointSets<Point> teritorij = new FastSets<Point>();
		Set<Point> prosta = new HashSet<Point>();
		//Poiščemo vsa prazna polja
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					Point p = new Point(i, j);
					prosta.add(p);
					teritorij.add(p);
				}
			}
		}
		//Prazna polja združimo v teritorije
		for (Point p : prosta) {
			for (Point q : sosedi(p)) {
				if (plosca[q.x][q.y] == Polje.PRAZNO) teritorij.union(q, p);
			}
		}
		//Teritorije zapišemo v množice s predstavnikom
		Map<Point,Set<Point>> teritorijiNaPlosci = new HashMap<Point,Set<Point>>();
		for (Point p : prosta) {
			Point key = skupine.find(p);
			if (!teritorijiNaPlosci.containsKey(key)) {
				Set<Point> skupina = new HashSet<Point>();
				skupina.add(p);
				teritorijiNaPlosci.put(key, skupina);
			}
			else {
				Set<Point> skupina = teritorijiNaPlosci.get(key);
				skupina.add(p);
				teritorijiNaPlosci.put(key, skupina);
			}
		}
		//Določimo, koliko teritorija nadzorujeta igralca
		int razlikaVelikostiTeritorijev = 0;
		for (Point p : teritorijiNaPlosci.keySet()) {
			int[] resitev = razresiTeritorij(teritorijiNaPlosci.get(p));
			razlikaVelikostiTeritorijev += resitev[0] * resitev[1];
		}
		for (Point p : odigrani) {
			if (plosca[p.x][p.y] == Polje.CRNO) razlikaVelikostiTeritorijev += 1;
			else razlikaVelikostiTeritorijev -= 1;
		}
		//Določimo zmagovalca
		if (razlikaVelikostiTeritorijev > 0) return Stanje.ZMAGA_CRNI;
		else if (razlikaVelikostiTeritorijev < 0) return Stanje.ZMAGA_BELI;
		else return Stanje.NEODLOCENO;
	}
	
	public Stanje stanje() {
		// Ali imamo zmagovalca?
		Set<Point> ujetiTocke = ujeteSkupine();
		Set<Igralec> ujeti = new HashSet<Igralec>();
		for (Point p : ujetiTocke) ujeti.add(cigavaSkupina(p));
		if (!ujeti.isEmpty()) {
			if (ujeti.contains(Igralec.BELI) && !ujeti.contains(Igralec.CRNI)) {
					for (Point p : ujetiTocke) {
						if (cigavaSkupina(p) == Igralec.BELI) oznaciSkupino(p, Igralec.BELI);
					}
					return Stanje.ZMAGA_CRNI;
				}
			else if (ujeti.contains(Igralec.CRNI) && !ujeti.contains(Igralec.BELI)) {
					for (Point p : ujetiTocke) {
						if (cigavaSkupina(p) == Igralec.CRNI) oznaciSkupino(p, Igralec.CRNI);
					}
					return Stanje.ZMAGA_BELI;
				}
			else if (ujeti.contains(naPotezi.nasprotnik()) && ujeti.contains(naPotezi)) {
				for (Point p : ujetiTocke) {
					if (cigavaSkupina(p) == naPotezi) oznaciSkupino(p, naPotezi);
				}
					return switch(naPotezi) {
					case BELI -> Stanje.ZMAGA_CRNI;
					case CRNI -> Stanje.ZMAGA_BELI;
				};
			}
		}
		// Ali imamo kakšno prazno polje?
		// Če ga imamo, igre ni konec in je nekdo na potezi
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					return Stanje.V_TEKU;
				}
			}
		}
		// Polje je polno, rezultat je neodločen
		return Stanje.NEODLOCENO;
	}
	
	/**
	 * Odigraj potezo p.
	 * 
	 * @param p
	 * @return true, če je bila poteza uspešno odigrana
	 */
	public boolean odigraj(Poteza p) {
		if (plosca[p.x()][p.y()] == Polje.PRAZNO) {
			plosca[p.x()][p.y()] = naPotezi.getPolje();
			Point u = new Point(p.x(), p.y());
			dodajZetonVSkupino(u);
			odigrani.add(u);
			naPotezi = naPotezi.nasprotnik();
			return true;
		}
		else {
			return false;
		}
	}
}
