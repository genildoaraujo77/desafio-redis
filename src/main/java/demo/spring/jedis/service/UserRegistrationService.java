package demo.spring.jedis.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.spring.jedis.exceptions.RegistrateException;
import demo.spring.jedis.model.User;
import demo.spring.jedis.utils.Utils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class UserRegistrationService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationService.class);
	
	@Autowired
	private JedisPool jedisPool;

//    private UserRepository userRepository;
//    
//    @Autowired
//    public UserRegistrationService(UserRepository userRepository){
//        this.userRepository = userRepository;
//    }
    
    public String registrate(User user) throws RegistrateException{
    	String pwd = Utils.geraSenha(user.getPassword());
    	pwd = pwd.replace("$argon2", "");
		user.setPassword(pwd);
    	if(salvarUser(user)) {
    		LOG.info("Usuário registrado com sucesso!");
    		return "Usuário registrado com sucesso!";
    	}else {
    		LOG.info("Não foi possível registrar o usuário!");
    				throw new RegistrateException();
    	}
    }
    
    public Boolean salvarUser(User user) {
		Jedis jedis = jedisPool.getResource();
		try {
			String email = user.getEmail();
			long iduser = jedis.rpush("ids", "s");
			Map<String, String> userhash = new HashMap<String, String>();
			userhash.put("nome", user.getNome());
			userhash.put("email", email);
			userhash.put("password", user.getPassword());
			userhash.put("iduser", String.valueOf(iduser));
			
			String feito = jedis.hmset("user:".concat(String.valueOf(email)), userhash);
			if(feito.equalsIgnoreCase("OK")) {
				Long usersalvo = jedis.sadd("users:user", String.valueOf(iduser));
				if(usersalvo == 1) {
					return true;
				}
			}
			
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		return false;
	}

//    public String registrate(User user) throws RegistrateException{
//    	String msg = null;
//    	String pwd = Utils.geraSenha(user.getPassword());
//    	pwd = pwd.replace("$argon2", "");
//		user.setPassword(pwd);
//    	User usersemtoken = userRepository.save(user);
//    	if(usersemtoken != null) {
//    		msg = "Usuário registrado com sucesso!";	
//    		LOG.info("Usuário registrado com sucesso!");
//    	}else {
//    		LOG.info("Não foi possível registrar o usuário!");
//    		msg = "Erro ao tentar registrar o usuário";
//    				throw new RegistrateException();
//    				
//    	}
//        return msg;
//    }
	
}
