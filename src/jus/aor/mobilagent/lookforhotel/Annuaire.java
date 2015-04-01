package jus.aor.mobilagent.lookforhotel;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jus.aor.mobilagent.kernel._Service;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;



public class Annuaire implements _Annuaire, _Service<Numero>{

	Map<String,Numero> annuaire;
	
/**
 * @author alex
 * @param fileParam
 */
public Annuaire(Object... args)
{
	annuaire = new HashMap<String, Numero>();
	String file =  (String) args[0];
	System.out.println("Opening file : "+file);
	/* Récupération de l'annuaire dans le fichier xml */
	DocumentBuilder docBuilder = null;
	Document doc=null;
	try {
		docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		doc = docBuilder.parse(new File(file));
	}
	catch (Exception e) {
		System.out.println("Annuaire l42");
		System.out.println(e);
	}
	String name, numero;
	NodeList list = doc.getElementsByTagName("Telephone");
	NamedNodeMap attrs;
	/* acquisition de toutes les entrées de l'annuaire */
	for(int i =0; i<list.getLength();i++) {
		attrs = list.item(i).getAttributes();
		name=attrs.getNamedItem("name").getNodeValue();
		numero=attrs.getNamedItem("numero").getNodeValue();
		annuaire.put(name, new Numero(numero));
	}
}


/**
 * @author alex
 */
@Override
public Numero get(String abonne) {
	
//	Iterator iterator = annuaire.entrySet().iterator();
	
//	Numero res = null;
//	//On parcourt avec l'itérateur pour associer les numéros de téléphone
//	while (iterator.hasNext())
//	{
//		res = iterator.
//		if(res.toString().compareTo(abonne)==0)
//		{
//			return res;
//		}
//	}
//	return res;
	System.out.println("ANNUAIRE "+abonne);
	if (annuaire.containsKey(abonne)) {
		System.out.println("numero"+annuaire.get(abonne));
		return annuaire.get(abonne);
	}
	else{
		System.out.println("No numero found for abonne : "+abonne);
		return null;
	}
		
}

/**
 * @param : 0 -> String : nom de l'hotel
 */
@Override
public Numero call(Object... params) throws IllegalArgumentException {
	return get((String) params[0]);
}

@Override
public String getServiceName() {
	return "Telephones";
}


}
