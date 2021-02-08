package demo.spring.jedis.exceptions;

public class InvalidTokenException extends MyExpetion{

	/**
	 * 
	 */
	private static final long serialVersionUID = 855448785152151L;

	public InvalidTokenException(){		  
	      super("Token inválido", null, false, false);
	    }
}