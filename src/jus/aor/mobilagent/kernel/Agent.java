package jus.aor.mobilagent.kernel;

public class Agent implements _Agent{

	//route que l'agent doit suivre
	Route _route;
	
	public Agent()
	{
		// TODO
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
		// TODO Auto-generated method stub
		
	}

}
