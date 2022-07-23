package warenautomat;

import java.time.LocalDate;

public class Ware {

	private String name;
	private int preis;
	private LocalDate verfallsDatum;
	private int grenzeBestellung;
	private int anzahlBestellung;
	private int letzterVerkaufbarerBestand;

	public Ware(String name, int preis, LocalDate verfallsDatum) {
		this.name = name;
		this.preis = preis;
		this.verfallsDatum = verfallsDatum;
	}

	public String gibName() {
		return name;
	}

	public int gibPreis() {
		return preis;
	}

	public void setzePreis(int pPreis) {
		preis = pPreis;
	}

	public int gibAnzahlBestellung() {
		return anzahlBestellung;
	}

	public void setzeAnzahlBestellung(int pAnzahlBestellung) {
		anzahlBestellung = pAnzahlBestellung;
	}

	public int gibGrenzeBestellung() {
		return grenzeBestellung;
	}

	public void setzeGrenzeBestellung(int pGrenzeBestellung) {
		grenzeBestellung = pGrenzeBestellung;
	}

	public int gibLetzterVerkaufbarerBestand() {
		return letzterVerkaufbarerBestand;
	}

	public void setzeLetzterVerkaufbarerBestand(int pBestand) {
		letzterVerkaufbarerBestand = pBestand;
	}

	public boolean istAbgelaufen() {
		LocalDate today = SystemSoftware.gibAktuellesDatum();
		if (today.isBefore(verfallsDatum)) {
			return false;
		} else {
			return true;
		}
	}

	public int getZustand() {
		LocalDate today = SystemSoftware.gibAktuellesDatum();
		if (today.isBefore(verfallsDatum)) {
			return 1;
		} else {
			return 2;
		}
	}

	// TEST
	public static void main(String[] args) {
		LocalDate date = SystemSoftware.gibAktuellesDatum();
		Ware ware = new Ware("Snickers", 2, date);
		System.out.println("  Name  = " + ware.name);
		System.out.println("  Preis  = " + ware.preis);
		System.out.println("  Ablaufdatum  = " + ware.verfallsDatum);
	}

}
