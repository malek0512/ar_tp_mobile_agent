package jus.aor.mobilagent.kernel;

import java.util.LinkedList;
import java.util.List;

public class AgentServer extends Thread{

	//port d'écoute du server
	protected int _port;
	//nom du server
	protected String _name;
	//liste des services qu'offre le server
	protected List<_Service> _services;
	//le AgentClassLoader associé a ce server
	protected BAMAgentClassLoader _agentClasseLoader;
	
	/**
	 * Initialise le server
	 * @argument name : nom du server
	 * @argument port : numero du port d'écoute
	 */
	public AgentServer(String name, int port)
	{
		_port = port;
		_name = name;
		_services = new LinkedList<>();
	}
	
	/**
	 * Ajoute un service sur le server
	 */
	public void addService(_Service service)
	{
		_services.add(service);
	}
	
	/**
	 * Recupere le service demander ?
	 */
	public _Service getService()
	{
		//TODO
		return null;
	}

	/**
	 * Demarre un Agent
	 */
	public void startAgent(_Agent agent)
	{
		agent.init(_agentClasseLoader, this, _name);
	}
	
	/**
	 * Boucle de reception des agents mobiles
	 */
	public void run()
	{
		//TODO
	}
}
