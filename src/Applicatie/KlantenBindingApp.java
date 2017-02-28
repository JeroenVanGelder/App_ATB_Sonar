package Applicatie;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class KlantenBindingApp extends JFrame implements ActionListener {
	
	private JButton checkKlanten, verzend, toevoegen, tekstonderhoudsbeurtknop, tekst2maandenknop;
	private JLabel regelmatig, bezoeken, maanden, mailonderhoudsbeurt, mailonderhoudsbeurt2, mail2maanden;
	private JTextField aantalBezoeken, aantalMaanden;
	private DefaultTableModel model, model2;
	private JTable table, table2;
	private databaseController controller;
	
	public KlantenBindingApp() {
		try{
			controller = new databaseController();
		}catch(IOException e){
			
		}
			
		setContentPane(new JLabel(""));
		
		regelmatig = new JLabel("Regelmatig betekend:"); 
		add(regelmatig); 
		regelmatig.setBounds(10, 10, 140, 20);
		
		aantalBezoeken = new JTextField(2);
		aantalBezoeken.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				aantalBezoeken.setBackground(Color.WHITE);
			}
			public void removeUpdate(DocumentEvent e) {}
			public void changedUpdate(DocumentEvent e) {}
		});
		add(aantalBezoeken);
		aantalBezoeken.setBounds(145, 10, 20, 20);
		
		bezoeken = new JLabel("bezoeken in");
		add(bezoeken);
		bezoeken.setBounds(175, 10, 80, 20);
		
		aantalMaanden = new JTextField(2);
		aantalMaanden.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				aantalMaanden.setBackground(Color.WHITE);
			}
			public void removeUpdate(DocumentEvent e) {}
			public void changedUpdate(DocumentEvent e) {}
		});
		add(aantalMaanden);
		aantalMaanden.setBounds(250, 10, 20, 20);
		
		maanden = new JLabel("maanden.");
		add(maanden);
		maanden.setBounds(275, 10, 75, 20);
	
		checkKlanten = new JButton("Check voor klanten");
		checkKlanten.addActionListener(e -> {
			boolean b = false;
			if(aantalBezoeken.getText().equals("") || !aantalBezoeken.getText().matches("\\d+")){
				aantalBezoeken.setBackground(Color.red);
				b = true;
			}
			if(aantalMaanden.getText().equals("") || !aantalMaanden.getText().matches("\\d+")){
				aantalMaanden.setBackground(Color.red);
				b = true;
			}
			if(b){
				JOptionPane.showMessageDialog(null, "Vul een getal in, in de met rood gevulde velden in AUB.");
			}else{
				controller.setAantalBezoeken(Integer.parseInt(aantalBezoeken.getText()));
				controller.setOverEenPeriodeVan(Integer.parseInt(aantalMaanden.getText()));
				tekenLijstVanKlanten(controller.check2MaandenKlanten(), true);
				tekenLijstVanKlanten(controller.checkOnderhoudKlanten(), false);
			}

		});
		add(checkKlanten);
		checkKlanten.setBounds(20, 55, 150, 30);
		
		verzend = new JButton("Verzend e-mails");	
		verzend.addActionListener(e -> {
			int i = 0;
			try{
				for(Klant k : controller.check2MaandenKlanten()){
					controller.verzendMail(controller.genereer2MaandenMail(k));
					i++;
				}
				for(Klant k : controller.checkOnderhoudKlanten()){
					controller.verzendMail(controller.genereerOnderhoudMail(k));
					i++;
				}
			}catch(IOException e2){
				
			}
			finally{
				JOptionPane.showMessageDialog(null, "Er zijn " + i + " mails verzonden!");
			}
		});
		add(verzend);
		verzend.setBounds(220, 55, 150, 30);
		
		toevoegen = new JButton("<html>Klanten, auto's, of onderhoudsbeurten toevoegen/verwijderen</html>");
		toevoegen.addActionListener(e -> {
			new DataToevoegenGUI(controller);
		});
		add(toevoegen);
		toevoegen.setBounds(400, 10, 150, 75);
		
		mailonderhoudsbeurt = new JLabel("Klanten die in aanmerking komen");
		add(mailonderhoudsbeurt);
		mailonderhoudsbeurt.setBounds(10, 95, 200, 40);
		
		mailonderhoudsbeurt2 = new JLabel("voor een e-mail over een onderhoudsbeurt:");
		add(mailonderhoudsbeurt2);
		mailonderhoudsbeurt2.setBounds(10, 95, 250, 80);
		
		mail2maanden = new JLabel("<html> Klanten die in aanmerking komen voor een e-mail over het niet meer regelmatig bezoeken van de zaak:<html>");
		add(mail2maanden);
		mail2maanden.setBounds(340, 100, 250, 60);
		
		model = new DefaultTableModel();
		table = new JTable(model);
		table.setBounds(10, 175, 300, 300);
		add(table);
		model.addColumn("Naam");
		model.addColumn("E-mail");
	    model.addColumn("Auto");
	    model.addColumn("Kenteken");
		model.addRow(new Object[]{"Naam", "E-mail", "Auto", "Kenteken"});
		
		model2 = new DefaultTableModel();
		table2 = new JTable(model2);
		table2.setBounds(340, 175, 300, 300);
		add(table2);
		model2.addColumn("Naam");
		model2.addColumn("E-mail");
	    model2.addColumn("Auto");
	    model2.addColumn("Kenteken");
		model2.addRow(new Object[]{"Naam", "E-mail", "Auto", "Kenteken"});
		
		tekstonderhoudsbeurtknop = new JButton("Tekst van een mail schrijven of aanpassen");
		tekstonderhoudsbeurtknop.addActionListener(e -> {
			new MailAanpassenGUI(true, controller);
		});
		add(tekstonderhoudsbeurtknop);
		tekstonderhoudsbeurtknop.setBounds(10, 500, 300, 35);
		
		tekst2maandenknop = new JButton("Tekst van een mail schrijven of aanpassen");
		tekst2maandenknop.addActionListener(e -> {
			new MailAanpassenGUI(false, controller);
		});
		add(tekst2maandenknop);
		tekst2maandenknop.setBounds(340, 500, 300 , 35);
	
		setSize(660, 600);
		setVisible(true);
		setResizable(false);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		
	}
	public void tekenLijstVanKlanten(ArrayList<Klant> klanten, boolean b){
		model = new DefaultTableModel();
		model.addColumn("Naam");
		model.addColumn("E-mail");
	    model.addColumn("Auto");
	    model.addColumn("Kenteken");
		model.addRow(new Object[]{"Naam", "E-mail", "Auto", "Kenteken"});
		if(b){
			remove(table);
			repaint();
			for(Klant k : klanten){
				model.addRow(new Object[]{k.getNaam(), k.getEmail(), k.getAuto().getMerk(),  k.getAuto().getKenteken()});
			}
			table = new JTable(model);
			table.setBounds(340, 175, 300, 300);

			table.setPreferredScrollableViewportSize(table.getPreferredSize());
			add(table);
		}else{
			remove(table2);
			repaint();
			for(Klant k : klanten){
				model.addRow(new Object[]{k.getNaam(), k.getEmail(), k.getAuto().getMerk(),  k.getAuto().getKenteken()});
			}
			table2 = new JTable(model);
			table2.setBounds(10, 175, 300, 300);
			add(table2);
		}
	}
	public void actionPerformed(java.awt.event.ActionEvent e) {
	}

	public static void main(String[] args){
		new KlantenBindingApp();
	}
}
