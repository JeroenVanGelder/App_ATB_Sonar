package Applicatie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class FactuurApp extends JFrame implements ActionListener {
	private JButton direct, achteraf, verstuurFactuur, annuleren, geefKorting,
			checkVoorFactuurKnop, overzichtGenererenKnop, betaald;
	private JLabel prijs, labkorting, klant, prijsMetKorting,
			labelBetaalMethode;
	private JTextField korting;
	private JComboBox betaalMethode, selectKlant;

	private databaseController deController;

	public FactuurApp() {
		try{
			deController = new databaseController();
		}catch(IOException e){
			
		}
		setContentPane(new JLabel(""));

		klant = new JLabel("Klant:");
		add(klant);
		klant.setBounds(10, 10, 200, 40);
		selectKlant = new JComboBox<String>();
		selectKlant.setBounds(60, 10, 200, 40);
		add(selectKlant);
		selectKlant.addItem("");
		selectKlant.addItem("Onbekend");
		selectKlant.addActionListener(e -> {
			prijs.setText("Te betalen bedrag: €"+berekenBedrag());
		});
		vulSelectKlant();

		checkVoorFactuurKnop = new JButton("<html>Checken en verzenden van facturen</html>");
		checkVoorFactuurKnop.addActionListener(e -> {
			deController.checkVoorFactuur();
		});
		add(checkVoorFactuurKnop);
		checkVoorFactuurKnop.setBounds(340, 70, 140, 50);

		overzichtGenererenKnop = new JButton("<html>Overzicht BTW genereren</html>");
		overzichtGenererenKnop.addActionListener(e -> {

		});
		add(overzichtGenererenKnop);
		overzichtGenererenKnop.setBounds(340, 10, 140, 50);

		direct = new JButton("Direct betalen");
		direct.addActionListener(e -> {
			directBetalen();
		});
		add(direct);
		direct.setBounds(10, 70, 130, 50);

		achteraf = new JButton("Achteraf betalen");
		achteraf.addActionListener(e -> {
			achterafBetalen();
		});
		add(achteraf);
		achteraf.setBounds(160, 70, 130, 50);

		annuleren = new JButton("Annuleren");
		annuleren.addActionListener(e -> {
			// dispose();
			// waarschuwingsbriefSchrijven();
			});
		add(annuleren);
		annuleren.setBounds(380, 260, 100, 30);
		
		prijs = new JLabel("Te betalen bedrag: ");
		add(prijs);
		prijs.setBounds(20, 140, 240, 20);

		labkorting = new JLabel("Percentage korting: ");
		add(labkorting);
		labkorting.setBounds(20, 170, 200, 20);

		korting = new JTextField(5);
		korting.setText("0");
		add(korting);
		korting.setBounds(150, 170, 40, 20);

		geefKorting = new JButton("Bereken met korting");
		add(geefKorting);
		geefKorting.setBounds(200, 170, 180, 20);
		geefKorting.addActionListener(e -> {
			geefKorting();
		});

		prijsMetKorting = new JLabel("Te betalen bedrag met ev korting: " + berekenBedrag());
		add(prijsMetKorting);
		prijsMetKorting.setBounds(20, 200, 240, 20);
		
		labelBetaalMethode = new JLabel("Wat is de gewenste betalingsmethode:");
		add(labelBetaalMethode);
		labelBetaalMethode.setBounds(20, 230, 240, 20);
		labelBetaalMethode.setVisible(false);

		verstuurFactuur = new JButton("Verstuur factuur");
		add(verstuurFactuur);
		verstuurFactuur.setBounds(20, 230, 150, 30);
		verstuurFactuur.setVisible(false);
		verstuurFactuur.addActionListener( e ->{
			verstuurFactuur();
		});
		String[] betaalMethodes = { "kies betaalmethode", "contant", "PIN",
		"Credit Card" };
		
		betaalMethode = new JComboBox(betaalMethodes);
		add(betaalMethode);
		betaalMethode.setBounds(20, 260, 150, 30);
		betaalMethode.setVisible(false);
		
		betaald = new JButton("Er is betaald");
		add(betaald);
		betaald.setBounds(180, 260, 150, 30);
		betaald.setVisible(false);
		
		setSize(500, 335);
		setVisible(true);
		setResizable(true);

	}
	private void checkVoorFactuur() { 
//		deController.checkVoorFactuur(); 
	}
	
	private void verstuurFactuur() { 
		String s = korting.getText(); 
		Double korting = Double.parseDouble(s); 
		double eraf = berekenBedrag() / 100 * korting; 
		double prijs = (berekenBedrag() - eraf); 
		Scanner sc = new Scanner((String)selectKlant.getSelectedItem());
		while(sc.hasNext()){
			sc.next();
			System.out.println(deController.zoekKlant(sc.next()));
		}
		//Factuur f = new Factuur(k, true, prijs, 0, 0, deController.getfacturenLijstSize());
		//deController.getBrievenService().genereerFactuurBrief(f); 
	}

	public double berekenBedrag() {
		Scanner sc = new Scanner((String)selectKlant.getSelectedItem());
		double b = 0;
		while(sc.hasNext()){
			sc.next();
			b = deController.berekenBedrag(deController.dienstenMetKlant(deController.zoekDienstenDieNietGebondenZijnAanFactuur(), deController.zoekKlant(sc.next(), true)));
		}
		System.out.println(b);
		return b;
	}

	public void directBetalen() {
		direct.setEnabled(false);
		achteraf.setEnabled(true);
		labelBetaalMethode.setVisible(true);
		verstuurFactuur.setVisible(false);
		betaald.setVisible(true);
		betaalMethode.setVisible(true);

	}

	public void achterafBetalen() {
		achteraf.setEnabled(false);
		direct.setEnabled(true);
		labelBetaalMethode.setVisible(false);
		verstuurFactuur.setVisible(true);
		betaald.setVisible(false);
		betaalMethode.setVisible(false);

	}

	public void geefKorting() {
		if (korting.getText().equals("0")) {
		} else {
			String s = korting.getText();
			Double korting = Double.parseDouble(s);
			double eraf = berekenBedrag() / 100 * korting;
			prijsMetKorting.setText("Te betalen bedrag met ev korting: "
					+ (berekenBedrag() - eraf));
		}
	}

	public void vulSelectKlant() { // klanten combobox vullen
		try {
			FileReader fr = new FileReader("Klanten");
			BufferedReader br = new BufferedReader(fr);
			Scanner sc = new Scanner(br);
			boolean found = false;
			sc.useDelimiter("\\s*,,\\s*|\\s*;\\s*");
			while (sc.hasNext()) {

				String klantNaam = sc.next();
				sc.next();
				int klantNr = Integer.parseInt(sc.next());
				String newItem = klantNr + ": " + klantNaam;
				for (int j = 0; j < selectKlant.getItemCount(); j++) {
					if (selectKlant.getItemAt(j).equals(newItem)) {
						found = true;
					}
				}
				if (!found) {
					selectKlant.addItem(newItem);
				}
			}
			sc.close();
		} catch (Exception e) {
		}
	}

	public String getOnderdelen() {
		String s = "veer";
		return s;
	}

	public static void main(String args[]) {
		FactuurApp mf = new FactuurApp();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}