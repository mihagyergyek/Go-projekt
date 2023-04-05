package logika;

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
	
	public Polje[][] najdiSkupino(int x, int y, Polje igralec, Polje[][] skupina) {
	    if (plosca[x][y] == igralec && skupina[x][y] == Polje.PRAZNO) {
	        skupina[x][y] = plosca[x][y];
	        if (x+1 < N) najdiSkupino(x+1, y, igralec, skupina);
	        if (x-1 > -1) najdiSkupino(x-1, y, igralec, skupina);
	        if (y+1 < N) najdiSkupino(x, y+1, igralec, skupina);
	        if (y-1 > -1) najdiSkupino(x, y-1, igralec, skupina);
	    }
	    return skupina;
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
