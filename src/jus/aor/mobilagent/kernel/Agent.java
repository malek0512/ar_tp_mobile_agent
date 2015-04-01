package jus.aor.mobilagent.kernel;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;


public class Agent implements _Agent{

	//route que l'agent doit suivre
	protected Route _route;
	transient protected BAMAgentClassLoader loader;
	transient protected Jar jar;
	transient protected AgentServer agentServer;
	private String serverName;
	private boolean finish;
	
	@Override
	public void run() {
		System.out.println(" Agent running on server "+this.serverName);
		//The agent executes his doing on this server
		Etape etape = _route.next();
		//effectue l'action
		if(finish==true)
			execute(this.retour());
		else
		{
			execute(etape.action);
			if(_route.hasNext)
			{
				//The agent connects to the next server
				etape = _route.get();
				move(etape.server);
			}
			else if(finish == false)
			{
				finish = true;
				move(_route.next().server);
			}
		}
	}
	/**
	 * Normally this function is called only once when the agent deployement. 
	 * So _route should be equal to null and the agent doesn't need the current serverloader 
	 */
	@Override
	public void init(AgentServer agentServer, String serverName)  {
		System.out.println(" Deployement of the agent on " + serverName);
		this.agentServer = agentServer;
		this.serverName = serverName;
		 
		if (_route == null) {
			try {
				URI uri = new URI(this.serverName);
				_route = new Route(new Etape(uri, this.retour()));
				_route.add(new Etape(new URI(this.serverName), _Action.NIHIL));
			} catch (Exception e) {
				System.out.println("Agent l61");
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * This function is called every time an agentServer charge an agent
	 */
	@Override
	public void init(BAMAgentClassLoader loader, AgentServer server, String serverName) {
		System.out.println(" Initiating the agent on " + serverName);
		this.loader = loader;
		this.agentServer = server;
		this.serverName = serverName;
		if (_route == null)
			try {
				URI uri = new URI(this.serverName);
				_route = new Route(new Etape(uri, this.retour()));
				_route.add(new Etape(new URI(this.serverName), _Action.NIHIL));
			} catch (Exception e) {
				System.out.println("Agent l61");
				System.out.println(e);
				e.printStackTrace();
			}
	}

	@Override
	public void addEtape(Etape etape) {
		_route.add(etape);
	}

	@Override
	public void move(URI serverDest) {
		Socket server = null;
		try {
			// Client connected
			System.out.println("Tentatve de connection : " + serverDest.getHost() + ":" + serverDest.getPort());
			server = new Socket("localhost", serverDest.getPort());
//			server = new Socket(etape.server.getHost(), etape.server.getPort());
			System.out.println("Connected to " + server.getInetAddress());
			// Get the client input stream
			OutputStream os = server.getOutputStream();
			//Get the associated object input stream
			ObjectOutputStream oos = new ObjectOutputStream(os);
			//Send the agent to the server
			oos.writeObject(jar);
			oos.writeObject(this);
			oos.close();
				
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//Not used
	@Override
	public void execute(_Action action) {
		 //gets the current step and goes to the next
		action.execute();
	}
	public void setJar(Jar jar2) {
		// TODO Auto-generated method stub
		this.jar = jar2;
	}

	/**
	 * Action a effectuer sur le server de retour
	 */
	protected _Action retour()
	{
		return _Action.NIHIL;
	}
}
