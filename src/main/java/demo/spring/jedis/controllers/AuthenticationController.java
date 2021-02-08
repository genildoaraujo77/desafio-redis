package demo.spring.jedis.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.jedis.model.DadosLogin;
import demo.spring.jedis.model.User;
import demo.spring.jedis.model.UserToken;
import demo.spring.jedis.service.UserAuthenticationService;

@RestController
@RequestMapping("/authuser")
@CrossOrigin(origins = "*")
public class AuthenticationController {
	
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    public AuthenticationController(UserAuthenticationService userAuthenticationService){
        this.userAuthenticationService = userAuthenticationService;
    }

    public AuthenticationController(){
    }

    @PostMapping
   	public ResponseEntity<UserToken> atualizaTkCokkie(@RequestBody DadosLogin dadosLogin, HttpServletResponse response) throws Exception {
   	    
   			String tokenatualizado = userAuthenticationService.generateNewTokenWithLogin(dadosLogin);
   			UserToken usertoken = new UserToken();
   			User user = userAuthenticationService.findByEmail(dadosLogin);
   			user.setPassword("segredo");
   			usertoken.setUser(user);
   			usertoken.setToken(tokenatualizado);
   	   		Cookie cookie = new Cookie("Token", tokenatualizado);
   	        cookie.setPath("/");
   	        cookie.setHttpOnly(true);
   	        cookie.setMaxAge(60 * 30); // 30 minutos
//   	        cookie.setSecure(true);
   	        	response.addCookie(cookie);
   	   	   		return ResponseEntity
   	   	   				.ok()
   	   	   				.body(usertoken);
   }

}
