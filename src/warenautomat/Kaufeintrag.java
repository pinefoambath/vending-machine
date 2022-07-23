package warenautomat;

import java.time.LocalDate;

public class Kaufeintrag {
	
	private LocalDate kaufdatum;
	private int kaufpreis;
	private String produktname;
	
	public Kaufeintrag(LocalDate kaufdatum, int kaufpreis, String produktname) {
		this.kaufdatum = kaufdatum;
		this.kaufpreis = kaufpreis;
		this.produktname = produktname;
	}
	
	public LocalDate getKaufdatum() {
		return kaufdatum;
	}
	
	public int getKaufpreis() {
		return kaufpreis;
	}
	
	public String getProduktname() {
		return produktname;
	}
	
	// TEST
    public static void main(String[] args) {
    	LocalDate date = SystemSoftware.gibAktuellesDatum();
        Kaufeintrag kaufeintrag = new Kaufeintrag(date, 360, "Apfelringe");
        System.out.println("  Kaufdatum = " + kaufeintrag.kaufdatum);
        System.out.println("  Preis in Rappen  = " + kaufeintrag.kaufpreis);
        System.out.println("  Produktname = " + kaufeintrag.produktname);
    }

}
