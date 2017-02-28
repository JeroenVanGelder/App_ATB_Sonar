package Applicatie;

import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.*;

import java.util.Scanner;

import javax.swing.JOptionPane;

public class MaakOverzicht extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean cancel = true;
	private JLabel maand;
	private JComboBox<String> selectMaand;
	private JButton genereer, annuleren;

	public MaakOverzicht() {
		setContentPane(new JLabel(""));
		setTitle("Overzicht emailen naar Henk");

		maand = new JLabel("Selecteer maand:");
		add(maand);
		maand.setBounds(10,50,200,40);
		
		selectMaand = new JComboBox<String>();
		for(int m = 1; m <= 12; m++){
			selectMaand.addItem(m+"");
		}
		add(selectMaand);
		selectMaand.setBounds(250,50,200,40);

		genereer = new JButton("Genereer");
		genereer.setBounds(10,200,150,40);
		add(genereer);
		genereer.addActionListener((ActionEvent e) -> {
			cancel = false;
			String checkMaand = (String)selectMaand.getSelectedItem();
			if(checkMaand.equals("")){
				JOptionPane.showMessageDialog(null, "Veld 'Maand' mag niet leeg zijn");
				return;
			}
			verzendOverzicht();
			this.processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));

		});

		annuleren = new JButton("Annuleren");
		add(annuleren);
		annuleren.setBounds(340,200,150,40);
		annuleren.addActionListener((ActionEvent e) -> {
			cancel = true;
			verzendOverzicht();
			this.processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
		});

		setSize(500,500);
		setVisible(true);
	}

	public boolean getCancel(){
		return cancel;
	}

	public void actionPerformed(ActionEvent event){}

	public void verzendOverzicht(){
		try{
			if(!getCancel()){
				String m = (String)selectMaand.getSelectedItem();
				int totaal = 0;
				int maand = Integer.parseInt(m);
				if(maand == 1){m = "Januari";}else  if(maand == 2){m = "Februari";}else  if(maand == 3){m = "Maart";}else
				if(maand == 4){m = "April";}else    if(maand == 5){m = "Mei";}else       if(maand == 6){m = "Juni";}else
				if(maand == 7){m = "Juli";}else     if(maand == 8){m = "Augustus";}else  if(maand == 9){m = "September";}else
				if(maand == 10){m = "Oktober";}else if(maand == 11){m = "November";}else if(maand == 12){m = "December";}
				PrintWriter p = new PrintWriter(new FileWriter("InboxHenk/emailNaarHenkMaand-"+maand+".txt"));
				p.println("");
				p.close();
				for(int i = 1; i < 32; i++){
                	File f = new File("Reserveringen/"+m+"/Dag"+i+".txt");
                	Scanner sc = new Scanner(f);
                	PrintWriter pw = new PrintWriter(new FileWriter("InboxHenk/emailNaarHenkMaand-"+maand+".txt", true));
                	int aantal = Integer.parseInt(sc.next()); 
                	totaal += aantal;
                	pw.println("Dag "+i+ ": "+aantal+" van de 50 plaatsen bezet");
                	if(i == 31){
                		int percentage = 100-(totaal*100/1550);
                		pw.println("");
                		pw.write("Bezetting deze maand was: "+percentage+"% ("+(1550-totaal)+" van de 1550 beschikbare plekken)");
                		JOptionPane.showMessageDialog(null, "Email verzonden naar Henk. De mail is terug te vinden in het het mapje ''InboxHenk''");
                	}              	
                	pw.close();
                	sc.close();    
                	
                }

			}else{
				JOptionPane.showMessageDialog(null, "Overzicht verzenden geannuleerd");
			}
		}catch(Exception e){};
	}
}