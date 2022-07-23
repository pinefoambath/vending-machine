package warenautomat;

import java.time.LocalDate;

/**
 * Der Automat besteht aus 7 Drehtellern welche wiederum je aus 16 Fächer
 * bestehen. <br>
 * Der erste Drehteller und das jeweils erste Fach haben jeweils die Nummer 1
 * (nicht 0!). <br>
 * Im Weitern hat der Automat eine Kasse. Diese wird vom Automaten instanziert.
 */
public class Automat {

	private static final int NR_DREHTELLER = 7;
	private Drehteller[] drehtellerGruppe;
	private Kasse kasse;

	/**
	 * Der Standard-Konstruktor. <br>
	 * Führt die nötigen Initialisierungen durch (u.a. wird darin die Kasse
	 * instanziert).
	 */
	public Automat() {
		kasse = new Kasse(this);
		drehtellerGruppe = new Drehteller[NR_DREHTELLER];
		for (int i = 0; i < drehtellerGruppe.length; i++) {
			drehtellerGruppe[i] = new Drehteller();
		}
	}

	/**
	 * Füllt ein Fach mit Ware. <br>
	 * Wenn das Service-Personal den Automaten füllt, wird mit einem Bar-Code-Leser
	 * zuerst die Ware gescannt. <br>
	 * Daraufhin wird die Schiebe-Tür geöffnet. <br>
	 * Das Service-Personal legt die neue Ware ins Fach und schliesst das Fach. <br>
	 * Die Hardware resp. System-Software ruft die Methode
	 * <code> Automat.neueWareVonBarcodeLeser() </code> auf.
	 * 
	 * @param pDrehtellerNr  Der Drehteller bei welchem das Fach hinter der
	 *                       Schiebe-Türe gefüllt wird. <br>
	 *                       Nummerierung beginnt mit 1 (nicht 0)!
	 * @param pWarenName     Der Name der neuen Ware.
	 * @param pPreis         Der Preis der neuen Ware.
	 * @param pVerfallsDatum Das Verfallsdatum der neuen Ware.
	 */
	public void neueWareVonBarcodeLeser(int pDrehtellerNr, String pWarenName, double pPreis, LocalDate pVerfallsDatum) {
		int preis = Kasse.convertFrankenToRappen(pPreis);
		Ware neueWare = new Ware(pWarenName, preis, pVerfallsDatum);
		updatePreisGleicheProdukte(neueWare);
		getDrehteller(pDrehtellerNr - 1).getFachHinterSchiebetuer().fuegeWarehinzu(neueWare);
		SystemSoftware.zeigeWareInGui(pDrehtellerNr, pWarenName, pVerfallsDatum);
		SystemSoftware.zeigeVerfallsDatum(pDrehtellerNr, neueWare.getZustand());
		SystemSoftware.zeigeWarenPreisAn(pDrehtellerNr, Kasse.convertRappenToFranken(neueWare.gibPreis()));
		pruefeBestand();
	}

	public Drehteller getDrehteller(int pDrehtellerNr) {
		return drehtellerGruppe[pDrehtellerNr];
	}

	/**
	 * Gibt die Objekt-Referenz auf die <em> Kasse </em> zurück.
	 */
	public Kasse gibKasse() {
		return kasse;
	}

	/**
	 * Wird von der System-Software jedesmal aufgerufen wenn der gelbe Dreh-Knopf
	 * gedrückt wird. <br>
	 * Die Applikations-Software führt die Drehteller-Anzeigen nach (Warenpreis,
	 * Verfallsdatum). <br>
	 * Das Ansteuern des Drehteller-Motors übernimmt die System-Software (muss nicht
	 * von der Applikations-Software gesteuert werden.). <br>
	 * Die System-Software stellt sicher, dass <em> drehen </em> nicht durchgeführt
	 * wird wenn ein Fach offen ist.
	 */
	public void drehen() {

		for (int drehtellerNr = 0; drehtellerNr < drehtellerGruppe.length; drehtellerNr++) {
			Drehteller drehteller = getDrehteller(drehtellerNr);
			drehteller.drehen();
			Ware ware = drehteller.getFachHinterSchiebetuer().gibWare();
			if (ware == null) {
				SystemSoftware.zeigeVerfallsDatum(drehtellerNr + 1, 0);
				SystemSoftware.zeigeWarenPreisAn(drehtellerNr + 1, 0);
			} else {
				SystemSoftware.zeigeVerfallsDatum(drehtellerNr + 1, ware.getZustand());
				SystemSoftware.zeigeWarenPreisAn(drehtellerNr + 1, kasse.convertRappenToFranken(ware.gibPreis()));
			}
		}
		SystemSoftware.dreheWarenInGui();
		pruefeBestand();

	}

	/**
	 * Beim Versuch eine Schiebetüre zu öffnen ruft die System-Software die Methode
	 * <code> oeffnen() </code> der Klasse <em> Automat </em> mit der
	 * Drehteller-Nummer als Parameter auf. <br>
	 * Es wird überprüft ob alles o.k. ist: <br>
	 * - Fach nicht leer <br>
	 * - Verfallsdatum noch nicht erreicht <br>
	 * - genug Geld eingeworfen <br>
	 * - genug Wechselgeld vorhanden <br>
	 * Wenn nicht genug Geld eingeworfen wurde, wird dies mit
	 * <code> SystemSoftware.zeigeZuWenigGeldAn() </code> signalisiert. <br>
	 * Wenn nicht genug Wechselgeld vorhanden ist wird dies mit
	 * <code> SystemSoftware.zeigeZuWenigWechselGeldAn() </code> signalisiert. <br>
	 * Wenn o.k. wird entriegelt (<code> SystemSoftware.entriegeln() </code>) und
	 * positives Resultat zurückgegeben, sonst negatives Resultat. <br>
	 * Es wird von der System-Software sichergestellt, dass zu einem bestimmten
	 * Zeitpunkt nur eine Schiebetüre offen sein kann.
	 * 
	 * @param pDrehtellerNr Der Drehteller bei welchem versucht wird die
	 *                      Schiebe-Türe zu öffnen. <br>
	 *                      Nummerierung beginnt mit 1 (nicht 0)!
	 * @return Wenn alles o.k. <code> true </code>, sonst <code> false </code>.
	 */
	public boolean oeffnen(int pDrehtellerNr) {

		Fach fach = getDrehteller(pDrehtellerNr - 1).getFachHinterSchiebetuer();
		pruefeBestand();
		Ware ware = fach.gibWare();
		if (ware == null) {
			return false;
		}
		if (ware.istAbgelaufen()) {
			return false;
		}
		if (kasse.gibKundenGuthaben() < ware.gibPreis()) {
			SystemSoftware.zeigeZuWenigGeldAn();
			return false;
		}
		int benoetigtesWechselgeld = kasse.gibKundenGuthaben() - ware.gibPreis();
		if (!kasse.berechneWechselgeldStuecke(benoetigtesWechselgeld)) {
			SystemSoftware.zeigeZuWenigWechselGeldAn();
			return false;
		}

		SystemSoftware.entriegeln(pDrehtellerNr);
		kasse.kaufeWare(ware);
		pruefeBestand();
		SystemSoftware.zeigeWarenPreisAn(pDrehtellerNr, 0);
		SystemSoftware.zeigeVerfallsDatum(pDrehtellerNr, 0);
		SystemSoftware.zeigeBetragAn(Kasse.convertRappenToFranken(kasse.gibKundenGuthaben()));
		fach.fuegeWarehinzu(null);
		SystemSoftware.zeigeWareInGui(pDrehtellerNr, null, null);
		return true;
	}

	/**
	 * Gibt den aktuellen Wert aller im Automaten enthaltenen Waren in Franken
	 * zurück. <br>
	 * Analyse: <br>
	 * Abgeleitetes Attribut. <br>
	 * 
	 * @return Der totale Warenwert des Automaten.
	 */
	public double gibTotalenWarenWert() {
		double aktuellerWarenwert = 0;
		for (Drehteller drehteller : drehtellerGruppe) {
			Fach[] faecher = drehteller.getFaecher();
			for (Fach fach : faecher) {
				aktuellerWarenwert = aktuellerWarenwert + fach.gibWarenWert();
			}
		}
		return aktuellerWarenwert;
	}

	/**
	 * Prueft, wie viele eines Produktypes verkauft werden koennen. Schliesst
	 * abgelaufene Ware aus.
	 * 
	 * @return Anzahl verkaufbarere Produkte eines Produkttypes.
	 */
	private int gibAnzahlVerkaufbareProdukte(String pWarenName) {
		int anzahlVerkaufbareProdukte = 0;
		for (Drehteller drehteller : drehtellerGruppe) {
			Fach[] faecher = drehteller.getFaecher();
			for (Fach fach : faecher) {
				if (fach.istGefuellt()) {
					Ware bestehendesProdukt = fach.gibWare();
					if (bestehendesProdukt.gibName().equals(pWarenName) && !bestehendesProdukt.istAbgelaufen()) {
						anzahlVerkaufbareProdukte++;
					}
				}
			}
		}
		return anzahlVerkaufbareProdukte;
	}

	/**
	 * Diese Funktion aktualisiert den Warenwert, wenn ein weiteres Produkt des
	 * gleichen Typs eingescannt wird. <br>
	 * Dann bekommen alle existierenden Waren des selben Types den aktuellen Preis
	 * des zuletzt eingescannten Produktes.
	 */
	private void updatePreisGleicheProdukte(Ware neueWare) {
		int drehtellerNr = 0;
		for (Drehteller drehteller : drehtellerGruppe) {
			Fach[] faecher = drehteller.getFaecher();
			for (Fach fach : faecher) {
				if (fach.istGefuellt()) {
					Ware bestehendesProdukt = fach.gibWare();
					if (bestehendesProdukt.gibName().equals(neueWare.gibName())) {
						bestehendesProdukt.setzePreis(neueWare.gibPreis());
						if (fach == drehteller.getFachHinterSchiebetuer()) {
							SystemSoftware.zeigeWarenPreisAn(drehtellerNr + 1,
									Kasse.convertRappenToFranken(neueWare.gibPreis()));
						}
					}
				}
			}
			drehtellerNr++;
		}
	}

	/**
	 * Gibt die Anzahl der verkauften Ware <em> pName </em> seit (>=) <em> pDatum
	 * </em> zurück.
	 * 
	 * @param pName  Der Name der Ware nach welcher gesucht werden soll.
	 * @param pDatum Das Datum seit welchem gesucht werden soll.
	 * @return Anzahl verkaufter Waren.
	 */
	public int gibVerkaufsStatistik(String pName, LocalDate pDatum) {
		return kasse.gibVerkaufsstatistik().berechneAnzahlWare(pName, pDatum);
	}

	/**
	 * Konfiguration einer automatischen Bestellung. <br>
	 * Der Automat setzt automatisch Bestellungen ab mittels
	 * <code> SystemSoftware.bestellen() </code> wenn eine Ware ausgeht.
	 * 
	 * @param pWarenName        Warenname derjenigen Ware, für welche eine
	 *                          automatische Bestellung konfiguriert wird.
	 * @param pGrenze           Ab welcher Anzahl von verkaufbarer Ware jeweils eine
	 *                          Bestellung abgesetzt werden soll.
	 * @param pAnzahlBestellung Wieviele neue Waren jeweils bestellt werden sollen.
	 */
	public void konfiguriereBestellung(String pWarenName, int pGrenze, int pAnzahlBestellung) {
		for (Drehteller drehteller : drehtellerGruppe) {
			Fach[] faecher = drehteller.getFaecher();
			for (Fach fach : faecher) {
				if (fach.istGefuellt()) {
					Ware bestehendesProdukt = fach.gibWare();
					if (bestehendesProdukt.gibName().equals(pWarenName)) {
						bestehendesProdukt.setzeAnzahlBestellung(pAnzahlBestellung);
						bestehendesProdukt.setzeGrenzeBestellung(pGrenze);
					}
				}
			}
		}
	}

	/**
	 * Ruft die Funktion pruefeBestellung fuer jedes Fach auf.
	 */
	public void pruefeBestand() {
		for (Drehteller drehteller : drehtellerGruppe) {
			Fach[] faecher = drehteller.getFaecher();
			for (Fach fach : faecher) {
				if (fach.istGefuellt()) {
					Ware bestehendesProdukt = fach.gibWare();
					pruefeBestellung(bestehendesProdukt);
				}
			}
		}
	}

	/**
	 * Prueft den Warenbestand und gibt wenn noetig eine neue Bestellung ab.
	 */
	public void pruefeBestellung(Ware pWare) {
		int verkaufbarerBestand = gibAnzahlVerkaufbareProdukte(pWare.gibName());
		int anzahlBestellung = pWare.gibAnzahlBestellung();
		int grenzeBestellung = pWare.gibGrenzeBestellung();
		if (verkaufbarerBestand <= grenzeBestellung && verkaufbarerBestand != pWare.gibLetzterVerkaufbarerBestand()) {
			SystemSoftware.bestellen(pWare.gibName(), anzahlBestellung, verkaufbarerBestand);
			pWare.setzeLetzterVerkaufbarerBestand(verkaufbarerBestand);
		}
	}

}
