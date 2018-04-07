package techit.rest.error;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

import io.jsonwebtoken.Claims;
import techit.model.User;
import techit.model.User.Type;
import techit.model.dao.UserDao;
import techit.util.JwtSignatureUtil;

@ControllerAdvice
public class RestExceptionHandler {

	private static Logger logger = LogManager.getLogger(RestExceptionHandler.class);

	@Autowired
	UserDao userDao;

	@ExceptionHandler(RestException.class)
	public ResponseEntity<Object> handleRestExceptions(RestException ex) {
		RestError error = ex.getError();
		return new ResponseEntity<Object>(ex.getError(), HttpStatus.valueOf(error.getStatusCode()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleOtherExceptions(Exception ex) {
		logger.error("General Error", ex);
		return new ResponseEntity<Object>(new RestError(500, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ModelAttribute("currentUser")
	public User getUserFromJwt(@RequestHeader(name = "Authorization", defaultValue = "NoAuth") String auth) {
		if (StringUtils.isEmpty(auth) || !auth.contains("Bearer "))
			return null;

		Claims claims = JwtSignatureUtil.verifyToken(auth.substring(7));
		if (claims == null)
			return null;

		User u = new User();
		u.setId(claims.get("id", Long.class));
		u.setUsername(claims.get("username", String.class));
		u.setType(Type.valueOf(claims.get("role", String.class).toUpperCase()));
		return u;
	}
}
