package logika;

import java.awt.Point;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import splosno.Poteza;

public class Igra {
	
	public static final int N = 9;
	
	private Polje[][] plosca;
	
	private Igralec naPotezi;
	
	public Igra() {
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		naPotezi = Igralec.CRNI;
	}
	
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
	
	public List<Point> liberties(Point zeton){
		List<Point> sosedi = sosedi(zeton);
		List<Point> liberties = new LinkedList<Point>();
		for (Point p : sosedi) {
			if (plosca[p.x][p.y] == Polje.PRAZNO) liberties.add(p);
		}
		return liberties;
	}
	
	public Set<Point> skupinaSosednjih(Point zeton){
		if (plosca[zeton.x][zeton.y] == Polje.PRAZNO) return new HashSet<Point>();
		List<Point> sosedi = sosedi(zeton);
		Set<Point> skupinaSosednjih = new HashSet<Point>();
		skupinaSosednjih.add(zeton);
		for (Point p : sosedi) {
			if (plosca[zeton.x][zeton.y] == (plosca[p.x][p.y])) {
				skupinaSosednjih.add(p);
			}
		}
		return skupinaSosednjih;
	}
	
	public Set<Point> skupinaLiberties(Set<Point> skupina) {
		Set<Point> liberties = new HashSet<Point>();
		for (Point p : skupina) {
			liberties.addAll(liberties(p));	
		}
		return liberties;
	}
	
	public List<Set<Point>> mergeSubsets(List<Set<Point>> skupine) {
	    List<Set<Point>> result = new LinkedList<>();
	    for (Set<Point> set : skupine) {
	        // try to find a set in result that intersects this set
	        // if one is found, merge the two.  otherwise, add this set to result
	        result.stream()
	                .filter(x -> !Collections.disjoint(x, set))
	                .findAny()
	                .ifPresentOrElse(   // this method was added in java 9
	                        x -> x.addAll(set),
	                        () -> result.add(new HashSet<>(set))
	                );
	    }
	    // if nothing got merged we are done; otherwise, recurse and try again
	    return result.size() == skupine.size() ? result : mergeSubsets(result);
	}
	
	public List<Set<Point>> skupineNaPlosci(){
		List<Set<Point>> skupine = new LinkedList<Set<Point>>();
		for (int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				Point p = new Point(i,j);
				if (skupinaSosednjih(p).size() > 0) skupine.add(skupinaSosednjih(p));
			}
		}
		List<Set<Point>> zdruzeneSkupine = mergeSubsets(skupine);
		return zdruzeneSkupine;
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
		LinkedList<Poteza> ps = new LinkedList<Poteza>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					ps.add(new Poteza(i, j));
				}
			}
		}
		return ps;
	}
	
	public Set<Igralec> ujeteSkupine() {
		List<Set<Point>> skupine = skupineNaPlosci();
		Set<Igralec> ujeteSkupine = new HashSet<Igralec>();
		for (Set<Point> skupina : skupine) {
			Set<Point> liberties = skupinaLiberties(skupina);
			Point p = skupina.iterator().next();
			if (liberties.size() == 0) {
				if (plosca[p.x][p.y] == Polje.BELO) ujeteSkupine.add(Igralec.BELI);
				else if (plosca[p.x][p.y] == Polje.CRNO) ujeteSkupine.add(Igralec.CRNI);
			}
		}
		System.out.println(ujeteSkupine);
		return ujeteSkupine;
	}
	
	public Stanje stanje() {
		// Ali imamo zmagovalca?
		Set<Igralec> ujeti = ujeteSkupine();
		if (!ujeti.isEmpty()) {
			System.out.println("konec");
			if (ujeti.contains(naPotezi.nasprotnik())) 
				return switch (naPotezi()) {
				case BELI -> Stanje.ZMAGA_BELI;
				case CRNI -> Stanje.ZMAGA_CRNI;
			};
		else if (ujeti.contains(naPotezi) && !ujeti.contains(naPotezi.nasprotnik()))
				return switch(naPotezi()) {
				case BELI -> Stanje.ZMAGA_CRNI;
				case CRNI -> Stanje.ZMAGA_BELI;
				};
		}
		// Ali imamo kakšno prazno polje?
		// Če ga imamo, igre ni konec in je nekdo na potezi
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					System.out.println("v teku");
					return Stanje.V_TEKU;
				}
			}
		}
		// Polje je polno, rezultat je neodločen
		System.out.println("neodloceno");
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
			naPotezi = naPotezi.nasprotnik();
			return true;
		}
		else {
			return false;
		}
	}
}
