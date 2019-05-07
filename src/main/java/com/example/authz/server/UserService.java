package com.example.authz.server;

import java.util.Arrays;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

//   private final UserRepository repository;
//
//   public UserService(UserRepository repository) {
//       this.repository = repository;
//   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       //User user = repository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
       GrantedAuthority authorityManager = new SimpleGrantedAuthority("MANAGER");
       GrantedAuthority authorityDeveloper = new SimpleGrantedAuthority("DEVELOPER");
       return new org.springframework.security.core.userdetails.User(username + "@gmail.com", "123", Arrays.asList(authorityManager, authorityDeveloper));
   }
}