package Applicatie;

import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Monteur{

	private String naam, achterNaam;
	private int ID;
	private File f;
	private Scanner sc;

	public Monteur(String nm, String anm, int i){
		naam = nm.replaceAll(" ","");
		achterNaam = anm.replaceAll(" ","");
		ID = i;
		slaMonteurOp();
	}

	public int getID(){
		return ID;
	}

	public String getNaam(){
		return naam;
	}

	public String getAchterNaam(){
		return achterNaam;
	}

	public void slaMonteurOp(){
		try{
			f = new File("Werknemers/Monteurs.txt");
			if(f.exists()){
				sc = new Scanner(f);
			}else{
				PrintWriter pw = new PrintWriter(new FileWriter(f));
				pw.print("");
				pw.close();
				sc = new Scanner(f);
			}
			while(true){  // controleren of het niet al in Monteurs.txt staat
				int match = 0;
				if(!sc.hasNext()){
					match = 99999999;
				}else{
					sc.next();
					sc.next();
					match = Integer.parseInt(sc.next());
				}
				if(match == ID){
					sc.close();
					return;
				}
				if(!sc.hasNext()){
					sc.close();
					schrijfWeg(naam, achterNaam, ID);
					return;
				}
			}
		}catch(Exception e){System.out.println(e);}
	}

	public void schrijfWeg(String naam, String achterNaam, int ID){ //nieuwe monteur opslaan in Werknemers/monteurs.txt
		try{
			f = new File("Werknemers/Monteurs.txt");
			PrintWriter pw = new PrintWriter(new FileWriter(f,true));
			sc = new Scanner(f);
			pw.println(naam);
			pw.println(achterNaam);
			pw.println(ID);
			sc.close();
			pw.close();
			JOptionPane.showMessageDialog(null, "Monteur "+naam+" "+achterNaam+" is toegevoegd met ID: "+ID);
		}catch(Exception e){System.out.println(e);}
	}
}