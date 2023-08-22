package com.gonzalomendozafullstack.app.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gonzalomendozafullstack.app.entity.User;
import com.gonzalomendozafullstack.app.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	//Create a new User
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody User user){
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
	}
	
	//Read an User
	@GetMapping("/{id}")
	public ResponseEntity<?> read(@PathVariable(value = "id") Long userId){
		Optional<User> oUser = userService.findById(userId);
		
		if(!oUser.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(oUser);		
		
	}
	
	//Update User
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody User user, @PathVariable(value = "id") Long userId){
		Optional<User> oUser = userService.findById(userId);

		if(!oUser.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		//Fill entity
		oUser.get().setName(user.getName());
		oUser.get().setSurname(user.getSurname());
		oUser.get().setEmail(user.getEmail());
		oUser.get().setEnabled(user.getEnabled());
		
		//This other way to fill the properties (BeanUtils.copyProperties(user, oUser.get();)
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(oUser.get()));

	}
	
	//Delete User
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long userId){
	
		Optional<User> oUser = userService.findById(userId);

		if(!oUser.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		userService.deleteById(userId);
		return ResponseEntity.ok().build();
		
	}
	
	//Read All Users
	
	@GetMapping
	public List<User> readAll(){
		
		List<User> users = StreamSupport
				.stream(userService.findAll().spliterator(),false)
				.collect(Collectors.toList());
		return users;
		
	}

}
