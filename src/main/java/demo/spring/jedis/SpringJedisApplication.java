package demo.spring.jedis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableAutoConfiguration
@Builder
public class SpringJedisApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringJedisApplication.class, args);
	}

	@Bean
	@ConfigurationProperties("redis")
	public JedisPoolConfig jedisPoolConfig() {
		return new JedisPoolConfig();
	}

	@Bean(destroyMethod = "close")
	public JedisPool jedisPool(@Value("${spring.redis.host}") String host) {
		return new JedisPool(jedisPoolConfig(), host);
	}

//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		log.info(jedisPoolConfig.toString());
//
//		try (Jedis jedis = jedisPool.getResource()) {
//			coffeeService.findAllCoffee().forEach(c -> {
//				jedis.hset("springbucks-menu",
//						c.getName(),
//						Long.toString(c.getPrice().getAmountMinorLong()));
//			});
//
//			Map<String, String> menu = jedis.hgetAll("springbucks-menu");
//			log.info("Menu: {}", menu);
//
//			String price = jedis.hget("springbucks-menu", "espresso");
//			log.info("espresso - {}",
//					Money.ofMinor(CurrencyUnit.of("CNY"), Long.parseLong(price)));
//		}
//	}
}

