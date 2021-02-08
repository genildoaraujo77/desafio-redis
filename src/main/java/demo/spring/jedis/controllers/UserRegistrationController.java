package demo.spring.jedis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.jedis.exceptions.RegistrateException;
import demo.spring.jedis.model.User;
import demo.spring.jedis.service.UserRegistrationService;

@RestController
@RequestMapping("/registros")
@CrossOrigin(origins = "*")
public class UserRegistrationController {

    private UserRegistrationService userRegistrationService;

    @Autowired
    public UserRegistrationController(UserRegistrationService userRegistrationService){
        this.userRegistrationService = userRegistrationService;
    }

    public UserRegistrationController(){

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registrateUser(@RequestBody User userRegistration) throws RegistrateException{
    	String result = userRegistrationService.registrate(userRegistration);
        return ResponseEntity
        		.ok(result);
    }

}