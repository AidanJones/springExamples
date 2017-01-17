package com.example;

import java.util.ArrayList;

import javax.jms.JMSException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;


@RequestMapping(value = "/divisions")
@RestController
public class DivisionController {

	ArrayList<Division> divisions = new ArrayList<Division>();
	
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public int createDivision(@RequestBody Division division) {
		return 1;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<Division> listDivisions() {
		return divisions;
	}
	
	@RequestMapping(value = "/{divisionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Division setImagesToHashCount(@PathVariable("divisionId") String divisionId){
		Division division = divisions.stream()				   // Convert to steam
				.filter(x -> divisionId.equals(x.getId()))	// we want "divisionId" only
				.findAny()									// If 'findAny' then return found
				.orElse(null);								// If not found, return null
		return division;	
	}

}
