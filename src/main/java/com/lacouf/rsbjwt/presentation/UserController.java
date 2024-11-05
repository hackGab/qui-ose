package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.model.Departement;
import com.lacouf.rsbjwt.service.UserAppService;
import com.lacouf.rsbjwt.service.dto.EtudiantDTO;
import com.lacouf.rsbjwt.service.dto.JWTAuthResponse;
import com.lacouf.rsbjwt.service.dto.LoginDTO;
import com.lacouf.rsbjwt.service.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	private final UserAppService userService;

	@PostMapping("/login")
	public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDTO loginDto){
		System.out.println("Login: " + loginDto.getEmail() + " " + loginDto.getPassword());
		try {
			String accessToken = userService.authenticateUser(loginDto);
			System.out.println("Token: " + accessToken);
			final JWTAuthResponse authResponse = new JWTAuthResponse(accessToken);
			return ResponseEntity.accepted()
					.contentType(MediaType.APPLICATION_JSON)
					.body(authResponse);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JWTAuthResponse());
		}
	}

	@GetMapping("/me")
	public ResponseEntity<UserDTO> getMe(HttpServletRequest request){
		return ResponseEntity.accepted().contentType(MediaType.APPLICATION_JSON).body(
			userService.getMe(request.getHeader("Authorization")));
	}


	@GetMapping("/departements")
	public ResponseEntity<List<String>> getAllDepartements() {
		List<String> departementValues = userService.getAllDepartements();
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(departementValues);
	}


	@GetMapping("/etudiants/departement/{departement}")
	public ResponseEntity<List<EtudiantDTO>> getEtudiantsByDepartement(@PathVariable String departement) {
		try {
			System.out.println("Fetching students for department: " + departement);
			List<EtudiantDTO> etudiants = userService.getEtudiantsByDepartement(departement);
			System.out.println("Students found: " + etudiants);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(etudiants);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
}
