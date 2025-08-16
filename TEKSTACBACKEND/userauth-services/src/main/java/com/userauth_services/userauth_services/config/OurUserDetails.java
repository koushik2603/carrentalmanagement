package com.userauth_services.userauth_services.config;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//import com.userauth_services.userauth_services.Model.Pojo.OurUsers;
import com.userauth_services.userauth_services.Model.Pojo.UserProfile;

public class OurUserDetails implements UserDetails  
{
  private String email;
  private String password;
  private List<GrantedAuthority> role;
	public OurUserDetails(UserProfile ourUsers) {
	super();
	this.email = ourUsers.getEmail();  //xyz
	this.password = ourUsers.getPassword();  //abc
	this.role = Arrays.stream(ourUsers.getUserRole().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());//admin
}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		System.out.println(this.role + " Role ___________________");
		return this.role;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}
}
