package logika;

import java.awt.Point;
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
		if (x+1 < N) {
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
	
	public List<Point> skupina(Point zeton, List<Point> pregledane){
		List<Point> skupina = new LinkedList<Point>();
		skupina.add(zeton);
		pregledane.add(zeton);
		List<Point> sosedi = sosedi(zeton);
		for (Point p : sosedi) {
			if (plosca[zeton.x][zeton.y] == plosca[p.x][p.y] && !pregledane.contains(p)) {
				skupina.add(p);
				pregledane.add(p);
				skupina.addAll(skupina(p, pregledane));
			}
		}
		return skupina;
	}
	
	public List<LinkedList<Point>> skupineNaPlosci(){
		List<Point> pregledane = new LinkedList<Point>();
		List<LinkedList<Point>> skupine = new LinkedList<LinkedList<Point>>();
		for (int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				Point p = new Point(i,j);
				skupine.add((LinkedList<Point>) skupina(p, pregledane));
			}
		}
		return skupine;
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
