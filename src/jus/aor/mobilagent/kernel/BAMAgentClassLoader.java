package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarException;

/**
 * Est associé a chaque agentServer
 * Contient les Agent.jar
 * @author alex
 *
 */
public class BAMAgentClassLoader extends BAMServerClassLoader
{
	//Une Map <classname, class>
	protected Map<String, Class<?>> contents;
	
	//Une ref vvers le clasloader parent
	private ClassLoader parent = null;
	
	public BAMAgentClassLoader(URL[] urls) {
		super(new URL[] {}); //Fake : to not allow the parent to have our classes
		contents = new HashMap<String, Class<?>>();
		for(URL url : urls)
			addJar(url);
	} 
	
	public BAMAgentClassLoader(URL[] urls, ClassLoader loader) {
		super(new URL[]{},loader);
		contents = new HashMap<String, Class<?>>();
		parent = loader;
		for(URL url : urls)
			addJar(url);
	}
	
	/**
	 * Le contenu d'un jar est fusionn� avec les class pr� charg�s
	 * @param url
	 * @throws JarException
	 * @throws IOException
	 * @author MAMMAR
	 */
	public void addJar (URL url ) {
//		addURL(url); we don't want the parent to know our classes
		Jar jar = null;
		try {
			jar = new Jar(url.getPath());
		} catch (IOException e) {
			System.out.println("bam agent class loader l54");
			System.out.println(e);
		}
		addJar(jar);
	}
	
	
	/**
	 * Le contenu d'un jar est fusionn� avec les class pr� charg�s
	 * @author MAMMAR
	 */
	@SuppressWarnings("deprecation")
	public void addJar (Jar jar) {
		for (Iterator<?> it = jar.classIterator().iterator(); it.hasNext();) {
			@SuppressWarnings("unchecked")
			Entry<String, byte[]> entry = (Entry<String, byte[]>) it.next();
			String className = entry.getKey();
			className = className.substring(className.lastIndexOf('/')+1);
			byte[] b = entry.getValue();
			Class<?> c= null;
			c = defineClass(b, 0, entry.getValue().length);
			contents.put(className,  c);
		}
	}
	
	/**
	 * @param classname
	 * @return la classe associ� a au nom classname
	 * @author MAMMAR
	 * @throws ClassNotFoundException 
	 */
	public Class<?> getClass(String classname) throws ClassNotFoundException {
		classname = classname.replace('.', '/').concat(".class");
		
		if(contents.containsKey(classname))
			return contents.get(classname);
		//if we have a parent loader let's ask him
		else if (parent != null)
			return parent.loadClass(classname);
		throw new RuntimeException("No class of that name found : "+classname);
	}
}
