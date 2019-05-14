package br.com.ternarius.inventario.sagi.infrastructure.security;

import br.com.ternarius.inventario.sagi.application.dto.CadastroDto;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author Elvis de Sousa
 */

public class PasswordsEqualConstraintValidator implements ConstraintValidator<PasswordsEqual, Object> {

    private String message;
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(PasswordsEqual constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        var valid = false;

        try {
            var password = BeanUtils.getProperty(value, firstFieldName);
            var confirmPassword = BeanUtils.getProperty(value, secondFieldName);

            if (!Objects.isNull(value)
                    && !Strings.isBlank(password)
                    && !Strings.isBlank(password)) {
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
