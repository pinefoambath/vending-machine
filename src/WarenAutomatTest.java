//==============================================================================
// Project   : Master of Advanced Studies in Software-Engineering 2022
// Modul     : Projektarbeit OO Softwareentwicklung "Warenautomat"
//             Teil: Design&Implementation
// Title     : Test-Applikation
// Author    : `Ihr Name`
// Tab-Width : 2
/*///===========================================================================
* Description: Test-Applikation um die Klassen-Bibliothek des Waren-Automaten 
               zu testen.
$Revision    : 1.17 $  $Date: 2022/06/05 18:23:28 $ 
/*///===========================================================================
//       1         2         3         4         5         6         7         8
//345678901234567890123456789012345678901234567890123456789012345678901234567890
//==============================================================================

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import warenautomat.*;

public class WarenAutomatTest {

	static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static void main(String[] args) {

		{
			Automat automat = new Automat();
			SystemSoftware.erzeugeGUI(automat);
			System.out.println("=== Drehteller-Test: =================================");

			System.out.println("*** Drehteller Nr.1 mit einem \"Eins\" füllen:");
			automat.neueWareVonBarcodeLeser(1, "Eins", 1.00, LocalDate.parse("01.01.2100", FORMATTER));
			System.out.println("*** automat.drehen():");
			automat.drehen();
			System.out.println("*** Drehteller Nr.2 mit einem \"Zwei\" füllen:");
			automat.neueWareVonBarcodeLeser(2, "Zwei", 2.00, LocalDate.parse("01.01.2002", FORMATTER));
			SystemSoftware.output(false);
			System.out.println("*** automat.drehen():");
			automat.drehen();
			SystemSoftware.output(true);
			System.out.println("*** Drehteller Nr.3 mit einem \"Drei\" füllen:");
			automat.neueWareVonBarcodeLeser(3, "Drei", 3.00, LocalDate.parse("01.01.2003", FORMATTER));
			SystemSoftware.output(false);
			System.out.println("*** Drehen bis Fach Nr.16 vor der Öffnung ist:");
			for (int i = 4; i <= 16; i++) {
				automat.drehen();
			}
			SystemSoftware.output(true);
			System.out.println("*** automat.drehen(): jetzt Fach Nr. 1:");
			automat.drehen();
			System.out.println("*** automat.drehen(): jetzt Fach Nr. 2:");
			automat.drehen();
			System.out.println("*** automat.drehen(): jetzt Fach Nr. 3:");
			automat.drehen();

			System.out.println("*** Automat.gibTotalenWarenWert() = " + automat.gibTotalenWarenWert());

			System.out.println("=== Drehteller-Test. =================================");

			System.out.println("=== Oeffnen-Test-Erfolgreich: =====================================");

			System.out.println("*** Drehteller Nr.5 mit einem Mars füllen:");
			automat.neueWareVonBarcodeLeser(5, "Mars", 1.50, LocalDate.parse("01.01.2100", FORMATTER));
			System.out.println("*** Automat.gibTotalenWarenWert() = " + automat.gibTotalenWarenWert());
			Kasse kasse = automat.gibKasse();
			System.out.println("*** kasse.einnehmen(1.00):");
			kasse.einnehmen(1.00);
			System.out.println("*** kasse.einnehmen(0.50):");
			kasse.einnehmen(0.50);
			System.out.println("*** Drehteller Nr.5 Mars erfolgreich kaufen:");
			System.out.println("*** automat.oeffnen(5):");
			System.out.println("*** automat.oeffnen(5): " + automat.oeffnen(5));
			LocalDate heute = SystemSoftware.gibAktuellesDatum();
			System.out.println("*** Gib Verkaufsstatistik Mars: " + automat.gibVerkaufsStatistik("Mars", heute));
			System.out.println("*** Neues Kundenguthaben: " + kasse.gibKundenGuthaben());
			;
			System.out.println(
					"*** Gib Gesamtwert verkaufte Waren (sollte 1.50 sein): " + kasse.gibBetragVerkaufteWaren());
			System.out.println(
					"*** Hat Fach 5 jetzt ware?: " + automat.getDrehteller(4).getFachHinterSchiebetuer().getWare());

			System.out.println("=== Oeffnen-Test-Erfolgreich: =====================================");
			System.out.println(
					"=== Oeffnen-Test-Nicht-Erfolgreich-Nicht-Genug-Geld: =====================================");

			System.out.println("*** Drehteller Nr.5 mit einem Mars füllen:");
			automat.neueWareVonBarcodeLeser(5, "Mars", 1.50, LocalDate.parse("01.01.2100", FORMATTER));
			System.out.println("*** Automat.gibTotalenWarenWert() = " + automat.gibTotalenWarenWert());
			Kasse kasse2 = automat.gibKasse();
			System.out.println("*** Drehteller Nr.5 Mars nicht erfolgreich kaufen:");
			System.out.println("*** automat.oeffnen(5):");
			System.out.println("*** automat.oeffnen(5): " + automat.oeffnen(5));
			LocalDate heute2 = SystemSoftware.gibAktuellesDatum();
			System.out.println("*** Gib Verkaufsstatistik Mars: " + automat.gibVerkaufsStatistik("Mars", heute2));
			System.out.println("*** Neues Kundenguthaben: " + kasse2.gibKundenGuthaben());

			System.out.println(
					"*** Gib Gesamtwert verkaufte Waren (sollte 1.50 sein): " + kasse2.gibBetragVerkaufteWaren());
			System.out.println("*** Hat Fach 5 jetzt ware? (ja): "
					+ automat.getDrehteller(4).getFachHinterSchiebetuer().getWare());
			System.out.println(
					"=== Oeffnen-Test-Nicht-Erfolgreich-Nicht-Genug-Geld: =====================================");
			System.out.println(
					"=== Oeffnen-Test-Nicht-Erfolgreich-Ware-Abgelaufen: =====================================");

			System.out.println("*** Drehteller Nr.5 mit einem Mars füllen:");
			automat.neueWareVonBarcodeLeser(5, "Mars", 1.50, LocalDate.parse("01.01.1978", FORMATTER));
			System.out.println("*** Automat.gibTotalenWarenWert() = " + automat.gibTotalenWarenWert());
			System.out.println("*** Drehteller Nr.5 Mars nicht erfolgreich kaufen:");
			System.out.println("*** automat.oeffnen(5):");
			System.out.println("*** automat.oeffnen(5): " + automat.oeffnen(5));
			System.out.println("*** Gib Verkaufsstatistik Mars: " + automat.gibVerkaufsStatistik("Mars", heute));
			System.out.println("*** Neues Kundenguthaben: " + kasse.gibKundenGuthaben());

			System.out.println(
					"*** Gib Gesamtwert verkaufte Waren (sollte 1.50 sein): " + kasse.gibBetragVerkaufteWaren());
			System.out.println("*** Hat Fach 5 jetzt ware? (ja): "
					+ automat.getDrehteller(4).getFachHinterSchiebetuer().getWare());
			System.out.println(
					"=== Oeffnen-Test-Nicht-Erfolgreich-Ware-Abgelaufen: =====================================");

			System.out.println("=== Wechselgeld Test mit neuem Automat: =====================================");
			automat = new Automat();
			System.out.println("*** Drehteller Nr.5 mit einem Snickers füllen:");
			automat.neueWareVonBarcodeLeser(5, "Snickers", 2.40, LocalDate.parse("01.01.2100", FORMATTER));
			kasse = automat.gibKasse();
			kasse.einnehmen(2.00);
			kasse.einnehmen(1.00);
			System.out.println("*** automat.oeffnen(5): " + automat.oeffnen(5));
			Muenzsaeule saeule20 = kasse.gibSaeule(20);
			Muenzsaeule saeule10 = kasse.gibSaeule(10);
			saeule20.saeuleAuffuellen(2);
			saeule10.saeuleAuffuellen(3);
			System.out.println("*** automat.oeffnen(5): " + automat.oeffnen(5));
			kasse.gibWechselGeld();
			System.out.println("=== Wechselgeld Test mit neuem Automat: =====================================");

			System.out.println("=== Wechselgeld Test 2: =====================================");
			automat = new Automat();
			System.out.println("*** Drehteller Nr.5 mit einem Snickers füllen:");
			automat.neueWareVonBarcodeLeser(5, "Snickers", 2.40, LocalDate.parse("01.01.2100", FORMATTER));
			kasse = automat.gibKasse();
			kasse.einnehmen(2.00);
			kasse.einnehmen(1.00);
			kasse.gibSaeule(50).saeuleAuffuellen(4);
			kasse.gibSaeule(20).saeuleAuffuellen(4);
			System.out.println("*** automat.oeffnen(5): " + automat.oeffnen(5));
			kasse.gibWechselGeld();
			System.out.println("=== Wechselgeld Test 2: =====================================");
			System.out.println("=== Runden Test: =====================================");
			automat = new Automat();
			kasse = automat.gibKasse();
			System.out.println("*** Drehteller Nr.5,  6, 7 mit einem Snickers füllen:");
			automat.neueWareVonBarcodeLeser(5, "Snickers", 1, LocalDate.parse("01.01.2100", FORMATTER));
			automat.neueWareVonBarcodeLeser(6, "Snickers", 2, LocalDate.parse("01.01.2000", FORMATTER));
			automat.neueWareVonBarcodeLeser(7, "Snickers", 3, LocalDate.parse("01.01.2000", FORMATTER));
			System.out.println("*** Automat.gibTotalenWarenWert() = " + automat.gibTotalenWarenWert());

		}

	}

}

/*
 * Session-Log:
 * 
 * === Drehteller-Test: ================================= Drehteller Nr.1 mit
 * einem "Eins" füllen: SystemSoftware::zeigeWarenPreisAn(): 1: 1.0
 * SystemSoftware::zeigeVerfallsDatum(): 1: 2 automat.drehen():
 * SystemSoftware::zeigeWarenPreisAn(): 1: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 1: 0
 * SystemSoftware::zeigeWarenPreisAn(): 2: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 2: 0
 * SystemSoftware::zeigeWarenPreisAn(): 3: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 3: 0
 * SystemSoftware::zeigeWarenPreisAn(): 4: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 4: 0
 * SystemSoftware::zeigeWarenPreisAn(): 5: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 5: 0
 * SystemSoftware::zeigeWarenPreisAn(): 6: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 6: 0
 * SystemSoftware::zeigeWarenPreisAn(): 7: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 7: 0 Drehteller Nr.2 mit einem "Zwei"
 * füllen: SystemSoftware::zeigeWarenPreisAn(): 2: 2.0
 * SystemSoftware::zeigeVerfallsDatum(): 2: 2 SystemSoftware::output(): false
 *** automat.drehen(): SystemSoftware::output(): true Drehteller Nr.3 mit einem
 * "Drei" füllen: SystemSoftware::zeigeWarenPreisAn(): 3: 3.0
 * SystemSoftware::zeigeVerfallsDatum(): 3: 2 SystemSoftware::output(): false
 *** Drehen bis Fach Nr.16 vor der Öffnung ist: SystemSoftware::output(): true
 *** automat.drehen(): jetzt Fach Nr. 1: SystemSoftware::zeigeWarenPreisAn(): 1:
 * 1.0 SystemSoftware::zeigeVerfallsDatum(): 1: 2
 * SystemSoftware::zeigeWarenPreisAn(): 2: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 2: 0
 * SystemSoftware::zeigeWarenPreisAn(): 3: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 3: 0
 * SystemSoftware::zeigeWarenPreisAn(): 4: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 4: 0
 * SystemSoftware::zeigeWarenPreisAn(): 5: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 5: 0
 * SystemSoftware::zeigeWarenPreisAn(): 6: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 6: 0
 * SystemSoftware::zeigeWarenPreisAn(): 7: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 7: 0 automat.drehen(): jetzt Fach Nr.
 * 2: SystemSoftware::zeigeWarenPreisAn(): 1: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 1: 0
 * SystemSoftware::zeigeWarenPreisAn(): 2: 2.0
 * SystemSoftware::zeigeVerfallsDatum(): 2: 2
 * SystemSoftware::zeigeWarenPreisAn(): 3: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 3: 0
 * SystemSoftware::zeigeWarenPreisAn(): 4: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 4: 0
 * SystemSoftware::zeigeWarenPreisAn(): 5: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 5: 0
 * SystemSoftware::zeigeWarenPreisAn(): 6: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 6: 0
 * SystemSoftware::zeigeWarenPreisAn(): 7: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 7: 0 automat.drehen(): jetzt Fach Nr.
 * 3: SystemSoftware::zeigeWarenPreisAn(): 1: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 1: 0
 * SystemSoftware::zeigeWarenPreisAn(): 2: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 2: 0
 * SystemSoftware::zeigeWarenPreisAn(): 3: 3.0
 * SystemSoftware::zeigeVerfallsDatum(): 3: 2
 * SystemSoftware::zeigeWarenPreisAn(): 4: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 4: 0
 * SystemSoftware::zeigeWarenPreisAn(): 5: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 5: 0
 * SystemSoftware::zeigeWarenPreisAn(): 6: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 6: 0
 * SystemSoftware::zeigeWarenPreisAn(): 7: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 7: 0 === Drehteller-Test.
 * ================================= === Öffnen-Test:
 * ===================================== Drehteller Nr.5 mit einem Mars füllen:
 * SystemSoftware::zeigeWarenPreisAn(): 5: 1.5
 * SystemSoftware::zeigeVerfallsDatum(): 5: 1 Automat.gibTotalenWarenWert() =
 * 3.1 kasse.einnehmen(1.00): SystemSoftware::zeigeBetragAn(): 1.0
 *** kasse.einnehmen(0.50): SystemSoftware::zeigeBetragAn(): 1.5
 *** automat.oeffnen(5): Drehteller::oeffnen(): mDrehtellerNr = 5 /
 * mFachVorOeffnung = 3 SystemSoftware::zeigeBetragAn(): 0.0
 * SystemSoftware::entriegeln(): 5 SystemSoftware::zeigeWarenPreisAn(): 5: 0.0
 * SystemSoftware::zeigeVerfallsDatum(): 5: 0 automat.oeffnen(5): true ===
 * Öffnen-Test. =====================================
 * 
 */
