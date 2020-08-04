package com.meddo.covid.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.meddo.covid.models.MyUserDetails;
import com.meddo.covid.models.User;
import com.meddo.covid.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	@Autowired
    UserRepository userRepository;
	@Autowired
	private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		/*
		 * return new User("foo", "foo", new ArrayList<>());
		 */
    	
    	 Optional<User> user = userRepository.findByUserName(userName);

         user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));

         return user.map(MyUserDetails::new).get();
    }
    public User save(User user) {
		User newUser = new User();
		newUser.setUserName(user.getUserName());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setActive(user.isActive());
		newUser.setRoles(user.getRoles());
		return userRepository.save(newUser);
	}
}