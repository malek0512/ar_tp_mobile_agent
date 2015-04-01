package jus.aor.mobilagent.lookforhotel;

/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

/**
 * Un hotel qui est caractérisé par son nom et sa localisation.
 * @author Morat 
 */
public class Hotel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	/** la localisation de l'hôtel */
	public String localisation;
	/** le nom de l'hôtel */
	public String name;
	/**
	 * Définition d'un hôtel par son nom et sa localisation.
	 * @param name le nom de l'hôtel
	 * @param localisation la localisation de l'hôtel
	 */
	public Hotel(String name, String localisation) { this.name=name; this.localisation=localisation;}

	@Override
	public String toString() {return "Hotel{"+name+","+localisation+"}";}
}
