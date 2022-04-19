package com.mazbah.ecomd.service;

import com.mazbah.ecomd.entity.AuthToken;
import com.mazbah.ecomd.entity.User;
import com.mazbah.ecomd.exceptions.AuthenticationFailException;

public interface AuthenticationService {
    public AuthToken getToken(User user);
    public User getUser(String token);
    public void saveConfirmationToken(AuthToken authToken);
    public void authenticate(String token) throws AuthenticationFailException;
}
