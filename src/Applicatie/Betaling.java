package Applicatie;

public class Betaling {
	private Factuur deFactuur;
	private String datum;
	public String getBetalingsInformatie(Onderdeel a, int b) { 
		String s = "Deze betaling is van " + datum +"\nHet betreft een " + a +".\nDe prijs is " + b + " euro.\nDe klant is " + deFactuur.getFactuurhouder().getNaam() + ".\n\nMet Vriendelijke groet ATD";
		return s;

		}
}

