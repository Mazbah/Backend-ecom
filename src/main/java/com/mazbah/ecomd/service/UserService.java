package com.mazbah.ecomd.service;

import com.mazbah.ecomd.dto.ResponseDto;
import com.mazbah.ecomd.dto.User.*;
import com.mazbah.ecomd.exceptions.AuthenticationFailException;
import com.mazbah.ecomd.exceptions.CustomException;

public interface UserService {
    public ResponseDto singUp(SignUpDto signUpDto);
    public SignInResponseDto signIn(SignInDto signInDto);
    public ResponseDto createUser(String token, UserCreateDto userCreateDto);
    public ResponseDto updateUser(String token, UserUpdateDto userUpdateDto);
}
