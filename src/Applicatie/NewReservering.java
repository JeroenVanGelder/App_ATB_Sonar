package Applicatie;

import java.awt.event.*;

import javax.swing.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class NewReservering extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String kenteken = "";
	private String datum = "";
	private String verblijf;
	private boolean cancel = true;
	private JLabel duur, date, ken;
	private JTextField tf1;
	private Scanner sc;
	private JComboBox<String> selectDuur, selectDag, selectMaand, selectJaar;
	private JButton toevoegen, annuleren;
	private ArrayList<Reservering> reserveringen = new ArrayList<Reservering>();

	public NewReservering() {
		setContentPane(new JLabel(""));
		setTitle("Reservering aanmaken");

		ken = new JLabel("Kenteken:");
		tf1 = new JTextField();
		add(ken);
		ken.setBounds(10,50,200,40);
		add(tf1);
		tf1.setBounds(250,50,200,40);
		
		date = new JLabel("Datum:");
		add(date);
		date.setBounds(10,100,200,40);
		selectDag = new JComboBox<String>();
		selectDag.setBounds(250, 100, 50, 40);
		add(selectDag);
		selectDag.addItem("");
		selectMaand = new JComboBox<String>();
		selectMaand.setBounds(310, 100, 50, 40);
		add(selectMaand);
		selectMaand.addItem("");
		selectJaar = new JComboBox<String>();
		selectJaar.setBounds(370, 100, 80, 40);
		add(selectJaar);
		selectJaar.addItem("");
		vulDatums();
		
		selectDuur = new JComboBox<String>();
		selectDuur.setBounds(250, 200, 50, 40);
		add(selectDuur);
		selectDuur.addItem("");
		selectDuur.addItem("Dag");
		selectDuur.addItem("Week");
		selectDuur.addItem("Maand");
		
		duur = new JLabel("Duur:");
		add(duur);
		duur.setBounds(10,200,200,40);

		toevoegen = new JButton("Toevoegen");
		toevoegen.setBounds(10,250,150,40);
		add(toevoegen);
		toevoegen.addActionListener((ActionEvent e) -> {
			cancel = false;
			try{
				kenteken = tf1.getText();
				verblijf = (String)selectDuur.getSelectedItem();
			}catch(Exception ex){
				System.out.println(ex);
				kenteken = "";
				datum = "";
				cancel = false;
			}
			if(kenteken.equals("")){
				JOptionPane.showMessageDialog(null, "Veld 'kenteken' mag niet leeg zijn");
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
			String checkDuur = (String)selectDuur.getSelectedItem();
			if(checkDuur.equals("")){
				JOptionPane.showMessageDialog(null, "Selecteer eerst de duur van het verblijf");
				return;
			}
			boolean checkVoorPlek = checkPlek(checkDag, checkMaand, checkDuur);
			if(checkVoorPlek){
				datum = (String)selectDag.getSelectedItem()+"-"+(String)selectMaand.getSelectedItem()+"-"+(String)selectJaar.getSelectedItem();
				setNewReservering();
				this.processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
			}else{
				return;
		    }
		});

		annuleren = new JButton("Annuleren");
		add(annuleren);
		annuleren.setBounds(340,250,150,40);
		annuleren.addActionListener((ActionEvent e) -> {
			cancel = true;
			setNewReservering();
			this.processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
		});

		setSize(500,500);
		setVisible(true);
	}

	public boolean getCancel(){
		return cancel;
	}

	public void actionPerformed(ActionEvent event){}

	public boolean checkPlek(String d, String ma, String dr){
		int dag = Integer.parseInt(d);
		int maand = Integer.parseInt(ma);
		String m = "";
		int lengte = 0;
		int bezet = 0;
		String s = "Reservering niet mogelijk op data: ";
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
				if(maand == 1){m = "Januari";}else if(maand == 2){m = "Februari";}else if(maand == 3){m = "Maart";}else
				if(maand == 4){m = "April";}else    if(maand == 5){m = "Mei";}else       if(maand == 6){m = "Juni";}else
				if(maand == 7){m = "Juli";}else     if(maand == 8){m = "Augustus";}else  if(maand == 9){m = "September";}else
				if(maand == 10){m = "Oktober";}else if(maand == 11){m = "November";}else if(maand == 12){m = "December";}
				File f = new File("Reserveringen/"+m+"/Dag"+dag+".txt");
			    sc = new Scanner(f);
				int aantal = Integer.parseInt(sc.next());	
				if(aantal == 0){
					bezet++;
					s += dag+"-"+m+"; ";
				}
				dag++;

			}
			if(bezet > 0){
				JOptionPane.showMessageDialog(null, s);
				return false;
			}
		}catch(Exception e){System.out.println(e);}
		sc.close();
		return true;
	}
	
	public void setNewReservering(){
		try{
			if(!getCancel()){
				Reservering nieuw = new Reservering(kenteken, datum, verblijf, 999);
				reserveringen.add(nieuw);
			}else{
				JOptionPane.showMessageDialog(null, "Reservering toevoegen geannuleerd");
			}
		}catch(Exception e){};
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