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
public class BAMAgentClassLoader extends BAMServerClassLoader {

	private ClassLoader parent = null;
	public BAMAgentClassLoader(URL[] urls) {
		super(urls);
	} 
	
	public void setParent(ClassLoader parent) {
		if (parent == null)
			this.parent = parent;
		throw new RuntimeException("BAMAgentClassLoader : a parent has already be defined");
	}
}
