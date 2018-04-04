package techit.rest.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import techit.model.User;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;
import techit.util.JwtSignatureUtil;
import techit.util.PropMap;

@RestController
public class LoginController {

	@Autowired
	UserDao userDao;

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
			throw new RestException(400, "Missing username and/or password.");

		User userObj = userDao.getUser(username);
		if (userObj == null)
			throw new RestException(400, "Wrong username and/or password.");

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
		if (encoder.matches(password, userObj.getHash()))
			return new PropMap<String, Object>().put("success", true)
					.put("jwt", JwtSignatureUtil.generateToken(userObj)).getMap();
		else
			throw new RestException(400, "Wrong username and/or password.");
	}
}
