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
	
	public BAMServerClassLoader(URL[] urls) {
		super(urls);
//		contents = new HashMap<>();
		
//		for(URL url : urls) 
//			addJar(url);
	}

	
	/**
	 * Le contenu d'un jar est fusionn� avec les class pr� charg�s
	 * @param url
	 * @throws JarException
	 * @throws IOException
	 * @author MAMMAR
	 */
	public void addJar (URL url ) {
		addURL(url);
//		Jar jar = null;
//		try {
//			jar = new Jar(url.getPath());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		for (Iterator it = jar.classIterator().iterator(); it.hasNext();) {
//			Entry<String, byte[]> entry = (Entry<String, byte[]>) it.next();
//			contents.put(entry.getKey(),  defineClass(entry.getValue(), 0, entry.getValue().length));
//		}
	}
	
	/**
	 * @param classname
	 * @return la classe associ� a au nom classname
	 * @author MAMMAR
	 */
	public Class<?> getClass(String classname) {
		return this.getClass(classname);
//		if(contents.containsKey(classname))
//			return contents.get(classname);
//		
//		throw new RuntimeException("No class of that name found : "+classname);
	}
	
}
