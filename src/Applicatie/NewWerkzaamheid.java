package Applicatie;

import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class NewWerkzaamheid extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String omschrijving = "";
	private int ID;
	private boolean cancel = true;
	private JLabel context, id;
	private JTextField tf1, tf3;
	private JButton toevoegen, annuleren;
	private ArrayList<Werkzaamheid> werkzaamheden = new ArrayList<Werkzaamheid>();

    public NewWerkzaamheid(int size, ArrayList<Werkzaamheid> w) {
		werkzaamheden = w;
		setContentPane(new JLabel(""));
		setTitle("Werkzaamheid toevoegen");
        //int size = 1;

        context = new JLabel("Omschrijving:");
        tf1 = new JTextField();
        add(context);
        context.setBounds(10,50,200,40);
        add(tf1);
        tf1.setBounds(250,50,200,40);

        id = new JLabel("ID:");
        add(id);
        id.setBounds(10,100,200,40);
        tf3 = new JTextField();
        add(tf3);
        tf3.setEditable(false);
        tf3.setBounds(250,100,200,40);
        tf3.setText(size+1+"");

	    toevoegen = new JButton("Toevoegen");
        toevoegen.setBounds(10,150,150,40);
        add(toevoegen);
        toevoegen.addActionListener((ActionEvent e) -> {
			cancel = false;
			try{
			    omschrijving = tf1.getText();
		    }catch(Exception ex){System.out.println(ex);omschrijving = ""; cancel = false;}
		    try{
				ID = Integer.parseInt(tf3.getText());
			}catch(Exception ex){
				JOptionPane.showMessageDialog(null, "Veld 'ID' moet een numerieke waarde zijn");
				return;
			}
		    if(omschrijving.equals("")){
				JOptionPane.showMessageDialog(null, "Veld 'naam' mag niet leeg zijn");
				return;
			}
            setNewWerkzaamheid();
            this.processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));

        });

        annuleren = new JButton("Annuleren");
        add(annuleren);
        annuleren.setBounds(340,150,150,40);
        annuleren.addActionListener((ActionEvent e) -> {
			cancel = true;
			setNewWerkzaamheid();
			this.processWindowEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
        });

	    setSize(500,500);
	    setVisible(true);
    }

    public String getOmschrijving(){
		return omschrijving;
	}

	public int getID(){
		return ID;
	}

	public boolean getCancel(){
		return cancel;
	}

    public void actionPerformed(ActionEvent event){

	}

    public void setNewWerkzaamheid(){
	    if(!getCancel()){
	        String omschrijving = getOmschrijving();
	        int ID = getID();
	        for(Werkzaamheid w : werkzaamheden){
				if(w.getID() == ID){
					JOptionPane.showMessageDialog(null, "Werkzaamheid niet toegevoegd want er bestaat al een werkzaamheid met ID: "+ID);
					return;
				}
			}
			Werkzaamheid nieuw = new Werkzaamheid(omschrijving, ID);
			werkzaamheden.add(nieuw);
		}else{
	    	JOptionPane.showMessageDialog(null, "Werkzaamheid toevoegen geannuleerd");
	    }
	}
}