package demo.spring.jedis.exceptions;

public class ExpiredTokenException extends MyExpetion{

	/**
	 * 
	 */
	private static final long serialVersionUID = 61541815168156156L;

	public ExpiredTokenException(){
	      super("Token expirou", null, false, false);
	    }
}