package jus.aor.mobilagent.kernel;

public class ServiceTest implements _Service<String>{

	@Override
	public String call(Object... params) throws IllegalArgumentException {
		return "Hi, my name is lulu, i'm a service test :)";
	}

}
