package com.mazbah.ecomd.service.impl;

import com.mazbah.ecomd.config.Messages;
import com.mazbah.ecomd.entity.AuthToken;
import com.mazbah.ecomd.entity.User;
import com.mazbah.ecomd.exceptions.AuthenticationFailException;
import com.mazbah.ecomd.repository.AuthTokenRepository;
import com.mazbah.ecomd.service.AuthenticationService;
import com.mazbah.ecomd.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    AuthTokenRepository repository;

    @Override
    public AuthToken getToken(User user) {
        return repository.findTokenByUser(user);
    }

    @Override
    public User getUser(String token) {
        AuthToken authToken = repository.findTokenByToken(token);

        if(Helper.notNull(authToken)){
            if(Helper.notNull(authToken.getUser())){
                return authToken.getUser();
            }
        }
        return null;
    }

    @Override
    public void saveConfirmationToken(AuthToken authToken) {
        repository.save(authToken);
    }

    @Override
    public void authenticate(String token) throws AuthenticationFailException {
        if(!Helper.notNull(token)){
            throw new AuthenticationFailException(Messages.AUTH_TOKEN_NOT_PRESENT);
        }
        if(!Helper.notNull(getUser(token))){
            throw new AuthenticationFailException(Messages.AUTH_TOKEN_NOT_VALID);
        }
    }
}
