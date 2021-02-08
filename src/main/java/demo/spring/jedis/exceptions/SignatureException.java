package demo.spring.jedis.exceptions;

public class SignatureException extends MyExpetion{

	/**
	 * 
	 */
	private static final long serialVersionUID = 855448785152151L;

	public SignatureException(){		  
	      super("Token inválido", null, false, false);
	    }
}