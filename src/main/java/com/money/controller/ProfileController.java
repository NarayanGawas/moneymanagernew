package com.money.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.money.dto.AuthDTO;
import com.money.dto.ProfileDTO;
import com.money.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService pofileService;
	@PostMapping("/register")
	public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO) {
	  ProfileDTO registerProfile =	pofileService.registerProfile(profileDTO);
	
	  return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);
	  
	}
	
	@GetMapping("/activate")
	public ResponseEntity<String> activateProfile(@RequestParam String token){
		boolean isActivated =pofileService.activateProfile(token);
		if(isActivated) {
			return ResponseEntity.ok("Profile activated successfully");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already used");
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDTO authdto){
		try {
			if(!pofileService.isAccountActive(authdto.getEmail())) {
				
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of( 
						"message","Account is not active.Please active your account first."
						));
			}
		Map<String,Object> response=pofileService.authenticateAndGenerateToken(authdto);
		return ResponseEntity.ok(response);
		}catch(Exception  e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
					"message",e.getMessage()
					));
		}
	}

}
