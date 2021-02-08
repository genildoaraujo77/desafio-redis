package demo.spring.jedis.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.jedis.model.Categoria;
import demo.spring.jedis.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {
	@Autowired
	private CategoriaService service;
		
	@PostMapping
	public Long salvarCategoria(@RequestBody Categoria categoria){
		return service.salvarCategoria(categoria);
	}
	
	@GetMapping
	public List<Categoria> getKeys(){
		return service.findAllCategorias();
	}

//	@GetMapping("/buscacategoria/{id}")
//	public Categoria getOneCategoria(@PathVariable Integer id){
//		return service.findById(id);
//	}
	
//	@PutMapping("/update/{id}")
//	public String getOneCategoria(@PathVariable Integer id, @RequestBody Categoria categoria){
//		return service.updateById(id, categoria);
//	}
	
//	@GetMapping("/grupocategoria/{gpo}")
//	public Set<String> getGpo(@PathVariable String gpo){
//		return service.findGpo(gpo);
//	}
//
//	@DeleteMapping("/deletecategoria/{id}")
//	public String deleteCategoriaById(@PathVariable Integer id){
//		return service.deletebyId(id);
//	}
//	
//	@DeleteMapping("/deletelist/{list}")
//	public void deleteList(@PathVariable String list){
//		service.deleteList(list);
//	}
//	
//	@DeleteMapping("/deletegpo/{id}")
//	public void deleteCategoria(@PathVariable Integer id){
//		service.deleteElementGpo(id);
//	}
}