/*******************************************************************************
 * Copyright (c) 2019 Michael Qin
 *
 * The freedom to run the program as you wish, for any purpose (freedom 0).
 *
 * The freedom to study how the program works, and change it so it does your computing 
 * as you wish (freedom 1). Access to the source code is a precondition for this.
 *
 * The freedom to redistribute copies so you can help your neighbor (freedom 2).
 *
 * The freedom to distribute copies of your modified versions to others (freedom 3). 
 * By doing this you can give the whole community a chance to benefit from your changes. 
 * Access to the source code is a precondition for this.
 *
 * Contributors:
 *     Michael Qin - initial API and implementation
 *******************************************************************************/
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