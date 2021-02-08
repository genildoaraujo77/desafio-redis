package demo.spring.jedis.exceptions;

public class InvalidEmailException extends MyExpetion{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9538441351515L;

	private String msg;
	private Throwable causa;
	private boolean enableSuppression;
	private boolean writableStackTrace;

	public InvalidEmailException(String msg, Throwable causa,
            boolean enableSuppression,
            boolean writableStackTrace) {
		super(msg, causa, enableSuppression, writableStackTrace);
		this.msg = msg;
		this.causa = causa;
		this.enableSuppression = enableSuppression;
		this.writableStackTrace = writableStackTrace;
	}

	public String getMessage() {
		return msg;
	}
	
	public Throwable getCause() {
		return causa;
	}
	
	public boolean getEnableSuppression() {
		return enableSuppression;
	}
	
	public boolean getWritableStackTrace() {
		return writableStackTrace;
	}
	
}