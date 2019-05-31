package br.com.ternarius.inventario.sagi.infrastructure.security;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author Elvis de Sousa
 *
 */

public class PasswordsEqualConstraintValidator implements ConstraintValidator<PasswordEqual, Object> {
    private String message;
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(PasswordEqual constraintAnnotation) {
        message = constraintAnnotation.message();
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        var valid = false;

        try {
            var password = BeanUtils.getProperty(value, firstFieldName);
            var confirmPassword = BeanUtils.getProperty(value, secondFieldName);

            if (!Objects.isNull(value)) {
                valid = password.equals(confirmPassword);
            }

            if (!valid) {
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode("senha")
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
            }

            return valid;
        } catch (final Exception ignoring) {

        }

        return valid;
    }
}
