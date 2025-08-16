package com.userauth_services.userauth_services.config;

import java.util.Optional;

  import org.springframework.beans.factory.annotation.Autowired; 
  import  org.springframework.security.core.userdetails.UserDetails; 
  import   org.springframework.security.core.userdetails.UserDetailsService; 
  import  org.springframework.security.core.userdetails.UsernameNotFoundException;

//import com.userauth_services.userauth_services.Model.Pojo.OurUsers;
import com.userauth_services.userauth_services.Model.Pojo.UserProfile;
import com.userauth_services.userauth_services.Model.repositories.UserRepository;
  
  
  
public class OurUserDetailService implements UserDetailsService {
  
@Autowired 
UserRepository userRepository;
@Override 
public UserDetails loadUserByUsername(String username) throws   UsernameNotFoundException
  {
	  Optional<UserProfile> Op= userRepository.findByEmail(username); 
	  return Op.map(OurUserDetails::new).orElseThrow(()->new
  UsernameNotFoundException("Username not found"));
  }
}
  
 