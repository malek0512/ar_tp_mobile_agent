package jus.aor.mobilagent.kernel;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import jus.aor.mobilagent.kernel.Server;

public class Agent implements _Agent{

	//route que l'agent doit suivre
	protected Route _route;
	protected BAMAgentClassLoader loader;
	protected Jar jar;
	protected AgentServer agentServer;
	protected _Action doIt = _Action.NIHIL;//TODO je suis pas sur de son existance (alex)
	
	public Agent()
	{
		// TODO
		/*
		_route = new Route(new Etape(new URI(agentServer._name+":"+agentServer._port), doIt)); //ATTENTION !! agentServer est null !!
		jar = new Jar(fileName); // y a pas de fileName xd mais why not
		loader = new BAMAgentClassLoader(null); // le loader est transmit dans le init
		*/
	}
	
	@Override
	public void run() {
		//on execute l'action
		this.execute();
		
		//on passe au prochain serveur
		this.move();
	}

	@Override
	public void init(AgentServer agentServer, String serverName) {
		this.agentServer = agentServer;
		
	}

	@Override
	public void init(BAMAgentClassLoader loader, AgentServer server,
			String serverName) {
		this.loader = loader;
		this.init(server,serverName);
		
	}

	@Override
	public void addEtape(Etape etape) {
		_route.add(etape);
	}

	@Override
	public void move() {
		//on ne se deplace que si la feuille de route n'est pas epuiser
		if(_route.hasNext())
		{
		//on crée une nouvelle connexion socket pour le prochain server
		Socket sock;
			//on initialise la connection
			try {
				
				URI server = _route.get().getServer();
				sock = new Socket(server.getHost(),server.getPort());
				//on envoie l'agent sur le server
				OutputStream outputStr;
				outputStr = sock.getOutputStream();
				ObjectOutputStream objOutputStr;
			
				objOutputStr = new ObjectOutputStream(outputStr);
				objOutputStr.writeObject(this);  
			}
			catch (NoSuchElementException e){
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void execute() {
		if (_route.hasNext()) {
			Etape step = _route.next();
			step.action.execute();
			System.out.println("Etape : "+step+" has executed action "+step.action);
		}
	}

}
