package techit.rest;

import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:techit-servlet.xml", "classpath:applicationContext.xml" })
class LoginControllerTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@BeforeClass
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	void login() throws Exception {
		this.mockMvc.perform(post("/login").content("username=techit&password=abcd").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
				.andExpect(status().isOk()).andExpect(jsonPath("success").value("true"))
				.andExpect(jsonPath("jwt").value(not(isEmptyString())));
	}

	@Test
	void login2() throws Exception {
		this.mockMvc.perform(post("/login").content("username=techit&password=abcde").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
				.andExpect(status().is4xxClientError()).andExpect(jsonPath("jwt").doesNotExist());
	}
}