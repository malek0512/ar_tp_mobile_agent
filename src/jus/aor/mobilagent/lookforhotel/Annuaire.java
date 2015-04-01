package jus.aor.mobilagent.lookforhotel;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jus.aor.mobilagent.kernel._Service;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;



public class Annuaire implements _Annuaire, _Service<Numero>{

	HashMap<String,Numero> annuaire;
	
	
/**
 * @author alex
 * @param fileParam
 */
public Annuaire(String file)
{
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
	Iterator<Numero> iterator = annuaire.values().iterator();
	Numero res = null;
	//On parcourt avec l'itérateur pour associer les numéros de téléphone
	while (iterator.hasNext())
	{
		res = iterator.next();
		if(res.toString() == abonne)
		{
			break;
		}
	}
	return res;
}

/**
 * @param : 0 -> String : nom de l'hotel
 */
@Override
public Numero call(Object... params)
		throws IllegalArgumentException {
	return get((String) params[0]);
}

@Override
public String getServiceName() {
	return "annuaire";
}


}
