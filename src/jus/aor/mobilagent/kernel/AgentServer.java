package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class AgentServer extends Thread{

	//port d'écoute du server
	protected int _port;
	//nom du server
	protected String _name;
	protected ServerSocket _socketServer;

	//TODEL voir la nouvelle def quelque ligne plus bas
	//liste des services qu'offre le server
	//protected List<_Service<?>> _services;
	
	//on a besoin de pouvoir identifier les service du coup j'ai rajouter le string T.T
	//le string represente le classeName du service
	protected HashMap<String, _Service<?>> _services;

	//le AgentClassLoader associé a ce server
	protected BAMAgentClassLoader _agentClasseLoader;
	
	/**
	 * Initialise le server
	 * @argument name : nom du server
	 * @argument port : numero du port d'écoute
	 */
	public AgentServer(String name, int port,BAMAgentClassLoader loader)
	{
		_port = port;
		_name = name;
		_services = new HashMap<String, _Service<?>>();
		_agentClasseLoader = loader;
	}
	
	/**
	 * Ajoute un service sur le server
	 */
	public void addService(_Service<?> service, String classeName)
	{
		_services.put(classeName,service);
	}
	
	/**
	 * Recupere le service demander
	 * @param classeName : le classeName du service a recuperer
	 */
	public _Service<?> getService(String classeName)
	{
		return _services.get(classeName);
	}

	/**
	 * Demarre un Agent
	 */
	public void startAgent(_Agent agent)
	{
		new Thread(agent).start();
	}
	
	/**
	 * Boucle de reception des agents mobiles
	 */
	public void run()
	{
		//on crée un socket
		try {
			_socketServer = new ServerSocket(_port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true)
		{
			try {
			//on accept les connections
			Socket  socketClient = _socketServer.accept();
			//on instancie les streams
			InputStream inStr;
			inStr = socketClient.getInputStream();
			ObjectInputStream objInputStr = new ObjectInputStream(inStr);
			
			//on recupere l'agent
			_Agent agent = (_Agent) objInputStr.readObject();
			//on initialise l'agent
			agent.init(_agentClasseLoader,this,_name);
			//enfin on demarre l'agent
			startAgent(agent);
			//on ferme les streams
			objInputStr.close();
			
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
