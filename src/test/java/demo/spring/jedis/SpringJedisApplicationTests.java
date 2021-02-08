package demo.spring.jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import demo.spring.jedis.model.Gasto;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringJedisApplicationTests {
	
	@Autowired
	private JedisPool jedisPool;

	@Test
	public void contextLoads() {
		Gasto gasto = new Gasto();
		gasto.setIdgasto(1);
		gasto.setDescricao("descricao");
		gasto.setValor(10.00);
		gasto.setCoduser(1);
		gasto.setData("10/10/2010");
        salvarGasto(gasto);
	}
	
	public Long salvarGasto(Gasto gasto) {
		Jedis jedis = jedisPool.getResource();
		try {
			long id = jedis.rpush("gids", "s");
			String feito1 = jedis.set("gasto:".concat(String.valueOf(id)).concat("descricao"), gasto.getDescricao());
			String feito2 = jedis.set("gasto:".concat(String.valueOf(id)).concat("valor"), String.valueOf(gasto.getValor()));
			String feito3 = jedis.set("gasto:".concat(String.valueOf(id)).concat("coduser"), String.valueOf(gasto.getCoduser()));
			String feito4 = jedis.set("gasto:".concat(String.valueOf(id)).concat("data"), String.valueOf(new Date()));
		
			if(feito1.equalsIgnoreCase("OK") && feito2.equalsIgnoreCase("OK") && feito3.equalsIgnoreCase("OK") && feito4.equalsIgnoreCase("OK")) {
				return jedis.sadd("gastos:gasto", String.valueOf(id));	
			}
			
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		return null;
	}

}

