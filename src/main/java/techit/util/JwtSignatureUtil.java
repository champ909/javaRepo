package techit.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import techit.model.User;
import techit.rest.error.RestExceptionHandler;

public class JwtSignatureUtil {

	private static Logger logger = LogManager.getLogger(RestExceptionHandler.class);
	private static Key key;
	private static String secretKey;// = "thisis16charkey"; // can be configured in a configuration file
	private static String issuer = "TechIT2"; // can be configured in a configuration file

	static {
		Resource resource = new ClassPathResource("key.txt");
		secretKey = null;
		try {
			secretKey = IOUtils.toString(new InputStreamReader(resource.getInputStream()));
			key = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String generateToken(User user) {
		return Jwts.builder().setSubject(user.getUsername()).setIssuer(issuer).signWith(SignatureAlgorithm.HS512, key)
				.compact();
	}

	public static String verifyToken(String token) {
		String username = null;
		try {
			Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
			if (claims.getIssuer().equals(issuer))
				username = claims.getSubject();
		} catch (Exception e) {
			logger.warn("Invalid JWT:" + e.getMessage());
		}
		return username;
	}
}
