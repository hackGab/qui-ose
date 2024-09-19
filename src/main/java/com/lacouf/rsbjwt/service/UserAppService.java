package com.lacouf.rsbjwt.service;

import com.lacouf.rsbjwt.model.*;
import com.lacouf.rsbjwt.repository.UserAppRepository;
import com.lacouf.rsbjwt.service.dto.*;
import com.lacouf.rsbjwt.security.JwtTokenProvider;
import com.lacouf.rsbjwt.security.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAppService {
//    private final AuthenticationManager authenticationManager;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final UserAppRepository userAppRepository;
//
//    public String authenticateUser(LoginDTO loginDto) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
//        return jwtTokenProvider.generateToken(authentication);
//    }
//
//    public UserDTO getMe(String token) {
//        token = token.startsWith("Bearer") ? token.substring(7) : token;
//        String email = jwtTokenProvider.getEmailFromJWT(token);
//        UserApp user = userAppRepository.findUserAppByEmail(email).orElseThrow(UserNotFoundException::new);
//        return switch(user.getRole()){
//        };
//    }
}
