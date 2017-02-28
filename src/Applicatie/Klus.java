package Applicatie;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Klus{

	private int klantID, autoID, ID, factuurID;
	private String datum, status, werk, werknemers;
	private File theFile;
	private Scanner sc;

	public Klus(String d, String s, int kID, int aID, String w, String wN, int i, int i2){
		datum = d;
		status = s;
		klantID = kID;
		autoID = aID;
		werk = w;
		werknemers = wN;
		ID = i;
		factuurID = i2;
		slaKlusOp();
		try{
			createFiles();
		}catch(Exception e){}
	}

	public String getDatum(){
		return datum;
	}
	public String getStatus(){
		return status;
	}
	
	public void setStatus(String s){
		status = s;
	}

	public int getKlantID(){
		return klantID;
	}

	public int getAutoID(){
		return autoID;
	}

	public String getWerk(){
		return werk;
	}

	public String getWerknemers(){
		return werknemers;
	}

	public int getID(){
		return ID;
	}

	public void createFiles() throws IOException{
		theFile = new File("Klussen/Klus"+getID()+"onderdelen.txt");
		if(!theFile.exists()){
			PrintWriter pw = new PrintWriter(new FileWriter(theFile));
			pw.print("");
			pw.close();	
		}

		theFile = new File("Klussen/Klus"+getID()+"manuren.txt");
		if(!theFile.exists()){
			PrintWriter pw = new PrintWriter(new FileWriter(theFile));
			pw.print("");
			pw.close();
		}
	}

    public class Foo {
        Bar bar;
        public Bar getbar() {
            return bar;
        }
    }

    public class Bar {
        private Foo impValue1;
        public Bar(Foo impValue) {
            impValue1 = impValue;
        }
        public Foo getImpValue(){
            return impValue1;
        }
    }

    public class Client {
        Foo a;
        Foo impValue = a.getbar().getImpValue();
    }


	public void slaKlusOp(){
		try{
			File f = new File("Klussen.txt");
			if(f.exists()){
				sc = new Scanner(f);
			}else{
				FileWriter fw = new FileWriter("Klussen.txt");
				PrintWriter pw = new PrintWriter(fw);
				pw.print("");
				pw.close();
				sc = new Scanner(f);
			}
			while(true){  // controleren of het bestand niet al in het Monteurs.txt staat
				int match = 0;
				if(!sc.hasNext()){
					match = 99999999;
				}else{
					for(int i = 0; i <= 5; i++){
						sc.next();
					}
					match = Integer.parseInt(sc.next());
				}
				if(match == ID){
					sc.close();
					return;
				}
				if(!sc.hasNext()){
					sc.close();
					schrijfWeg(datum, status, klantID, autoID, werk, werknemers, ID, factuurID);
					return;
				}
			}
		}catch(Exception e){System.out.println(e+"hier?");}
	}

	public void schrijfWeg(String datum, String status, int klantID, int autoID, String werk, String werknemers, int ID, int factuurID){ //nieuwe klus opslaan in Klussen.txt
		try{
			File f = new File("Klussen.txt");
			PrintWriter pw = new PrintWriter(new FileWriter(f,true));
			Scanner sc = new Scanner(f);
			pw.println(datum);
			pw.println(status);
			pw.println(klantID);
			pw.println(autoID);
			pw.println(werk);
			pw.println(werknemers);
			pw.println(ID);
			pw.println("factuurID:");
			pw.println(factuurID);
			sc.close();
			pw.close();
			JOptionPane.showMessageDialog(null, "Klus "+ID+" aangemaakt.   KlantID:"+klantID+"   AutoID:"+ autoID+"    WerknemerNummers:"+werknemers);
		}catch(Exception e){System.out.println(e);}
	}
	
	public boolean wijzigStatus(String newS, int id){ // brandstof verwijderen
		try{
			File f = new File("Klussen.txt");
			File f2 = new File("KlussenTempFile.txt");
			sc = new Scanner(f);
			PrintWriter pw = new PrintWriter(new FileWriter(f2));
			while(sc.hasNext()){ // tijdelijk bestand maken
				String datum      = sc.next();
				String status     = sc.next();
				int klantID       = Integer.parseInt(sc.next());
				int autoID        = Integer.parseInt(sc.next());
				String werk       = sc.next();
				String werkNemers = sc.next();
				int ID            = Integer.parseInt(sc.next());
				if(ID == id){
					pw.println(datum);
					pw.println(newS);
					pw.println(klantID);
					pw.println(autoID);
					pw.println(werk);
					pw.println(werkNemers);
					pw.println(ID);
				}else{
					pw.println(datum);
					pw.println(status);
					pw.println(klantID);
					pw.println(autoID);
					pw.println(werk);
					pw.println(werkNemers);
					pw.println(ID);
				}
				if(!sc.hasNext()){
					sc.close();
					pw.close();
				}
			}
		}catch(Exception e){}
		sc.close();
		boolean r = hernoemFile(); // als hernoemen van tijdelijk bestand is gelukt hier verder
		return r;
	}

	public boolean hernoemFile(){ // tijdelijke bestand hernoemen
		try{
			Path p = FileSystems.getDefault().getPath( "Klussen.txt");
			Files.delete(p);    // Oude bestand eerst verwijderen
			File file = new File("KlussenTempFile.txt");  // File (or directory) with old name
			File file2 = new File("Klussen.txt");  // File (or directory) with new name
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
}