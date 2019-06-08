package br.com.ternarius.inventario.sagi.infrastructure.security;

import br.com.ternarius.inventario.sagi.application.dto.AlteraSenhaDto;
import br.com.ternarius.inventario.sagi.application.dto.CadastroDto;
import br.com.ternarius.inventario.sagi.application.dto.NovaSenhaDto;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PasswordEqual {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidPasswordsNovaSenhaDto() {
        var novaSenhaDto = NovaSenhaDto.builder()
                .senha("eloah1106")
                .confirmacaoSenha("eloah1106")
                .build();

        var constraintViolations = validator.validate(novaSenhaDto);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

    @Test
    public void testInvalidPasswordNovaSenhaDto() {
        var novaSenhaDto = NovaSenhaDto.builder()
                .senha("cal323546")
                .confirmacaoSenha("cal2356")
                .build();

        var constraintViolations = validator.validate(novaSenhaDto);

        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream()
                .map(ConstraintViolation::getMessage)).contains("As senhas não coincidem.");
    }

    @Test
    public void testWhenPasswordIsNullNovaSenhaDto() {
        var novaSenhaDto = NovaSenhaDto.builder()
                .senha(null)
                .confirmacaoSenha(null)
                .build();

        var constraintViolations = validator.validate(novaSenhaDto);

        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream()
                .map(ConstraintViolation::getMessage)).contains("As senhas não coincidem.");
    }

    @Test
    public void testInvalidPasswordCadastroDto() {
        var cadastroDto = CadastroDto.builder()
                .nome("Elvis de Sousa")
                .email("leoguardah@gmail.com")
                .senha("cal110673")
                .confirmacaoSenha("cal0546967")
                .build();

        var constraintViolations = validator.validate(cadastroDto);

        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream()
                .map(ConstraintViolation::getMessage)).contains("As senhas não coincidem.");
    }

    @Test
    public void testValidPasswordCadastroDto() {
        var cadastroDto = CadastroDto.builder()
                .nome("Elvis de Sousa")
                .email("leoguardah@gmail.com")
                .senha("CALO1107")
                .confirmacaoSenha("CALO1107")
                .build();

        var constraintViolations = validator.validate(cadastroDto);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }

    @Test
    public void testValidPasswordAlteraSenhaDto() {
        var alteraSenhaDto = AlteraSenhaDto.builder()
                .senhaAtual("CALO0303")
                .novaSenha("CALO1106")
                .confirmacaoSenha("CALO1106")
                .build();

        var constraintViolations = validator.validate(alteraSenhaDto);

        Assert.assertEquals(0, constraintViolations.size());
    }

    @Test
    public void testInvalidPasswordAlteraSenhaDto() {
        var alteraSenhaDto = AlteraSenhaDto.builder()
                .senhaAtual("CALO0303")
                .novaSenha("CALO1105")
                .confirmacaoSenha("CALO1106")
                .build();

        var constraintViolations = validator.validate(alteraSenhaDto);

        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations.stream()
                .map(ConstraintViolation::getMessage)).contains("As senhas não coincidem.");
    }
}