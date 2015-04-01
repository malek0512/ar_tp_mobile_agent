package jus.aor.mobilagent.lookforhotel;

import java.util.HashMap;
import jus.aor.mobilagent.kernel.Agent;

/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

/**
 * Représente un client effectuant une requête lui permettant d'obtenir les numéros de téléphone des hôtels répondant à son critère de choix.
 * @author  Morat
 */
public class LookForHotel extends Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** le critère de localisaton choisi */
	private String localisation;
	/** le resultat de l'agent */
	private HashMap<String, Hotel> hotels;
	
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère
	 *          de localisation
	 */
	public LookForHotel(String... args){
		localisation = args[0];
		hotels = new HashMap<String, Hotel>();
	}
	
	/**
	 * réalise une intérrogation
	 * @return la durée de l'interrogation
	 * @throws RemoteException
	 */
	public long call() {
		// ...
		return 0;
	}

	// ...
}
