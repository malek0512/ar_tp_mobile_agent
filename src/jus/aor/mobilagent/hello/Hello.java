package jus.aor.mobilagent.hello;

import java.net.URI;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jus.aor.mobilagent.kernel.Route;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel.Agent;

/**
 * Classe de test élémentaire pour le bus à agents mobiles
 * @author  Morat
 */
public class Hello extends Agent{

	/**
	* construction d'un agent de type hello.
	* @param args aucun argument n'est requis
	*/
	public Hello(Object... args) {
		
	}
	
	/**
	* l'action à entreprendre sur les serveurs visités  
	*/
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		
	}
	
	/* (non-Javadoc)
	 * @see jus.aor.mobilagent.kernel.Agent#retour()
	 */
	protected _Action retour(){
		// return ...;
		return null;
	}
};
