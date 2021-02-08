package demo.spring.jedis.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	private Integer iduser;
	private String nome;
	private String email;
	private String password;
	
	public User() {
		super();
	}

	public User(Integer iduser, String nome, String email, String password) {
		super();
		this.iduser = iduser;
		this.nome = nome;
		this.email = email;
		this.password = password;
	}

	public Integer getIduser() {
		return iduser;
	}

	public void setIduser(Integer iduser) {
		this.iduser = iduser;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
		return "User [iduser=" + iduser + ", nome=" + nome + ", email=" + email + ", password=" + password + "]";
	}

}
