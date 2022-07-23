package warenautomat;

import java.time.LocalDate;

public class Drehteller {

	public static final int NR_FAECHER = 16;
	private Fach[] faecher = new Fach[NR_FAECHER];
	private int schiebetuerPosition = 0;

	public Drehteller() {
		for (int i = 0; i < faecher.length; i++) {
			faecher[i] = new Fach();
		}
	}

	public void fuegeWareHinzu(Ware ware) {
		Fach fach = faecher[schiebetuerPosition];
		fach.fuegeWarehinzu(ware);
	}

	public void drehen() {
		schiebetuerPosition = (schiebetuerPosition + 1) % NR_FAECHER;

	}

	public Fach getFachHinterSchiebetuer() {
		return faecher[schiebetuerPosition];
	}

	public Fach[] getFaecher() {
		return faecher;
	}

	// TEST
	public static void main(String[] args) {
		Drehteller drehteller = new Drehteller();
		LocalDate date = LocalDate.of(2023, 5, 12);
		drehteller.fuegeWareHinzu(new Ware("Dinkelmehl", 420, date));
		System.out.println(
				"  Ware in vorderstem Fach ist = " + drehteller.getFachHinterSchiebetuer().gibWare().gibName());
	}

}
