package jus.aor.mobilagent.lookforhotel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel._Service;

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
	
	/** les hotels recuperer petit a petit */
	private LinkedList<Hotel> hotels;
	/** les numeros recuperer dans le server annuaire */
	private LinkedList<Numero> numeros;
	
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère
	 *          de localisation
	 */
	public LookForHotel(String... args){
		localisation = args[0];
		hotels = new LinkedList<Hotel>();
		numeros = new LinkedList<Numero>();
	}
	
	/**
	 * réalise une intérrogation
	 * @return la durée de l'interrogation
	 * @throws RemoteException
	 */
	public long call() {
		return 0;
	}

	public _Action findHotel = new _Action() {
		private static final long serialVersionUID = 1L;
		public void execute() {
			_Service<List<Hotel>> service = (_Service<List<Hotel>>) agentServer.getService("Hotels");
			hotels.addAll(service.call(new Object[]{localisation}));
		}
	};
	
	public _Action findTelephone = new _Action() {
		private static final long serialVersionUID = 1L;
		public void execute() {
			_Service<Numero> service = (_Service<Numero>) agentServer.getService("Telephones");
			for(Hotel it : hotels)
			{
				numeros.add(service.call(it.name));
			}
		}
	};
	
	
}
