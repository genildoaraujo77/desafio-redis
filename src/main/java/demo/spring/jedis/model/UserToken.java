package demo.spring.jedis.model;

public class UserToken {
	private User user;
	private String token;

	public UserToken() {
		super();
	}

	public UserToken(User user, String token) {
		super();
		this.user = user;
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "UserToken [user=" + user + ", token=" + token + "]";
	}

}
