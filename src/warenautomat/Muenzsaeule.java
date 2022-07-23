package warenautomat;

public class Muenzsaeule {

	private static final int MAX = 100;
	private int muenzTyp;
	private int muenzAnzahl = 0;

	public Muenzsaeule(int pTyp) {
		this.muenzTyp = pTyp;
	}

	public int saeuleAuffuellenPruefen(int pAnzahl) {
		int geaenderteMenge = 0;
		if (muenzAnzahl + pAnzahl >= 0 && muenzAnzahl + pAnzahl <= 100) {
			return pAnzahl;
		}
		if (muenzAnzahl + pAnzahl > 100) {
			geaenderteMenge = 100 - muenzAnzahl;
			return geaenderteMenge;
		} else {
			geaenderteMenge = -muenzAnzahl;
			return geaenderteMenge;
		}
	}

	public void saeuleAuffuellen(int pAnzahl) {
		muenzAnzahl += pAnzahl;
	}

	public void muenzeZufuegen() {
		muenzAnzahl++;
	}

	public int getMuenzAnzahl() {
		return muenzAnzahl;
	}

	public boolean hatPlatz() {
		if (muenzAnzahl <= MAX) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Muenzsaeule " + muenzTyp + " hat " + muenzAnzahl;
	}

	public int gibMuenzsaeulenWert() {
		return muenzAnzahl * muenzTyp;
	}

	void gibaus(int menge) {
		muenzAnzahl -= menge;
		while (menge > 0) {
			SystemSoftware.auswerfenWechselGeld(Kasse.convertRappenToFranken(muenzTyp));
			menge--;
		}
	}

	public void gibausOhneWechselgeld(int menge) {
		muenzAnzahl -= menge;
	}

	// TEST
	public static void main(String[] args) {
		Muenzsaeule saeule = new Muenzsaeule(20);
		System.out.println(" So viele Muenzen zugefuegt:" + saeule.saeuleAuffuellenPruefen(60));
	}

}
