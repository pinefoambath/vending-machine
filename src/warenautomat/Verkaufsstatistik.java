package warenautomat;

import java.time.LocalDate;
import java.util.ArrayList;

public class Verkaufsstatistik {

	private ArrayList<Kaufeintrag> kaufeintraege;

	public Verkaufsstatistik() {
		this.kaufeintraege = new ArrayList<Kaufeintrag>();
	}

	public void addEintrag(Ware ware) {
		LocalDate kaufdatum = SystemSoftware.gibAktuellesDatum();
		Kaufeintrag eintrag = new Kaufeintrag(kaufdatum, ware.gibPreis(), ware.gibName());
		kaufeintraege.add(eintrag);
	}

	public int berechneAnzahlWare(String pName, LocalDate pDatum) {
		int anzahl = 0;
		for (Kaufeintrag kaufeintrag : kaufeintraege) {
			if (kaufeintrag.getKaufdatum().isAfter(pDatum) || kaufeintrag.getKaufdatum().isEqual(pDatum)) {
				if (kaufeintrag.getProduktname().equals(pName)) {
					anzahl++;
				}
			}
		}
		return anzahl;
	}

	public int gibGesamtVerkauf() {
		int gesamtVerkauf = 0;
		for (Kaufeintrag kaufeintrag : kaufeintraege) {
			gesamtVerkauf += kaufeintrag.getKaufpreis();
		}
		return gesamtVerkauf;
	}

	// TEST
	public static void main(String[] args) {
		LocalDate date1 = SystemSoftware.gibAktuellesDatum();
		LocalDate date2 = LocalDate.of(2019, 1, 16);
		LocalDate date3 = LocalDate.of(2010, 1, 12);
		Verkaufsstatistik statistik = new Verkaufsstatistik();
		statistik.kaufeintraege.add(new Kaufeintrag(date1, 360, "Apfelringe"));
		statistik.kaufeintraege.add(new Kaufeintrag(date2, 360, "Apfelringe"));
		statistik.kaufeintraege.add(new Kaufeintrag(date1, 4500, "Glatz: Betriebssysteme"));
		statistik.kaufeintraege.add(new Kaufeintrag(date1, 4500, "Glatz: Betriebssysteme"));
		statistik.kaufeintraege.add(new Kaufeintrag(date2, 440, "Kaegi"));
		statistik.kaufeintraege.add(new Kaufeintrag(date2, 980, "Schinken Pizza"));
		statistik.kaufeintraege.add(new Kaufeintrag(date2, 4500, "Glatz: Betriebssysteme"));
		statistik.kaufeintraege.add(new Kaufeintrag(date3, 4500, "Glatz: Betriebssysteme"));
		for (Kaufeintrag kaufeintrag : statistik.kaufeintraege) {
			System.out.println("Kaufeintrag:");
			System.out.println("  Kaufdatum = " + kaufeintrag.getKaufdatum());
			System.out.println("  Preis  = " + kaufeintrag.getKaufpreis());
			System.out.println("  Produktname = " + kaufeintrag.getProduktname());
		}
		int verkauf = statistik.berechneAnzahlWare("Glatz: Betriebssysteme", date1);
		System.out.println("Anzahl verkaufte Glatz Betriebssysteme Buecher ab heute: " + verkauf);

		verkauf = statistik.berechneAnzahlWare("Glatz: Betriebssysteme", date2);
		System.out.println("Anzahl verkaufte Glatz Betriebssysteme Buecher ab 16/1/2019: " + verkauf);
	}

}
