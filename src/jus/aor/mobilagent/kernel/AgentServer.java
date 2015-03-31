package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
<<<<<<< HEAD
import java.net.URL;
=======
import java.nio.channels.SocketChannel;
>>>>>>> parent of 1b059a0... suppresion des warnings
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AgentServer extends Thread{

	//port d'écoute du server
	protected int _server_port;
	//nom du server
	protected String _server_name;
	//liste des services qu'offre le server
	protected Map<String,_Service> _services;
	//le AgentClassLoader associé a ce server
	protected BAMAgentClassLoader _agentClasseLoader;
	protected BAMServerClassLoader _serverClasseLoader;
	private ServerSocket _socketServer;
	
	/**
	 * Initialise le server
	 * @argument name : nom du server
	 * @argument port : numero du port d'écoute
	 */
	public AgentServer(String name, int port,BAMAgentClassLoader loader)
	{
		_server_port = port;
		_server_name = name;
		_services = new HashMap<String, _Service>();
		_agentClasseLoader = loader;
	}
	
	/**
	 * Ajoute un service sur le server
	 */
	public void addService(_Service<?> service, String classeName)
	{
		//_services.put(service.getServiceName(), service);
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
		agent.init(_agentClasseLoader, this, _server_name);
		//we run the agent in a thread, to let the agentServer receive other agents
		new Thread(agent).start();
	}
	
	/**
	 * Boucle de reception des agents mobiles
	 */
	public void run()
	{
		System.out.println(" Running the Agent server on " + this._server_name);

		//on crée un socket
		try {
			_socketServer = new ServerSocket(_server_port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true)
		{
			try {
			//on accept les connections
			Socket  socketClient = _socketServer.accept();
			//on instancie les streams
			InputStream inStr = socketClient.getInputStream();
			ObjectInputStream objInputStr = new ObjectInputStream(inStr);
			
			//gets the serializable jar first
			_agentClasseLoader = new BAMAgentClassLoader(new URL[]{}, this._serverClasseLoader);
			Jar jar = (Jar) objInputStr.readObject();
			//we load the jar in the new BAMAgent
			_agentClasseLoader.addJar(jar);
			
			//on recupere l'agent
			_Agent agent = (_Agent) objInputStr.readObject();
			//on initialise l'agent
			agent.init(_agentClasseLoader,this,_server_name);
			//enfin on demarre l'agent
			startAgent(agent);
			//on ferme les streams
			objInputStr.close();
			
			} catch (IOException | ClassNotFoundException e) {
				System.out.println("AgentServer l128");
				System.out.println(e);
				e.printStackTrace();
			}	
		}
	}
}
