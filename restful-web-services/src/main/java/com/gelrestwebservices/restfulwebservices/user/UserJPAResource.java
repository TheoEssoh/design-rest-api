package com.gelrestwebservices.restfulwebservices.user;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserJPAResource {
	
	
	
	@Autowired
	private  UserDaoService  service;
	
	@Autowired
	private  UserRepository  userRepository;
	
	
	//GET /users
	//retrieveAllUsers
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers(){
		// return service.findAll();
		return userRepository.findAll();
	}
	
	//Get /users/{id}
	//retrieveUser(int id)
	@GetMapping("/jpa/users/{id}")
	public Resource<User> retrieveUser(@PathVariable int id) {
		//User user = service.findOne(id);
		Optional<User> user = userRepository.findById(id); //id exists or not, so return Optional
		//throw an error if user not found
		if(!user.isPresent())
			throw new UserNotFoundException("id-" + id);
		//HATEOAS - Hypermedia As the Engine of Application State
		Resource<User> resource = new Resource<User>(user.get());
		ControllerLinkBuilder linkTo = 
				linkTo(methodOn(this.getClass()).retrieveAllUsers());
		
		// resource.add(linkTo.withRel("all-users"));
		
		resource.add(linkTo.withRel("all-users"));

		return resource;
	}
	
	//DELETE
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		//User user = service.deleteById(id);
		userRepository.deleteById(id);
	}
	
	//HATEOAS
	
	//input - details of user
	//output - CREATED & Return the created URI
	@PostMapping("/jpa/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = service.save(user);
		
		//CREATED 201 SUCCESS
		// /user/4
		URI location = ServletUriComponentsBuilder
		.fromCurrentRequest()
		.path("/{id}")
		.buildAndExpand(savedUser.getId()).toUri();
		
		
		return ResponseEntity.created(location).build();
	}
	
	
	


}