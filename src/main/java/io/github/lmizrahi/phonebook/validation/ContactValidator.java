package io.github.lmizrahi.phonebook.validation;

import io.github.lmizrahi.phonebook.model.Contact;
import io.github.lmizrahi.phonebook.utils.PhonebookUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class ContactValidator {

    public void validate(Contact contact) {
        if (!StringUtils.hasText(contact.getFirstName())) {
            throw new IllegalArgumentException("First name must not be empty");
        }

        if (!isValidPhoneNumber(contact.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            return false;
        }

        String normalized = PhonebookUtils.normalizedPhoneNumber(phoneNumber);
        return phoneNumber.matches("^\\+?[0-9]+([\\-\\s]?[0-9]+)*$") &&
                normalized.length() >= 7 && normalized.length() <= 15;
    }
}
