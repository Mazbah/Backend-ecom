package com.mazbah.ecomd.dto.User;

import com.mazbah.ecomd.enums.Role;

public class UserUpdateDto {

    // skipping updating password as of now
    private String firstName;
    private String lastName;
    private Role role;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
