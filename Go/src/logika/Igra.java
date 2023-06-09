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
	
	public static int N = 15;
	private Polje[][] plosca;
	private Igralec naPotezi;
	private DisjointSets<Point> skupine;
	private Set<Point> odigrani;
	private int skips;
	private List<Set<Point>> zgodovinaStanj;
	public Set<Point> nadzorujeCrni = new HashSet<Point>();
	public Set<Point> nadzorujeBeli = new HashSet<Point>();
	
	public Igra() {
		// Začetek nove igre
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		naPotezi = Igralec.CRNI;
		skupine = new FastSets<Point> ();
		odigrani = new HashSet<Point>();
		skips = 0;
		zgodovinaStanj = new LinkedList<>();
	}
	
	public Igra(Igra igra) {
		//Kopija igre
		this.plosca = new Polje[N][N];
		this.skupine = new FastSets<Point> ();
		this.odigrani = new HashSet<Point>();
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
		this.skips = igra.skips;
		this.zgodovinaStanj = new LinkedList<>();
		for (Set<Point> stanje : zgodovinaStanj) {
			this.zgodovinaStanj.add(stanje);
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
	
	/**
	 * 
	 * @return skupine žetonov na plošči
	 */
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
	
	/**
	 * poišče polja, ki so obkrožena le z enim igralcem ("očesa") in jim določi lastnika
	 * @return slovar igralcev in število "očes", ki so jih igralci ustvarili na plošči
	 */
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
	 * funkcija določi vrednost, ki predstavlja verjetnost, da bodo polja okoli žetona pripadla igralcu
	 * vpliv z razdaljo eksponentno pada
	 * @param p
	 * @return vpliv žetona na plošči
	 */
	public double vpliv(Point p) {
		double tabela = 0;
		for (int di = -1; di < 2; di++) {
			for (int dj = -1; dj < 2; dj++) {
				if (di + dj == 0 || Math.abs(di + dj) == 2) {
					for (int k = 1; k < 3; k++) {
						if (odigrani.contains(new Point(p.x + k*di, p.y + k*dj))) {
							if (plosca[p.x + k*di][p.y + k*dj] != plosca[p.x][p.y]) break;
						}
						double razdalja = Math.abs(k*di) + Math.abs(k*dj);
						if (razdalja > 0) tabela += 0.5 * 1 / razdalja * 100;
					}
				}
				else {
					for (int k = 1; k < 4; k++) {
						if (odigrani.contains(new Point(p.x + k*di, p.y + k*dj))) {
							if (plosca[p.x + k*di][p.y + k*dj] != plosca[p.x][p.y]) break;
						}
						double razdalja = Math.abs(k*di) + Math.abs(k*dj);
						if (razdalja > 0) tabela += 0.5 * 1 / razdalja * 100;
					}
				}
			}
		}
		return tabela;
	}
	
	/**
	 * izračuna vpliv za vse žetone in jih sešteje
	 * @return oceno razlike vpliva, ki ga imata igralca na plošči
	 */
	public double trenutniVpliv() {
		double skupaj = 0;
		for (Point p : odigrani) {
			if (cigavaSkupina(p) == Igralec.BELI) skupaj -= vpliv(p);
			else skupaj += vpliv(p);
		}
		return skupaj;
	}
	
	/**
	 * @param predstavnik skupine
	 * @return lastnik skupine na plošči
	 */
	public Igralec cigavaSkupina(Point predstavnik) {
		if (plosca[predstavnik.x][predstavnik.y] == Polje.BELO) return Igralec.BELI;
		else return Igralec.CRNI;
	}
	
	/**
	 * @return igralca na potezi
	 */
	public Igralec naPotezi () {
		return naPotezi;
	}
	
	/**
	 * nastavi igralca na potezi (funkcija za shranjevanje)
	 * @param ime
	 */
	public void setNaPotezi(String ime) {
		switch(ime) {
		case "CRNI":
			naPotezi = Igralec.CRNI;
			break;
		case "BELI":
			naPotezi = Igralec.BELI;
			break;
		}
	}
	
	/**
	 * @return igralna plošča
	 */
	public Polje[][] getPlosca () {
		return plosca;
	}
	
	/**
	 * 
	 * @return število zaporednih preskočenih potez
	 */
	public int skips() {
		return skips;
	}
	
	/**
	 * spremeni število preskočenih potez (funkcija za shranjevanje)
	 * @param n
	 */
	public void setSkips(int n) {
		skips = n;
	}
	
	public Set<Point> odigrani() {
		return odigrani;
	}
	
	/**
	 * 
	 * @param zeton
	 * @param navideznaPlosca
	 * @return prosta polja žetona v teoretični potezi
	 */
	public Set<Point> libertiesNavideznaPlosca(Point zeton, Polje[][] navideznaPlosca) {
		List<Point> sosedi = sosedi(zeton);
		Set<Point> liberties = new HashSet<Point>();
		for (Point p : sosedi) {
			if (navideznaPlosca[p.x][p.y] == Polje.PRAZNO) liberties.add(p);
		}
		return liberties;
	}
	
	/**
	 * 
	 * @param p
	 * @return true, če bi poteza ponovila prejšnje stanje na plošči
	 */
	public boolean ponoviStanje(Point p) {
		Set<Point> kopijaOdigranih = new HashSet<Point>();
		for (Point q : odigrani) {
			kopijaOdigranih.add(q);
		}
		kopijaOdigranih.add(p);
		if (zgodovinaStanj.contains(kopijaOdigranih)) return true;
		return false;
	}
	
	/**
	 * 
	 * @param p
	 * @return true, če je poteza ilegalna
	 */
	public boolean ilegalnaPoteza(Point p) {
		if (liberties(p).size() > 0) return false;
		Map<Point, Set<Point>> stareSkupine = skupineNaPlosciVseLiberties();
		//Najprej odpravimo potencialno ujete nasprotnikove skupine
		for (Point skupina : sosedi(p)) {
			if (stareSkupine.get(skupine.find(skupina)).size() == 1 && plosca[skupina.x][skupina.y] == naPotezi.nasprotnik().getPolje()) {
				return false;
			}
		}
		//Sedaj je treba preveriti, ali bi bila poteza samomorilna
		//Kopija trenutne igre
		Polje[][] kopijaPlosce = new Polje[N][N];
		DisjointSets<Point> kopijaSkupin = new FastSets<Point> ();
		List<Point> kopijaOdigranih = new LinkedList<Point>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				kopijaPlosce[i][j] = plosca[i][j];
				if (kopijaPlosce[i][j] != Polje.PRAZNO) {
					Point zeton = new Point(i,j);
					kopijaSkupin.add(zeton);
					kopijaOdigranih.add(zeton);
				}
			}
		}
		kopijaPlosce[p.x][p.y] = naPotezi.getPolje(); 
		kopijaOdigranih.add(p);
		kopijaSkupin.add(p);
		for (Point zeton : kopijaOdigranih) {
			List<Point> sosedi = sosedi(zeton);
			for (Point q : sosedi) {
				if (kopijaPlosce[q.x][q.y] == kopijaPlosce[zeton.x][zeton.y]) kopijaSkupin.union(zeton, q);
			}
		}
		//Skupine na plošči, če bi dodali žeton
		Map<Point,Set<Point>> skupineNaPlosciLiberties = new HashMap<Point,Set<Point>>();
		for (Point u : kopijaOdigranih) {
			Point key = kopijaSkupin.find(u);
			if (!skupineNaPlosciLiberties.containsKey(key)) skupineNaPlosciLiberties.put(key, libertiesNavideznaPlosca(u, kopijaPlosce));
			else {
				Set<Point> skupina = skupineNaPlosciLiberties.get(key);
				skupina.addAll(libertiesNavideznaPlosca(u, kopijaPlosce));
				skupineNaPlosciLiberties.put(key, skupina);
			}
		}
		//Če je skupina položenega žetona ujeta, je ta poteza ilegalna
		if (skupineNaPlosciLiberties.get(kopijaSkupin.find(p)).size() == 0) return true;
		else return false;
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
		List<Poteza> ps = new LinkedList<Poteza>();
		Map<Point,Set<Point>> skupineNaPlosci = skupineNaPlosciVseLiberties();
		for (Point p : skupineNaPlosci.keySet()) {
			for (Point q : skupineNaPlosci.get(p)) {
				if (pregledani[q.x][q.y] == 0 && !ilegalnaPoteza(q) && !ponoviStanje(q)) {
					ps.add(new Poteza(q.x,q.y));
					pregledani[q.x][q.y] = 1;
				}
			}
		}
		Collections.shuffle(ps);
		LinkedList<Poteza> ps2 = new LinkedList<Poteza>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO && pregledani[i][j] == 0 && !ilegalnaPoteza(new Point(i,j)) && !ponoviStanje(new Point(i,j))) {
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
	 * preskočimo potezo
	 */
	public void preskoci() {
		naPotezi = naPotezi.nasprotnik();
		skips += 1;
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
	 * skupine na koncu igre označi za barvanje
	 * @param teritorijiNaPlosci
	 * @return 
	 */
	public void oznaciSkupine(Map<Point,Set<Point>> teritorijiNaPlosci) {
		for (Point p : teritorijiNaPlosci.keySet()) {
			if (razresiTeritorij(teritorijiNaPlosci.get(p))[0] == 1) {
				for (Point q : teritorijiNaPlosci.get(p)) {
					nadzorujeCrni.add(q);
				}
			}
			else if (razresiTeritorij(teritorijiNaPlosci.get(p))[0] == -1) {
				for (Point q : teritorijiNaPlosci.get(p)) {
					nadzorujeBeli.add(q);
				}
			}
		}
	}
	
	/**
	 * 
	 * @return teritoriji na plošči
	 */
	public Map<Point,Set<Point>> teritorijiNaPlosci() {
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
				if (plosca[q.x][q.y] == Polje.PRAZNO) teritorij.union(p, q);
			}
		}
		//Teritorije zapišemo v množice s predstavnikom
		Map<Point,Set<Point>> teritorijiNaPlosci = new HashMap<Point,Set<Point>>();
		for (Point p : prosta) {
			Point key = teritorij.find(p);
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
		return teritorijiNaPlosci;
	}
	
	/**
	 * @param teritorij
	 * @return lastnika in velikost teritorija
	 */
	public int[] razresiTeritorij(Set<Point> teritorij) {
		int[] resitevTeritorija = new int[2];
		Set<Polje> sosediTeritorija = new HashSet<Polje>();
		for (Point p : teritorij) {
			for (Point q : sosedi(p)) {
				if (plosca[q.x][q.y] != Polje.PRAZNO) sosediTeritorija.add(plosca[q.x][q.y]);
			}
		}
		//Če teritorij omejujejo samo žetoni ene barve, ima lastnika in velikost
		if (sosediTeritorija.size() == 1) {
			if (sosediTeritorija.iterator().next() == Polje.CRNO) resitevTeritorija[0] = 1;
			else resitevTeritorija[0] = -1;
			resitevTeritorija[1] = teritorij.size();
			return resitevTeritorija;
		}
		//Sicer teritorij nima lastnika
		else return new int[] {0, 0};
	}
	
	/**
	 * 
	 * @return velikost teritorijev, ki jih igralca nadzorujeta (za statusno vrstico)
	 */
	public int[] rezultat() {
		int[] rezultat = new int[2];
		int crni = 0;
		int beli = 0;
		Map<Point, Set<Point>> teritorijiNaPlosci = teritorijiNaPlosci();
		for (Point p : teritorijiNaPlosci.keySet()) {
			int[] resitev = razresiTeritorij(teritorijiNaPlosci.get(p));
			if (resitev[0] == 1) crni += resitev[1];
			else if (resitev[0] == -1) beli += resitev[1];
		}
		for (Point p : odigrani) {
			if (plosca[p.x][p.y] == Polje.CRNO) {
				crni += 1;
			}
			else {
				beli += 1;
			}
		}
		rezultat[0] = crni;
		rezultat[1] = beli;
		return rezultat;
	}
	
	/**
	 * 
	 * @return zmagovalca igre Go glede na obvladan teritorij
	 */
	public Stanje razdelitevTeritorija() {
		Map<Point, Set<Point>> teritorijiNaPlosci = teritorijiNaPlosci();
		//Določimo, koliko teritorija nadzorujeta igralca
		int razlikaVelikostiTeritorijev = 0;
		for (Point p : teritorijiNaPlosci.keySet()) {
			int[] resitev = razresiTeritorij(teritorijiNaPlosci.get(p));
			razlikaVelikostiTeritorijev += resitev[0] * resitev[1];
		}
		for (Point p : odigrani) {
			if (plosca[p.x][p.y] == Polje.CRNO) {
				razlikaVelikostiTeritorijev += 1;
			}
			else {
				razlikaVelikostiTeritorijev -= 1;
			}
		}
		//Določimo zmagovalca
		oznaciSkupine(teritorijiNaPlosci);
		if (razlikaVelikostiTeritorijev > 0) return Stanje.ZMAGA_CRNI;
		else if (razlikaVelikostiTeritorijev < 0) return Stanje.ZMAGA_BELI;
		else return Stanje.NEODLOCENO;
	}
	
	/**
	 * 
	 * @return stanje igre Go
	 */
	public Stanje stanjeGo() {
		if (skips >= 2 || odigrani.size() >= N * N - 1) return razdelitevTeritorija();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					return Stanje.V_TEKU;
				}
			}
		}
		return Stanje.NEODLOCENO;
	}
	
	/**
	 * Odstrani ujete točke v tej potezi.
	 */
	public void odstraniUjete() {
		Set<Point> ujetiTocke = ujeteSkupine();
		if (ujetiTocke.size() > 0) {
			for (Point p : ujetiTocke) {
				if (cigavaSkupina(p) == naPotezi.nasprotnik()) {
					for (Point q : odigrani) {
						if (skupine.find(q) == p) {
							plosca[q.x][q.y] = Polje.PRAZNO;
						}
					}
				}
			}
			odigrani.clear();
			FastSets<Point> noveSkupine = new FastSets<Point>();
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (plosca[i][j] != Polje.PRAZNO) {
						Point zeton = new Point(i,j);
						noveSkupine.add(zeton);
						odigrani.add(zeton);
					}
				}
			}
			for (Point u : odigrani) {
				List<Point> sosedi = sosedi(u);
				for (Point v : sosedi) {
					if (plosca[v.x][v.y] == plosca[u.x][u.y]) noveSkupine.union(v, u);
				}
			}
			skupine = noveSkupine;
		}
	}
	
	/**
	 * Odigraj potezo p v igri Go
	 * @param p
	 * @return true, če je poteza uspešna
	 */
	public boolean odigrajGo(Poteza p) {
		//prepreči konec igre takoj na začetku
		if (p == null && odigrani.size() > 0) {
			preskoci();
			return true;
		}
		if (p == null) {
			p = poteze().get(0);
			return odigrajGo(p);
		}
		if (plosca[p.x()][p.y()] == Polje.PRAZNO) {
			plosca[p.x()][p.y()] = naPotezi.getPolje();
			Point u = new Point(p.x(), p.y());
			dodajZetonVSkupino(u);
			odigrani.add(u);
			zgodovinaStanj.add(new HashSet<Point>(odigrani));
			if (zgodovinaStanj.size() > 3) zgodovinaStanj.remove(0);
			skips = 0;
			odstraniUjete();
			naPotezi = naPotezi.nasprotnik();
			return true;
		}
		else {
			return false;
		}
	}
	
}