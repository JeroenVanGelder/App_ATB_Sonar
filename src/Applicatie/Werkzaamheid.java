package Applicatie;

import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Werkzaamheid{

	private String omschrijving;
	private int ID;
	private File f;
	private Scanner sc;

	public Werkzaamheid(String o, int i){
		omschrijving = o.replaceAll(" ","");
		ID = i;
		slaWerkzaamheidOp();
	}

	public int getID(){
		return ID;
	}

	public String getOmschrijving(){
		return omschrijving;
	}

	public void slaWerkzaamheidOp(){
		try{
			f = new File("Werkzaamheden.txt");
			if(f.exists()){
				sc = new Scanner(f);
			}else{
				PrintWriter pw = new PrintWriter(new FileWriter(f));
				pw.print("");
				pw.close();
				sc = new Scanner(f);
			}
			while(true){  // controleren of het bestand niet al in het Monteurs.txt staat
				int match = 0;
				if(!sc.hasNext()){
					match = 99999999;
				}else{
					sc.next();
					match = Integer.parseInt(sc.next());
				}
				if(match == ID){
					sc.close();
					return;
				}
				if(!sc.hasNext()){
					sc.close();
					schrijfWeg(omschrijving, ID);
					return;
				}
			}
		}catch(Exception e){System.out.println(e);}
	}

	public void schrijfWeg(String omschrijving, int ID){ //nieuwe werkzaamheid opslaan in werkzaamheden.txt
		try{
			f = new File("Werkzaamheden.txt");
			PrintWriter pw = new PrintWriter(new FileWriter(f,true));
			sc = new Scanner(f);
			pw.println(omschrijving);
			pw.println(ID);
			sc.close();
			pw.close();
			JOptionPane.showMessageDialog(null, "Werkzaamheid "+omschrijving+" is toegevoegd met ID: "+ID);
		}catch(Exception e){System.out.println(e);}
	}
}