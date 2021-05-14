package br.com.rmf.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {

	@PostMapping
	public ResponseEntity<?> test() {
		return ResponseEntity.ok().build();
	}
}
