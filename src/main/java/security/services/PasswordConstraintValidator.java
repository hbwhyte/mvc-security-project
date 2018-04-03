package security.services;

import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

import com.google.common.base.Joiner;

/**
 * Custom password validation rules.
 *
 * Implements the JavaX ConstraintValidator
 */

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                // Password must be between 8 and 100 characters
                // Need a high upperbound because it also validates encrypted passwords
                new LengthRule(8,100),
                // Password needs at least one upper case
                new UppercaseCharacterRule(1),
                // Password needs at least one digit
                new DigitCharacterRule(1),
                // Password cannot have any whitespace
                new WhitespaceRule()));

        // Checks if password passes all rules
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }

        // Custom constraint violation
        context.disableDefaultConstraintViolation();
        // Formats message if they input an unacceptable password
        context.buildConstraintViolationWithTemplate(
                Joiner.on("\n").join(validator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }
}
