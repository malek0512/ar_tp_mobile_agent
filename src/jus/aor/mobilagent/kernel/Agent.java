package jus.aor.mobilagent.kernel;
import java.net.URI;
import java.net.URISyntaxException;

import jus.aor.mobilagent.kernel.Server;

public class Agent implements _Agent{

	//route que l'agent doit suivre
	protected Route _route;
	protected BAMAgentClassLoader loader;
	protected Jar jar;
	protected AgentServer agentServer;
	protected _Action doIt = _Action.NIHIL;
	
	public Agent()
	{
		// TODO
		_route = new Route(new Etape(new URI(agentServer._name+":"+agentServer._port), doIt));
		jar = new Jar(fileName);
		loader = new BAMAgentClassLoader(null);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(AgentServer agentServer, String serverName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(BAMAgentClassLoader loader, AgentServer server,
			String serverName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEtape(Etape etape) {
		_route.add(etape);
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
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
