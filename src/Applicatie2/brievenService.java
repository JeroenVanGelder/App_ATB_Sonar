package Applicatie2;

import Applicatie.Factuur;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class brievenService {
	public brievenService(){}
	private static int i = 0;
	public void genereerFactuurBrief(Factuur f){
		Calendar datum = Calendar.getInstance(); 
		
		SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy"); 
		Calendar curDateTime = Calendar.getInstance(); 
		String formattedDate = SDF.format(curDateTime.getTime()); 
		try {  
			FileWriter fw = new FileWriter("FactuurApp/FactuurMails/"+i+"FactuurMail"+formattedDate+f.getFactuurhouder().getEmail()+".txt", true); 
			PrintWriter pw = new PrintWriter(fw); 
			pw.println("Datum " + formattedDate); 
			pw.println("De prijs is " + (f.getBedrag() - f.getBedragBetaald())); 
			pw.println("De klant is: " + f.getFactuurhouder().getNaam()); 
			pw.println("Met vriendelijke groet ATD"); 
			pw.close(); 
			i++;
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
    	
	} 
	public void genereerEersteWaarschuwingsBrief(Factuur f){ 
		Calendar datum = Calendar.getInstance(); 
		SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy"); 
		Calendar curDateTime = Calendar.getInstance(); 
		String formattedDate = SDF.format(curDateTime.getTime()); 
		try {  
			FileWriter fw = new FileWriter("FactuurApp/FactuurMails/"+i+"EersteWaarschuwingsMail"+formattedDate+f.getFactuurhouder().getEmail()+".txt", true); 
			PrintWriter pw = new PrintWriter(fw); 
			pw.println("Datum " + formattedDate); 
			pw.println("Dit is de eerste waarschuwingsbrief voor een factuur die nog betaald moet worden"); 
			pw.println("De factuurhouder is: " + f.getFactuurhouder().getNaam()); 
			pw.println("Als er binnen een maand niet betaald wordt, dan wordt er een incassobureau ingelicht"); 
			pw.println("De factuur wordt voor een tweede keer nu naar u verstuurd."); 
			pw.println("Met vriendelijke groet ATD"); 
			pw.close(); 
			i++;
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
    	
	} 
	public void genereerTweedeWaarschuwingsBrief(Factuur f){ 
		Calendar datum = Calendar.getInstance(); 
		SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy"); 
		Calendar curDateTime = Calendar.getInstance(); 
		String formattedDate = SDF.format(curDateTime.getTime()); 
		try {  
			FileWriter fw = new FileWriter("FactuurApp/FactuurMails/TweedeWaarschuwingsMail"+formattedDate+f.getFactuurhouder().getEmail()+".txt", true); 
			PrintWriter pw = new PrintWriter(fw); 
			pw.println("Datum " + formattedDate); 
			pw.println("Dit is de tweede waarschuwingsbrief voor een factuur die nog betaald moet worden"); 
			pw.println("De factuurhouder is: " + f.getFactuurhouder().getNaam()); 
			pw.println("Er wordt nu een incassobureau ingelicht omdat er 90 dagen niet betaald is"); 
			pw.println("De factuur wordt voor een derde keer nu naar u verstuurd."); 
			pw.println("Het is vanaf nu niet meer mogelijk voor u om via factuur te betalen bij ATD"); 
			pw.println("Met vriendelijke groet ATD");
			pw.close(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
    	
	} 
}