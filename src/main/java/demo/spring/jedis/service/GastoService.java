package demo.spring.jedis.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import demo.spring.jedis.model.Categoria;
import demo.spring.jedis.model.Gasto;
import demo.spring.jedis.model.GastoDetail;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

@Slf4j
@Service
@Builder
public class GastoService {

	@Autowired
	private JedisPool jedisPool;

	@Autowired
	private CategoriaService categoriaService;
	
	/*
	 * Funcionalidade: Categorização automatica de gasto
	 * No processo de integração de gastos, a categoria deve ser incluida automaticamente
	 * caso a descrição de um gasto seja igual a descrição de qualquer outro gasto já categorizado pelo cliente
	 * o mesmo deve receber esta categoria no momento da inclusão do mesmo 
	 */
	  

	public ResponseEntity<String> salvarGasto(Gasto gasto, String iduser) {
		Jedis jedis = jedisPool.getResource();
		ScanParams params = new ScanParams();
		Date data = new Date();
		Integer idcategoria = null;
		gasto.setCoduser(Integer.parseInt(iduser));
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String dataformatada = sdf.format(data);
//			String dataformatada = "27/03/1992";

			long idgasto = jedis.rpush("gids", "s");
			String descricao = gasto.getDescricao().trim();
			ScanResult<String> result = jedis.sscan("descricoes:gasto:".concat(String.valueOf(gasto.getCoduser())), "0",
					params.match(descricao + "*"));

			if (result.getResult() != null && !result.getResult().isEmpty()) {
				for (String str : result.getResult()) {
					String[] str_tratada = str.split(":id=");
					if (str_tratada[0].trim().equalsIgnoreCase(descricao)) {
						idcategoria = Integer.parseInt(str_tratada[1]);
					}
				}
			}

			if (idcategoria != null && idcategoria > 0) {
				Categoria categoria = categoriaService.findById(idcategoria);
				if (categoria != null) {
					String nomecategoria = jedis
							.set("gasto:".concat(dataformatada).concat(String.valueOf(gasto.getCoduser()))
									.concat(String.valueOf(idgasto)).concat("nomecategoria"), categoria.getNome());
					if (nomecategoria.equalsIgnoreCase("OK"))
						jedis.sadd("descricoes:gasto:".concat(String.valueOf(gasto.getCoduser())),
								descricao + ":id=" + categoria.getIdcategoria());
				}
			} else {
				String semcategoria = jedis
						.set("gasto:".concat(dataformatada).concat(String.valueOf(gasto.getCoduser()))
								.concat(String.valueOf(idgasto)).concat("nomecategoria"), "sem categoria");
				if (semcategoria.equalsIgnoreCase("OK"))
					jedis.sadd("descricoes:gasto:".concat(String.valueOf(gasto.getCoduser())), descricao + ":id=0");
			}

			String gastodescricao = jedis.set("gasto:".concat(dataformatada).concat(String.valueOf(gasto.getCoduser()))
					.concat(String.valueOf(idgasto)).concat("descricao"), gasto.getDescricao());
			String gastovalor = jedis.set("gasto:".concat(dataformatada).concat(String.valueOf(gasto.getCoduser()))
					.concat(String.valueOf(idgasto)).concat("valor"), String.valueOf(gasto.getValor()));
			String gastocoduser = jedis.set("gasto:".concat(dataformatada).concat(String.valueOf(gasto.getCoduser()))
					.concat(String.valueOf(idgasto)).concat("coduser"), String.valueOf(gasto.getCoduser()));
			String gastodata = jedis.set("gasto:".concat(dataformatada).concat(String.valueOf(gasto.getCoduser()))
					.concat(String.valueOf(idgasto)).concat("data"), String.valueOf(dataformatada));

			if (gastodescricao.equalsIgnoreCase("OK") && gastovalor.equalsIgnoreCase("OK")
					&& gastocoduser.equalsIgnoreCase("OK") && gastodata.equalsIgnoreCase("OK")) {
				Long gastosalvo = jedis.sadd("gastos:gasto:".concat(String.valueOf(dataformatada))
						.concat(String.valueOf(gasto.getCoduser())), String.valueOf(idgasto));
				if(gastosalvo == 1) {
					return new ResponseEntity<String>("Gasto salvo com sucesso", HttpStatus.CREATED);
				}
			}

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return new ResponseEntity<String>("Houve algum erro ao tentar salvar o gasto", HttpStatus.BAD_REQUEST);
	}

//	Funcionalidade: Categorização de gastos
//	  Dado que acesso como um cliente autenticado
//	  Quando acesso o detalhe de um gasto
//	  E este não possui uma categoria
//	  Então devo conseguir incluir uma categoria para este

	public ResponseEntity<String> saveGastoDetail(GastoDetail gastodetail, String iduser) {
		Jedis jedis = jedisPool.getResource();
		ScanParams params = new ScanParams();
		String element = null;
		String descricao = gastodetail.getDescricao().trim();
		String dataformatada = gastodetail.getData();
		String nomecategoria = gastodetail.getNomecategoria();
		gastodetail.setCoduser(Integer.parseInt(iduser));
		try {
			
			if (nomecategoria != null && !nomecategoria.equalsIgnoreCase("sem categoria")) {
				Categoria categoria = new Categoria();
				categoria.setNome(nomecategoria);
				Long idcategoria = categoriaService.salvarCategoria(categoria);
				
				if (idcategoria != null && idcategoria > 0) {
					
					ScanResult<String> result = jedis.sscan("descricoes:gasto:".concat(String.valueOf(gastodetail.getCoduser())), "0",
							params.match(descricao + "*"));
					
					if (result.getResult() != null && !result.getResult().isEmpty()) {
						for (String str : result.getResult()) {
								jedis.srem("descricoes:gasto:".concat(String.valueOf(gastodetail.getCoduser())), str);
						}
					}
					
						String updatenomecategoria = jedis
								.set("gasto:".concat(dataformatada).concat(String.valueOf(gastodetail.getCoduser()))
										.concat(String.valueOf(gastodetail.getIdgasto())).concat("nomecategoria"), nomecategoria);
						if (updatenomecategoria.equalsIgnoreCase("OK"))
							jedis.sadd("descricoes:gasto:".concat(String.valueOf(gastodetail.getCoduser())),
									descricao + ":id=" + idcategoria);
				} else {
                       System.out.println("Houve um erro ao tentar criar a categoria");				
				}
				
			}else {
				Integer idnovacategoria = null;
				ScanResult<String> result = jedis.sscan("descricoes:gasto:".concat(String.valueOf(gastodetail.getCoduser())), "0",
						params.match(descricao + "*"));
				
				if (result.getResult() != null && !result.getResult().isEmpty()) {
					for (String str : result.getResult()) {
						String[] str_tratada = str.split(":id=");
						if (str_tratada[0].trim().equalsIgnoreCase(descricao)) {
							idnovacategoria = Integer.parseInt(str_tratada[1]);
						}
					}
				}
				if (idnovacategoria != null && idnovacategoria > 0) {
					Categoria categoria = categoriaService.findById(idnovacategoria);
					if (categoria != null) {
						String novonomecategoria = jedis
								.set("gasto:".concat(dataformatada).concat(String.valueOf(gastodetail.getCoduser()))
										.concat(String.valueOf(gastodetail.getIdgasto())).concat("nomecategoria"), categoria.getNome());
						if (novonomecategoria.equalsIgnoreCase("OK"))
							jedis.sadd("descricoes:gasto:".concat(String.valueOf(gastodetail.getCoduser())),
									descricao + ":id=" + categoria.getIdcategoria());
					}
				} else {
					String novasemcategoria = jedis
							.set("gasto:".concat(dataformatada).concat(String.valueOf(gastodetail.getCoduser()))
									.concat(String.valueOf(gastodetail.getIdgasto())).concat("nomecategoria"), "sem categoria");
					if (novasemcategoria.equalsIgnoreCase("OK"))
						jedis.sadd("descricoes:gasto:".concat(String.valueOf(gastodetail.getCoduser())), descricao + ":id=0");
				}
			}

			String chvdescricao = jedis.set(
					"gasto:".concat(dataformatada).concat(String.valueOf(gastodetail.getCoduser()))
							.concat(String.valueOf(gastodetail.getIdgasto())).concat("descricao"),
					descricao);
			String chvvalor = jedis.set(
					"gasto:".concat(dataformatada).concat(String.valueOf(gastodetail.getCoduser()))
							.concat(String.valueOf(gastodetail.getIdgasto())).concat("valor"),
					String.valueOf(gastodetail.getValor()));
			String chvcoduser = jedis.set(
					"gasto:".concat(dataformatada).concat(String.valueOf(gastodetail.getCoduser()))
							.concat(String.valueOf(gastodetail.getIdgasto())).concat("coduser"),
					String.valueOf(gastodetail.getCoduser()));
			String chvdata = jedis.set(
					"gasto:".concat(dataformatada).concat(String.valueOf(gastodetail.getCoduser()))
							.concat(String.valueOf(gastodetail.getIdgasto())).concat("data"),
					String.valueOf(gastodetail.getData()));

			if (chvdescricao.equalsIgnoreCase("OK") && chvvalor.equalsIgnoreCase("OK")
					&& chvcoduser.equalsIgnoreCase("OK") && chvdata.equalsIgnoreCase("OK")) {
				element = "Dados do gasto atualizados com sucesso";
				return new ResponseEntity<String>(element, HttpStatus.ACCEPTED);
			}

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		
		return new ResponseEntity<String>(element, HttpStatus.BAD_REQUEST);
	}

	/*
	 * Funcionalidade: Listagem de gastos* Dado que acesso como um cliente
	 * autenticado que pode visualizar os gastos do cartão Quando acesso a interface
	 * de listagem de gastos Então gostaria de ver meus gastos mais atuais.
	 *
	 * Para esta funcionalidade é esperado 2.000 acessos por segundo. O cliente
	 * espera ver gastos realizados a 5 segundos atrás.
	 */

	public ResponseEntity<List<Gasto>> findAllGastos(Integer iduser, Integer limit) {
		Jedis jedis = jedisPool.getResource();
		try {
			Date data = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String dataformatada = sdf.format(data);
			Set<String> listids = jedis
					.smembers("gastos:gasto:".concat(String.valueOf(dataformatada)).concat(String.valueOf(iduser)));
			List<Gasto> listgastos = new ArrayList<Gasto>();

			if (listids != null && !listids.isEmpty()) {
				listids.forEach(idgasto -> {
					Gasto gasto = new Gasto();
					String descricao = jedis.get("gasto:".concat(String.valueOf(dataformatada))
							.concat(String.valueOf(iduser)).concat(String.valueOf(idgasto)).concat("descricao"));
					String valor = jedis.get("gasto:".concat(String.valueOf(dataformatada))
							.concat(String.valueOf(iduser)).concat(String.valueOf(idgasto)).concat("valor"));
					String coduser = jedis.get("gasto:".concat(String.valueOf(dataformatada))
							.concat(String.valueOf(iduser)).concat(String.valueOf(idgasto)).concat("coduser"));
					String datanabase = jedis.get("gasto:".concat(String.valueOf(dataformatada))
							.concat(String.valueOf(iduser)).concat(String.valueOf(idgasto)).concat("data"));
					gasto.setIdgasto(Integer.parseInt(idgasto));
					gasto.setDescricao(descricao);
					gasto.setValor(Double.parseDouble(valor));
					gasto.setCoduser(Integer.parseInt(coduser));
					gasto.setData(datanabase);
					listgastos.add(gasto);
				});
			}

			listgastos.sort((Gasto s1, Gasto s2) -> s2.getData().compareTo(s1.getData()));
			listgastos.sort((Gasto s1, Gasto s2) -> s2.getCoduser() - s1.getCoduser());
			listgastos.sort((Gasto s1, Gasto s2) -> s2.getIdgasto() - s1.getIdgasto());

			Integer limitfinal = limit > 5 ? 5 : listgastos.size();

			return new ResponseEntity<List<Gasto>>(listgastos.subList(0, limitfinal), HttpStatus.ACCEPTED);
		} catch (IndexOutOfBoundsException iou) {
			return ResponseEntity
					.notFound()
					.build();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/*
	 * Funcionalidade: Filtro de gastos Dado que acesso como um cliente autenticado
	 * E acessei a interface de listagem de gastos E configure o filtro de data
	 * igual a 27/03/1992
	 *
	 * Então gostaria de ver meus gastos apenas deste dia.
	 */

	public ResponseEntity<List<Gasto>> findGastosByDate(Integer iduser, String data) {
		Jedis jedis = jedisPool.getResource();
		try {
			Set<String> listids = jedis
					.smembers("gastos:gasto:".concat(String.valueOf(data)).concat(String.valueOf(iduser)));
			List<Gasto> listgastos = new ArrayList<Gasto>();

			if (listids != null && !listids.isEmpty()) {
				listids.forEach(idgasto -> {
					Gasto gasto = new Gasto();
					String descricao = jedis.get("gasto:".concat(data).concat(String.valueOf(iduser))
							.concat(String.valueOf(idgasto)).concat("descricao"));
					String valor = jedis.get("gasto:".concat(data).concat(String.valueOf(iduser))
							.concat(String.valueOf(idgasto)).concat("valor"));
					String coduser = jedis.get("gasto:".concat(data).concat(String.valueOf(iduser))
							.concat(String.valueOf(idgasto)).concat("coduser"));
					String datanabase = jedis.get("gasto:".concat(data).concat(String.valueOf(iduser))
							.concat(String.valueOf(idgasto)).concat("data"));
					gasto.setIdgasto(Integer.parseInt(idgasto));
					gasto.setDescricao(descricao);
					gasto.setValor(Double.parseDouble(valor));
					gasto.setCoduser(Integer.parseInt(coduser));
					gasto.setData(datanabase);
					listgastos.add(gasto);
				});
			}

			listgastos.sort((Gasto s1, Gasto s2) -> s2.getData().compareTo(s1.getData()));
			listgastos.sort((Gasto s1, Gasto s2) -> s2.getCoduser() - s1.getCoduser());
			listgastos.sort((Gasto s1, Gasto s2) -> s2.getIdgasto() - s1.getIdgasto());

			return new ResponseEntity<List<Gasto>>(listgastos, HttpStatus.ACCEPTED);
		} catch (IndexOutOfBoundsException iou) {
			return ResponseEntity
					.notFound()
					.build();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	public ResponseEntity<GastoDetail> findDetailById(Gasto gasto, String iduser) {
		Jedis jedis = jedisPool.getResource();
		ScanParams params = new ScanParams();
		Integer idcategoria = 0;
		gasto.setCoduser(Integer.parseInt(iduser));
		try {
			String dataformatada = gasto.getData();

			String descricao = jedis
					.get("gasto:".concat(String.valueOf(dataformatada)).concat(String.valueOf(gasto.getCoduser()))
							.concat(String.valueOf(gasto.getIdgasto())).concat("descricao"));
			String valor = jedis
					.get("gasto:".concat(String.valueOf(dataformatada)).concat(String.valueOf(gasto.getCoduser()))
							.concat(String.valueOf(gasto.getIdgasto())).concat("valor"));
			String coduser = jedis
					.get("gasto:".concat(String.valueOf(dataformatada)).concat(String.valueOf(gasto.getCoduser()))
							.concat(String.valueOf(gasto.getIdgasto())).concat("coduser"));
			String datanabase = jedis
					.get("gasto:".concat(String.valueOf(dataformatada)).concat(String.valueOf(gasto.getCoduser()))
							.concat(String.valueOf(gasto.getIdgasto())).concat("data"));
			
			descricao = descricao.trim();
			ScanResult<String> result = jedis.sscan("descricoes:gasto:".concat(String.valueOf(gasto.getCoduser())), "0",
					params.match(descricao + "*"));

			if (result.getResult() != null && !result.getResult().isEmpty()) {
				for (String str : result.getResult()) {
					String[] str_tratada = str.split(":id=");
					if (str_tratada[0].trim().equalsIgnoreCase(descricao) && Integer.parseInt(str_tratada[1]) != 0) {
						idcategoria = Integer.parseInt(str_tratada[1]);
					}
				}
			}
			
			String nomecategoria = null;
			if(idcategoria == 0) {
			    nomecategoria = "sem categoria";	
			}else {
				nomecategoria = jedis.get("categoria:".concat(String.valueOf(idcategoria)));	
			}
			

			if (descricao != null && valor != null && coduser != null && datanabase != null) {
				GastoDetail gastodetail = new GastoDetail();
				gastodetail.setIdgasto(gasto.getIdgasto());
				gastodetail.setDescricao(descricao);
				gastodetail.setValor(Double.parseDouble(valor));
				gastodetail.setCoduser(Integer.parseInt(coduser));
				gastodetail.setData(datanabase);
				if (idcategoria != null) {
					gastodetail.setCodcategoria(idcategoria);
				}
				if (nomecategoria != null) {
					gastodetail.setNomecategoria(nomecategoria);
				}
				return new ResponseEntity<GastoDetail>(gastodetail, HttpStatus.ACCEPTED);
			} else {
				return null;
			}

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public String deletebyId(Integer iduser, Integer id) {
		Jedis jedis = jedisPool.getResource();
		String element = null;
		try {
			Boolean resultgpoids = jedis.sismember("gastos:gasto:".concat(String.valueOf(iduser)), String.valueOf(id));
			if (resultgpoids) {
				Long feito1 = jedis
						.del("gasto:".concat(String.valueOf(iduser)).concat(String.valueOf(id)).concat("descricao"));
				Long feito2 = jedis
						.del("gasto:".concat(String.valueOf(iduser)).concat(String.valueOf(id)).concat("valor"));
				Long feito3 = jedis
						.del("gasto:".concat(String.valueOf(iduser)).concat(String.valueOf(id)).concat("coduser"));
				Long feito4 = jedis
						.del("gasto:".concat(String.valueOf(iduser)).concat(String.valueOf(id)).concat("data"));

				if (feito1 == 1 && feito2 == 1 && feito3 == 1 && feito4 == 1) {
					long remconj = jedis.srem("gastos:gasto".concat(String.valueOf(iduser)), String.valueOf(id));
					if (remconj == 1) {
						element = "Usuário deletado com sucesso";
					} else {
						element = "Houve um erro ao tentar deletar o usuário";
					}
				}
			}

		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		return element;
	}

	public Set<String> findGpo(String gpo) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.smembers(gpo);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void deleteElementGpo(Integer iduser, Integer id) {
		Jedis jedis = jedisPool.getResource();
		try {
			Long resultdelelementgpo = jedis.srem("gastos:gasto:".concat(String.valueOf(iduser)), String.valueOf(id));
			System.out.println(resultdelelementgpo);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public void deleteList(String list) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.del(list);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
}
