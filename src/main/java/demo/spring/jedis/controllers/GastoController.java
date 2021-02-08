package demo.spring.jedis.controllers;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.jedis.exceptions.InvalidTokenException;
import demo.spring.jedis.exceptions.SignatureException;
import demo.spring.jedis.model.Gasto;
import demo.spring.jedis.model.GastoDetail;
import demo.spring.jedis.service.GastoService;
import demo.spring.jedis.service.UserAuthenticationService;
import demo.spring.jedis.utils.Utils;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/gastos")
@CrossOrigin(origins = "*")
public class GastoController {
	
	private UserAuthenticationService userAuthenticationService;
	private GastoService service;

    @Autowired
    public GastoController(UserAuthenticationService userAuthenticationService, GastoService service){
        this.userAuthenticationService = userAuthenticationService;
        this.service = service;
    }
    
	public GastoController() {
		super();
	}

	@PostMapping
	public ResponseEntity<String> salvarGasto(@RequestBody Gasto gasto, HttpServletRequest request) throws InvalidTokenException, SignatureException {
		String iduser;
		return (iduser = authToken(request)) != null ? 
	        			service.salvarGasto(gasto, iduser) : 
	        		    	   ResponseEntity
	   	   	   				   .badRequest()
	   	   	   				   .body("Token inválido ou usuário sem autorização");
	}
	
	@GetMapping("/gastosatuais")
	public ResponseEntity<List<Gasto>> getKeys(@RequestParam(name = "limit") Integer limit, HttpServletRequest request) throws InvalidTokenException, SignatureException {
		String iduser;
		return (iduser = authToken(request)) != null ? 
				service.findAllGastos(Integer.parseInt(iduser), limit) : 
    		    	   ResponseEntity
	   	   				   .notFound()
	   	   				   .build();
	}
	
	@GetMapping("/listgastobydate")
	public ResponseEntity<List<Gasto>> getKeys(@RequestParam(value = "date") String date, HttpServletRequest request) throws InvalidTokenException, SignatureException {
		String iduser;
		return (iduser = authToken(request)) != null ? 
				service.findGastosByDate(Integer.parseInt(iduser), date) : 
    		    	   ResponseEntity
	   	   				   .notFound()
	   	   				   .build();
	}
	
	@GetMapping("/gastodetail")
	public ResponseEntity<GastoDetail> getGastoDetail(@RequestBody Gasto gasto, HttpServletRequest request) throws InvalidTokenException, SignatureException {
		String iduser;
		return (iduser = authToken(request)) != null ? 
				service.findDetailById(gasto, iduser) : 
    		    	   ResponseEntity
	   	   				   .notFound()
	   	   				   .build();
	}
	
	@PostMapping("/gastodetail")
	public ResponseEntity<String> saveGastoDetail(@RequestBody GastoDetail gastodetail, HttpServletRequest request) throws InvalidTokenException, SignatureException {
		String iduser;
		return (iduser = authToken(request)) != null ?
				service.saveGastoDetail(gastodetail, iduser) : 
    		    	   ResponseEntity
	   	   				   .notFound()
	   	   				   .build();
	}
	
	public String authToken(HttpServletRequest request) throws InvalidTokenException {
		Cookie[] cookies = request.getCookies();
		Claims iduser;
		if (cookies != null && cookies.length != 0) {
	        for (Cookie cookie : cookies) {
	        	return userAuthenticationService.validaToken(cookie.getValue()) ?
	        			(iduser = Utils.decodeToken(cookie.getValue())) != null ?
	        					iduser.getSubject()	: null 
	        				: null;
	       		}
		}
		return null;
	}
	
//	@PostMapping("/teste/{members}/{idcategoria}")
//	public void teste(@PathVariable String members, @PathVariable String idcategoria) {
//		service.testeConj(members, idcategoria);
//	}
//	
//	@GetMapping("/buscateste/{members}")
//	public ScanResult<String> buscaTeste(@PathVariable String members) {
//		return service.buscaTeste(members);
//	}
	
//	@GetMapping("/buscagasto/{iduser}/{id}")
//	public Gasto getOneGasto(@PathVariable Integer iduser, @PathVariable Integer id){
//		return service.findById(iduser, id);
//	}
	
//	@PutMapping("/update/{id}")
//	public String getOneGasto(@PathVariable Integer id, @RequestBody Gasto gasto){
//		return service.updateById(id, gasto);
//	}
	
//	@GetMapping("/grupogasto/{gpo}")
//	public Set<String> getGpo(@PathVariable String gpo){
//		return service.findGpo(gpo);
//	}
//
//	@DeleteMapping("/deletegasto/{iduser}/{id}")
//	public String deleteGastoById(@PathVariable Integer iduser, @PathVariable Integer id){
//		return service.deletebyId(iduser, id);
//	}
//	
//	@DeleteMapping("/deletelist/{list}")
//	public void deleteList(@PathVariable String list){
//		service.deleteList(list);
//	}
//	
//	@DeleteMapping("/deletegpo/{iduser}/{id}")
//	public void deleteGasto(@PathVariable Integer iduser, @PathVariable Integer id){
//		service.deleteElementGpo(iduser, id);
//	}
}