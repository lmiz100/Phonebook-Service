package io.github.lmizrahi.phonebook.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.util.StringUtils;

import java.util.Locale;


public final class PhonebookUtils {

    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    private PhonebookUtils() {}

    public static String normalizedPhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[-\\s]", "");
    }

    //return phone number in E.164 format. I.e. with + and digits only
    public static String formatPhoneNumber(String phoneNumberStr) {
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phoneNumberStr, Locale.getDefault().getCountry());
            return phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Heuristically determines if a query string likely represents a phone number or part of one.
     * Accepts +, digits, and optional separators like '-' or space.
     * Allow formats like "+123456789", "123-456", "456789", etc.
     */
    public static boolean isLikelyPhoneNumber(String input) {
        if (!StringUtils.hasText(input)){
            return false;
        }

        String cleanedInput = input.trim();
        return cleanedInput.matches("^\\+?[0-9\\-\\s]{3,}$");
    }
}
