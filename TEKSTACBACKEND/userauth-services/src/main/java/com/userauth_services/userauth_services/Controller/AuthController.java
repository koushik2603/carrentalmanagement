package com.userauth_services.userauth_services.Controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.userauth_services.userauth_services.Model.DAO.ServiceImple.JwtService;
import com.userauth_services.userauth_services.Model.DAO.ServiceImple.UserServiceImpl;
import com.userauth_services.userauth_services.Model.Pojo.UserProfile;
//import com.userauth_services.userauth_services.Model.dto.UserRegistration;
import com.userauth_services.userauth_services.Model.dto.UserRequestDto;
import com.userauth_services.userauth_services.Model.dto.UserResponseDto;
import com.userauth_services.userauth_services.Model.repositories.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/save")
    public String register(@RequestBody UserProfile user) {
        return userServiceImpl.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserRequestDto userdto) {
        Map<String, String> map = new HashMap<>();
        Optional<UserProfile> op = userRepository.findByEmail(userdto.getEmail());

        if (op.isEmpty()) {
            map.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else if (!passwordEncoder.matches(userdto.getPassword(), op.get().getPassword())) {
            map.put("error", "Invalid password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userdto.getEmail(), userdto.getPassword())
        );

        if (auth.isAuthenticated()) {
            String role = op.get().getUserRole().toUpperCase();
            map.put("role", role);
            map.put("token", jwtService.generateToken(userdto.getEmail(), role));
            return ResponseEntity.ok(map);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @GetMapping("/Validatetoken")
    public String validateToken(@RequestParam("Authorization") String token) {
        return jwtService.validateToken(token)
            ? jwtService.extractRoleFromToken(token)
            : "Not Valid";
    }

    @GetMapping("/getusers")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public List<UserResponseDto> getUsers() {
    	System.out.println("getuser called");
        return userServiceImpl.AllUsers();
    }


    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")

    public UserProfile updateProfile(@PathVariable String userId, @RequestBody UserProfile updatedUser) {
        return userServiceImpl.updateUserProfile(userId, updatedUser);
    }
    
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')or hasAuthority('USER')")

    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        boolean deleted = userServiceImpl.deleteUserById(userId);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } 
    }
    @GetMapping("/getregularusers")
    @PreAuthorize("hasAuthority('ADMIN')")

        public List<UserResponseDto> GetUsers() {
        return userServiceImpl.getUsersByRole("USER");
    }
    @GetMapping("/getadmins")
    @PreAuthorize("hasAuthority('ADMIN')")

        public List<UserResponseDto> getAdmins() {
        return userServiceImpl.getUsersByRole("ADMIN");
    }
    
    @GetMapping("/getuserbyemail/{email}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")

    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userServiceImpl.getUserByEmail(email));
    }



    
}
