package Applicatie;

import java.util.ArrayList;

/**
 * Created by ricko on 27-2-2017.
 */
public interface InterfacedatabaseController {
    ArrayList<Auto> getAutos();

    ArrayList<Klant> getKlanten();

    String getOnderhoudsmailtekst();
}
