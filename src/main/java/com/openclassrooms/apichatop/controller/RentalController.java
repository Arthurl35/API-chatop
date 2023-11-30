package com.openclassrooms.apichatop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RentalController {
	
	@GetMapping("/rentals")
	public String getResource() {
		return "a value...";
	}	
	
}