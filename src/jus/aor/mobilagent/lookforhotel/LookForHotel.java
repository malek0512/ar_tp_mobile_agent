package jus.aor.mobilagent.lookforhotel;

import java.util.Collection;
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
	private List<Hotel> hotels;
	/** les numeros recuperer dans le server annuaire */
	private List<Numero> numeros;
	
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère
	 *          de localisation
	 */
	public LookForHotel(Object... args){
		super();
		System.out.println("lulu");
		localisation = (String) args[0];
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
			if (jus.aor.mobilagent.kernel.Starter.DEBUG)
				System.out.println("Agents asking service : Hotels");
			_Service<List<String>> service = (_Service<List<String>>) agentServer.getService("Hotels");
			System.out.println(((List<String>) service.call(localisation)).get(0));
//			System.out.println(hotels);
		}
	};
	
	public _Action findTelephone = new _Action() {
		private static final long serialVersionUID = 1L;
		public void execute() {
			System.out.println("Agents asking service : Telephones");
			_Service<Numero> service = (_Service<Numero>) agentServer.getService("Telephones");
			for(Hotel hotel : hotels)
			{
				numeros.add(service.call(hotel.name));
			}
			System.out.println("Here is the resultat of the search ");
			for(int i=0; i<hotels.size(); i++) {
				System.out.println("Hotel : "+hotels.get(i)+", "+"Number : "+numeros.get(i));
			}
		}
	};
	
	protected _Action retour() {
		return new _Action() {
			
			@Override
			public void execute() {
				System.out.println("Action retour de l'agent LookForHotel");
				System.out.println(hotels);
			}
		};
	};
	
}
