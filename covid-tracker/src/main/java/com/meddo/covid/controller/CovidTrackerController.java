package com.meddo.covid.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.meddo.covid.models.AuthenticationRequest;
import com.meddo.covid.models.AuthenticationResponse;
import com.meddo.covid.models.CovidData;
import com.meddo.covid.models.User;
import com.meddo.covid.service.MyUserDetailsService;
import com.meddo.covid.util.JwtUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Covid Tracke Details", description = "API for fetching the details of covid 19 ")
class CovidTrackerController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	RestTemplate resTemplate;
	
	@Value("${covid.api.url}")
	String covidApiUrl;

	@ApiOperation(value = "View the Current Status of Covid 19 Cases in India ", response = CovidData.class, httpMethod = "GET")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully retrieved list"),
	        @ApiResponse(code = 500, message = "Failed to retrieve the resource you were trying to reach")
	})
	@RequestMapping(value =  "/dashboard",method = RequestMethod.GET)
	public ResponseEntity<CovidData[]> getCovidDetails() {
		logger.info("Fetching current Covid 19 details ");
		CovidData[] covidData =null;
		covidData  =   resTemplate.getForObject(covidApiUrl,  CovidData[].class);
		Arrays.stream(covidData).forEach(obj -> obj.setActiveCases(obj.getNoOfCases() - obj.getCured() - obj.getDeaths()));
		return ResponseEntity.ok(covidData);
	}

    @ApiOperation(value = "Authenticate the user based on given userName & passWord",response = AuthenticationResponse.class, httpMethod = "POST")
    @ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successfully authenticated & A JWT token will be returned as a response "),
	        @ApiResponse(code = 401, message = "UserName & PassWord are invalid hence, UnAuthorized")
	})
	@RequestMapping(value = "/authenticateUser", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

    	logger.info("Request received for authentication of user : "+authenticationRequest.getUsername());
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
				);


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

    @ApiOperation(value = "Register the user based on given userName & passWord which will get stored in database",
    		response = User.class, httpMethod = "POST")
    @ApiResponses(value = {
	        @ApiResponse(code = 200, message = "user Successfully registered")        
    })
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
    	logger.info("Registering the user with user : "+user.toString());
		return ResponseEntity.ok(userDetailsService.save(user));
	}
}