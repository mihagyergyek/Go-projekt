package inteligenca;

import logika.Igra;
import splosno.Poteza;

public abstract class Inteligenca extends splosno.KdoIgra{

	public Inteligenca() {
		super("Miha in Tiana");
	}
	
	public abstract Poteza izberiPotezo(Igra igra);

	
}
