package demo.spring.jedis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.spring.jedis.model.User;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Service
@Builder
public class UserService {
	
	@Autowired
	private JedisPool jedisPool;

//    @Autowired
//    private UserRepository gastosRepository;

	public String deletebyId(Integer id) {
		Jedis jedis = jedisPool.getResource();
		String element = null;
		try {
			Boolean resultgpoids = jedis.sismember("users:user", String.valueOf(id));
			if(resultgpoids) {
				Long feito = jedis.del("user:".concat(String.valueOf(id)));
				if(feito == 1) {
					long remconj = jedis.srem("users:user", String.valueOf(id));
					if(remconj == 1) {
						element = "Usuário deletado com sucesso";
					}else {
						element = "Houve um erro ao tentar deletar o usuário";
					}
				}
			}
			
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		return element;
	}
	
	public String updateById(Integer id, User user) {
		Jedis jedis = jedisPool.getResource();
		String element = null;
		try {
			Boolean resultgpoids = jedis.sismember("users:user", String.valueOf(id));
			if(resultgpoids) {
				String feito = jedis.set("user:".concat(String.valueOf(id)), user.getNome());
				if(feito.equalsIgnoreCase("OK")) {
					element = "Dados do usuário atualizados com sucesso";
				}
			}
			else {
				element = "Este usuário não existe para alterar";
			}
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		return element;
	}
	
	public String findById(Integer id) {
		Jedis jedis = jedisPool.getResource();
		try {
		String element = jedis.get("users:user");
		
		return element;
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
	}
	
	public List<User> findAllUsers() {
		Jedis jedis = jedisPool.getResource();
		try {
			Set<String>  listids = jedis.smembers("users:user");
			List<User> listusers = new ArrayList<User>();
			
			if(listids != null && !listids.isEmpty()) {
				listids.forEach(iduser -> {
					User newuser = new User();
					newuser.setIduser(Integer.parseInt(iduser));
					String nome = jedis.get("user:".concat(iduser));
					newuser.setNome(nome);
					listusers.add(newuser);
				});	
			}
			
			return listusers;
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
	}
	
	public Set<String> findGpo(String gpo) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.smembers(gpo);
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
	}
	
	public void deleteElementGpo(Integer id) {
		Jedis jedis = jedisPool.getResource();
		try {
			Long resultdelelementgpo = jedis.srem("users:user", String.valueOf(id));
			System.out.println(resultdelelementgpo);
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
	}

	public void deleteDatabase() {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.flushDB();
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
	}

	public void deleteList(String list) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.del(list);
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
	}

}
