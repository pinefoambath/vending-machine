package warenautomat;

public class Fach {

	private Ware ware;

	public Ware getWare() {
		return ware;
	}

	public void fuegeWarehinzu(Ware ware) {
		this.ware = ware;
	}

	public Ware gibWare() {
		return ware;
	}

	public boolean istGefuellt() {
		return gibWare() != null;
	}

	public double gibWarenWert() {
		if (istGefuellt()) {
			return gibWert();
		} else {
			return 0;
		}
	}

	private double gibWert() {
		if (gibWare().istAbgelaufen()) {
			return discountPreis(gibWare().gibPreis());
		} else {
			return Kasse.convertRappenToFranken((gibWare().gibPreis()));
		}
	}

	private double discountPreis(int pPreis) {
		double preisInFranken = Kasse.convertRappenToFranken(pPreis);
		double discountedValue = preisInFranken * 0.25;
		return round(discountedValue, 1);
	}

	private double round(double value, int decimalPoints) {
		double d = Math.pow(10, decimalPoints);
		return Math.round(value * d) / d;
	}

}
