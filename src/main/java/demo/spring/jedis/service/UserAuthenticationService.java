package demo.spring.jedis.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.spring.jedis.exceptions.InvalidEmailException;
import demo.spring.jedis.exceptions.InvalidTokenException;
import demo.spring.jedis.model.DadosLogin;
import demo.spring.jedis.model.User;
import demo.spring.jedis.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Service
public class UserAuthenticationService {
	
//    private final UserRepository userRepository;
//    
//    @Autowired
//    public UserAuthenticationService(UserRepository userRepository){
//        this.userRepository = userRepository;
//    }
	
	@Autowired
	private JedisPool jedisPool;
    
	 public String generateNewTokenWithLogin(DadosLogin dados) throws Exception{
	    	User user;
	    	String tokentmpgerado = null;
	    	return (user = findByEmail(dados)) != null ?
	    	         Utils.validaSenha(dados.getPassword(), user.getPassword()) ?
	        	        (tokentmpgerado = Utils.generateTokenTmp(user)) != null ?
	    		          (!tokentmpgerado.isEmpty() && validaToken(tokentmpgerado)) ?
	            	          tokentmpgerado : acionaExceptionEmail("Erro ao tentar gerar um token válido") : 
	            	        	  acionaExceptionEmail("Erro ao tentar gerar o token") : 
	            	        		  acionaExceptionEmail("Dados email inválidos") : 
	            	        			  acionaExceptionEmail("Dados email inválidos");		
	    }
	 
	 public User findByEmail(DadosLogin email) {
			Jedis jedis = jedisPool.getResource();
			try {
			Map<String, String> elementuser = jedis.hgetAll("user:".concat(String.valueOf(email.getEmail())));
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
	 
	 private String acionaExceptionEmail(String msg) throws InvalidEmailException {
		 throw new InvalidEmailException(msg, null, false, false);
	}

	public boolean validaToken(String authorization) throws InvalidTokenException {
			try {
				return Utils.validaTokens(authorization);
			} catch (Exception e) {
	            e.printStackTrace();
	            throw new InvalidTokenException();
	        }
		}
	 

}
