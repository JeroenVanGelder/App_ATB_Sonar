package Applicatie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Auto {
	public static final String PATTERN = "\\s*,,\\s*|\\s*;\\s*";
	private String kenteken;
	private ArrayList<Onderhoudsbeurt> onderhoudsbeurten;
	private String merk;
	private int volgNummer;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	private Scanner scanner;
	private final String DELIMITER_PATTERN = "\\s*,,\\s*";

    public Auto(int vn, String kt, String mk){
		setVolgNummer(vn);
		kenteken = kt;
		merk = mk;
		onderhoudsbeurten = new ArrayList<>();
	}
	
	public ArrayList<Onderhoudsbeurt> getOnderhoudsbeurten(){
		return onderhoudsbeurten;
	}
	
	public String getMerk(){
		return merk;
	}
	
	public void setMerk(String mk){
		merk = mk;
	}
	
	public String getKenteken(){
		return kenteken;
	}
	
	public void setKenteken(String kt){
		kenteken = kt;
	}
	
	public Onderhoudsbeurt zoekOnderhoudsbeurt(int vN){
		for(Onderhoudsbeurt ond : onderhoudsbeurten){
			if(ond.getVolgNummer()==vN){
				return ond;
			}
		}
		return null;
	}
	//boolean geeft aan of het uit de lijst wordt toegevoegd bij het opstarten van het programma(false), of dat er een onderhoudsbeurt wordt toegevoegd via het programma zelf(true)
	public void saveOnderhoudsbeurt(Onderhoudsbeurt onderhoudsbeurt, Boolean addedViaApplication) throws IOException{
		if(!checkOnderhoudsbeurt(onderhoudsbeurt)){
			onderhoudsbeurten.add(onderhoudsbeurt);
			if(addedViaApplication){
                writeOnderhoudsbeurtToFile(onderhoudsbeurt);
			}
		}
	}

	//the method before the extract parameter object refactoring
//	private void bullShitMethod(String name, String address, String postal){
//		//do nothing
//	}

	private void bullShitMethod(TestCustomer testCustomer){
		//do nothing
	}

    private void writeOnderhoudsbeurtToFile(Onderhoudsbeurt onderhoudsbeurt) throws IOException {
		checkAutoVolgNummers(onderhoudsbeurt);
		Scanner autoFileScanner;

        ArrayList<String> onderhoudsbeurtLijst = new ArrayList<>();
        onderhoudsbeurten.add(onderhoudsbeurt);
        fileReader = new FileReader("Onderhoudsbeurten");
        bufferedReader = new BufferedReader(fileReader);
        autoFileScanner = new Scanner(bufferedReader);
        int volgnummer = 0;
        while(autoFileScanner.hasNextLine()){
            String s = autoFileScanner.nextLine();
            Scanner sc2 = new Scanner(s);
            sc2.useDelimiter(DELIMITER_PATTERN);
            volgnummer = Integer.parseInt(sc2.next());
            onderhoudsbeurtLijst.add(s);
            sc2.close();
        }
        autoFileScanner.close();
        volgnummer++;
        LocalDate datum = onderhoudsbeurt.getDatum();
        onderhoudsbeurtLijst.add(volgnummer+",,"+datum.getYear()+",,"+datum.getMonthValue()+",,"+datum.getDayOfMonth()+";");
        writeListToFile(onderhoudsbeurtLijst, "Onderhoudsbeurten");//
    }

	private void checkAutoVolgNummers(Onderhoudsbeurt onderhoudsbeurt) throws IOException {
		ArrayList<String> autoLijst = new ArrayList<>();
		fileReader = new FileReader("Autos");
		bufferedReader = new BufferedReader(fileReader);
		Scanner autoFileScanner = new Scanner(bufferedReader);
		while(autoFileScanner.hasNextLine()){
            String auto = autoFileScanner.nextLine();
            Scanner autoScanner = new Scanner(auto);
            autoScanner.useDelimiter(DELIMITER_PATTERN);
            if(Integer.parseInt(autoScanner.next()) == volgNummer){
                auto = auto.substring(0, auto.length()-1);
                auto += ",," + onderhoudsbeurt.getVolgNummer() + ";";
                autoLijst.add(auto);
            }else{
                autoLijst.add(auto);
            }
            autoScanner.close();
        }
		autoFileScanner.close();
		writeListToFile(autoLijst, "Autos");
	}

	//deze method is hier gekomen door: "Extract method"		String fileName is hier door Change Signature
	private void writeListToFile(ArrayList<String> listToSave, String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(String word : listToSave){
            printWriter.println(word);
        }
        printWriter.close();
    }

    public void verwijderOnderhoudsbeurt(Onderhoudsbeurt o) throws IOException{
		if(checkOnderhoudsbeurt(o)){
			ArrayList<String> stringLijst = new ArrayList<>();
			onderhoudsbeurten.remove(o);
			//verwijderen uit de lijst achter de auto
			fileReader = new FileReader("Autos");
			bufferedReader = new BufferedReader(fileReader);
			Scanner sc = new Scanner(bufferedReader);
			while(sc.hasNextLine()){
				String s = sc.nextLine();
				Scanner sc2 = new Scanner(s);
				sc2.useDelimiter(PATTERN); //extract constant
				if(Integer.parseInt(sc2.next()) == volgNummer){
					String nieuweRegel = volgNummer + ",,";
					nieuweRegel += sc2.next() + ",," + sc2.next();
					while(sc2.hasNext()){
						int onderhoudsNummer = Integer.parseInt(sc2.next());
						if(onderhoudsNummer != o.getVolgNummer()){
							nieuweRegel += ",," + onderhoudsNummer;
						}
					}
					nieuweRegel += ";";
					stringLijst.add(nieuweRegel);
				}else{
					stringLijst.add(s);
				}	
				sc2.close();
		    }
			FileWriter fw = new FileWriter("Autos");
		    PrintWriter pw = new PrintWriter(fw);
			for(String s : stringLijst){
				pw.println(s);
			}
			sc.close();
			pw.close();
			
			//verwijderen van onderhoudsbeurt uit de onderhoudsbeurtenlijst zelf
			stringLijst = new ArrayList<>();
			onderhoudsbeurten.remove(o);
			fileReader = new FileReader("Onderhoudsbeurten");
			bufferedReader = new BufferedReader(fileReader);
			sc = new Scanner(bufferedReader);
			while(sc.hasNextLine()){
				String word = sc.nextLine();
				Scanner sc2 = new Scanner(word);
				sc2.useDelimiter("\\s*,,\\s*");
				if(!sc2.next().equals(""+o.getVolgNummer())){
					stringLijst.add(word);
				}	
				sc2.close();
		    }
			fw = new FileWriter("Onderhoudsbeurten");
			pw = new PrintWriter(fw);
			for(String s : stringLijst){
				pw.println(s);
			}
			sc.close();
			pw.close();
		}
	}

	public boolean checkOnderhoudsbeurt(Onderhoudsbeurt o){
		for(Onderhoudsbeurt ond : onderhoudsbeurten){
			if(o.getVolgNummer() == ond.getVolgNummer()){
				return true;
			}
		} 
		return false;
	}

	public int getVolgNummer() {
		return volgNummer;
	}

	public void setVolgNummer(int volgNummer) {
		this.volgNummer = volgNummer;
	}

	public String toString(){
		return kenteken + " " + merk;
	}

	static class TestCustomer {
		private final String name;
		private final String address;
		private final String postal;

		TestCustomer(String name, String address, String postal) {
			this.name = name;
			this.address = address;
			this.postal = postal;
		}

		public String getName() {
			return name;
		}

		public String getAddress() {
			return address;
		}

		public String getPostal() {
			return postal;
		}
	}

	

}
