package com.meddo.covid.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meddo.covid.models.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CovidTrackerControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void covidDataTest() throws Exception {
		this.mockMvc
		.perform(get("/dashboard"))
		.andExpect(status().isOk());
	}
	
	@Test
    public void registerUserTest() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
        User user = new User();
        user.setUserName("BansaMa");
        user.setActive(true);
        user.setPassword("Manas123");
        user.setRoles("ADMIN");
        byte[] userByte = mapper.writeValueAsBytes(user);
        this.mockMvc
        	.perform(post("/registerUser")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userByte))
            .andExpect(status().isOk());
    }
}