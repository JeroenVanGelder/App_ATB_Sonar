package Applicatie;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import javax.swing.JOptionPane;

public class Brandstof{

	private String type;
	private int liters;
	private int TSIC;
	private File f;
	private FileWriter fw;
	private PrintWriter pw;
	private Scanner sc;
	private Calendar c = Calendar.getInstance();

	public Brandstof(String tp, int tC){
		type = tp;
		liters = getLiters();
		TSIC = tC;
		slaBrandstofOp(type, TSIC);
	}

	public String getType(){
		return type;
	}

	public void setType(String tp){
		type = tp;
	}

	public int getTSIC(){
		return TSIC;
	}

	public void setTSIC(int t){
		TSIC = t;
	}

	public int getLiters(){ // huidige voorraad ophalen als het databestand niet bestaat of als het de eerste keer is wordt de voorraad ingesteld op 5000 liter
		try{
			f = new File("Brandstofvoorraad/"+type+".txt");
			if(f.length() == 0 ||!f.exists()){
				setLiters(5000);
			}
			if (f.exists() && f.isFile()) {
				Scanner sc = new Scanner(f);
				int gelezenAantal = Integer.parseInt(sc.next());
				sc.close();
				liters = gelezenAantal;
			}
		}catch(Exception e){sc.close(); System.out.print(e);}
		return liters;
	}

	public void slaBrandstofOp(String type, int TSIC){ // nieuwe brandstof opslaan in brandstoffen.txt
		try{
			f = new File("Brandstofvoorraad/brandstoffen.txt");
			if(f.exists()){
				sc = new Scanner(f);
			}else{
				pw = new PrintWriter(new FileWriter(f));
				pw.close();
				sc = new Scanner(f);
				sc.close();
			}
			while(true){  // controleren of het bestand niet al in het brandstoffen.txt staat
				String match = "";
				if(!sc.hasNext()){
					match = "vergelijkmijmaar";
				}else{
					match = sc.next();
				}
				if(match.equals(type) || match.equals(TSIC)){
					sc.close();
					return;
				}
				if(!sc.hasNext()){
					sc.close();
					bestandSchrijven(type, TSIC);
					return;
				}
			}
		}catch(Exception e){System.out.println(e);}
	}

	public boolean verwijderBrandstof(String type){ // brandstof verwijderen
		try{
			File f = new File("Brandstofvoorraad/brandstoffen.txt");
			File f2 = new File("Brandstofvoorraad/tempFile.txt");
			sc = new Scanner(f);
			pw = new PrintWriter(new FileWriter(f2));
			while(sc.hasNext()){ // tijdelijk bestand maken
				String regel = sc.next();
				if(!regel.equals(type)){
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
		boolean r = hernoemFile(); // als hernoemen van tijdelijk bestand is gelukt hier verder
		try{
			if(r){
				f = new File("Brandstoffen/"+type+".txt");
				sc.close();
				pw.close();
				Path path = FileSystems.getDefault().getPath( "Brandstofvoorraad", type+".txt");
				Files.delete(path);
				JOptionPane.showMessageDialog(null, "Brandstof "+type+" is succesvol verwijderd");
			}
		}catch(Exception e){
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Er is iets fout gegaan tijdens het verwijderen");
		}
		return r;
	}

	public boolean hernoemFile(){ // tijdelijke bestand hernoemen
		try{
			sc.close();
			pw.close();
			Path p = FileSystems.getDefault().getPath( "Brandstofvoorraad/brandstoffen.txt");
			Files.delete(p);    // Oude bestand eerst verwijderen
			File file = new File("Brandstofvoorraad/tempFile.txt");  // File (or directory) with old name
			File file2 = new File("Brandstofvoorraad/brandstoffen.txt");  // File (or directory) with new name
			if(file2.exists()){
				//bestand bestaat nog // controleren of het echt verwijderd is voor dat we verder gaan
				return false;
			}else{
				//bestand bestaat niet
			}
			boolean success = file.renameTo(file2);  // Rename file (or directory)
			if (!success) {
				//Hernoemen mislukt
				return false;
			}else{
				//Hernoemen gelukt
				return true;
			}
		}catch(Exception e){
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "Er is iets fout gegaan tijdens het verwijderen");
		}
		return false;
	}

	public void bestandSchrijven(String tp, int tsic){ //nieuwe brandstof opslaan in brandstoffen.txt
		try{
			f = new File("Brandstofvoorraad/brandstoffen.txt");
			fw = new FileWriter(f,true);
			pw = new PrintWriter(fw);
			sc = new Scanner(f);
			pw.println(tp);
			pw.println(tsic);
			sc.close();
			pw.close();
			f = null;
		}catch(Exception e){
			System.out.println(e);
		}
	}

	public void setLiters(int newlt){ //voorraad instellen
		try{
			f = new File("Brandstofvoorraad/"+type+".txt");
			fw = new FileWriter(f);
			pw = new PrintWriter(fw);
			pw.println(newlt);
			pw.close();
			liters = getLiters();
			f = new File("Bestellingen/"+getType()+".txt");
			if(f.delete()){
				System.out.println("Brandstof bestelling ontvangen");
			}
		}catch(Exception e){}
	}

	public void verwerkTankBeurt(int lt){ // een gesimuleerde tankbeurt verwerken
		liters = getLiters();
		if(liters <= 500){ // als de voorraad lager dan 10% is bijbestellen
			stuurBestelling(this);
		}
		if(liters >= lt){
			liters = liters - lt;
			String datum = "D"+c.get(Calendar.DAY_OF_MONTH)+"M"+c.get(Calendar.MONTH)+"J"+c.get(Calendar.YEAR)+"-";
			String tijd = "H"+c.get(Calendar.HOUR)+"M"+c.get(Calendar.MINUTE)+"S"+c.get(Calendar.SECOND);
			try{
				fw = new FileWriter("Tankbeurten/"+type+"-"+datum+tijd+"V"+liters+".txt");
				pw = new PrintWriter(fw);
				pw.println(lt+" liters getankt op "+datum+" om "+tijd); //Tankbeurt opslaan in database
				pw.close();
				fw = new FileWriter("Brandstofvoorraad/"+type+".txt");
				pw = new PrintWriter(fw);
				pw.println(liters); //nieuwe voorraad opslaan
				pw.close();
			}catch(Exception e){System.out.println(e);}
		}else{
			stuurBestelling(this);
		}
	}

	public static void stuurBestelling(Brandstof brandstof){ // Bestelling naar leverancier sturen en orderbestand aanmaken
		brandstof.liters = brandstof.getLiters();
		brandstof.f = new File("Bestellingen/"+ brandstof.getType()+".txt");
		if(!brandstof.f.exists()){
			try{
				brandstof.fw = new FileWriter(brandstof.f);
				brandstof.pw = new PrintWriter(brandstof.fw);
				brandstof.pw.println(5000- brandstof.getLiters());
				brandstof.pw.close();
			}catch(Exception e){System.out.println(e);}
			System.out.println("Brandstof is bijna op. Bericht naar leverancier wordt nu verzonden\n");
			String s = "Ik wil graag "+ (5000- brandstof.getLiters()) + " liters " + brandstof.getType()+ " bestellen bij u\nDe TSIC van ons pompstation is "+ brandstof.getTSIC();
			System.out.println(s);
		}
	}
}

