//package com.example.demo.authserver.users;
//
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//
//import java.io.Serializable;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@Component
//public class MyAuthenticationProvider implements AuthenticationProvider, Serializable {
//    private static Logger logger = Logger.getLogger(MyAuthenticationProvider.class.getName());
//    private final UserDetailsService userService;
//
//    public MyAuthenticationProvider(UserDetailsService userService){
//        this.userService = userService;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication auth) throws AuthenticationException {
//        logger.log(Level.INFO, "Authentication attempt by " + auth.getName());
//
//        UserDetails userDetails = userService.loadUserByUsername(auth.getName());
//
//        boolean isAllowed =
//                userDetails.getPassword().equals(auth.getCredentials());
//
//        if (isAllowed)
//            return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), userDetails.getAuthorities());
//        else
//            throw new BadCredentialsException("Username/password does not match for "+ auth.getPrincipal());
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        logger.log(Level.INFO, aClass.getName());
//        return aClass.getName().equals(UsernamePasswordAuthenticationToken.class.getName());
//    }
//}
