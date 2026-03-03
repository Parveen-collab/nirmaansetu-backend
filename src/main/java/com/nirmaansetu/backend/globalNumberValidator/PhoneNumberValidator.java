package com.nirmaansetu.backend.globalNumberValidator;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        try {
            // "ZZ" allows parsing if the number already contains a "+" country code
            PhoneNumber number = phoneUtil.parse(value, "ZZ");
            return phoneUtil.isValidNumber(number);
        } catch (Exception e) {
            return false;
        }
    }
}

