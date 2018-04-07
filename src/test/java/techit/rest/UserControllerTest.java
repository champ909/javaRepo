package techit.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.bytebuddy.utility.RandomString;

@Test
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:techit-servlet.xml", "classpath:applicationContext.xml" })
class UserControllerTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	String adminToken; // id=1
	String regularUserToken; // id=5

	@BeforeClass
	void setup() throws UnsupportedEncodingException, Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		String res = this.mockMvc
				.perform(post("/login").content("username=techit&password=abcd")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
				.andReturn().getResponse().getContentAsString();
		adminToken = new ObjectMapper().readTree(res).get("jwt").textValue();

		res = this.mockMvc
				.perform(post("/login").content("username=jojo&password=abcd")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
				.andReturn().getResponse().getContentAsString();
		regularUserToken = new ObjectMapper().readTree(res).get("jwt").textValue();

		System.out.println(adminToken);
		System.out.println(regularUserToken);
	}

	@Test
	void getUser() throws Exception {
		this.mockMvc.perform(get("/users/5").header("Authorization", "Bearer " + regularUserToken))
				.andExpect(status().isOk()).andExpect(jsonPath("username").value("jojo"));
	}

	@Test
	void getUser2() throws Exception {
		this.mockMvc.perform(get("/users/4").header("Authorization", "Bearer " + regularUserToken))
				.andExpect(status().is(403)).andExpect(jsonPath("username").doesNotExist());
	}

	@Test
	void getUser3() throws Exception {
		this.mockMvc.perform(get("/users/4").header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk())
				.andExpect(jsonPath("username").value("blee"));
	}

	@Test
	void getUsers1() throws Exception {
		this.mockMvc.perform(get("/users").header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].username").value("techit"));
	}

	@Test
	void getUsers2() throws Exception {
		this.mockMvc.perform(get("/users").header("Authorization", "Bearer " + regularUserToken))
				.andExpect(status().is(403)).andExpect(jsonPath("$[0].username").doesNotExist());
	}

	@Test
	void addUser1() throws Exception {
		this.mockMvc
				.perform(post("/users").header("Authorization", "Bearer " + regularUserToken).content(
						"{\"username\": \"techit\",\"password\":\"abcd\",\"firstName\": \"FName\",\"lastName\": \"LName\"}")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(403)).andExpect(jsonPath("username").doesNotExist());
	}

	@Test
	void addUser2() throws Exception {
		this.mockMvc
				.perform(post("/users").header("Authorization", "Bearer " + adminToken).content(
						"{\"username\": \"techit\",\"password\":\"abcd\",\"firstName\": \"FName\",\"lastName\": \"LName\"}")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andExpect(jsonPath("username").value("user1"));
	}

}