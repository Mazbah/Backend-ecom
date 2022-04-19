package com.mazbah.ecomd.service.impl;

import com.mazbah.ecomd.config.Messages;
import com.mazbah.ecomd.dto.ResponseDto;
import com.mazbah.ecomd.dto.User.*;
import com.mazbah.ecomd.entity.AuthToken;
import com.mazbah.ecomd.entity.User;
import com.mazbah.ecomd.enums.ResponseStatus;
import com.mazbah.ecomd.enums.Role;
import com.mazbah.ecomd.exceptions.AuthenticationFailException;
import com.mazbah.ecomd.exceptions.CustomException;
import com.mazbah.ecomd.repository.UserRepository;
import com.mazbah.ecomd.service.AuthenticationService;
import com.mazbah.ecomd.service.UserService;
import com.mazbah.ecomd.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

@Service("UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public ResponseDto singUp(SignUpDto signUpDto) throws CustomException{
        // Check to see if the current email address has already been registered
        if(Helper.notNull(userRepository.findByEmail(signUpDto.getEmail()))){
            // If the email address has been registered then throw an exception.
            throw new CustomException("User with this email already exists!!!");
        }

        // First encrypt the password
        String encryptedPassword = signUpDto.getPassword();
        try{
            encryptedPassword = hashPassword(signUpDto.getPassword());
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            logger.error("Hashing password failed {} ", e.getMessage());
        }

        User user = new User(signUpDto.getFirstName(), signUpDto.getLastName(), signUpDto.getEmail(), Role.user, encryptedPassword);

        User createdUser;
        try{
            //save the user
            createdUser = userRepository.save(user);
            // generate token for user
            final AuthToken authToken = new AuthToken(createdUser);
            // save token in database
            authenticationService.saveConfirmationToken(authToken);
            // success in creating
            return new ResponseDto(ResponseStatus.success.toString(), Messages.USER_CREATED);
        }catch (Exception e){
            // handle signup error
            throw new CustomException(e.getMessage());
        }
    }

    public SignInResponseDto signIn(SignInDto signInDto) throws CustomException{
        // first find User by email
        User user = userRepository.findByEmail(signInDto.getEmail());
        if(!Helper.notNull(user)){
            throw new AuthenticationFailException("User not Present!!!");
        }

        try{
            // check if the password is right
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))){
                // passowrd doesnot match
                throw new AuthenticationFailException(Messages.WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("Hashing password is failed!!!", e.getMessage());
            throw new CustomException(e.getMessage());
        }

        AuthToken token = authenticationService.getToken(user);
        if(!Helper.notNull(token)){
            // Not not present
            throw new CustomException("Token not present");
        }

        return new SignInResponseDto("success", token.getToken());
    }

    String hashPassword(String password) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return myHash;
    }

    public ResponseDto createUser(String token, UserCreateDto userCreateDto) throws CustomException, AuthenticationFailException{
        User creatingUser = authenticationService.getUser(token);
        if(!canCrudUser(creatingUser.getRole())){
            // user can't create new user
            throw new AuthenticationFailException(Messages.USER_NOT_PERMITTED);
        }
        String encryptPassword = userCreateDto.getPassword();
        try {
            encryptPassword = hashPassword(userCreateDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("Hashing password failed {} ",e.getMessage());
        }
        User user = new User(userCreateDto.getFirstName(), userCreateDto.getLastName(), userCreateDto.getEmail(), userCreateDto.getRole(), encryptPassword);

        User createdUser;
        try{
            createdUser = userRepository.save(user);
            final AuthToken authToken = new AuthToken(createdUser);
            authenticationService.saveConfirmationToken(authToken);
            return new ResponseDto(ResponseStatus.success.toString(), Messages.USER_CREATED);
        }catch (Exception e){
            throw new CustomException(e.getMessage());
        }
    }

    public ResponseDto updateUser(String token, UserUpdateDto userUpdateDto) throws CustomException, AuthenticationFailException{
        User user = authenticationService.getUser(token);
        user.setFirstName(userUpdateDto.getFirstName());
        user.setLastName(userUpdateDto.getLastName());
        user.setRole(userUpdateDto.getRole());
        userRepository.save(user);
        return new ResponseDto(ResponseStatus.success.toString(), "User is updated");
    }

    boolean canCrudUser(Role role){
        if(role == Role.admin || role == Role.manager){
            return true;
        }
        return false;
    }

    boolean canCrudUser(User userUpdating, Integer userIdBeingUpdated) {
        Role role = userUpdating.getRole();
        // admin and manager can crud any user
        if (role == Role.admin || role == Role.manager) {
            return true;
        }
        // user can update his own record, but not his role
        if (role == Role.user && userUpdating.getId() == userIdBeingUpdated) {
            return true;
        }
        return false;
    }

}
