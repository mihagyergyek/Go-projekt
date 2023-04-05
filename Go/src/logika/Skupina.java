package logika;

import java.util.Arrays;

public class Skupina {
	
	public int[] x;
	public int[] y;
	
	public Skupina (int[] x, int y[]) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "Vrsta [x=" + Arrays.toString(x) + ", y=" + Arrays.toString(y) + "]";
	}
	
}
