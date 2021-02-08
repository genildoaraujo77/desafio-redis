package demo.spring.jedis.utils;

import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import demo.spring.jedis.exceptions.ExpiredTokenException;
import demo.spring.jedis.exceptions.InvalidTokenException;
import demo.spring.jedis.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Utils {
	
	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
	
	 private static String key = geraKeyRandom();
	 
	//30 minutos
	    private static final long expirationTime = 1800000;
	
	public static String geraSenha(String senha) {
		String senhagerada = null;
		Argon2 argon2 = Argon2Factory.create();
	  
		char[] password = senha.toCharArray();

		try {
		    String hash = argon2.hash(10, 65536, 1, password);
		    if (argon2.verify(hash, password)) {
		    	senhagerada = hash;
		    	LOG.info("Senha válida gerada com sucesso");
		    } else {
		    	LOG.info("Não foi possível gerar a senha");
		    }
		} finally {
		    argon2.wipeArray(password);
		}
		return senhagerada;
	}
	
	public static boolean validaSenha(String senha, String senhauser) {
    	boolean validou = false;
    	String aux = "$argon2";
		Argon2 argon2 = Argon2Factory.create();
	  
		char[] password = senha.toCharArray();
		
		try {
			if (argon2.verify(aux.concat(senhauser), password)) {
				validou = true;
				LOG.info("Senha válida");
			} else {
				LOG.info("Senha não válida");
		    }
		} finally {
		    argon2.wipeArray(password);
		}
		return validou;
	}
	
	 //validar token
	 public static boolean validaTokens(String authorization) throws InvalidTokenException {
			if(!authorization.isEmpty() && validate(authorization)) {
				return true;
			}
			return false;
		}
	 
	 public static String getRandomPass(int len){
		 char[] chart ={'0','1','2','3','4','5','6','7','8','9'};

		 char[] senha= new char[len];

		 int chartLenght = chart.length;
		 Random rdm = new Random();

		 for (int x=0; x<len; x++)
		 senha[x] = chart[rdm.nextInt(chartLenght)];

		 return new String(senha);
		 }
	 
	 private static boolean validate(String token) throws InvalidTokenException {
	        try {
	            String tokenTratado = token.replace("Token=", "");
	            Claims claims = decodeToken(tokenTratado);
	            
	            LOG.info("Data de emissão do token: " + claims.getIssuedAt());
	            LOG.info("Id do usuário: " + claims.getSubject());
	            
	            if (claims.getExpiration().before(new Date(System.currentTimeMillis()))) throw new ExpiredTokenException();
	            System.out.println("Data de expiração do token: " + claims.getExpiration());
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new InvalidTokenException();
	        }

	    }
	 
	   public static Integer getIdUser(String token) {
	            String tokenTratado = token.replace("Token=", "");
	            Claims claims = decodeToken(tokenTratado);
	            
	            return Integer.parseInt(claims.getSubject());
	        
	    }
	 
	    public static String generateTokenTmp(User user) {
	        return Jwts.builder()
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setSubject(String.valueOf(user.getIduser()))
	                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
	                .signWith(SignatureAlgorithm.HS256, key)
	                .compact();
	    }
	    
	    public static Claims decodeToken(String token) {
	        return Jwts.parser()
	                .setSigningKey(key)
	                .parseClaimsJws(token)
	                .getBody();
	    }
	    
	    public static String geraKeyRandom() {
			String letras = "ABCDEFGHIJKLMNOPQRSTUVYWXZ";
			Random random = new Random();
			String armazenaChaves = "";
			int index = -1;
			for( int i = 0; i < 9; i++ ) {
			    index = random.nextInt( letras.length() );
			    armazenaChaves += letras.substring( index, index + 1 );
			}
			return armazenaChaves;
		}
	    
	    public static boolean validaDatas(Date emissão, Date vencimento) {
//	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	    	    Date emissão = sdf.parse("2020-04-30 12:00:12.0070000");
//	    	    Date vencimento = sdf.parse("2020-04-30 13:00:01.0070000");
  	 
			long diferencaSegundos = (vencimento.getTime() - emissão.getTime()) / (1000);
			long diferencaMinutos = (vencimento.getTime() - emissão.getTime()) / (1000*60);
			long diferencaHoras = (vencimento.getTime() - emissão.getTime()) / (1000*60*60);
			long diferencaDias = (vencimento.getTime() - emissão.getTime()) / (1000*60*60*24);
			long diferencaMeses = (vencimento.getTime() - emissão.getTime()) / (1000*60*60*24) / 30;
			long diferencaAnos = ((vencimento.getTime() - emissão.getTime()) / (1000*60*60*24) / 30) / 12;
  	 
			System.out.println(diferencaSegundos);
			System.out.println(diferencaMinutos);
			System.out.println(diferencaHoras);
			System.out.println(diferencaDias);
			System.out.println(diferencaMeses);
			System.out.println(diferencaAnos);
			
			return (diferencaMinutos < 30) ? true : false;
			
	    }
	    
}
