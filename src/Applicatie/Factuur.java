package Applicatie;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Factuur {
	private Klant deFactuurhouder;
	private int aantalKeerVerzonden;
	private boolean isAfbetaald;
	private double bedrag, bedragBetaald;
	private File f;
	private FileWriter fw;
	private PrintWriter pw;
	private Scanner sc;
	private int id;

	private Factuur(Klant k, boolean betaald, double bg, double afbetaald, int aantal, int i) {
		deFactuurhouder = k;
		isAfbetaald = betaald;
		bedrag = bg;
		bedragBetaald = afbetaald;
		aantalKeerVerzonden = aantal;
		id = i;
	}

    public static Factuur createFactuur(Klant k, boolean betaald, double bg, double afbetaald, int aantal, int i) {
        return new Factuur(k, betaald, bg, afbetaald, aantal, i);
    }

    public void schrijfWeg(){
		try{
			f = new File("FactuurApp/Facturen");
			PrintWriter pw = new PrintWriter(new FileWriter(f,true));
			sc = new Scanner(f);
			pw.println(deFactuurhouder.getEmail());
			pw.println(isAfbetaald);
			pw.println(bedrag);
			pw.println(bedragBetaald);
			pw.println(aantalKeerVerzonden);
			pw.println(id);
			sc.close();
			pw.close();
		}catch(Exception e){System.out.println(e);}
	}
	public boolean isAfbetaald() {
		return isAfbetaald;
	}
	public void Afbetaald() {
		isAfbetaald = true;
	}
	public int getAantalkeerverzonden() {
		return aantalKeerVerzonden;
	}

//	public String getFactuurInformatie() {
//	}

	public void verhoogAantalkeerverzonden() {
		aantalKeerVerzonden++;
	}

	public void maakNieuweFactuur() {
	}

	public void addBetaling() {
		// bedragBetaald += bedrag
	}

	public Klant getFactuurhouder() {
		return deFactuurhouder;
	}
	public double getBedrag() {
		return bedrag;
	}
	public double getBedragBetaald() {
		return bedragBetaald;
	}

//	public boolean isAfbetaald() {
//	}
//
//	public int getAantalkeerVerzonden() {
//	}
//
//	public String getFactuurInformatie() {
//	}
//
//	public void verhoogAantalkeerVerzonden() {
//	}
//
//	public void maakNieuweFactuur() {
//	}
//
//	public void addBetaling() {
//	}

}
