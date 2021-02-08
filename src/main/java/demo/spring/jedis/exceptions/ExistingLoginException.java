package demo.spring.jedis.exceptions;

public class ExistingLoginException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 993130561521521L;

	public ExistingLoginException(){
	      super("Login já está em uso");
	    }
}
