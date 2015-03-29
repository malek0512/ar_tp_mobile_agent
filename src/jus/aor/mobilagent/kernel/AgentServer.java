package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
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
	private ServerSocket serverSocket;
	
	/**
	 * Initialise le server
	 * @argument name : nom du server
	 * @argument port : numero du port d'écoute
	 */
	public AgentServer(String name, int port)
	{
		_server_port = port;
		_server_name = name;
		_services = new HashMap<String, _Service>();
		try {
			serverSocket = new ServerSocket(_server_port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ajoute un service sur le server
	 */
	public void addService(_Service service)
	{
		_services.put(service.getServiceName(), service);
	}
	
	/**
	 * Recupere le service demander ?
	 */
	public _Service getService(String service_name)
	{
		return _services.get(service_name);
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
		for(;;) 
		{
			try 
			{
				//wait for an agent connection
				Socket socket = serverSocket.accept();
				InputStream is = socket.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				//gets the serializable jar first
				this._agentClasseLoader = new BAMAgentClassLoader(null, this._serverClasseLoader);
				Jar jar = (Jar) ois.readObject();
				//we load the jar in the new BAMAgent
				this._agentClasseLoader.addJar(jar);
				//gets the serializable agent 
				Agent _agent = (Agent) ois.readObject();
				startAgent(_agent);
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}	
		}
		
	}
}
