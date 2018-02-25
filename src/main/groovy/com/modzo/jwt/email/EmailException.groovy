package com.modzo.jwt.email

class EmailException extends RuntimeException{
    EmailException(String message) {
        super(message)
    }

    static EmailException templateParsingFailed(String templatePath){
        return EmailException("Template parsing from path `${templatePath}` has failed")
    }
}
