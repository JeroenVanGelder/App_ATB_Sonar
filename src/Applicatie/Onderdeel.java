package Applicatie;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.JOptionPane;

public class Onderdeel{

	private int artikelNummer;
	private int aantal;
	private String onderdeelNaam;
	private File f;
	private FileWriter fw;
	private PrintWriter pw;
	private Scanner sc;

	public Onderdeel(int aN, String nm){
		onderdeelNaam = nm;
		artikelNummer = aN;
		aantal = getVoorraad();
		slaOnderdeelOp(aN, nm); // onderdeel opslaan in een tekstfile
	}

	public int getArtikelNummer(){
		return artikelNummer;
	}

	public void setArtikelNummer(int aN){
		artikelNummer = aN;
	}

	public String getOnderdeelNaam(){
		return onderdeelNaam;
	}

	public void setOnderdeelNaam(String nm){
		onderdeelNaam = nm;
	}

	public void slaOnderdeelOp(int artNr, String ondNm){ // onderdeel opslaan in tekstfile
		try{
			f = new File("Onderdelenvoorraad/onderdelen.txt");
			if(f.exists()){
				sc = new Scanner(f);
			}else{
				fw = new FileWriter("Onderdelenvoorraad/onderdelen.txt");
				pw = new PrintWriter(fw);
				pw.close();
				sc = new Scanner(f);
			}
			while(true){
				String match = "";
				int getalMatch = 99999999;
				if(!sc.hasNext()){
					getalMatch = 99999999;
				}else{
					match = sc.next();
					getalMatch = Integer.parseInt(match);
				}
				if(sc.hasNext()){
					sc.next();
				}
				if(getalMatch == artNr ){
					sc.close();
					return;
				}
				if(!sc.hasNext()){
					sc.close();
					bestandSchrijven(artNr, ondNm);
					return;
				}
			}
		}catch(Exception e){System.out.println(e);}
		sc.close();
		f = null;
	}

	public boolean verwijderOnderdeel(int nr){ // onderdeel verwijderen
		try{
			String aNr = ""+nr;
			File f = new File("Onderdelenvoorraad/onderdelen.txt");
			File f2 = new File("Onderdelenvoorraad/tempFile.txt");
			sc = new Scanner(f);
			fw = new FileWriter(f2);
			pw = new PrintWriter(fw);
			while(sc.hasNext()){ //tijdelijk bestand maken met nieuwe gegevens
				String regel = sc.next();
				if(!regel.equals(aNr)){
					pw.println(regel);
				}else{
					sc.next();
				}
				if(!sc.hasNext()){
					sc.close();
					pw.close();
					f = null;
				}
			}
		}catch(Exception e){}
		sc.close();
		boolean r = hernoemFile(); // als hernoemen gelukt is
		try{
			if(r){
				sc.close();
				pw.close();
				Path path = FileSystems.getDefault().getPath( "Onderdelenvoorraad", "artikel-"+nr+".txt"); // onderdeel bestand verwijderen
				Files.delete(path);
				JOptionPane.showMessageDialog(null, "artikel-"+nr+" is succesvol verwijderd");
			}
		}catch(Exception e){
			System.out.println(e);JOptionPane.showMessageDialog(null, "Er is iets fout gegaan tijdens het verwijderen");
		}
		return r;
	}

	public boolean hernoemFile(){ // hier hernoemen we het hierboven aangemaakte tijdelijke bestand
		try{
			sc.close();
			pw.close();
			Path p = FileSystems.getDefault().getPath( "Onderdelenvoorraad", "onderdelen.txt");
			Files.delete(p); // Oude bestand weggooien
			File file = new File("Onderdelenvoorraad/tempFile.txt");  // File (or directory) with old name
			File file2 = new File("Onderdelenvoorraad/onderdelen.txt");  // File (or directory) with new name
			if(file2.exists()){ // controleren of het weggooien geslaagd is voordat we verder gaan
				//bestand bestaat nog
				return false;
			}else{
				//bestand bestaat niet
			}
			boolean success = file.renameTo(file2);  // Hernoemen van het tijdelijke bestand
			if (!success) {
				//Hernoemen mislukt
				return false;
			}else{
				//Hernoemen gelukt
				return true;
			}
		}catch(Exception e){
			System.out.println(e);JOptionPane.showMessageDialog(null, "Er is iets fout gegaan tijdens het verwijderen");
		}
		return false;
	}

	public void bestandSchrijven(int artNr, String ondNm){ // onderdelen opslaan in het bestand onderdelen.txt
		try{
			f = new File("Onderdelenvoorraad/onderdelen.txt");
			fw = new FileWriter(f,true);
			pw = new PrintWriter(fw);
			sc = new Scanner(f);
			pw.println(artNr);
			pw.println(ondNm);
			sc.close();
			pw.close();
			f = null;
		}catch(Exception e){System.out.println(e);}
	}

	public int getVoorraad(){ // voorraad van een onderdeel ophalen
		try{
			f = new File("Onderdelenvoorraad/artikel-"+getArtikelNummer()+".txt");
			if(f.length() == 0 || !f.exists()){
				setVoorraad(10); //de eerste keer als er nog geen databestand is een standaarwaarde instellen
			}
			if (f.exists() && f.isFile()) { // anders de waarde ophalen uit het databestand
				Scanner sc = new Scanner(f);
				int gelezenAantal = Integer.parseInt(sc.next());
				aantal = gelezenAantal;
				sc.close();
			}
		}catch(Exception e){System.out.println(e);}
		return aantal;
	}

	public void setVoorraad(int aT){ // voorraad instellen
		aantal = aT;
		try{
			fw = new FileWriter("Onderdelenvoorraad/artikel-"+getArtikelNummer()+".txt");
			pw = new PrintWriter(fw);
			pw.println(aT);
			pw.close();
			aantal = aT;
			f = new File("Bestellingen/besteld-artikel-"+getArtikelNummer()+".txt");
			if(f.delete()){
				System.out.println("Onderdelen bestelling ontvangen");
			}
		}catch(Exception e){System.out.println(e);}
	}

	public void gebruikOnderdeel(){ // een onderdeel gebruiken
		aantal = getVoorraad();
		if(aantal > 0){
			aantal--;
			try{
				setVoorraad(aantal);
			}catch(Exception e){System.out.println(e);}
			stuurBestelling();
		}
	}

	public void stuurBestelling(){ // bestelling verzenden naar leverancier en order bestand aanmaken
		int voorraad = getVoorraad();
		if(voorraad < 2){
			f = new File("Bestellingen/artikel-"+getArtikelNummer()+".txt");
			if(!f.exists()) {
				try{
					fw = new FileWriter(f);
					pw = new PrintWriter(fw);
					pw.println(10-getVoorraad());
					pw.close();
				}catch(Exception e){System.out.println(e);}
				System.out.println("Onderdelen zijn bijna op. Bericht naar leverancier wordt nu verzonden\n");
				String s = "Ik zou graag "+(10-voorraad)+"  onderdelen bestellen.\nHet artikelnummer is "+getArtikelNummer();
				System.out.println(s);
			}
		}
	}
}


