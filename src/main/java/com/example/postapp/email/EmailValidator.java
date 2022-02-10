package com.example.postapp.email;

import org.springframework.stereotype.Component;

@Component
public class EmailValidator {

    public boolean isValid(String email) {
        boolean hasAt = email.contains("@");
        boolean hasDot = email.split("@")[1].contains(".");
        boolean validSize = email.split("@")[0].length() > 3;
        return hasAt && hasDot && validSize;
    }
}
