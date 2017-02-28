package Applicatie;

import java.awt.event.*;

import javax.swing.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

public class VoorraadBeheer extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private File f;
	private File theDir;
	private JButton voorraadBijwerken, itemToevoegen, itemVerwijderen;
	private JLabel lab5, lab6, lab7;
	private ArrayList<Brandstof> brandstoffen = new ArrayList<Brandstof>();
	private ArrayList<Onderdeel> onderdelen = new ArrayList<Onderdeel>();
	private JComboBox<String> selectItem, chooseItem, deleteItem;
	private JTextField tf1, tf2;
	private DefaultTableModel model;
	private JTable table;
	private Timer t = new Timer(1000, this);
	private Timer simulatieTimer = new Timer(5000, this);
	private Brandstof b, d;
	private Onderdeel a, a2, a3, a4;
	private boolean tonen = false;



	public void createDirs(){  // directory's maken
		for(int i = 0; i <= 3; i++){
			if(i == 0){theDir = new File("Bestellingen");}
			if(i == 1){theDir = new File("Brandstofvoorraad");}
			if(i == 2){theDir = new File("Onderdelenvoorraad");}
			if(i == 3){theDir = new File("Tankbeurten");}

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

	public void getFiles(){ //bij te werken items opzoeken en in eerste combobox zetten
		try{
			f = new File("Bestellingen/");
			File[] bestellingen = f.listFiles();

			for (int i = 0; i < bestellingen.length; i++) {
				if(bestellingen[i].isFile()) {
					String newItem = (String)bestellingen[i].getName().replaceAll(".txt", "");
					for(int j = 0; j < selectItem.getItemCount(); j++){
						if(selectItem.getItemAt(j).equals(newItem) ){
							return;
						}
					}
					selectItem.addItem(newItem);
				}
			}
		}catch(Exception e){}
	}

	public void vulVerwijderLijst(){  //delete combobox vullen
		try{
			for(int c = 0; c < 2; c++){
				if(c == 0){f = new File("Brandstofvoorraad/");}
				else if(c == 1){f = new File("Onderdelenvoorraad/");}
				File[] items = f.listFiles();
				for (int i = 0; i < items.length; i++) {
					if(items[i].isFile()) {
						String newItem = (String)items[i].getName().replaceAll(".txt", "");
						for(int j = 0; j < deleteItem.getItemCount(); j++){
							if(deleteItem.getItemAt(j).equals(newItem) ){
								return;
							}
						}
						if(!(newItem.equals("onderdelen") || newItem.equals("brandstoffen"))){
							deleteItem.addItem(newItem);
						}
					}
				}
				f = null;
				items = null;
			}
		}catch(Exception e){}
	}

	public void brandstoffenInlezen(){ // brandstoffen uit tekstfiles halen en anders nieuwe aanmaken
		f = new File("Brandstofvoorraad/brandstoffen.txt");
		if(f.isFile() && f.exists()&& (!(f.length() == 0))){
			try{
				Scanner sc = new Scanner(f);
				while(sc.hasNext()){
					String typeNaam = sc.next();//br.readLine();
					int TSICNaam = Integer.parseInt(sc.next());//br.readLine());
					if(typeNaam != null){
						Brandstof x = new Brandstof(typeNaam, TSICNaam);
						brandstoffen.add(x);
						for(Brandstof b : brandstoffen){
							if(b.getLiters() != (int)b.getLiters()){
								b.setLiters(5000);
							}
						}
						if(!sc.hasNext()){
							sc.close();
							f = null;
							return;
						}
					}else{
						sc.close();
						f = null;
						return;
					}
				}
				sc.close();
				f = null;
			}catch(Exception e){System.out.println(e);}
		}else{
			f = null;
			b = new Brandstof("benzine", 12345678);
			brandstoffen.add(b);
			d = new Brandstof("diesel", 87654321);
			brandstoffen.add(d);
		}
	}

	public void onderdelenInlezen(){ //onderdelen uit textfiles halen en anders nieuwe aanmaken
		f = new File("Onderdelenvoorraad/onderdelen.txt");
		if(f.isFile() && f.exists()&& (!(f.length() == 0))){
			try{
				Scanner sc = new Scanner(f);
				while(sc.hasNext()){
					int artNr = Integer.parseInt(sc.next());
					String onderdeelNaam = sc.next();
					if(onderdeelNaam != null){
						Onderdeel x = new Onderdeel(artNr, onderdeelNaam);
						onderdelen.add(x);
						for(Onderdeel o : onderdelen){
							if(o.getArtikelNummer() != (int)o.getArtikelNummer()){
								o.setVoorraad(10);
							}
						}
						if(!sc.hasNext()){
							sc.close();
							return;
						}

					}else{
						sc.close();
						f = null;
						return;
					}
				}
				sc.close();
			}catch(Exception e){System.out.println(e);}
		}else{
			f = null;
			a = new Onderdeel(1, "Wiel");
			a2 = new Onderdeel(2, "Uitlaat");
			a3 = new Onderdeel(3, "Raam");
			a4 = new Onderdeel(4, "Ruitenwisser");
			onderdelen.add(a);
			onderdelen.add(a2);
			onderdelen.add(a3);
			onderdelen.add(a4);
		}
	}

	public VoorraadBeheer(){
		createDirs();               //INIT
		brandstoffenInlezen();      //INIT
		onderdelenInlezen();        //INIT
		setContentPane(new JLabel(""));

		selectItem = new JComboBox<String>();
		selectItem.setBounds(150, 20, 200, 40);
		add(selectItem);
		selectItem.addItem("Kies Item");

		voorraadBijwerken = new JButton("voorraad bijwerken");
		voorraadBijwerken.setBounds(370, 20, 200, 40);
		add(voorraadBijwerken);
		voorraadBijwerken.addActionListener(this);

		deleteItem = new JComboBox<String>();
		deleteItem.setBounds(590, 20, 200, 40);
		add(deleteItem);
		deleteItem.addItem("Kies Item");

		itemVerwijderen = new JButton("Verwijderen");
		itemVerwijderen.setBounds(810, 20, 200, 40);
		add(itemVerwijderen);
		itemVerwijderen.addActionListener(this);

		lab5 = new JLabel("Item toevoegen: ");
		lab5.setBounds(20, 80, 200, 40);
		add(lab5);

		chooseItem = new JComboBox<String>();
		chooseItem.setBounds(150, 80, 200, 40);
		add(chooseItem);
		chooseItem.addItem("Brandstof");
		chooseItem.addItem("Onderdeel");
		chooseItem.addActionListener(this);

		lab6 = new JLabel("Naam: ");
		lab6.setBounds(370, 50, 200, 40);
		add(lab6);

		tf1 = new JTextField(10);
		tf1.setBounds(370, 80, 200, 40);
		add(tf1);

		lab7 = new JLabel("TSIC:");
		lab7.setBounds(590, 50, 200, 40);
		add(lab7);

		tf2 = new JTextField(10);
		tf2.setBounds(590, 80, 200, 40);
		add(tf2);

		itemToevoegen = new JButton("item toevoegen");
		itemToevoegen.setBounds(810, 80, 200, 40);
		add(itemToevoegen);
		itemToevoegen.addActionListener(this);

		model = new DefaultTableModel();
		table = new JTable(model);
		table.setBounds(20, 140, 990, 600);
		add(table);
		model.addColumn("Naam");
		model.addColumn("Nummer");
		model.addColumn("Voorraad");
		model.addRow(new Object[]{"Naam", "Nummer", "Voorraad"});
		werkTextAreaBij();

		t.start();
		simulatieTimer.start();

		setSize(1100,1000);
		setVisible(true);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void simulatieVoorraadGebruik(){ // simuleren gebruik onderdelen/brandstoffen
		for(Brandstof b : brandstoffen){
			if(b.getType().equals("benzine")){b.verwerkTankBeurt(125);}
			if(b.getType().equals("diesel")) {b.verwerkTankBeurt(100);}
		}
		for(Onderdeel o : onderdelen){
			if(o.getArtikelNummer() == 1){o.gebruikOnderdeel();}
			if(o.getArtikelNummer() == 3){o.gebruikOnderdeel();}
		}
	}

	public void werkTextAreaBij(){
		model.setRowCount(1); // de getoonde items up to date houden
		for(Brandstof b : brandstoffen){
			model.addRow(new Object[]{b.getType(),b.getTSIC(),b.getLiters()});
		}
		for(Onderdeel o : onderdelen){
			model.addRow(new Object[]{o.getOnderdeelNaam(), o.getArtikelNummer(), o.getVoorraad()});
		}
	}

	public void actionPerformed(ActionEvent event){
		String selected = (String)chooseItem.getSelectedItem();
		werkTextAreaBij();
		if(event.getSource() == itemToevoegen){
			if(tf1.getText().equals("")){
				JOptionPane.showMessageDialog(null, "vul een naam in onder naam");
				return;
			}
			if(selected == "Brandstof"){ // brandstof toevoegen
				String naam = tf1.getText().replaceAll(" ","");
				int TSIC;
				boolean TSICGetal = false;
				try{
					TSIC = Integer.parseInt(tf2.getText());
					TSICGetal = true;
				}catch(Exception e){JOptionPane.showMessageDialog(null, "Vul een getal in bij TSIC");return;} //gebruiker attenderen dat er een getal ingevuld moet worden
				TSIC = Integer.parseInt(tf2.getText());
				if(!naam.equals("") && TSICGetal){
					for(Brandstof b : brandstoffen){ // kijken of de brandstof die toegevoegd wordt nog niet bestaat
						if(b.getType().equals(naam)){
							JOptionPane.showMessageDialog(null, "Deze brandstof bestaat al");
							return;
						}
					}
					Brandstof nieuw = new Brandstof(naam, TSIC);
					brandstoffen.add(nieuw);                        //brandstof aan ArrayList toevoegen
					deleteItem.addItem(naam);                       //brandstof aan verwijder lijst toevoegen
					//j.showMessageDialog(null, "Brandstof "+naam+" succesvol toegevoegd");
				}
			}else{
				String naam = tf1.getText(); // onderdeel toevoegen
				int artikelNummer;
				try{
					artikelNummer = Integer.parseInt(tf2.getText());
				}catch(Exception e){JOptionPane.showMessageDialog(null, "Vul een getal in bij Artikelnummer");return;}
				artikelNummer = Integer.parseInt(tf2.getText());
				if(!naam.equals("") && artikelNummer == (int)artikelNummer){
					for(Onderdeel o : onderdelen){  // kijken of het onderdeel dat toegevoegd wordt nog niet bestaat
						if(o.getArtikelNummer() == artikelNummer){
							JOptionPane.showMessageDialog(null, "Dit artikel bestaat al");
							return;
						}
					}
					Onderdeel nieuw = new Onderdeel(artikelNummer, naam);
					onderdelen.add(nieuw);                                 // onderdeel aan ArrayList toevoegen
					deleteItem.addItem("artikel-"+artikelNummer);          // onderdeel aan verwijder lijst toevoegen
					JOptionPane.showMessageDialog(null, "Onderdeel "+nieuw.getOnderdeelNaam()+" succesvol toegevoegd");
				}
			}
			tf1.setText("");
			tf2.setText("");
		}
		if(event.getSource() == itemVerwijderen){
			String verwijder = (String)deleteItem.getSelectedItem();
			boolean verwijderBrandstof = false;
			for(Brandstof b : brandstoffen){
				if(verwijder.equals(b.getType())){
					verwijderBrandstof = true;
				}
			}
			if(verwijderBrandstof){ // hier verwijderen we de brandstof
				boolean verwijderd = false;
				for(Brandstof b : brandstoffen){
					if(b.getType().equals(verwijder)){
						verwijderd = b.verwijderBrandstof(verwijder);
						if(verwijderd){
							brandstoffen.remove(b);
							verwijderd = false;
							deleteItem.removeItemAt(deleteItem.getSelectedIndex());
							return;
						}
					}
				}
			}else{
				boolean verwijderd = false;  // hier verwijderen we het onderdeel
				for(Onderdeel o : onderdelen){
					String vergelijk = "artikel-"+o.getArtikelNummer();
					if(vergelijk.equals(verwijder)){
						verwijderd = o.verwijderOnderdeel(o.getArtikelNummer());
						if(verwijderd){
							onderdelen.remove(o);
							verwijderd = false;
							deleteItem.removeItemAt(deleteItem.getSelectedIndex());
							return;
						}
					}
				}
			}
		}
		if(event.getSource() == chooseItem){  // de labels boven de twee inputsfields wijzigen wanneer er een andere optie geselecteerd wordt
			if(selected == "Brandstof"){
				lab7.setText("TSIC: ");
			}else{
				lab7.setText("artikelnummer: ");
			}
		}
		if(event.getSource() == t){  // timer die er voor zorgt dat er gecontroleerd wordt of er nieuwe items bijgewerkt kunnen worden
			getFiles();
			vulVerwijderLijst();
		}
		if(event.getSource() == simulatieTimer){ // simuleert het gebruik van onderdelen en brandstof
			simulatieVoorraadGebruik();
			if(tonen){
				werkTextAreaBij();
			}
		}
		type = (String)selectItem.getSelectedItem(); // Kijken welk item er geselecteerd is
		if(event.getSource() == voorraadBijwerken){
			int levering = 0;
			try{
				f = new File("Bestellingen/"+type+".txt");
				if(f.exists() && f.isFile()) {
					Scanner sc = new Scanner(f);
					int gelezenAantal = Integer.parseInt(sc.next());
					levering = gelezenAantal;
					sc.close();
				}else{
					if(type.equals("Kies Item")){
						return;
					}else{
						System.out.println("Er is geen order geplaats dus er kan ook geen levering hebben plaats gevonden");
						return;
					}
				}
			}catch(Exception e){System.out.println(e);}
			try{
				String bestand = "";
				boolean brandstof = false;
				for(Brandstof b : brandstoffen){  // kijken of we een brandstof of een onderdeel moeten bijwerken
					if(type.equals(b.getType())){
						brandstof = true;
					}
				}
				if(brandstof){
					bestand = "Brandstofvoorraad/";
				}else{
					bestand = "Onderdelenvoorraad/";
				}
				if(levering == (int)levering){
					try{
						f = new File(bestand+type+".txt");
						int huidigeVoorraad = 0;
						if (f.exists() && f.isFile()) {
							Scanner sc = new Scanner(f);
							int gelezenAantal = Integer.parseInt(sc.next());
							huidigeVoorraad = gelezenAantal;
							sc.close();
						}
						FileWriter fw = new FileWriter(f);
						PrintWriter pw = new PrintWriter(fw);
						int newVoorraad = huidigeVoorraad + levering;
						pw.println(newVoorraad);
						pw.close();
						verwijderOrderBestand(); // als het bijwerken is gelukt wordt het order bestand verwijderd
					}catch(Exception e){System.out.println(e);}
				}
			}catch(Exception e){System.out.println(e);}
		}
	}

	public void verwijderOrderBestand(){  // weggooien orderbestand zodat er weer plaats in om een nieuwe order te plaatsen
		try{
			f = new File("Bestellingen/"+type+".txt");
			if(f.delete()){
				System.out.println("Voorraad bijgewerkt en lopende order verwijderd");
				selectItem.removeItemAt(selectItem.getSelectedIndex());
			}
		}catch(Exception e){System.out.println("Geen orderfile gevonden");};
	}
}

