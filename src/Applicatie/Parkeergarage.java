package Applicatie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Parkeergarage extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton overzicht, addRes;
	private File f;

	public Parkeergarage() {
		setContentPane(new JLabel(""));
		createDir();

		addRes = new JButton("Reservering toevoegen");
		addRes.addActionListener(e -> {
			new NewReservering();
		});
		add(addRes);
		addRes.setBounds(10, 100, 200, 30);

		overzicht = new JButton("Overzichten mailen");
		overzicht.addActionListener(e -> {
			new MaakOverzicht();
		});
		add(overzicht);
		overzicht.setBounds(300, 100, 200, 30);

		setSize(500, 400);
		setVisible(true);
		setResizable(true);
	}

	public static void main (String [] args){
		new Parkeergarage();
	}

	public void createDir(){
		File theDir = null;
		for(int i = 0; i <= 13; i++){
			if(i == 0){theDir = new File("Reserveringen");}else
			if(i == 1){theDir = new File("Reserveringen/Januari");}else
			if(i == 2){theDir = new File("Reserveringen/Februari");}else
			if(i == 3){theDir = new File("Reserveringen/Maart");}else
			if(i == 4){theDir = new File("Reserveringen/April");}else
			if(i == 5){theDir = new File("Reserveringen/Mei");}else
			if(i == 6){theDir = new File("Reserveringen/Juni");}else
			if(i == 7){theDir = new File("Reserveringen/Juli");}else
			if(i == 8){theDir = new File("Reserveringen/Augustus");}else
			if(i == 9){theDir = new File("Reserveringen/September");}else
			if(i == 10){theDir = new File("Reserveringen/Oktober");}else
			if(i == 11){theDir = new File("Reserveringen/November");}else
			if(i == 12){theDir = new File("Reserveringen/December");}else
			if(i == 13){theDir = new File("InboxHenk");}


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
					try{
						System.out.println("DIR: "+theDir+" created");
						if(!theDir.equals(new File("Reserveringen"))||!theDir.equals(new File("InboxHenk"))){
							for(int j = 1; j <= 31; j++){
								f = new File(theDir+"/Dag"+j+".txt");
								PrintWriter pw = new PrintWriter(new FileWriter(f));
								pw.write(""+50);
								pw.close();
							}
						}
					}catch(Exception e){e.printStackTrace();}
					theDir = null;
				}
			}
		}
	}


	public void actionPerformed(ActionEvent e) {}
}
