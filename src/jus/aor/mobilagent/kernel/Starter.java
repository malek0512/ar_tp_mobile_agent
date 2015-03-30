/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RMISecurityManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Morat 
 */
public class Starter{
	/** le document xml en cours */
	protected Document doc;
	/** le logger pour ce code */
	protected Logger logger;
	/** le server associé à ce starter */
	protected jus.aor.mobilagent.kernel.Server server;
	/** le Loader utilisé */
	protected BAMServerClassLoader loader;
	/** la classe du server : jus.aor.mobilagent.kernel.Server */
	protected Class<jus.aor.mobilagent.kernel.Server> classe;
	/**
	 * 
	 * @param args
	 */
	public Starter(String... args){
		// récupération du niveau de log
		java.util.logging.Level level;
		try {
//			level = Level.parse(System.getProperty("LEVEL"));	
			level = java.util.logging.Level.FINE;
		}catch(NullPointerException e) {
			System.out.println("Starter l54");
			System.out.println(e);
			level=java.util.logging.Level.OFF;
		}catch(IllegalArgumentException e) {
			System.out.println("Starter l58");
			System.out.println(e);
			level=java.util.logging.Level.SEVERE;
		}
		try {
			/* Mise en place du logger pour tracer l'application */
			String loggerName = "jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+args[1];
			logger = Logger.getLogger(loggerName);
			logger.setUseParentHandlers(false);
			logger.addHandler(new IOHandler());
			logger.setLevel(level);
			/* Récupération d'informations de configuration */
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = docBuilder.parse(new File(args[0]));
			int port=Integer.parseInt(doc.getElementsByTagName("port").item(0).getAttributes().getNamedItem("value").getNodeValue());
			// Création du serveur 
			createServer(port,args[1]);
			// ajout des services
			addServices();
			// déploiement d'agents
			deployAgents();
		}catch(Exception ex){
			System.out.println("Starter l80");
			System.out.println(ex);
			logger.log(Level.FINE,"Ce programme nécessite un argument : <conf file> <name server>",ex);
			return;
		}
	}
	
	/**
	 * Instancie un server a travers l'introspection de classe Server dans le .jar MobilagentServer.jar
	 * @param port
	 * @param name
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@SuppressWarnings("unchecked")
	protected void createServer(int port, String name) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		//Charge le .jar du Server qui doit etre nomm� MobilagentServer.jar
//		loader = new BAMServerClassLoader(new URL[]{new URL("file:///.../MobilagentServer.jar")});
		
		//Modified by malek
//		loader = new BAMServerClassLoader(new URL[]{new URL(doc.getElementsByTagName("jar").item(0).getAttributes().getNamedItem("value").getNodeValue())});
		try {
			loader = new BAMServerClassLoader(new URL[]{new URL("file:/"+System.getProperty("user.dir")+"/MobilagentServer.jar")});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Cherche la classe Server dedans
		classe = (Class<jus.aor.mobilagent.kernel.Server>) Class.forName("jus.aor.mobilagent.kernel.Server",true,loader);
		//instancie un server par un appel au constructeur
		server = classe.getConstructor(int.class,String.class).newInstance(port,name);
	}
	
	/**
	 * Ajoute les services définis dans le fichier de configuration xml pass� en argument "Document doc;"
	 */
	protected void addServices() {
		NamedNodeMap attrs;
		Object[] args;
		String codeBase, classeName, name;
		for(Node item : iterable(doc,"service")) {
			attrs = item.getAttributes();
			codeBase = attrs.getNamedItem("codebase").getNodeValue();
			classeName = attrs.getNamedItem("class").getNodeValue();
			args = attrs.getNamedItem("args").getNodeValue().split(" ");
			name = attrs.getNamedItem("name").getNodeValue();
			addService(name, classeName, codeBase, args);
		}
	}
	/**
	 * Ajoute un service
	 * @param name le nom du service
	 * @param classeName la classe du service
	 * @param codeBase le code du service
	 * @param args les arguments de la construction du service
	 */
	protected void addService(String name, String classeName, String codeBase, Object... args) {
		try{
			server.addService(name,classeName,codeBase,args);
		}catch(Exception e){
			System.out.println("Starter l146");
			System.out.println(e);
			logger.log(Level.FINE," erreur durant l'ajout d'un service",e);
		}
	}
	/**
	 * déploiement les agents définis dans le fichier de configuration
	 */
	protected void deployAgents() {
		NamedNodeMap attrsAgent, attrsEtape;
		Object[] args=null;
		String codeBase;
		String classeName;
		List<String> serverAddress=new LinkedList<String>(), serverAction=new LinkedList<String>();

		// XML example : hello.client1.xml
		//	<agent>
		//		<agent class="jus.aor.mobilagent.hello.Hello" codebase=".../Hello.jar" args="">
		//		<etape server="mobilagent://...:222/" action="doIt" />
		//		<etape server="mobilagent://...:333/" action="doIt" />
		//	</agent>
		for(Node  item1 : iterable(doc,"agent")) {
			attrsAgent = item1.getAttributes();
			codeBase = attrsAgent.getNamedItem("codebase").getNodeValue();
			classeName = attrsAgent.getNamedItem("class").getNodeValue();
			args = attrsAgent.getNamedItem("args").getNodeValue().split(" ");
			for(Node item2 : iterable((Element)item1,"etape")) {
				attrsEtape = item2.getAttributes();
				serverAction.add(attrsEtape.getNamedItem("action").getNodeValue());
				serverAddress.add(attrsEtape.getNamedItem("server").getNodeValue());
			}

			deployAgent(classeName, args, codeBase,serverAddress, serverAction);
		}
	}
	/**
	 * Déploie un agent
	 * @param classeName la classe de l'agent
	 * @param args les arguments de la construction de l'agent
	 * @param codeBase le code de l'agent
	 * @param serverAddress la liste des serveurs des étapes
	 * @param serverAction la liste des actions des étapes
	 */
	protected void deployAgent(String classeName, Object[] args, String codeBase, List<String> serverAddress, List<String> serverAction) {
		try{
			server.deployAgent(classeName,args,codeBase,serverAddress,serverAction);
		}catch(Exception e){
			System.out.println("Starter l193");
			System.out.println(e);
			logger.log(Level.FINE," erreur durant le déploiement de l'agent",e);
		}
	}
	
	//Malek comments
	//Use example : iterable(Document doc, "service") renvoie un it�rator de tous les tag "service"
	//Use example : iterable(Element e, "service") renvoie un it�rator de tous les tag "service"
	private static Iterable<Node> iterable(final Node racine, final String element){
		return new Iterable<Node>() {
			@Override
			public Iterator<Node> iterator(){
				return new Iterator<Node>() {
					NodeList nodelist;
					int current = 0, length;
					{ //init
						try{ 
							nodelist = ((Document)racine).getElementsByTagName(element); // si racine est un document
						}catch(ClassCastException e){
							nodelist = ((Element)racine).getElementsByTagName(element); // si racine est un element 
						}
						length = nodelist.getLength();
					}
					@Override
					public boolean hasNext(){return current<length;}
					@Override
					public Node next(){return nodelist.item(current++);}
					@Override
					public void remove(){}
				};
			}
		};
	}
	/**
	 * Application starter
	 * @param args
	 */
	public static void main(String... args) {
		if(System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		
		new Starter(args);
	}
}

