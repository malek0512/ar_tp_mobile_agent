package jus.aor.mobilagent.kernel;

/**
 * Action qui consiste a afficher le serveur sur lequel on se situe
 * @author alex
 */
public class ActionTest implements _Action{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void execute() {
		System.out.println("Hi, my name is fiora, i'm the action test :)");
		
	}
}
