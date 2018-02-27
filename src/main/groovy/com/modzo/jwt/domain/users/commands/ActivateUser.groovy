package com.modzo.jwt.domain.users.commands

import com.modzo.jwt.domain.users.User
import com.modzo.jwt.domain.users.Users
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.jwt.domain.DomainException.userActivationCodeIsIncorrect
import static com.modzo.jwt.domain.DomainException.userWithActivationCodeIsAlreadyActivated
import static org.apache.commons.lang3.StringUtils.isBlank

class ActivateUser {

    final String activationCode

    ActivateUser(String activationCode) {
        this.activationCode = activationCode
    }

    @Component
    private static class Validator {
        private final Users users

        Validator(Users users) {
            this.users = users
        }

        void validate(ActivateUser activateUser) {
            if (isBlank(activateUser.activationCode)) {
               throw userActivationCodeIsIncorrect(activateUser.activationCode)
            }

            User user = users.findByActivationCode(activateUser.activationCode)
                    .orElseThrow { userActivationCodeIsIncorrect(activateUser.activationCode) }

            if(user.enabled){
                userWithActivationCodeIsAlreadyActivated(activateUser.activationCode)
            }
        }
    }

    @Component
    static class Handler {
        private final Users users

        private final Validator validator

        Handler(Users users, Validator validator) {
            this.users = users
            this.validator = validator
        }

        @Transactional
        void handle(ActivateUser activateUser) {
            validator.validate(activateUser)

            User user = users.findByActivationCode(activateUser.activationCode).get()
            user.activate()
        }
    }
}
