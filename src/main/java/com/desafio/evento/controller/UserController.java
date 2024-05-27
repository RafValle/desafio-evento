package com.desafio.evento.controller;

import com.desafio.evento.model.User;
import com.desafio.evento.model.request.RegisterRequest;
import com.desafio.evento.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<Void> login(@RequestBody @Valid RegisterRequest request){
        var user = userService.saveUser(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping()
    public ResponseEntity<List<User>> login(){
        var user = userService.findAll();
        return ResponseEntity.ok(user);
    }
}