package demo.spring.jedis.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.jedis.model.User;
import demo.spring.jedis.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
	@Autowired
	private UserService service;

	@GetMapping("/{id}")
	public String getOneUser(@PathVariable Integer id) {
		return service.findById(id);
	}

	@PutMapping("/{id}")
	public String getOneUser(@PathVariable Integer id, @RequestBody User user) {
		return service.updateById(id, user);
	}

	@GetMapping("/listusers")
	public List<User> getKeys() {
		return service.findAllUsers();
	}

	@GetMapping("/grupouser/{gpo}")
	public Set<String> getGpo(@PathVariable String gpo) {
		return service.findGpo(gpo);
	}

	@DeleteMapping("/deleteuser/{id}")
	public String deleteUserById(@PathVariable Integer id) {
		return service.deletebyId(id);
	}

	@DeleteMapping("/deletelist/{list}")
	public void deleteList(@PathVariable String list) {
		service.deleteList(list);
	}

	@DeleteMapping("/deletegpo/{id}")
	public void deleteUser(@PathVariable Integer id) {
		service.deleteElementGpo(id);
	}

	@DeleteMapping("/dropdatabase")
	public void deleteDatabase() {
		service.deleteDatabase();
	}
}