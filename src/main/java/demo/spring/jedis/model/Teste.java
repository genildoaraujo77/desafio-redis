package demo.spring.jedis.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class Teste {
	
	@Autowired
	private static JedisPool jedisPool;

	public static void main(String[] args) {
            Boolean result = salvarUser();
            System.out.println(result);
            User user = findByEmail();
            System.out.println(user.toString());
	}
	
	public static Boolean salvarUser() {
		Jedis jedis = jedisPool.getResource();
		try {
			Map<String, String> userhash = new HashMap<String, String>();
			userhash.put("email", "email1");
			userhash.put("nome", "nome1");
			userhash.put("password", "senha1");
			userhash.put("iduser", "id1");
			
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		return false;
	}
	
	public static User findByEmail() {
		Jedis jedis = jedisPool.getResource();
		try {
		Map<String, String> elementuser = jedis.hgetAll("users:user");
		if(elementuser != null && !elementuser.isEmpty()) {
			User user = new User();
			user.setNome(elementuser.get("nome"));
			user.setEmail(elementuser.get("email"));
			user.setIduser(Integer.parseInt(elementuser.get("iduser")));
			user.setPassword(elementuser.get("password"));
			
		return user;
		  }
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

}
