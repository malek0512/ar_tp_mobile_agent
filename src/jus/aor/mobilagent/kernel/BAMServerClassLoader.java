package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarException;

public class BAMServerClassLoader extends URLClassLoader {

	//Une Map <classname, class>
	private Map<String, Class<?>> contents;
	
	public BAMServerClassLoader(URL[] urls) throws JarException, IOException {
		super(urls);
		contents = new HashMap<>();
		
		for(URL url : urls) 
			addToContents(url);
	}

	/**
	 * Le contenu d'un jar est fusionné avec les class pré chargés
	 * @param url
	 * @throws JarException
	 * @throws IOException
	 * @author MAMMAR
	 */
	public void addToContents (URL url ) throws JarException, IOException {
		Jar jar = new Jar(url.getPath());
		
		for (Iterator it = jar.classIterator().iterator(); it.hasNext();) {
			Entry<String, byte[]> entry = (Entry<String, byte[]>) it.next();
			contents.put(entry.getKey(),  defineClass(entry.getValue(), 0, entry.getValue().length));
		}
	}
	
	/**
	 * @param classname
	 * @return la classe associé a au nom classname
	 * @author MAMMAR
	 */
	public Class<?> getClass(String classname) {
		if(contents.containsKey(classname))
			return contents.get(classname);
		
		return null; 
	}
}
