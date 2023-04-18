import gui.Okno;
import vodja.Vodja;

public class CaptureGo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Okno glavno_okno = new Okno();
		glavno_okno.pack();
		glavno_okno.setVisible(true);
		Vodja.okno = glavno_okno;
	}

}
