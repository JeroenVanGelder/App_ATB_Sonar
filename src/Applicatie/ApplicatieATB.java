package Applicatie;

import java.awt.event.*;
import javax.swing.*;

public class ApplicatieATB extends JFrame implements ActionListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton parkeerGarage, voorraadBeheer, facturatie, werkPlaats, klantenBinding;
    private int b = 1000, h = 100;


    public ApplicatieATB(){

        werkPlaats = new JButton("Werkplaats");
        werkPlaats.setBounds(0, 0, b/5, h);
        add(werkPlaats);
        werkPlaats.addActionListener((ActionEvent e) -> {
                 new WerkPlaats();
        });

        voorraadBeheer = new JButton("Voorraadbeheer");
        voorraadBeheer.setBounds(b/5, 0, b/5, h);
        add(voorraadBeheer);
        voorraadBeheer.addActionListener((ActionEvent e) -> {
		             new VoorraadBeheer();
        });

        facturatie = new JButton("Facturatie");
        facturatie.setBounds((b/5)*2, 0, b/5, h);
        add(facturatie);
        facturatie.addActionListener((ActionEvent e) -> {
        			new FactuurApp();
        });

        klantenBinding = new JButton("Klantenbinding");
        klantenBinding.setBounds((b/5)*3, 0, b/5, h);
        add(klantenBinding);
        klantenBinding.addActionListener((ActionEvent e) -> {
		         new KlantenBindingApp();
        });
        
        parkeerGarage = new JButton("Parkeergarage");
        parkeerGarage.setBounds((b/5)*4, 0, b/5, h);
        add(parkeerGarage);
        parkeerGarage.addActionListener((ActionEvent e) -> {
		         new Parkeergarage();
        });

        JLabel lab1 = new JLabel("");
        add(lab1);

        setTitle("ATB App");
	    setSize(b+20,h+40);
	    setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new ApplicatieATB();
    }

    public void actionPerformed(ActionEvent event){}
}