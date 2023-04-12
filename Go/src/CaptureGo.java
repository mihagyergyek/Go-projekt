import gui.Okno;
import logika.Igra;
import logika.Polje;

public class CaptureGo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Polje[][] plosca = new Polje[Igra.N][Igra.N];
		for (int i = 0; i < Igra.N; i++) {
			for (int j = 0; j < Igra.N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		Okno okno = new Okno();
		okno.pack();
		okno.setVisible(true);
		okno.platno.nastaviPlosco(plosca);
	}

}
