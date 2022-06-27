package com.mazbah.ecomd.controller;

import com.mazbah.ecomd.dto.ResponseDto;
import com.mazbah.ecomd.dto.User.*;
import com.mazbah.ecomd.entity.User;
import com.mazbah.ecomd.exceptions.AuthenticationFailException;
import com.mazbah.ecomd.exceptions.CustomException;
import com.mazbah.ecomd.repository.UserRepository;
import com.mazbah.ecomd.service.AuthenticationService;
import com.mazbah.ecomd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public List<User> findAllUser(@RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public ResponseDto signUp(@RequestBody SignUpDto signUpDto) throws CustomException {
        return userService.singUp(signUpDto);
    }

    //TODO token should be updated
    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInDto signInDto) throws CustomException {
        return userService.signIn(signInDto);
    }

    @PutMapping("/updateUser")
    public ResponseDto updateUser(@RequestParam("token") String token, @RequestBody UserUpdateDto userUpdateDto){
        authenticationService.authenticate(token);
        return userService.updateUser(token, userUpdateDto);
    }

    @PostMapping("/createUser")
    public ResponseDto createUser(@RequestParam("token") String token, @RequestBody UserCreateDto userCreateDto)
        throws CustomException, AuthenticationFailException{
        authenticationService.authenticate(token);
        return userService.createUser(token, userCreateDto);
    }

}
