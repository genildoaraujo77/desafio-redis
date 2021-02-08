package demo.spring.jedis.model;

public class DadosLogin {

	private String email;
	private String password;

	public DadosLogin() {
		super();
	}

	public DadosLogin(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "DadosLogin [email=" + email + ", password=" + password + "]";
	}

}
