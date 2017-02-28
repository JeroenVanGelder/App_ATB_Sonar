package Applicatie;

public class Klant {
	private Auto klantAuto;
	private String naam;
	private String email;
	
	public Klant(String nm, String em, Auto a){
		naam = nm;
		email = em;
		klantAuto = a;
	}
	
	public String getNaam(){
		return naam;
	}
	public void setNaam(String nm){
		naam = nm;
	}
	public String getEmail(){
		return email;
	}
	public void setEmail(String em){
		email = em;
	}
	public void setAuto(Auto kA){
		if(klantAuto==null){
			klantAuto = kA;
		} else {
			System.out.println("De klant heeft al een auto!.");
		}
		
	}
	public Auto getAuto(){
		return klantAuto;
	}
	
	public String toString(){
		return naam+"("+email+")";
	}
}
