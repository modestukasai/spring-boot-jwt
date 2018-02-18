package com.modzo.jwt.resources.admin.users.register

import com.modzo.jwt.domain.users.commands.CreateUser
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank

class RegisterUserRequest {
    @NotBlank
    @Email
    String email

    @NotBlank
    String password

    CreateUser toCreateUser() {
        return new CreateUser(email, password)
    }
}
