package com.modzo.jwt.resources.user.reminder

import com.modzo.jwt.domain.users.commands.RemindUserPassword
import org.hibernate.validator.constraints.NotBlank

class RemindUserPasswordRequest {
    @NotBlank
    String email

    RemindUserPassword toRemindUserPassword() {
        return new RemindUserPassword(email)
    }
}
