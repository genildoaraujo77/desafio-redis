package demo.spring.jedis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.spring.jedis.model.Categoria;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Service
@Builder
public class CategoriaService {
	
	@Autowired
	private JedisPool jedisPool;

	
//    @Autowired
//    private CategoriaRepository categoriasRepository;

	public Long salvarCategoria(Categoria categoria) {
		Jedis jedis = jedisPool.getResource();
		try {
			Long tamanho = jedis.llen("cids");
			Boolean exist = jedis.sismember("categorias:categoria", categoria.getNome().concat(":id=").concat(String.valueOf(tamanho)));
			if(exist) {
				return tamanho;
			}
			long idcategoria = jedis.rpush("cids", "s");
			String feito1 = jedis.set("categoria:".concat(String.valueOf(idcategoria)), categoria.getNome());
		
			if(feito1.equalsIgnoreCase("OK")) {
				Long idscategoria = jedis.sadd("categorias:ids", String.valueOf(idcategoria));
				Long catsalva = jedis.sadd("categorias:categoria", categoria.getNome().concat(":id=").concat(String.valueOf(idcategoria)));
				if(catsalva == 1 && idscategoria == 1) {
					return idcategoria;
				}else {
					return 0L;
				}
			}
			
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		return null;
	}
	
	public List<Categoria> findAllCategorias() {
		Jedis jedis = jedisPool.getResource();
		try {
			Set<String>  listids = jedis.smembers("categorias:ids");
			List<Categoria> listcats = new ArrayList<Categoria>();
			
			if(listids != null && !listids.isEmpty()) {
				listids.forEach(idcategoria -> {
					Categoria newcat = new Categoria();
					newcat.setIdcategoria(Integer.parseInt(idcategoria));
					String nome = jedis.get("categoria:".concat(idcategoria));
					newcat.setNome(nome);
					listcats.add(newcat);
				});	
			}
			
			return listcats;
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
	}
	
	public Categoria findById(Integer idcategoria) {
		Jedis jedis = jedisPool.getResource();
		try {
		String element = jedis.get("categoria:".concat(String.valueOf(idcategoria)));
		Categoria cat = new Categoria();
		cat.setIdcategoria(idcategoria);
		cat.setNome(element);
		
		return cat;
		}finally {
			if(jedis != null) {
				jedis.close();
			}
		}
	}
	
//	public String deletebyId(Integer idcategoria) {
//		Jedis jedis = jedisPool.getResource();
//		String element = null;
//		try {
//			Boolean resultgpoids = jedis.sismember("categorias:categoria", String.valueOf(idcategoria));
//			if(resultgpoids) {
//				Long feito = jedis.del("categoria:".concat(String.valueOf(idcategoria)));
//				if(feito == 1) {
//					long remconj = jedis.srem("categorias:categoria", String.valueOf(idcategoria));
//					if(remconj == 1) {
//						element = "Categoria deletada com sucesso";
//					}else {
//						element = "Houve um erro ao tentar deletar a categoria";
//					}
//				}
//			}
//			
//		}finally {
//			if(jedis != null) {
//				jedis.close();
//			}
//		}
//		return element;
//	}
	
//	public String updateById(Integer idcategoria, Categoria categoria) {
//		Jedis jedis = jedisPool.getResource();
//		String element = null;
//		try {
//			Boolean resultgpoids = jedis.sismember("categorias:categoria", String.valueOf(idcategoria));
//			if(resultgpoids) {
//				String feito1 = jedis.set("categoria:".concat(String.valueOf(idcategoria)), categoria.getNome());
//				
//				if(feito1.equalsIgnoreCase("OK")) {
//					return element = "Dados do categoria atualizados com sucesso";	
//				}
//			}
//			else {
//				element = "Não foi possível encontrar o categoria para realizar a alteração dos dados";
//			}
//		}finally {
//			if(jedis != null) {
//				jedis.close();
//			}
//		}
//		return element;
//	}
}
