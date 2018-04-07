package techit.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

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
	private static int expirationTime = 3600; // in seconds

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
		Map<String, Object> claims = new PropMap<String, Object>()
				.put("id", user.getId())
				.put("username", user.getUsername())
				.put("role", user.getType())
				.getMap();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, expirationTime);
		return Jwts.builder().setClaims(claims).setIssuer(issuer).setExpiration(cal.getTime())
				.signWith(SignatureAlgorithm.HS512, key).compact();
	}

	public static Claims verifyToken(String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
			if (claims.getIssuer().equals(issuer))
				return claims;
		} catch (Exception e) {
			logger.warn("Invalid JWT:" + e.getMessage());
		}
		return null;
	}
}
