package Applicatie;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class DataToevoegenGUI extends JFrame implements ActionListener{
	private JButton toevoegen, verwijderen;
	private JComboBox<String> soortObject, cb1;
	private JTextField tf1, tf2;
	private DefaultTableModel model;
	private JTable table;
	private String geselecteerdSoort;
	
	public DataToevoegenGUI(databaseController controller) {			
		setContentPane(new JLabel(""));
		
		toevoegen = new JButton("Toevoegen");	
		toevoegen.addActionListener(e -> {
			if(checkOfIngevuld(geselecteerdSoort)){
				voegToe(controller);
				tekenTabel(geselecteerdSoort, controller);
				JOptionPane.showMessageDialog(null, "Nieuwe data toegevoegd!");
				this.dispose();
			}
		});
		add(toevoegen);
		toevoegen.setBounds(20, 20, 120, 30);
		
		verwijderen = new JButton("Verwijderen");	
		verwijderen.addActionListener(e -> {
			if(checkOfIngevuld(geselecteerdSoort)){
				verwijder(controller);
				tekenTabel(geselecteerdSoort, controller);
				JOptionPane.showMessageDialog(null, "Oude data verwijderd!");
				this.dispose();
			}
		});
		add(verwijderen);
		verwijderen.setBounds(20, 60, 120, 30);
		
		tf1 = new JTextField(20);
		tf1.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				tf1.setBackground(Color.WHITE);
			}
			public void removeUpdate(DocumentEvent e) {}
			public void changedUpdate(DocumentEvent e) {}
		});
		
		tf2 = new JTextField(20);
		tf2.getDocument().addDocumentListener(new DocumentListener() {
		public void insertUpdate(DocumentEvent e) {
			tf2.setBackground(Color.WHITE);
			}
			public void removeUpdate(DocumentEvent e) {}
			public void changedUpdate(DocumentEvent e) {}
		});
		
		String[] autos = new String[20];
		int i = 0;
		for(Auto a : controller.getAutos()){
			autos[i] = a.toString();
			i++;
		}
		cb1 = new JComboBox<String>(autos);
		
		model = new DefaultTableModel();
		table = new JTable(model);
		table.setBounds(20, 100, 460, 600);
		add(table);
		
		String[] comboBoxVelden = {"Klant", "Auto", "Onderhoudsbeurt"};
		soortObject = new JComboBox<String>(comboBoxVelden);
		soortObject.addActionListener(e -> {
			JComboBox<String> cb1 = (JComboBox<String>)e.getSource();
			geselecteerdSoort = (String)(soortObject.getSelectedItem());
			tekenTabel(geselecteerdSoort, controller);
		});

		add(soortObject);
		soortObject.setBounds(160, 20, 150, 30);
		
		setSize(500, 760);
		setVisible(true);
		setResizable(false);
	}
	public void verwijder(databaseController controller){
		if(geselecteerdSoort.equals("Klant")){
			String s = (String)cb1.getSelectedItem();
			Scanner sc = new Scanner(s);
			if(controller.zoekKlant(tf2.getText()) != null && controller.zoekKlant(tf2.getText()).getNaam().equals(tf1.getText()) && controller.zoekKlant(tf2.getText()).getAuto().equals(controller.zoekAuto(sc.next()))){
				try {
					controller.verwijderKlant(controller.zoekKlant(tf2.getText()));
				} catch (IOException e) {
				}
			}else{
				JOptionPane.showMessageDialog(null, "Deze klant bestaat niet!");
			}
			sc.close();
		}else if(geselecteerdSoort.equals("Auto")){
			int i = 0;
			for(Auto a : controller.getAutos()){
				if(a.getVolgNummer() > i){
					i = a.getVolgNummer();
				}
			}
			i++;
			Auto a = new Auto(i, tf1.getText(), tf2.getText());
			if(controller.zoekAuto(tf2.getText()) != null && controller.zoekAuto(tf2.getText()).getMerk().equals(tf1.getText()) &&  controller.zoekAuto(tf2.getText()).getKenteken().equals(tf2.getText()))
				try {
					controller.verwijderAuto(a);
				} catch (Exception e1) {
			}else{
				JOptionPane.showMessageDialog(null, "Deze auto bestaat niet!");
			}
		}else if(geselecteerdSoort.equals("Onderhoudsbeurt")){
			String s = (String)cb1.getSelectedItem();
			Scanner sc = new Scanner(s);
			Auto a = controller.zoekAuto(sc.next());
			sc.close();
			boolean b = false;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date = LocalDate.parse(tf1.getText(), formatter);
			Onderhoudsbeurt oudebeurt = null;
			for(Onderhoudsbeurt o : a.getOnderhoudsbeurten()){
				if(o.getDatum().equals(date)){
					b = true;
					oudebeurt = o;
				}
			}
			if(b){
				try {
					a.verwijderOnderhoudsbeurt(oudebeurt);
				} catch (Exception e1) {
				}
			}
		}
	}
	public void voegToe(databaseController controller){
		if(geselecteerdSoort.equals("Klant")){
			String s = (String)cb1.getSelectedItem();
			Scanner sc = new Scanner(s);
			Klant k = new Klant(tf1.getText(), tf2.getText(), controller.zoekAuto(sc.next()));
			sc.close();
			try {
				controller.voegKlantToe(k);
			} catch (Exception e1) {
			}
		}else if(geselecteerdSoort.equals("Auto")){
			int i = 0;
			for(Auto a : controller.getAutos()){
				if(a.getVolgNummer() > i){
					i = a.getVolgNummer();
				}
			}
			i++;
			Auto a = new Auto(i,tf2.getText() , tf1.getText());
			try {
				controller.voegAutoToe(a);
			} catch (Exception e1) {
			}
		}else if(geselecteerdSoort.equals("Onderhoudsbeurt")){
			String s = (String)cb1.getSelectedItem();
			Scanner sc = new Scanner(s);
			Auto a = controller.zoekAuto(sc.next());
			sc.close();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date = LocalDate.parse(tf1.getText(), formatter);
			int i = 0;
			ArrayList<Onderhoudsbeurt> alleBeurten = new ArrayList<Onderhoudsbeurt>();
			for(Auto a1 : controller.getAutos()){
				for(Onderhoudsbeurt o : a1.getOnderhoudsbeurten()){
					alleBeurten.add(o);
				}
			}
			for(Onderhoudsbeurt o : alleBeurten){
				System.out.println("ding 1");
				if(o.getVolgNummer() > i){
					System.out.println("ding 2 i:" + i);
					i = o.getVolgNummer();
				}
			}
			i++;
			try {
				a.saveOnderhoudsbeurt(new Onderhoudsbeurt(i, date), true);
			} catch (Exception e1) {
			}
		}
	}
	public boolean checkOfIngevuld(String s){
		boolean b = false;
		if(s.equals("Klant")){
			if(!tf1.getText().equals("") && tf1.getText() != null && !tf1.getText().equals("Naam")){
			}else {
				tf1.setBackground(Color.red);
				b = true;
				System.out.println("2");
			}
			if(!tf2.getText().equals("") && tf2.getText() != null && !tf2.getText().equals("E-mail")){
				System.out.println("3");
			}else{
				tf2.setBackground(Color.red);
				b = true;
				System.out.println("4");
			}
		}else if(s.equals("Auto")){
			if(!tf1.getText().equals("") && tf1.getText() != null && !tf1.getText().equals("Merk")){
				b = false;
			}else {
				tf1.setBackground(Color.red);
				b = true;
			}
			if(!tf2.getText().equals("") && tf2.getText() != null && !tf1.getText().equals("Kenteken")){
				b = false;
			}else{
				tf2.setBackground(Color.red);
				b = true;
			}
		}else if(s.equals("Onderhoudsbeurt")){
			if(!tf1.getText().equals("") && tf1.getText() != null && !tf1.getText().equals("Datum (yyyy-MM-dd)")){
				b = false;
			}else {
				tf1.setBackground(Color.red);
				b = true;
			}
		}
		if(b){
			JOptionPane.showMessageDialog(null, "Vul de met rood gevulde velden in AUB.");
			return false;
		}else{
			return true;
		}
	}

	
	public void tekenTabel(String s, InterfacedatabaseController controller){
		if(s.equals("Klant")){
			remove(tf1);
			remove(tf2);
			remove(cb1);
			remove(table);
			repaint();

			tf1.setText("Naam");
			add(tf1);
			tf1.setBounds(160, 60, 150, 30);
			
			tf2.setText("E-mail");
			add(tf2);
			tf2.setBounds(330, 60, 150, 30);
			
			add(cb1);
			cb1.setBounds(330, 20, 150, 30);
		
			model = new DefaultTableModel();
			model.addColumn("Naam");
			model.addColumn("E-mail");
		    model.addColumn("Auto");
		    model.addColumn("Kenteken");
			model.addRow(new Object[]{"Naam", "E-mail", "Auto", "Kenteken"});
			for(Klant k : controller.getKlanten()){
				model.addRow(new Object[]{k.getNaam(), k.getEmail(), k.getAuto().getMerk(), k.getAuto().getKenteken()});
			}
			table = new JTable(model);
			table.setBounds(20, 100, 460, 600);
			add(table);

		}else if(s.equals("Onderhoudsbeurt")){
			remove(tf1);
			remove(tf2);
			remove(cb1);
			remove(table);

			repaint();

			tf1.setText("Datum (yyyy-MM-dd)");
			add(tf1);
			tf1.setBounds(160, 60, 150, 30);
			
			add(cb1);
			cb1.setBounds(330, 20, 150, 30);
			
			model = new DefaultTableModel();
			model.addColumn("Datum (yyyy-MM-dd)");
		    model.addColumn("Auto");
			model.addRow(new Object[]{"Datum (yyyy-mm-dd)", "Kenteken"});
			for(Auto a : controller.getAutos()){
				for(Onderhoudsbeurt o : a.getOnderhoudsbeurten()){
					model.addRow(new Object[]{o.getDatum(), a.getKenteken()});
				}
			}
			table = new JTable(model);
			table.setBounds(20, 100, 460, 600);
			add(table);
		}else if(s.equals("Auto")){
			remove(tf1);
			remove(tf2);
			remove(cb1);
			remove(table);
			repaint();

			tf1.setText("Merk");
			add(tf1);
			tf1.setBounds(160, 60, 150, 30);
			
			tf2.setText("Kenteken");
			add(tf2);
			tf2.setBounds(330, 60, 150, 30);
			
			model = new DefaultTableModel();
			model.addColumn("Auto");
			model.addColumn("Kenteken");
			model.addRow(new Object[]{"Auto", "Kenteken"});
			for(Auto a : controller.getAutos()){
				model.addRow(new Object[]{a.getMerk(), a.getKenteken()});
			}
			table = new JTable(model);
			table.setBounds(20, 100, 460, 600);
			add(table);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {}
}
