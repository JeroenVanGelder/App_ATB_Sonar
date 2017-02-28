package Applicatie;

import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Reservering{

	private String kenteken, datum, duur;
	private int ID, kosten, factuurID;
	private File f;
	private Scanner sc;

	public Reservering(String k, String d,String dr, int fID){
		kenteken = k.replaceAll(" ","");
		datum = d.replaceAll(" ","");
		duur = dr;
		factuurID = fID;
		berekenKosten();
		//slaReserveringOp();
	}

	public String getKenteken(){
		return kenteken;
	}
	public int getKosten(){
		return kosten;
	}
	public String getDatum(){
		return datum;
	}

	public void slaReserveringOp(){
		try{
			f = new File("Reserveringen.txt");
			if(f.exists()){
				schrijfWeg(kenteken, datum, duur, kosten, factuurID);
			}else{
				PrintWriter pw = new PrintWriter(new FileWriter(f));
				pw.print("");
				pw.close();
				schrijfWeg(kenteken, datum, duur, kosten, factuurID);
			}
		}catch(Exception e){System.out.println(e);}
	}

	public void schrijfWeg(String k, String d, String dr, int ks, int factuurID){ //nieuwe reservering opslaan in Reserveringen.txt
		try{
			f = new File("Reserveringen.txt");
			PrintWriter pw = new PrintWriter(new FileWriter(f,true));
			sc = new Scanner(f);
			pw.println(kenteken);
			pw.println(datum);
			pw.println(dr);
			pw.println(kosten);
			pw.println("factuurID:");
			pw.println(factuurID);
			sc.close();
			pw.close();
			JOptionPane.showMessageDialog(null, "Reservering met kenteken "+kenteken+" voor "+datum+" is toegevoegd met ID: "+ID);
			verwerkReservering(datum, dr);
		}catch(Exception e){System.out.println(e);}
	}

	public void berekenKosten(){
		try{
			File f = new File("ParkeerPrijzen.txt");
			Scanner sc = new Scanner(f);
			sc.next();
			int prijsPerDag = Integer.parseInt(sc.next());
			sc.next();
			int prijsPerWeek = Integer.parseInt(sc.next());
			sc.next();
			int prijsPerMaand = Integer.parseInt(sc.next());
			if(duur.equals("Dag")){
				kosten = prijsPerDag;
			}else if(duur.equals("Week")){
				kosten = prijsPerWeek;
			}else if(duur.equals("Maand")){
				kosten = prijsPerMaand;
			}
			sc.close();
		}catch(Exception e){}
	}

	public void verwerkReservering(String d, String dr){
		System.out.println("verwerkReservering()");
		Scanner sc = new Scanner(d);
		sc.useDelimiter("-");
		int dag = Integer.parseInt(sc.next());
		int maand = Integer.parseInt(sc.next());
		//int jaar = Integer.parseInt(sc.next()); niet nodig voor deze opdracht
		String m = "";
		int lengte = 0;
		sc.close();
		if(dr.equals("Dag")){
			lengte = 1;
		}else if(dr.equals("Week")){
			lengte = 7;
		}else if(dr.equals("Maand")){
			lengte = 31;
		}
		try{
			for(int i = 0; i < lengte; i++){
				if(dag == 32){
					dag = 1;
					maand++;
				}
				if(maand == 1){m = "Januari";}else  if(maand == 2){m = "Februari";}else  if(maand == 3){m = "Maart";}else
				if(maand == 4){m = "April";}else    if(maand == 5){m = "Mei";}else       if(maand == 6){m = "Juni";}else
				if(maand == 7){m = "Juli";}else     if(maand == 8){m = "Augustus";}else  if(maand == 9){m = "September";}else
				if(maand == 10){m = "Oktober";}else if(maand == 11){m = "November";}else if(maand == 12){m = "December";}
				File f = new File("Reserveringen/"+m+"/Dag"+dag+".txt");
				Scanner scan = new Scanner(f);
				int aantal = Integer.parseInt(scan.next());
				System.out.println("Gelezen: "+aantal);
				FileWriter fw = new FileWriter(f);
				PrintWriter pw = new PrintWriter(fw);
				int newFreeSpace = aantal-1;
				System.out.println(newFreeSpace);
				pw.println(newFreeSpace);
				pw.close();
				scan.close();
				dag++;
			}

		}catch(Exception e){}
	}
}