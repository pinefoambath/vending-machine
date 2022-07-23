package warenautomat;

import java.util.HashMap;
import java.util.Map;

/**
 * Die Kasse verwaltet das eingenommene Geld sowie das Wechselgeld. <br>
 * Die Kasse hat fünf Münz-Säulen für: <br>
 * - 10 Rappen <br>
 * - 20 Rappen <br>
 * - 50 Rappen <br>
 * - 1 Franken <br>
 * - 2 Franken <br>
 */
public class Kasse {
	private static final int NR_MUENZSAEULEN = 5;
	private static final int[] MUENZTYPEN = { 200, 100, 50, 20, 10 };
	private final Map<Muenzsaeule, Integer> wechselgeldStuecke = new HashMap<>();
	private final Automat automat;
	private final Map<Integer, Muenzsaeule> muenzsaeulen = new HashMap<>();
	private final Verkaufsstatistik verkaufsstatistik;
	private Muenzsaeule muenzsaeuleZumAuffuellen;
	private int aufzufuellenderBetrag;
	private int kundenGuthaben;

	/**
	 * Standard-Konstruktor. <br>
	 * Führt die nötigen Initialisierungen durch.
	 */
	public Kasse(Automat pAutomat) {
		automat = pAutomat;
		verkaufsstatistik = new Verkaufsstatistik();
		for (int typ : MUENZTYPEN) {
			muenzsaeulen.put(typ, new Muenzsaeule(typ));
		}
	}

	/**
	 * Diese Methode wird aufgerufen nachdem das Personal beim Verwalten des
	 * Wechselgeldbestand die Münzart und die Anzahl der Münzen über die Tastatur
	 * eingegeben hat (siehe Use-Case "Wechselgeldbestand (Münzbestand) verwalten").
	 * 
	 * @param pMuenzenBetrag Der Betrag der Münzart in Franken.
	 * @param pAnzahl        Die Anzahl der Münzen. Bei der Entnahme von Münzen als
	 *                       entsprechender negativer Wert.
	 * @return Anzahl der Münzen welche hinzugefügt resp. entnommen werden (bei
	 *         Entnahme als negativer Wert). <br>
	 *         Im Normalfall entspricht dieser Wert dem Übergabeparameter
	 *         <code>pAnzahl</code>. <br>
	 *         Er kann kleiner sein falls beim Hinzufügen in der Münzsäule zu wenig
	 *         Platz vorhanden ist oder wenn bei der Entnahme ein grössere Anzahl
	 *         angegeben wurde als tatsächlich in der Münzsäule vorhanden ist. <br>
	 *         Wenn ein nicht unterstützter Münzbetrag übergeben wurde: -200
	 */
	public int verwalteMuenzbestand(double pMuenzenBetrag, int pAnzahl) {
		int muenzTypZumAuffuellen = Kasse.convertFrankenToRappen(pMuenzenBetrag);
		if (gibSaeule(muenzTypZumAuffuellen) == null) {
			muenzsaeuleZumAuffuellen = null;
			aufzufuellenderBetrag = 0;
			return -200;
		} else {
			muenzsaeuleZumAuffuellen = gibSaeule(muenzTypZumAuffuellen);
			aufzufuellenderBetrag = muenzsaeuleZumAuffuellen.saeuleAuffuellenPruefen(pAnzahl);
			return aufzufuellenderBetrag;
		}
	}

	/**
	 * Diese Methode wird aufgerufen nachdem das Personal beim Geldauffüllen den
	 * Knopf "Bestätigen" gedrückt hat (siehe Use-Case "Wechselgeldbestand
	 * (Münzbestand) verwalten"). <br>
	 * Verbucht die Münzen gemäss dem vorangegangenen Aufruf der Methode
	 * <code>verwalteMuenzbestand()</code>.
	 */
	public void verwalteMuenzbestandBestaetigung() {
		muenzsaeuleZumAuffuellen.saeuleAuffuellen(aufzufuellenderBetrag);
		muenzsaeuleZumAuffuellen = null;
		aufzufuellenderBetrag = 0;
	}

	/**
	 * Diese Methode wird aufgerufen wenn ein Kunde eine Münze eingeworfen hat. <br>
	 * Führt den eingenommenen Betrag entsprechend nach. <br>
	 * Stellt den nach dem Einwerfen vorhandenen Betrag im Kassen-Display dar. <br>
	 * Eingenommenes Geld steht sofort als Wechselgeld zur Verfügung. <br>
	 * Die Münzen werden von der Hardware-Kasse auf Falschgeld, Fremdwährung und
	 * nicht unterstützte Münzarten geprüft, d.h. diese Methode wird nur aufgerufen
	 * wenn ein Münzeinwurf soweit erfolgreich war. <br>
	 * Ist die Münzsäule voll (d.h. 100 Münzen waren vor dem Einwurf bereits darin
	 * enthalten), so wird mittels
	 * <code> SystemSoftware.auswerfenWechselGeld() </code> unmittelbar ein
	 * entsprechender Münz-Auswurf ausgeführt. <br>
	 * Hinweis: eine Hardware-Münzsäule hat jeweils effektiv Platz für 101 Münzen.
	 * 
	 * @param pMuenzenBetrag Der Betrag der neu eingeworfenen Münze in Franken.
	 * @return <code> true </code>, wenn er Einwurf erfolgreich war. <br>
	 *         <code> false </code>, wenn Münzsäule bereits voll war.
	 */
	public boolean einnehmen(double pMuenzenBetrag) {
		automat.pruefeBestand();
		int muenzeInRappen = Kasse.convertFrankenToRappen(pMuenzenBetrag);
		Muenzsaeule muenzsaeule = gibSaeule(muenzeInRappen);
		if (muenzsaeule.hatPlatz() == false) {
			SystemSoftware.auswerfenWechselGeld(pMuenzenBetrag);
			return false;
		} else {
			muenzsaeule.muenzeZufuegen();
			kundenGuthaben += muenzeInRappen;
			SystemSoftware.zeigeBetragAn(Kasse.convertRappenToFranken(gibKundenGuthaben()));
			return true;
		}
	}

	public Verkaufsstatistik gibVerkaufsstatistik() {
		return verkaufsstatistik;
	}

	public void kaufeWare(Ware ware) {
		kundenGuthaben -= ware.gibPreis();
		verkaufsstatistik.addEintrag(ware);
	}

	/**
	 * Bewirkt den Auswurf des Restbetrages.
	 */
	public void gibWechselGeld() {
		automat.pruefeBestand();
		berechneWechselgeldStuecke(kundenGuthaben);
		for (Muenzsaeule saeule : wechselgeldStuecke.keySet()) {
			saeule.gibaus(wechselgeldStuecke.get(saeule));
		}
		kundenGuthaben = 0;
		SystemSoftware.zeigeBetragAn(kundenGuthaben);
	}

	/**
	 * Gibt den Gesamtbetrag der bisher verkauften Waren zurück. <br>
	 * Analyse: Abgeleitetes Attribut.
	 * 
	 * @return Gesamtbetrag der bisher verkauften Waren.
	 */
	public double gibBetragVerkaufteWaren() {
		return convertRappenToFranken(verkaufsstatistik.gibGesamtVerkauf());
	}

	public int gibKundenGuthaben() {
		return kundenGuthaben;
	}

	public static int convertFrankenToRappen(double pFranken) {
		return (int) Math.round(pFranken * 100);
	}

	public static double convertRappenToFranken(int pRappen) {
		return pRappen / 100.0;
	}

	public Muenzsaeule gibSaeule(int pTyp) {
		return muenzsaeulen.get(pTyp);
	}

	public boolean berechneWechselgeldStuecke(int betrag) {
		wechselgeldStuecke.clear();
		for (int typ : MUENZTYPEN) {
			betrag = nimmWechselGeld(typ, betrag);
		}
		return betrag == 0;
	}

	private int nimmWechselGeld(int typ, int betrag) {
		Muenzsaeule saeule = gibSaeule(typ);
		int maximalBenoetigteMuenzen = betrag / typ;
		int vorhandeneMuenzanzahl = saeule.getMuenzAnzahl();
		int zuGebendeAnzahl;
		if (maximalBenoetigteMuenzen > vorhandeneMuenzanzahl) {
			zuGebendeAnzahl = vorhandeneMuenzanzahl;
		} else {
			zuGebendeAnzahl = maximalBenoetigteMuenzen;
		}
		wechselgeldStuecke.put(saeule, zuGebendeAnzahl);
		int bereitgestellterBetrag = zuGebendeAnzahl * typ;
		betrag -= bereitgestellterBetrag;
		return betrag;
	}

	// TEST
	public static void main(String[] args) {
		Automat automat = new Automat();
		Kasse kasse = new Kasse(automat);
		for (int typ : MUENZTYPEN) {
			kasse.verwalteMuenzbestand(convertRappenToFranken(typ), 5);
			kasse.verwalteMuenzbestandBestaetigung();
		}
		kasse.berechneWechselgeldStuecke(210);
	}

}
