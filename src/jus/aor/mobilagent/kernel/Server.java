/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Le serveur principal permettant le lancement d'un serveur d'agents mobiles et les fonctions permettant de déployer des services et des agents.
 * @author     Morat
 */
public final class Server {
	/** le nom logique du serveur */
	protected String name;
	/** le port où sera attaché le service du bus à agents mobiles. Pafr défaut on prendra le port 10140 */
	protected int port=38000;
	/** le server d'agent démarré sur ce noeud */
	protected AgentServer agentServer;
	/** le nom du logger */
	protected String loggerName;
	/** le logger de ce serveur */
	protected Logger logger=null;
	/** Le classLoader pour les agents */
//	protected BAMAgentClassLoader loader;
	/** Le classLoader du server*/
	protected BAMAgentClassLoader loaderServer;
	
	/**
	 * Démarre un serveur de type mobilagent 
	 * @param port le port d'écoute du serveur d'agent 
	 * @param name le nom du serveur
	 */
	public Server(final int port, final String name){
		this.name=name;
		this.port=port;
		
		try {
			/* mise en place du logger pour tracer l'application */
			loggerName = "jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.name;
			logger=Logger.getLogger(loggerName);
			logger.log(Level.FINE,"SERVER "+name);
//			//On instancie un loader pour ce serveur
//			loader = (BAMAgentClassLoader) new BAMServerClassLoader(new URL[]{});
			loaderServer = new BAMAgentClassLoader(new URL[]{}, this.getClass().getClassLoader());
			
			/* démarrage du server d'agents mobiles attaché à cette machine */
			agentServer = new AgentServer(name, port, loaderServer,this);
			agentServer.start();
			
			/* temporisation de mise en place du server d'agents */
			Thread.sleep(1000);
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			System.out.println("Server l55");
			System.out.println(ex);
			return;
		}
	}
	
	
	/**
	 * Ajoute le service caractérisé par les arguments
	 * Crée une classe de type _Service et de nom ClasseName.
	 * Ce service est accompagné d'un jar codeBase, qui doit etre chargé dans le BAMServerClassLoader, et 
	 * d'une suite d'arguments de type Object.
	 * 
	 * @param name nom du service
	 * @param classeName classe du service
	 * @param codeBase codebase du service  (un .jar)
	 * @param args arguments de construction du service
	 */
	public final void addService(String name, String classeName, String codeBase, Object... args) {
		try {
			//Logger
			System.out.println(" Adding a service : " + name +" "+ classeName);
			logger.log(Level.FINE," Adding a service ");

			//On charge le code du service dans le BAMServerClassLoader
			//Path vers le JAR
			String jarPath = "file:///"+System.getProperty("user.dir")+codeBase;
			loaderServer.addJar(new URL(jarPath));
			
			//On recupere l'objet class de la classe className du Jar
			Class<?> serviceClass = Class.forName(classeName, true, loaderServer);
//			Class<?> serviceClass = loaderServer.findClass(classeName);
			
			//On instancie ce service au sein d'un objet de type _Service
			_Service<?> service = (_Service<?>) serviceClass.getConstructor(Object[].class).newInstance(new Object[]{args});
			//On ajoute le service a l'agentServer
			agentServer.addService(service,name);
			
		}catch(Exception ex){
			System.out.println("server l88");
			System.out.println(ex);
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * deploie l'agent caractérisé par les arguments sur le serveur
	 * déploie l'agent sur le serveur courant.
	 * On l'instancie sur un classLoader fils, pour des raison de renforcement securité
	 * On l'initialise par une liste d'etapes composée d'adresse de serveur et d'action 
	 * @param classeName classe du service
	 * @param args arguments de construction de l'agent
	 * @param codeBase codebase du service ~~ de l'agent plutot à mon avis ~~
	 * @param etapeAddress la liste des adresse des étapes
	 * @param etapeAction la liste des actions des étapes
	 */
	public final void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress, List<String> etapeAction) {
		try {
			//Logger
			System.out.println(" Deploying an agent ");
			logger.log(Level.FINE," Deploying an agent ");
			//Path vers le JAR
			String jarPath = "file:///"+System.getProperty("user.dir")+codeBase;
			//Le deploiement d'un agent se fait sur un classLoader fils du classLOader actuel
			BAMAgentClassLoader agentLoader = new BAMAgentClassLoader(new URL[]{}, loaderServer);
			agentLoader.addJar(new URL(jarPath));
			//Chargement de l'objet classe
			Class<?> agentClass = Class.forName(classeName, true, agentLoader);
//			Class<?> agentClass = agentLoader.findClass(classeName);
			
			//Instanciation  de l'agent
			Agent agent = (Agent) agentClass.getConstructor(Object[].class).newInstance(new Object[]{args});
			agent.setJar(new URL(jarPath)); //Mis a jour de la caisse a outils de l'agent
			
			//Initialisation de l'agent
			agent.init(agentServer, "mobilagent://" + name + ":" + port +"/");

			//Initialisation de la route a partir des actions en attribut de la classe
			for(int i=0; i<etapeAddress.size(); i++) {
				Field field = agentClass.getDeclaredField(etapeAction.get(i));
				field.setAccessible(true);
				_Action act = (_Action) field.get(agent);
				
//				Class<?> actClass = agentLoader.getClass(etapeAction.get(i));
				//_Action act = (_Action) actClass.getConstructor().newInstance();
				URI server = new URI(etapeAddress.get(i));
				Etape etp =  new Etape(server, act);
				agent.addEtape(etp);
			}

			
			new Thread(agent).start();
			System.out.println(" Agent deployed ");
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			System.out.println("Server : l127");
			System.out.println(ex);
			return;
		}
	}
}
