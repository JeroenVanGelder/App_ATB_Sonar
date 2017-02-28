package Applicatie;

import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class NewMonteur extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String naam = "";
	private String achterNaam = "";
	private int ID;
	private boolean cancel = true;
	private JLabel name, surName, id;
	private JTextField tf1, tf2, tf3;
	private JButton toevoegen, annuleren;
	private ArrayList<Monteur> monteurs = new ArrayList<Monteur>();

	public NewMonteur(int size, ArrayList<Monteur> m) {
		monteurs = m;
		setContentPane(new JLabel(""));
		setTitle("Monteur toevoegen");

		name = new JLabel("Naam:");
		tf1 = new JTextField();
		add(name);
		name.setBounds(10,50,200,40);
		add(tf1);
		tf1.setBounds(250,50,200,40);

		surName = new JLabel("Achternaam:");
		add(surName);
		surName.setBounds(10,100,200,40);
		tf2 = new JTextField();
		add(tf2);
		tf2.setBounds(250,100,200,40);

		id = new JLabel("ID:");
		add(id);
		id.setBounds(10,150,200,40);
		tf3 = new JTextField();
		add(tf3);
		tf3.setEditable(false);
		tf3.setBounds(250,150,200,40);
		tf3.setText(size+1+"");

		toevoegen = new JButton("Toevoegen");
		toevoegen.setBounds(10,200,150,40);
		add(toevoegen);
		toevoegen.addActionListener((ActionEvent e) -> {
			cancel = false;
			try{
				naam = tf1.getText();
				achterNaam = tf2.getText();
			}catch(Exception ex){
				System.out.println(ex);
				naam = "";
				achterNaam = "";
				cancel = false;
			}
			try{
				ID = Integer.parseInt(tf3.getText());
			}catch(Exception ex){
				JOptionPane.showMessageDialog(null, "Veld 'ID' moet een numerieke waarde zijn");
				return;
			}
			if(naam.equals("")){
				JOptionPane.showMessageDialog(null, "Veld 'naam' mag niet leeg zijn");
				return;
			}
			if(achterNaam.equals("")){
				JOptionPane.showMessageDialog(null, "Veld 'achternaam' mag niet leeg zijn");
				return;
			}
			setNewMonteur();
			this.processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));

		});

		annuleren = new JButton("Annuleren");
		add(annuleren);
		annuleren.setBounds(340,200,150,40);
		annuleren.addActionListener((ActionEvent e) -> {
			cancel = true;
			setNewMonteur();
			this.processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
		});

		setSize(500,500);
		setVisible(true);
	}

	public String getNaam(){
		return naam;
	}

	public String getAchterNaam(){
		return achterNaam;
	}

	public int getID(){
		return ID;
	}

	public boolean getCancel(){
		return cancel;
	}

	public void actionPerformed(ActionEvent event){}

	public void setNewMonteur(){
		try{
			if(!getCancel()){
				String naam = getNaam();
				String achterNaam = getAchterNaam();
				int ID = getID();
				for(Monteur m : monteurs){
					if(m.getID() == ID){
						JOptionPane.showMessageDialog(null, "Monteur niet toegevoegd want er bestaat al een monteur met ID: "+ID);
						return;
					}
				}
				Monteur nieuw = new Monteur(naam, achterNaam, ID);
				monteurs.add(nieuw);
			}else{
				JOptionPane.showMessageDialog(null, "Monteur toevoegen geannuleerd");
			}
		}catch(Exception e){};
	}
}