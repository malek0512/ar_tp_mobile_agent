package jus.aor.mobilagent.kernel;

/**
 * Action qui consiste a afficher le serveur sur lequel on se situe
 * @author alex
 */
public class ActionTest implements _Action{

	@Override
	public void execute() {
		System.out.println("je fais l'action test");
		
	}
}
