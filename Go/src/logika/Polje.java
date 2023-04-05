package splosno;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Plosca {
	
	protected Set<Point> polja;
	
	public Plosca(int velikost) {
		polja = new HashSet<Point>();
		for (int i = 0; i < velikost; i++) {
			for (int j = 0; j < velikost; j++) {
				Point tocka = new Point(i, j);
				polja.add(tocka);
			}
		}
	}
	
}
