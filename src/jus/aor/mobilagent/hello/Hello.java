package jus.aor.mobilagent.hello;

import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel.Route;

/**
 * Classe de test élémentaire pour le bus à agents mobiles
 * @author  Morat
 */
public class Hello extends Agent {

	private _Action doIt = new _Action() {
		private static final long serialVersionUID = 1L;
		public void execute() {
//			super().execute();
			System.out.println("Hello action !");
		}
	};
	
	/**
	* construction d'un agent de type hello.
	* @param args aucun argument n'est requis
	*/
	public Hello(Object... args) {
		super();
		System.out.println("vant super");
	}
	
//	public Hello() {
//		super();
//		System.out.println("vantssss super");
//	}
//	
	/* (non-Javadoc)
	 * @see jus.aor.mobilagent.kernel.Agent#retour()
	 */
	protected _Action retour() {
		return this.doIt;
	}
	
}
