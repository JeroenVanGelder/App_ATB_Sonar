package Applicatie;

import Applicatie2.brievenService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class databaseController implements InterfacedatabaseController {

	private static Scanner sc;
	private int aantalBezoeken = 4, overEenPeriodeVan = 6, aantalMails = 0;
	private static ArrayList<Auto> autos = new ArrayList<Auto>();
	private static ArrayList<Klant> klanten = new ArrayList<Klant>();
	private static ArrayList<Factuur> facturen = new ArrayList<Factuur>();
 	private String 	onderhoudsmailtekst = "Deze mail is bestemt voor <klantNaam>,\nuw auto <klantAuto> Is toe aan een onderhoudsbeurt.\nDe laatste onderhoudsbeurt voor deze auto was op <laatsteOnderhoudsbeurt>", 
					maandenmailtekst = "Deze mail is bestemt voor <klantNaam>,\nhet is opgevallen dat u de laatste tijd geen bezoeken meer maakt\nDe laatste <periodeRecentelijkeBezoeken> maanden heeft u <aantalRecentelijkeBezoeken> of meer bezoeken gemaakt bij ons.\nMaar u bent hier niet meer geweest voor 2 maanden.";
	private Applicatie2.brievenService brievenService = new brievenService();
	
	
	public databaseController() throws IOException{
        new DatabaseFiles().invoke();
		
		
		//alle onderhoudsbeurten van de lijst worden aangemaakt
		ArrayList<Onderhoudsbeurt> onderhoud = new ArrayList<Onderhoudsbeurt>();
		FileReader fr = new FileReader("Onderhoudsbeurten");
		BufferedReader br = new BufferedReader(fr);
		sc = new Scanner(br);
		sc.useDelimiter("\\s*,,\\s*|\\s*;\\s*");
		while (sc.hasNext()) {
			int volgnummer = Integer.parseInt(sc.next());
			LocalDate datum = LocalDate.of(Integer.parseInt(sc.next()), Integer.parseInt(sc.next()), Integer.parseInt(sc.next()));

			//			datum.getDayOfMonth();
            LocalDate.of(Integer.parseInt(sc.next()), Integer.parseInt(sc.next()), Integer.parseInt(sc.next())).getDayOfMonth();
			Onderhoudsbeurt o = new Onderhoudsbeurt(volgnummer, datum);
			onderhoud.add(o);
			//System.out.println(o);
		}
		//alle autos uit de lijst worden aangemaakt en de onderhoudsbeurten van de autos worden gebonden aan de auto
		fr = new FileReader("Autos");
		br = new BufferedReader(fr);
		sc = new Scanner(br);
		sc.useDelimiter("\\s*;\\s*");
		while (sc.hasNext()) {
			Scanner sc2 = new Scanner(sc.next());
			sc2.useDelimiter("\\s*,,\\s*");
			int volgnummer = Integer.parseInt(sc2.next());
			String kenteken = sc2.next();
			String merk = sc2.next();
			Auto a = new Auto(volgnummer, kenteken, merk);
			autos.add(a);
			while (sc2.hasNext()) {
				int onderhoudsvolgnummer = Integer.parseInt(sc2.next());
				for(Onderhoudsbeurt ond : onderhoud){
					if(ond.getVolgNummer() == onderhoudsvolgnummer){
						a.saveOnderhoudsbeurt(ond, false);
						break;
					}
				}
			}
			sc2.close();
		}
		
		//alle klanten uit de lijst worden aangemaakt en de autos van deze klanten worden gebonden aan de klant
		fr = new FileReader("Klanten");
		br = new BufferedReader(fr);
		sc = new Scanner(br);
		sc.useDelimiter("\\s*,,\\s*|\\s*;\\s*");
		while (sc.hasNext()) {
			String Naam = sc.next();
			String Email = sc.next();
			int autovolgnummer = Integer.parseInt(sc.next());
			for(Auto au : autos){
				if(au.getVolgNummer() == autovolgnummer){
					Klant k = new Klant(Naam, Email, au);
					klanten.add(k);
					break;
				}
			}
			//System.out.println(k);
		}
		facturenInlezen();

	}

    @Override
    public ArrayList<Auto> getAutos(){
		return autos;
	}
	@Override
    public ArrayList<Klant> getKlanten(){
		return klanten;
	}
	@Override
    public String getOnderhoudsmailtekst() {
		return onderhoudsmailtekst;
	}
	public void setOnderhoudsmailtekst(String onderhoudsmailtekst) {
		this.onderhoudsmailtekst = onderhoudsmailtekst;
	}
	public String getMaandenmailtekst() {
		return maandenmailtekst;
	}
	public void setMaandenmailtekst(String maandenmailtekst) {
		this.maandenmailtekst = maandenmailtekst;
	}
	public void setAantalBezoeken(int aantalBezoeken) {
		this.aantalBezoeken = aantalBezoeken;
	}
	public int getfacturenLijstSize(){
		return facturen.size();
	}
	public brievenService getBrievenService(){
		return brievenService;
	}
	public void setOverEenPeriodeVan(int overEenPeriodeVan) {
		this.overEenPeriodeVan = overEenPeriodeVan;
	}

	public void verzendMail(String s) throws IOException{
		File theDir = new File("KlantenbindingMails");
		if (theDir.exists()) {
			try{
				theDir.delete();
			} catch(SecurityException se){
			}
		}
		theDir = new File("KlantenbindingMails");
		if (!theDir.exists()) {
			try{
				theDir.mkdir();
			} catch(SecurityException se){
			}
		}
		File f = new File("KlantenbindingMails/Mail"+aantalMails+".txt");
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(s);
		bw.close();
	    aantalMails++;
	}
	
	public String genereerOnderhoudMail(Klant k){
		ArrayList<Onderhoudsbeurt> beurten = k.getAuto().getOnderhoudsbeurten();
		Onderhoudsbeurt laatstekeer = beurten.get(beurten.size()-1);
		String s = "";
		Scanner sc2 = new Scanner(onderhoudsmailtekst);
		while(sc2.hasNextLine()){
			s += "\n";
			Scanner sc = new Scanner(sc2.nextLine());
			sc.useDelimiter("\\s*<\\s*|\\s*>\\s*");
			while(sc.hasNext()){
				s += sc.next();
				if(sc.hasNext()){
					String speciaalDing = sc.next();
					if(speciaalDing.equals("klantNaam")){
						s += " "+k.getNaam()+" ";
					}else if(speciaalDing.equals("klantAuto")){
						s += " "+k.getAuto()+" ";
					}else if(speciaalDing.equals("laatsteOnderhoudsbeurt")){
						s += " "+laatstekeer.getDatum()+" ";
					}else if(speciaalDing.equals("periodeRecentelijkeBezoeken")){
						s += " "+overEenPeriodeVan+" ";
					}else if(speciaalDing.equals("aantalRecentelijkeBezoeken")){
						s += " "+aantalBezoeken+" ";
					}
				}
			}
			sc.close();
	    }
		sc2.close();
		return s;
	}
	
	public String genereer2MaandenMail(Klant k){
		ArrayList<Onderhoudsbeurt> beurten = k.getAuto().getOnderhoudsbeurten();
		Onderhoudsbeurt laatstekeer = beurten.get(beurten.size()-1);
		String s = "";
		Scanner sc2 = new Scanner(maandenmailtekst);
		while(sc2.hasNextLine()){
			s += "\n";
			Scanner sc = new Scanner(sc2.nextLine());
			sc.useDelimiter("\\s*<\\s*|\\s*>\\s*");
			while(sc.hasNext()){
				s += sc.next();
				if(sc.hasNext()){
					String speciaalDing = sc.next();
					if(speciaalDing.equals("klantNaam")){
						s += " "+k.getNaam()+" ";
					}else if(speciaalDing.equals("klantAuto")){
						s += " "+k.getAuto()+" ";
					}else if(speciaalDing.equals("laatsteOnderhoudsbeurt")){
						s += " "+laatstekeer.getDatum()+" ";
					}else if(speciaalDing.equals("periodeRecentelijkeBezoeken")){
						s += " "+overEenPeriodeVan+" ";
					}else if(speciaalDing.equals("aantalRecentelijkeBezoeken")){
						s += " "+aantalBezoeken+" ";
					}
				}
			}
			sc.close();
	    }
		sc2.close();
		return s;
	}
	
	public void checkVoorFactuur() { 
		for(Klant k : klanten){
			for(Factuur f : facturen){
				
				if(f.getFactuurhouder().equals(k) && !f.isAfbetaald()){
					if(f.getAantalkeerverzonden() == 1){
						brievenService.genereerEersteWaarschuwingsBrief(f);
					}else if(f.getAantalkeerverzonden() > 1){
						brievenService.genereerTweedeWaarschuwingsBrief(f);
					}
					brievenService.genereerFactuurBrief(f);
					f.verhoogAantalkeerverzonden();
				}
			}
			ArrayList<Object> diensten = zoekDienstenDieNietGebondenZijnAanFactuur();
			ArrayList<Object> dienstenVanKlant = dienstenMetKlant(diensten, k);

			if(dienstenVanKlant.size() > 0){
				Factuur f2 = Factuur.createFactuur(k, false, berekenBedrag(dienstenVanKlant), 0, 0, facturen.size()+1);
				System.out.println(berekenBedrag(dienstenVanKlant));
				brievenService.genereerFactuurBrief(f2);
				if(!facturen.contains(f2)){
					f2.schrijfWeg();
					facturenInlezen();
				}
			}
		}
	}

	public int berekenBedrag(ArrayList<Object> dienstenVanKlant) {
		int bedrag = 0;
		for (Object o : dienstenVanKlant) {
			if (o instanceof Reservering) {
				bedrag += ((Reservering) o).getKosten();
			}
			if (o instanceof Klus) {
				File f = new File("FactuurApp/Werkzaamheden.txt");
				if (f.isFile() && f.exists() && (!(f.length() == 0))) {
					try {
						Scanner sc = new Scanner(f);
						while (sc.hasNext()) {
							String naam = sc.next();
							String ID = sc.next();
							int kosten = Integer.parseInt(sc.next());
							String s = ((Klus) o).getWerk();
							if (ID.equals(s)) {
								bedrag += kosten;
							}
						}
						sc.close();
					} catch (Exception e) {
						System.out.println(e);
					}
				}
				int klusID = ((Klus) o).getID();
				f = new File("Klussen/Klus"+klusID+"manuren"+".txt");
				if (f.isFile() && f.exists() && (!(f.length() == 0))) {
					try {
						Scanner sc = new Scanner(f);
						while (sc.hasNext()) {
							int manuren = Integer.parseInt(sc.next());
							bedrag += manuren*10;
						}
						sc.close();
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		}
		return bedrag;

	}
	
	public ArrayList<Object> dienstenMetKlant(ArrayList<Object> diensten , Klant k){
		ArrayList<Object> returnLijst = new ArrayList<Object>();
		for(Object o : diensten){

			if(o instanceof Reservering){
				Auto a = zoekAuto(((Reservering)o).getKenteken());
				if(k.getAuto().equals(a)){
					returnLijst.add(o);
				}
			}
			if(o instanceof Klus){
				
				int i = ((Klus)o).getAutoID();

				int i2 = k.getAuto().getVolgNummer();

				if(i == i2){

					returnLijst.add(o);
				}
			}
		}
		return returnLijst;
	}
	public ArrayList<Object> zoekDienstenDieNietGebondenZijnAanFactuur(){
		ArrayList<Object> diensten = new ArrayList<Object>();
		File f = new File("Klussen.txt");
		if(f.isFile() && f.exists()&& (!(f.length() == 0))){

			try{
				Scanner sc = new Scanner(f);
				while(sc.hasNext()){

					String datum      = sc.next();
					String status     = sc.next();
					int klantID       = Integer.parseInt(sc.next());
					int autoID        = Integer.parseInt(sc.next());
					String werk       = sc.next();
					String werkNemers = sc.next();
					int ID            = Integer.parseInt(sc.next()); 
					sc.next();
					int factuurID = Integer.parseInt(sc.next()); 
					Klus x = new KlusBuilder().setD(datum).setS(status).setkID(klantID).setaID(autoID).setW(werk).setwN(werkNemers).setI(ID).setI2(factuurID).createKlus();
					if(factuurID == 999){
						diensten.add(x);

					}
				}
				sc.close();
			}catch(Exception e){System.out.println(e);}
		}
		f = new File("Reserveringen.txt");
		if(f.isFile() && f.exists()&& (!(f.length() == 0))){

			try{
				Scanner sc = new Scanner(f);
				while(sc.hasNext()){

					String kenteken      = sc.next();
					String datum     = sc.next();
					String duur       = sc.next();
					int kosten            = Integer.parseInt(sc.next()); 
					int ID            = Integer.parseInt(sc.next());
					sc.next();
					int factuurID            = Integer.parseInt(sc.next());

					Reservering x2 = new Reservering(kenteken, datum, duur, factuurID);
					if(factuurID == 999){

						diensten.add(x2);
					}
				}
				sc.close();
			}catch(Exception e){System.out.println(e);}
		}
		return diensten;
	}
	
	public void facturenInlezen(){
		File f = new File("FactuurApp/Facturen");
		if(f.isFile() && f.exists()&& (!(f.length() == 0))){
			try{
				Scanner sc = new Scanner(f);
				while(sc.hasNext()){
					String email      = sc.next();
					boolean afbetaald  = Boolean.parseBoolean(sc.next());
					double bedrag       = Double.parseDouble(sc.next());
					double betaald       = Double.parseDouble(sc.next());
					int aantalKeerverzonden       = Integer.parseInt(sc.next());
					int ID            = Integer.parseInt(sc.next()); 

					Factuur x = Factuur.createFactuur(zoekKlant(email), afbetaald, bedrag, betaald, aantalKeerverzonden, ID);
					facturen.add(x);
				}
				sc.close();
			}catch(Exception e){System.out.println(e);}
		}
	}
	
	public ArrayList<Klant> checkOnderhoudKlanten(){
		ArrayList<Klant> mailKlanten = new ArrayList<Klant>();
		for(Auto a : autos){
			ArrayList<Onderhoudsbeurt> onderhoud = a.getOnderhoudsbeurten();
			if(onderhoud.size() >= 1){
				Onderhoudsbeurt o = onderhoud.get(onderhoud.size()-1);
				LocalDate datum2 = o.getDatum().plusMonths(6);
				if(datum2.isBefore(LocalDate.now())){
					for(Klant k : klanten){
						if(k.getAuto() == a){
							mailKlanten.add(k);
							break;
						}
					}
				}
			}
		}
		return mailKlanten;
	}
	
	
	//dit moet waarschijnlijk aanpasbaar gemaakt worden (hoeveel bezoeken oven een bepaalde periode)
	public ArrayList<Klant> check2MaandenKlanten(){
		ArrayList<Klant> mailKlanten = new ArrayList<Klant>();
		for(Auto a : autos){
			ArrayList<Onderhoudsbeurt> onderhoud = a.getOnderhoudsbeurten();
			ArrayList<Onderhoudsbeurt> beurtenJongerDan = new ArrayList<Onderhoudsbeurt>();

			for(Onderhoudsbeurt o : onderhoud){
				LocalDate datum2 = o.getDatum().plusMonths(overEenPeriodeVan);
				if(datum2.isAfter(LocalDate.now())){
					beurtenJongerDan.add(o);
				}
			}
			if(beurtenJongerDan.size() >= aantalBezoeken){
				if(beurtenJongerDan.get(beurtenJongerDan.size()-1).getDatum().plusMonths(2).isBefore(LocalDate.now())){
					for(Klant k : klanten){
						if(k.getAuto() == a){
							mailKlanten.add(k);
							break;
						}
					}
				}
			}
		}
		return mailKlanten;
	}
	
	public void voegKlantToe(Klant k) throws IOException{
		ArrayList<String> stringLijst = new ArrayList<String>();
		if(zoekKlant(k.getEmail()) == null){
			klanten.add(k);
			FileReader fr = new FileReader("Klanten");
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			while(sc.hasNextLine()){
				String s = sc.nextLine();
				stringLijst.add(s);
		    }
			if(k.getAuto() != null){
				stringLijst.add(k.getNaam()+",,"+k.getEmail()+",,"+k.getAuto().getVolgNummer()+";");
			}else{
				stringLijst.add(k.getNaam()+",,"+k.getEmail()+";");
			}
			FileWriter fw = new FileWriter("Klanten");
		    PrintWriter pw = new PrintWriter(fw);
			for(String s : stringLijst){
				pw.println(s);
			}
			sc.close();
			pw.close();
		}
	}
	
	public void verwijderAuto(Auto a) throws IOException{
		ArrayList<String> stringLijst = new ArrayList<String>();
		if(zoekAuto(a.getKenteken()) != null){
			klanten.remove(a);
			FileReader fr = new FileReader("Autos");
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			while(sc.hasNextLine()){
				String s = sc.nextLine();
				Scanner sc2 = new Scanner(s);
				sc2.useDelimiter("\\s*,,\\s*");
				sc2.next();
				if(!sc2.next().equals(a.getKenteken())){
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
		}
	}
	public void voegAutoToe(Auto a) throws IOException{
		ArrayList<String> stringLijst = new ArrayList<String>();
		if(zoekAuto(a.getKenteken()) == null){
			autos.add(a);
			FileReader fr = new FileReader("Autos");
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			while(sc.hasNextLine()){
				String s = sc.nextLine();
				stringLijst.add(s);
		    }
			String s = a.getVolgNummer()+",,"+a.getKenteken()+",,"+a.getMerk();
			ArrayList<Onderhoudsbeurt> onderhoud = a.getOnderhoudsbeurten();
			for(Onderhoudsbeurt o : onderhoud){
				s +=",," + o.getVolgNummer();
			}
			s += ";";
			stringLijst.add(s);
			FileWriter fw = new FileWriter("Autos");
		    PrintWriter pw = new PrintWriter(fw);
			for(String s2 : stringLijst){
				pw.println(s2);
			}
			sc.close();
			pw.close();
		}
	}
	
	public void verwijderKlant(Klant k) throws IOException{
		ArrayList<String> stringLijst = new ArrayList<String>();
		if(zoekKlant(k.getEmail()) != null){
			klanten.remove(k);
			FileReader fr = new FileReader("Klanten");
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			while(sc.hasNextLine()){
				String s = sc.nextLine();
				Scanner sc2 = new Scanner(s);
				sc2.useDelimiter("\\s*,,\\s*");
				sc2.next();
				if(!sc2.next().equals(k.getEmail())){
					stringLijst.add(s);
				}	
				sc2.close();
		    }
			FileWriter fw = new FileWriter("Klanten");
		    PrintWriter pw = new PrintWriter(fw);
			for(String s : stringLijst){
				pw.println(s);
			}
			sc.close();
			pw.close();
		}
	}
	
	public Klant zoekKlant(String email){
		for(Klant k : klanten){
			if(k.getEmail().equals(email)){
				return k;
			}
		}
		return null;
	}
	public Klant zoekKlant(String naam, boolean b){
		for(Klant k : klanten){
			if(k.getNaam().equals(naam)){
				return k;
			}
		}
		return null;
	}
	
	public Auto zoekAuto(String kenteken){
		for(Auto a : autos){
			if(a.getKenteken().equals(kenteken)){
				return a;
			}
		}
		return null;
	}


    private class DatabaseFiles {
        public void invoke() throws IOException {
            File theFile = null;
            for(int i = 0; i <= 2; i++){
                if(i == 0){theFile = new File("Autos");}
                if(i == 1){theFile = new File("Klanten");}
                if(i == 2){theFile = new File("Onderhoudsbeurten");}
                // if the directory does not exist, create it
                if (!theFile.exists()) {
                    try{
                        theFile.createNewFile();
                    } catch(SecurityException se){
                    }
                }
            }
        }
    }
}
