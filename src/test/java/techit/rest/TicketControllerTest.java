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
class TicketControllerTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	String bleeUserToken; // id=4
	String adminToken; // id=1
	String regularUserToken; // id=5

	@BeforeClass
	void setup() throws UnsupportedEncodingException, Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		String res = this.mockMvc
				.perform(post("/login").content("username=blee&password=abcd")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
				.andReturn().getResponse().getContentAsString();
		bleeUserToken = new ObjectMapper().readTree(res).get("jwt").textValue();

		res = this.mockMvc
				.perform(post("/login").content("username=techit&password=abcd")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
				.andReturn().getResponse().getContentAsString();
		adminToken = new ObjectMapper().readTree(res).get("jwt").textValue();

		res = this.mockMvc
				.perform(post("/login").content("username=jojo&password=abcd")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
				.andReturn().getResponse().getContentAsString();
		regularUserToken = new ObjectMapper().readTree(res).get("jwt").textValue();
	}

	@Test
	void getTickets1() throws Exception {
		this.mockMvc.perform(get("/tickets").header("Authorization", "Bearer " + adminToken)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1));
	}

	@Test
	void getTickets2() throws Exception {
		this.mockMvc.perform(get("/tickets").header("Authorization", "Bearer " + bleeUserToken))
				.andExpect(status().is(403)).andExpect(jsonPath("$[0].id").doesNotExist());
	}

	@Test
	void getTicket1() throws Exception {
		this.mockMvc.perform(get("/tickets/1").header("Authorization", "Bearer " + regularUserToken))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1));
	}

	@Test
	void getTicket2() throws Exception {
		this.mockMvc.perform(get("/tickets/5").header("Authorization", "Bearer " + regularUserToken))
				.andExpect(status().is(404)).andExpect(jsonPath("$[0].id").doesNotExist());
	}

	@Test
	void addTicket1() throws Exception {
		this.mockMvc
				.perform(post("/tickets").header("Authorization", "Bearer " + regularUserToken).content(
						"{\"createdForName\": \"Joseph Joestar\",\"subject\": \"Projector Malfunction\",\"unitId\":1}")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1));
	}

	@Test
	void addTicket2() throws Exception {
		this.mockMvc.perform(post("/tickets").header("Authorization", "Bearer " + regularUserToken))
				.andExpect(status().is(404)).andExpect(jsonPath("$[0].id").doesNotExist());
	}

}