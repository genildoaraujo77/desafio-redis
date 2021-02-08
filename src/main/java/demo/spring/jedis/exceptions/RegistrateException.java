package demo.spring.jedis.exceptions;

public class RegistrateException extends MyExpetion{
	/**
	 * 
	 */
	private static final long serialVersionUID = 518135168181918L;

	public RegistrateException(){
	      super("Erro ao tentar registrar o usuário", null, false, false);
	    }
}