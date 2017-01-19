package com.example;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
	public String createDivision(@RequestBody Division newDivision) throws com.example.DuplicateResultException {
		if(newDivision.getId()!=null)
		{
			Division division = divisions.stream()				   // Convert to steam
					.filter(x -> newDivision.getId().equals(x.getId()))	// we want "divisionId" only
					.findAny()									// If 'findAny' then return found
					.orElse(null);	
			// If not found, return null
			if (division!=null)
			{
				throw new DuplicateResultException("Division already exists");
			}
			else
			{
				divisions.add(newDivision);
			}
			
		}
		else
		{
			String uuid = UUID.randomUUID().toString();
			newDivision.setId(uuid);
			divisions.add(newDivision);
			
		}
		String newId = newDivision.getId();
		return newId;
	}
	
	@RequestMapping(value = "/{divisionId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public String updateDivision(@RequestBody Division newDivision) throws com.example.DuplicateResultException {
		if(newDivision.getId()!=null)
		{	
			divisions = (ArrayList<Division>) divisions.stream().filter(x -> !newDivision.getId().equals(x.getId())).collect(Collectors.toList());
	
			divisions.add(newDivision);
		}
		else
		{
			String uuid = UUID.randomUUID().toString();
			newDivision.setId(uuid);
			divisions.add(newDivision);
		}
		String newId = newDivision.getId();
		return newId;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<Division> listDivisions() throws com.example.NoSuchResultException {
		if (divisions.isEmpty())
		{
			throw new NoSuchResultException("No matching divisions");
		}
		return divisions;
	}
	
	@RequestMapping(value = "/{divisionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Division setImagesToHashCount(@PathVariable("divisionId") String divisionId) throws com.example.NoSuchResultException{
		Division division = divisions.stream()				   // Convert to steam
				.filter(x -> divisionId.equals(x.getId()))	// we want "divisionId" only
				.findAny()									// If 'findAny' then return found
				.orElse(null);	
		// If not found, return null
		if (division==null)
		{
			throw new NoSuchResultException("No such division");
		}
		return division;	
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Such Result")
	@ExceptionHandler(NoSuchResultException.class)
	public String NoSuchResultError(NoSuchResultException e) {
		String error = "An Error has occurred: " + e.getMessage();
		return error;
	}

	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Result already exists")
	@ExceptionHandler(DuplicateResultException.class)
	public String DuplicateResultError(DuplicateResultException e) {
		String error = "An Error has occurred: " + e.getMessage();
		return error;
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Issue with Server")
	@ExceptionHandler(Exception.class)
	public String JsonProcessingError(Exception e) {
		String error = "An Error has occurred: " + e.getMessage();
		return error;
	}

}
