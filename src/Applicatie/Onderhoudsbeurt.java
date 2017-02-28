package Applicatie;

import java.time.LocalDate;

public class Onderhoudsbeurt {
	private LocalDate datum;
	private int volgNummer;
	
	public Onderhoudsbeurt(int vN, LocalDate dt){
		volgNummer = vN;
		datum = dt;
	}
	
	public int getVolgNummer(){
		return volgNummer;
	}
	public LocalDate getDatum(){
		return datum;
	}
}
