package Applicatie;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

import javax.swing.JOptionPane;

public class WerkPlaats extends JFrame implements ActionListener{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JButton statusWijzigen, onderdeelToevoegen, urenToevoegen, previousDay, nextDay, nieuweMonteur, nieuweKlus, nieuweWerkzaamheid;
	private JLabel manuren, onderdelen, werkzaamHeden, klusStatus, autoNaam, monteurNamen;
	private JTextField tf1;
	private JComboBox<String> DatePicker, selectPart, selectStatus;
	private ArrayList<Monteur> monteurs = new ArrayList<Monteur>();
	private ArrayList<Werkzaamheid> werkzaamheden = new ArrayList<Werkzaamheid>();
	private ArrayList<Klus> klussen = new ArrayList<Klus>();
	private ArrayList<String> parts = new ArrayList<String>();
	private Scanner sc;
	private File f;
	private Timer t;
	private int huidigID, huidigOnderdeelID;
	private Font font = new Font("Calibri", Font.BOLD, 20);

	public WerkPlaats(){
		createDirs();
		monteursInlezen();
		werkzaamhedenInlezen();
		klussenInlezen();


		t = new Timer(3000, this);
		t.addActionListener((ActionEvent e)->{
			vulDatePicker();
		});
		t.start();

		DatePicker = new JComboBox<String>();
		DatePicker.setBounds(100, 0, 500, 50);
		add(DatePicker);
		DatePicker.addItem("Selecteer datum");
		vulDatePicker();
		DatePicker.addActionListener((ActionEvent e)->{
			vindAgendaItem();
		});

		previousDay = new JButton("<< Vorige");
		add(previousDay);
		previousDay.setBounds(0,0,100,400);
		previousDay.addActionListener((ActionEvent e)->{
			vorigeAgendaItem();
		});

		nextDay = new JButton("Volgende >>");
		add(nextDay);
		nextDay.setBounds(600,0,100,400);
		nextDay.addActionListener((ActionEvent e)->{
			volgendAgendaItem();
		});

		nieuweMonteur = new JButton("Monteur toevoegen");
		add(nieuweMonteur);
		nieuweMonteur.setBounds(0,400,200,40);
		nieuweMonteur.addActionListener((ActionEvent e)->{
			int hoogste = 0;
			for(Monteur m : monteurs){
				if(m.getID() > hoogste){
					hoogste = m.getID();
				}
			}
			new NewMonteur(hoogste, monteurs);
		});


		nieuweKlus = new JButton("Klus toevoegen");
		nieuweKlus.setBounds(0,450,200,40);
		add(nieuweKlus);
		nieuweKlus.addActionListener((ActionEvent e)->{
			int hoogste = 0;
			for(Klus k : klussen){
				if(k.getID() > hoogste){
					hoogste = k.getID();
				}
			}
			new NewKlus(hoogste, klussen);
		});

		nieuweWerkzaamheid = new JButton("Werkzaamheid toevoegen");
		nieuweWerkzaamheid.setBounds(0,500,200,40);
		add(nieuweWerkzaamheid);
		nieuweWerkzaamheid.addActionListener((ActionEvent e)->{
			int hoogste = 0;
			for(Werkzaamheid w : werkzaamheden){
				if(w.getID() > hoogste){
					hoogste = w.getID();
				}
			}
			new NewWerkzaamheid(hoogste, werkzaamheden);

		});

		onderdeelToevoegen = new JButton("Artikel toevoegen aan klus");
		onderdeelToevoegen.setBounds(500,400,200,40);
		onderdeelToevoegen.addActionListener((ActionEvent e)->{
			String check = (String)selectPart.getSelectedItem();
			if(!check.equals("Selecteer datum")){
				try{
					File f = new File("Onderdelenvoorraad/artikel-"+huidigOnderdeelID+".txt");
					sc = new Scanner(f);
					int aantal = Integer.parseInt(sc.next());
					sc.close();
					if(aantal > 0){
						PrintWriter pw = new PrintWriter(new FileWriter(f));
						pw.print(aantal-1);
						pw.close();
						pw = new PrintWriter(new FileWriter("Klussen/Klus"+huidigID+"onderdelen.txt",true));
						pw.println(huidigOnderdeelID);
						pw.close();
					}else{
						JOptionPane.showMessageDialog(null, "Dit onderdeel is volgens het systeem niet meer in voorraad. Mogelijk helpt het als u eerst de voorraad bijwerkt");
					}
				}catch(Exception ex){}
			}
			for(Klus k : klussen){
				if(k.getID() == huidigID){
					toonAgendaItem(k);
				}
			}
		});

		selectPart = new JComboBox<String>();
		selectPart.setBounds(300,400,200,40);
		add(selectPart);
		selectPart.addItem("Selecteer onderdeel");
		selectPart.addActionListener((ActionEvent e)->{
			String select = (String)selectPart.getSelectedItem();
			if(!select.equals("Selecteer onderdeel")){
				sc = new Scanner(select);
				sc.useDelimiter(":");
				huidigOnderdeelID = Integer.parseInt(sc.next());
			}
		});
		vulSelectPart();

		statusWijzigen = new JButton("Status bijwerken");
		statusWijzigen.setBounds(500,450,200,40);
		add(statusWijzigen);
		statusWijzigen.addActionListener((ActionEvent e)->{
			String newStatus = (String)selectStatus.getSelectedItem();
			try{
				for(Klus k : klussen){
					if(k.getID() == huidigID){
						k.wijzigStatus(newStatus, huidigID);
						k.setStatus(newStatus);
					}
				}
			}catch(Exception ex){}
			for(Klus k : klussen){
				if(k.getID() == huidigID){
					toonAgendaItem(k);
				}
			}
		});

		selectStatus = new JComboBox<String>();
		selectStatus.setBounds(300, 450,200,40);
		add(selectStatus);
		selectStatus.addItem("Niet-afgerond");
		selectStatus.addItem("Mee-bezig");
		selectStatus.addItem("Afgerond");

		urenToevoegen = new JButton("Uren toevoegen aan klus");
		urenToevoegen.setBounds(500, 500,200,40);
		urenToevoegen.addActionListener((ActionEvent e)->{
			String check = (String)DatePicker.getSelectedItem();
			if(!check.equals("Selecteer datum")){
				int uren = 0;
				try{
					uren = Integer.parseInt(tf1.getText());
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Veld 'uren' moet een numerieke waarde zijn");
					return;
				}
				if(uren > 0){
					try{
						PrintWriter pw = new PrintWriter(new FileWriter("Klussen/Klus"+huidigID+"manuren.txt",true));
						pw.println(uren);
						pw.close();
					}catch(Exception ex){}
				}
			}
			for(Klus k : klussen){
				if(k.getID() == huidigID){
					toonAgendaItem(k);
				}
			}
			tf1.setText("");
		});

		tf1 = new JTextField();
		tf1.setBounds(300,500,200,40);

		add(onderdeelToevoegen);
		add(urenToevoegen);
		add(tf1);

		klusStatus = new JLabel("Status:");
		add(klusStatus);
		klusStatus.setBounds(120,50,500,40);
		klusStatus.setFont(font);

		autoNaam = new JLabel("Auto:");
		add(autoNaam);
		autoNaam.setBounds(120,100,500,40);
		autoNaam.setFont(font);

		monteurNamen = new JLabel("Monteurs:");
		add(monteurNamen);
		monteurNamen.setBounds(120,150,500,40);
		monteurNamen.setFont(font);

		werkzaamHeden = new JLabel("Werkzaamheden:");
		add(werkzaamHeden);
		werkzaamHeden.setBounds(120,200,500,40);
		werkzaamHeden.setFont(font);

		onderdelen = new JLabel("Onderdelen:");
		add(onderdelen);
		onderdelen.setBounds(120,250,500,40);
		onderdelen.setFont(font);

		manuren = new JLabel("Manuren:");
		add(manuren);
		manuren.setBounds(120,300,500,40);
		manuren.setFont(font);

		JLabel overig = new JLabel("");
		add(overig);

		setSize(720,585);
		setVisible(true);
	}

	public void createDirs(){  // directory's maken
		File theDir = null;
		for(int i = 0; i <= 1; i++){
			if(i == 0){theDir = new File("Klussen");}
			if(i == 1){theDir = new File("Werknemers");}

			// if the directory does not exist, create it
			if (!theDir.exists()) {
				System.out.println("creating directory: " + theDir);
				boolean result = false;

				try{
					theDir.mkdir();
					result = true;
				} catch(SecurityException se){
					//handle it
				}
				if(result) {
					System.out.println("DIR created");
					theDir = null;
				}
			}
		}
	}

	public void monteursInlezen(){
		monteurs = new ArrayList<Monteur>();
		f = new File("Werknemers/Monteurs.txt");
		if(f.isFile() && f.exists()&& (!(f.length() == 0))){
			try{
				Scanner sc = new Scanner(f);
				while(sc.hasNext()){
					String naam = sc.next();
					String achterNaam = sc.next();
					int ID = Integer.parseInt(sc.next());
					Monteur x = new Monteur(naam, achterNaam, ID);
					monteurs.add(x);
				}
				sc.close();
			}catch(Exception e){System.out.println(e);}
		}else{
			//hardcoded vullen
			Monteur m1 = new Monteur("Joop", "Vriezen", 1);
			Monteur m2 = new Monteur("Rick", "Ossendrijver", 2);
			Monteur m3 = new Monteur("Niek", "deWit", 3);
			monteurs.add(m1);
			monteurs.add(m2);
			monteurs.add(m3);
		}
	}

	public void werkzaamhedenInlezen(){
		f = new File("Werkzaamheden.txt");
		if(f.isFile() && f.exists()&& (!(f.length() == 0))){
			try{
				Scanner sc = new Scanner(f);
				while(sc.hasNext()){
					String omschrijving = sc.next();
					int ID = Integer.parseInt(sc.next());
					Werkzaamheid x = new Werkzaamheid(omschrijving, ID);
					werkzaamheden.add(x);
				}
				sc.close();
			}catch(Exception e){System.out.println(e);}
		}else{
			//hardcoded vullen
			Werkzaamheid w1 = new Werkzaamheid("APK-Keuring", 1);
			Werkzaamheid w2 = new Werkzaamheid("Band vervangen", 2);
			Werkzaamheid w3 = new Werkzaamheid("Remmen stellen", 3);
			werkzaamheden.add(w1);
			werkzaamheden.add(w2);
			werkzaamheden.add(w3);
		}
	}

	public void klussenInlezen(){
		f = new File("Klussen.txt");
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
					klussen.add(x);
				}
				sc.close();
			}catch(Exception e){System.out.println(e);}
		}else{
			//hardcoded vullen
		}
	}

	public static void main(String[] args) {
		new WerkPlaats();
	}

	public void actionPerformed(ActionEvent event){}

	public void vulDatePicker(){ //DatePicker combobox vullen
		String newItem = "";
		try{
			File f = new File("Klussen.txt");
			Scanner sc = new Scanner(f);
			boolean found = false;
			while(sc.hasNext()){
				found = false;
				String datum = sc.next();
				for(int i = 0; i < 6; i++){
					sc.next();
				}
				newItem = datum;
				for(int j = 0; j < DatePicker.getItemCount(); j++){
					if(DatePicker.getItemAt(j).equals(newItem) ){
						found = true;
					}
				}
				if(!found){
					DatePicker.addItem(newItem);
				}
				for(int i = 0; i < 2; i++){
					sc.next();
				}
			}
			sc.close();
		}catch(Exception e){}
	}

	public void vulSelectPart(){
		f = new File("Onderdelenvoorraad/onderdelen.txt");
		if(f.isFile() && f.exists()&& (!(f.length() == 0))){
			try{
				Scanner sc = new Scanner(f);
				while(sc.hasNext()){
					int artNr = Integer.parseInt(sc.next());
					String onderdeelNaam = sc.next();
					if(onderdeelNaam != null){
						selectPart.addItem(artNr+": "+onderdeelNaam);
						parts.add(artNr+": "+onderdeelNaam);
					}else{
						sc.close();
						f = null;
						return;
					}
				}
				sc.close();
			}catch(Exception e){}
		}
	}

	public void vindAgendaItem(){
		try{
			for(Klus k : klussen){
				String datum = k.getDatum();
				String dateSelected = (String)DatePicker.getSelectedItem();
				if(datum.equals(dateSelected)){
					toonAgendaItem(k);
					return;
				}
			}
		}catch(Exception e){}
	}

	public void toonAgendaItem(Klus k){
		//Labels resetten
		klusStatus.setText("Status: ");
		autoNaam.setText("Auto: ");
		monteurNamen.setText("Monteurs: ");
		werkzaamHeden.setText("Werkzaamheden: ");
		onderdelen.setText("Onderdelen: ");
		manuren.setText("Manuren: ");

		// toon status
		klusStatus.setText("Status: "+k.getStatus());

		// toon auto waaraan gewerkt moet worden
		huidigID = k.getID();
		int huidigAutoID = k.getAutoID();
		int id;
		String kenteken = "";
		String autoType = "";
		try{
			File f = new File("Autos");
			sc = new Scanner(f);
			while(sc.hasNext()){
				sc.useDelimiter("\\s*,,\\s*|\\s*;\\s*");
				id = Integer.parseInt(sc.next());
				kenteken = sc.next();
				autoType = sc.next();
			 sc.next();

				if(huidigAutoID == id){
					autoNaam.setText("Auto: "+autoType+" met kenteken "+kenteken);
				}
			}
		}catch(Exception e){e.printStackTrace();}

		// toon monteurnamen
		String toonWerknemers = "";
		String werkers = k.getWerknemers();
		sc = new Scanner(werkers);
		sc.useDelimiter("-");
		for(int i = 0; i < monteurs.size(); i++){
			if(sc.hasNext()){
				int monteurId = Integer.parseInt(sc.next());
				for(Monteur m : monteurs){
					if(m.getID() == monteurId){
						toonWerknemers += m.getNaam()+" "+m.getAchterNaam()+"; ";
						monteurNamen.setText("Monteurs: "+toonWerknemers);
					}
				}
			}
		}

		//toon werkzaamheden
		String toonWerk = "";
		String werkLijst = k.getWerk();
		sc = new Scanner(werkLijst);
		sc.useDelimiter("-");
		for(int i = 0; i < werkzaamheden.size(); i++){
			if(sc.hasNext()){
				int werkId = Integer.parseInt(sc.next());
				for(Werkzaamheid w : werkzaamheden){
					if(w.getID() == werkId){
						toonWerk += w.getOmschrijving()+"; ";
						werkzaamHeden.setText("Werkzaamheden: "+toonWerk);
					}
				}
			}
		}

		//toon onderdelen
		String toonOnderdelen = "";
		try{
			for(int i = 0; i < 20; i++){
				f = new File("Klussen/Klus"+huidigID+"onderdelen.txt");
				sc = new Scanner(f);
				for(int j = 0; j < i;j++){
					sc.next();
				}
				int ondID = Integer.parseInt(sc.next());
				for(String s : parts){
					sc = new Scanner(s);
					sc.useDelimiter(":");
					int foundID = Integer.parseInt(sc.next());
					String ondNaam = sc.next();
					if(ondID == foundID){
						toonOnderdelen += ondNaam+"; ";
						onderdelen.setText("Onderdelen: "+toonOnderdelen);
						sc.close();
					}
				}
			}
		}catch(Exception ex){}

		//toon manuren
		int totaal = 0;
		try{

			sc = new Scanner(new File("Klussen/Klus"+huidigID+"manuren.txt"));
			while(sc.hasNext()){
				int aantal = Integer.parseInt(sc.next());
				totaal += aantal;
				manuren.setText("Manuren: "+totaal);
			}
		}catch(Exception ex){System.out.println(ex);}
	}

	public void vorigeAgendaItem(){
		String dateSelected = (String)DatePicker.getSelectedItem();
		Klus zoeken = null;
		String foundDate;
		for(Klus k : klussen){
			foundDate = k.getDatum();
			if(foundDate.equals(dateSelected)){
				for(Klus k2 : klussen){
					if(k2.getDatum().equals(dateSelected) && k2.getID() < huidigID){
						zoeken = k2;
					}
				}
			}
		}
		if(zoeken == null){
			JOptionPane.showMessageDialog(null, "Dit is de eerste klus van vandaag");
		}else{
			toonAgendaItem(zoeken);
		}
	}

	public void volgendAgendaItem(){
		String dateSelected = (String)DatePicker.getSelectedItem();
		Klus zoeken = null;
		boolean found = false;
		for(Klus k : klussen){
			if(k.getDatum().equals(dateSelected) && k.getID() > huidigID && found == false){
				zoeken = k;
				found = true;
			}
		}
		if(zoeken == null){
			JOptionPane.showMessageDialog(null, "Dit is de laatse klus van vandaag");
		}else{
			toonAgendaItem(zoeken);
		}
	}
}