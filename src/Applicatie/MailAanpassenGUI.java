package Applicatie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class MailAanpassenGUI extends JFrame implements ActionListener  {
	private JButton b1, b2;
	private JLabel l1;
	private JComboBox cb1;
	private JTextArea ta1;
	private JScrollPane sp1;
	public MailAanpassenGUI(boolean b, databaseController controller){
		setContentPane(new JLabel(""));
		b1 = new JButton("Opslaan");
		b1.addActionListener(e -> {
			if(b){
				controller.setOnderhoudsmailtekst(ta1.getText());
				JOptionPane.showMessageDialog(null, "Nieuwe mail tekst opgeslagen!");
				dispose();
			}else{
				controller.setMaandenmailtekst(ta1.getText());
				JOptionPane.showMessageDialog(null, "Nieuwe mail tekst opgeslagen!");
				dispose();
			}
		});
		add(b1);
		b1.setBounds(20, 20, 85, 30);
		b2 = new JButton("Annuleren");
		b2.addActionListener(e -> {
			dispose();
		});
		add(b2);
		b2.setBounds(115, 20, 95, 30);
		l1 = new JLabel("Invoegen:");
		add(l1);
		l1.setBounds(220, 20, 100, 30);
		String[] comboBoxVelden = { "Naam van de klant", "Auto(Kenteken, merk)", "Datum van de laatste onderhoudsbeurt", "Periode recentelijke bezoeken", "Aantal recentelijke bezoeken" };
		cb1 = new JComboBox(comboBoxVelden);
		cb1.addActionListener(e -> {
			JComboBox cb = (JComboBox)e.getSource();
			String s = (String)cb.getSelectedItem();
			if(s.equals("Naam van de klant")){
				ta1.append("<klantNaam>");
			}else if(s.equals("Auto(Kenteken, merk)")){
				ta1.append("<klantAuto>");
			}else if(s.equals("Datum van de laatste onderhoudsbeurt")){
				ta1.append("<laatsteOnderhoudsbeurt>");
			}else if(s.equals("Periode recentelijke bezoeken")){
				ta1.append("<periodeRecentelijkeBezoeken>");
			}else if(s.equals("Aantal recentelijke bezoeken")){
				ta1.append("<aantalRecentelijkeBezoeken> ");
			}
		});
		add(cb1);
		cb1.setBounds(290 , 20, 240, 30);
		ta1 = new JTextArea();
		sp1 = new JScrollPane(ta1);
		sp1.setBounds(20, 70, 510, 300);
		add(sp1);

		if(b){
			setTitle("Mail over onderhoudsbeurt aanpassen");
			ta1.setText(controller.getOnderhoudsmailtekst());
		}else{
			setTitle("Mail over regelmatig bezoeken aanpassen");
			ta1.setText(controller.getMaandenmailtekst());
		}
		setSize(555, 415);
		setVisible(true);
		setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Auto-generated method stub
		
	}
}
