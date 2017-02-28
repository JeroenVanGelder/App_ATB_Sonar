package Applicatie;

import java.awt.event.*;

import javax.swing.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

import javax.swing.JOptionPane;

public class NewKlus extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String date = "", status = "", werk = "", werkNemers = "";
	private int klantID, autoID,  ID;
	private boolean cancel = true;
	private JLabel klant, auto, id, monteur, werkzaamheid, datum;
	private JTextField tf3;
	private JButton toevoegen, annuleren, addMonteur, addWerk;
	private JComboBox<String> selectKlant, selectAuto, selectMonteur, selectWerkzaamheid, selectDag, selectMaand, selectJaar;
	private ArrayList<Klus> klussen = new ArrayList<Klus>();



	public NewKlus(int size, ArrayList<Klus> k) {
		klussen = k;
		setContentPane(new JLabel(""));
		setTitle("Klus toevoegen");

		klant = new JLabel("Klant:");
		add(klant);
		klant.setBounds(10,50,200,40);
		selectKlant = new JComboBox<String>();
		selectKlant.setBounds(250, 50, 200, 40);
		add(selectKlant);
		selectKlant.addItem("");
		selectKlant.addItem("Onbekend");
		selectKlant.addActionListener((ActionEvent e) -> {
			String selectedKlant = (String)selectKlant.getSelectedItem();
			if(selectedKlant.equals("")){
				return;
			}
		    if(selectedKlant.equals("Onbekend")){
				vulSelectAuto(true);
			}else{
				vulSelectAuto(false);
			}
		});
		vulSelectKlant();

		auto = new JLabel("Auto:");
		add(auto);
		auto.setBounds(10,100,200,40);
		selectAuto = new JComboBox<String>();
		selectAuto.setBounds(250, 100, 200, 40);
		add(selectAuto);
		selectAuto.addItem("");
		selectAuto.addActionListener((ActionEvent e) -> {
			String selectedAuto = (String)selectAuto.getSelectedItem();
			if(selectedAuto.equals("")){
				return;
			}
			autoID = Integer.parseInt(selectedAuto.replaceAll(":(.*)", ""));
		});

		monteur = new JLabel("Monteur:");
		add(monteur);
		monteur.setBounds(10,150,200,40);
		selectMonteur = new JComboBox<String>();
		selectMonteur.setBounds(250, 150, 200, 40);
		add(selectMonteur);
		selectMonteur.addItem("");
		vulSelectMonteur();
		addMonteur = new JButton("+");
		add(addMonteur);
		addMonteur.setBounds(450,150,50,40);
		addMonteur.addActionListener((ActionEvent e) -> {
			String werkNemer = (String)selectMonteur.getSelectedItem();
			if(!werkNemer.equals("")){
				setWerkNemers(werkNemer);
				selectMonteur.removeItemAt(selectMonteur.getSelectedIndex());
				JOptionPane.showMessageDialog(null, "Werknemer toegevoegd");
			}else{
				JOptionPane.showMessageDialog(null, "Selecteer eerst een monteur");
			}
		});


		werkzaamheid = new JLabel("Werkzaamheid");
		add(werkzaamheid);
		werkzaamheid.setBounds(10,200,200,40);
		selectWerkzaamheid = new JComboBox<String>();
		selectWerkzaamheid.setBounds(250, 200, 200, 40);
		add(selectWerkzaamheid);
		selectWerkzaamheid.addItem("");
		vulSelectWerkzaamheid();
		addWerk = new JButton("+");
		add(addWerk);
		addWerk.setBounds(450,200,50,40);
		addWerk.addActionListener((ActionEvent e) -> {
			String werk = (String)selectWerkzaamheid.getSelectedItem();
			if(!werk.equals("")){
				setWerk(werk);
				//selectWerkzaamheid.removeItemAt(selectWerkzaamheid.getSelectedIndex()); ligt er aan of dubbele werkzaamheden moeten kunnen??
				JOptionPane.showMessageDialog(null, "Werkzaamheid toegevoegd");
			}else{
				JOptionPane.showMessageDialog(null, "Selecteer eerst een werkzaamheid");
			}
		});


		id = new JLabel("ID:");
		add(id);
		id.setBounds(10,250,200,40);
		tf3 = new JTextField();
		add(tf3);
		tf3.setEditable(false);
		tf3.setBounds(250,250,200,40);
		tf3.setText(size+1+"");

		datum = new JLabel("Datum:");
		add(datum);
		datum.setBounds(10,300,200,40);
		selectDag = new JComboBox<String>();
		selectDag.setBounds(250, 300, 50, 40);
		add(selectDag);
		selectDag.addItem("");
		selectMaand = new JComboBox<String>();
		selectMaand.setBounds(310, 300, 50, 40);
		add(selectMaand);
		selectMaand.addItem("");
		selectJaar = new JComboBox<String>();
		selectJaar.setBounds(370, 300, 80, 40);
		add(selectJaar);
		selectJaar.addItem("");
		vulDatums(); 


		toevoegen = new JButton("Toevoegen");
		toevoegen.setBounds(10,350,150,40);
		add(toevoegen);
		toevoegen.addActionListener((ActionEvent e) -> {
			cancel = false;
			try{

			}catch(Exception ex){System.out.println(ex); cancel = false;}
			try{
				ID = Integer.parseInt(tf3.getText());
			}catch(Exception ex){
				JOptionPane.showMessageDialog(null, "Veld 'ID' moet een numerieke waarde zijn");
				return;
			}
			String checkKlant = (String)selectKlant.getSelectedItem();
			if(checkKlant.equals("")){
				JOptionPane.showMessageDialog(null, "Selecteer eerst een klant");
				return;
			}
			String checkAuto = (String)selectAuto.getSelectedItem();
			if(checkAuto.equals("")){
				JOptionPane.showMessageDialog(null, "Selecteer eerst een auto");
				return;
			}
			if(werk.equals("")){
				JOptionPane.showMessageDialog(null, "Selecteer eerst een werkzaamheid");
				return;
			}
			if(werkNemers.equals("")){
				JOptionPane.showMessageDialog(null, "Selecteer eerst een werknemer");
				return;
			}
			String checkDag = (String)selectDag.getSelectedItem();
			if(checkDag.equals("")){
				JOptionPane.showMessageDialog(null, "Selecteer eerst een dag");
				return;
			}
			String checkMaand = (String)selectMaand.getSelectedItem();
			if(checkMaand.equals("")){
				JOptionPane.showMessageDialog(null, "Selecteer eerst een maand");
				return;
			}
			String checkJaar = (String)selectJaar.getSelectedItem();
			if(checkJaar.equals("")){
				JOptionPane.showMessageDialog(null, "Selecteer eerst een jaar");
				return;
			}

			setNewKlus();
			this.processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));

		});

		annuleren = new JButton("Annuleren");
		add(annuleren);
		annuleren.setBounds(340,350,150,40);
		annuleren.addActionListener((ActionEvent e) -> {
			cancel = true;
			setNewKlus();
			this.processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
		});

		setSize(600,500);
		setVisible(true);
	}

public void setWerkNemers(String w){
	String wNr = w.replaceAll(":(.*)","" );
	if(werkNemers.equals("")){
		werkNemers = wNr;
	}else{
		werkNemers += "-"+wNr;
	}
}

public void setWerk(String w){
	String wr = w.replaceAll(":(.*)","" );
	if(werk.equals("")){
		werk = wr;
	}else{
		werk += "-"+wr;
	}
}

public int getID(){
	return ID;
}

public boolean getCancel(){
	return cancel;
}

public void actionPerformed(ActionEvent event){}

public void setNewKlus(){
	try{
		if(!getCancel()){
			if(klussen.size() > 0){
				for(Klus k : klussen){
					if(k.getID() == ID){
						JOptionPane.showMessageDialog(null, "Klus niet toegevoegd want er bestaat al een klus met ID: "+ID);
						return;
					}
				}
			}
			status = "niet-afgerond";
			date = (String)selectDag.getSelectedItem()+"-"+(String)selectMaand.getSelectedItem()+"-"+(String)selectJaar.getSelectedItem();
			Klus nieuw = new KlusBuilder().setD(date).setS(status).setkID(klantID).setaID(autoID).setW(werk).setwN(werkNemers).setI(ID).setI2(0).createKlus();
			klussen.add(nieuw);
		}else{
			JOptionPane.showMessageDialog(null, "Klus toevoegen geannuleerd");
		}
	}catch(Exception e){};
}

public void vulSelectKlant(){ //klanten combobox vullen
	try{
		FileReader fr = new FileReader("Klanten");
		BufferedReader br = new BufferedReader(fr);
		Scanner sc = new Scanner(br);
		boolean found = false;
		sc.useDelimiter("\\s*,,\\s*|\\s*;\\s*");
		while(sc.hasNext()){

			String klantNaam = sc.next();
			sc.next();
			int klantNr = Integer.parseInt(sc.next());
			String newItem = klantNr+": "+klantNaam;
			for(int j = 0; j < selectKlant.getItemCount(); j++){
				if(selectKlant.getItemAt(j).equals(newItem) ){
					found = true;
				}
			}
			if(!found){
				selectKlant.addItem(newItem);
			}
		}
		sc.close();
	}catch(Exception e){}
}

public void vulSelectAuto(boolean b){ //klanten combobox vullen
	boolean found = false;
	int autoNr;
	if(b){
		try{
			File f = new File("Autos");
			Scanner sc = new Scanner(f);
			sc.useDelimiter(";");
			sc.useDelimiter(",,");
			sc.useDelimiter("\\s*,,\\s*|\\s*;\\s*");
			while(sc.hasNext()){
				found = false;
				autoNr = Integer.parseInt(sc.next());
				String kenteken = sc.next();
				String autoType = sc.next();
				String newItem = autoNr+": "+autoType+"--"+kenteken;

				for(int j = 0; j < selectAuto.getItemCount(); j++){
					if(selectAuto.getItemAt(j).equals(newItem) ){
						found = true;
					}
				}
				if(!found){
					selectAuto.addItem(newItem);
				}
			}
			sc.close();
		}catch(Exception e){}
	}else{
		String selected = (String)selectKlant.getSelectedItem();
		if(selected != ""){
			String selectedNew = selected.replaceAll(":(.*)","" );
			int autoVolgNr = Integer.parseInt(selectedNew);
			String newItem = "";
			klantID = autoVolgNr;
			autoID = autoVolgNr;
			try{
				File f = new File("Autos");
				Scanner sc = new Scanner(f);
				found = false;
				sc.useDelimiter(";");
				sc.useDelimiter(",,");
				sc.useDelimiter("\\s*,,\\s*|\\s*;\\s*");
				while(sc.hasNext()){
					found = false;
					autoNr = Integer.parseInt(sc.next());
					String kenteken = sc.next();
					String autoType = sc.next();
					if(autoNr == autoVolgNr){
						newItem = autoNr+": "+autoType+"--"+kenteken;
					};

					for(int j = 0;  j < selectAuto.getItemCount(); j++){
						if(selectAuto.getItemAt(j).equals(newItem) ){
							found = true;
						}
					}
					if(!found){
						selectAuto.addItem(newItem);
					}
				}
				sc.close();
			}catch(Exception e){e.printStackTrace();}
		}
	}
}

public void vulSelectMonteur(){ //werknemers combobox vullen
	String newItem = "";
	try{
		File f = new File("Werknemers/Monteurs.txt");
		Scanner sc = new Scanner(f);
		boolean found = false;
		while(sc.hasNext()){
			String naam = sc.next();
			String aNaam = sc.next();
			int id = Integer.parseInt(sc.next());
			newItem = id+": "+naam+" "+aNaam;
			for(int j = 0; j < selectMonteur.getItemCount(); j++){
				if(selectMonteur.getItemAt(j).equals(newItem) ){
					found = true;
				}
			}
			if(!found){
				selectMonteur.addItem(newItem);
			}
		}
		sc.close();
	}catch(Exception e){}
}

public void vulSelectWerkzaamheid(){ //werkzaamheden combobox vullen
	String newItem = "";
	try{
		File f = new File("Werkzaamheden.txt");
		Scanner sc = new Scanner(f);
		boolean found = false;
		while(sc.hasNext()){
			String omschrijving = sc.next();
			int id = Integer.parseInt(sc.next());
			newItem = id+": "+omschrijving;
			for(int j = 0; j < selectWerkzaamheid.getItemCount(); j++){
				if(selectWerkzaamheid.getItemAt(j).equals(newItem) ){
					found = true;
				}
			}
			if(!found){
				selectWerkzaamheid.addItem(newItem);
			}
		}
		sc.close();
	}catch(Exception e){}
}

public void vulDatums(){ //datum comboboxen vullen
	for(int d = 1; d <= 31; d++){
		selectDag.addItem(d+"");
	}
	for(int m = 1; m <= 12; m++){
		selectMaand.addItem(m+"");
	}
	for(int j = 2015; j <= 2100; j++){
		selectJaar.addItem(j+"");
	}
}

}