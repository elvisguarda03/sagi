package br.com.ternarius.inventario.sagi.domain.entity;

import br.com.ternarius.inventario.sagi.domain.enums.StatusUsuario;
import br.com.ternarius.inventario.sagi.domain.enums.TipoUsuario;
import br.com.ternarius.inventario.sagi.domain.repository.UsuarioRepository;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository repository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void createData() {
        var usuario = Usuario.builder()
                .nome("Elvis de Sousa")
                .email("leoguardah@gmail.com")
                .senha("eloah1106")
                .tipoUsuario(TipoUsuario.ADMIN)
                .status(StatusUsuario.EMAIL_VALIDADO)
                .build();
        usuario.criptografarSenha();

        var usuario2 = Usuario.builder()
                .nome("Wagner Santana")
                .email("pessoalwagner@gmail.com")
                .senha("greg1234")
                .tipoUsuario(TipoUsuario.ADMIN)
                .status(StatusUsuario.EMAIL_VALIDADO)
                .build();
        usuario2.criptografarSenha();

        var usuario3 = Usuario.builder()
                .nome("Guacom")
                .email("gguacom@gmail.com")
                .senha("joao1234")
                .tipoUsuario(TipoUsuario.ADMIN)
                .status(StatusUsuario.EMAIL_VALIDADO)
                .build();
        usuario3.criptografarSenha();

        repository.save(usuario);
        repository.save(usuario2);
        repository.save(usuario3);
    }

    @Test
    public void createShouldPersistData() {
        Usuario user = Usuario.builder()
                .nome("Ozama Binladen")
                .email("ozamabinladen11@hotmail.com")
                .senha("ozama1234")
                .tipoUsuario(TipoUsuario.USER)
                .status(StatusUsuario.EMAIL_VALIDADO)
                .build();
        user.criptografarSenha();

        user = repository.save(user);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getNome()).isNotNull();
        assertThat(user.getNome()).isEqualTo("Ozama Binladen");
        assertThat(user.getEmail()).isEqualTo("ozamabinladen11@hotmail.com");
    }

    @Test
    public void findByEmail() {
        Usuario user = repository.findByEmail("leoguardah@gmail.com").get();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user).hasFieldOrPropertyWithValue("nome", "Elvis de Sousa");
    }

    @Test
    public void findAll() {
        var elvis = repository.findByEmail("leoguardah@gmail.com").get();
        var joao = repository.findByEmail("joao.bosco.seixas@gmail.com").get();
        var greg = repository.findByEmail("gregperes84@gmail.com").get();

        List<Usuario> users = repository.findAll();

        assertThat(users).isNotNull();

        MatcherAssert.assertThat(users, hasSize(3));
        MatcherAssert.assertThat(users, hasItems(elvis, joao, greg));
    }

    @Test
    public void deleteShouldRemoveData() {
        var id = repository.findByEmail("leoguardah@gmail.com")
                .get().getId();

        repository.deleteById(id);

        assertThat(repository.findById(id)).isEmpty();
    }

    @Test
    public void updateShouldChangeAndPersistData() {
        Usuario usuario = repository.findByEmail("leoguardah@gmail.com").get();

        usuario.setNome("ELVIS");
        usuario.setStatus(StatusUsuario.CONFIRMACAO_EMAIL_PENDENTE);

        usuario = repository.save(usuario);

        assertThat(usuario).hasFieldOrPropertyWithValue("nome", "ELVIS");
        assertThat(usuario).hasFieldOrPropertyWithValue("status", StatusUsuario.CONFIRMACAO_EMAIL_PENDENTE);
    }

    @Test
    public void createWhenNomeIsEmptyShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Por favor, verifique o seu nome");

        Usuario user = Usuario.builder()
                .email("elvisdesouza10@hotmail.com")
                .senha("eloah1106")
                .status(StatusUsuario.EMAIL_VALIDADO)
                .tipoUsuario(TipoUsuario.USER)
                .build();

        repository.save(user);
    }

    @Test
    public void createUserWithCPFShouldPersistData() {
        Usuario usuario = Usuario.builder()
                .nome("Ozama Bin Laden")
                .email("ozamabinladen123@hotmail.com")
                .senha("ozama123")
                .status(StatusUsuario.EMAIL_VALIDADO)
                .tipoUsuario(TipoUsuario.USER)
                .build();

        usuario = repository.save(usuario);

        assertThat(usuario.getId()).isNotNull();
        assertThat(usuario).hasFieldOrPropertyWithValue("nome", "Ozama Bin Laden");
        assertThat(usuario).hasFieldOrPropertyWithValue("email", "ozamabinladen123@hotmail.com");
    }

    @Test
    public void createWhenCPFIsTooLongShouldThrowDataIntegrityViolationException() {
        thrown.expect(DataIntegrityViolationException.class);

        Usuario usuario = Usuario.builder()
                .nome("Elvis de Sousa")
                .email("ozamabinladen123@hotmail.com")
                .senha("ozama123")
                .status(StatusUsuario.EMAIL_VALIDADO)
                .tipoUsuario(TipoUsuario.USER)
                .build();

        repository.save(usuario);
    }

    @Test
    public void createWhenNomeAndSenhaAndEmailAreEmptyShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);

        Usuario user = Usuario.builder()
                .status(StatusUsuario.EMAIL_VALIDADO)
                .tipoUsuario(TipoUsuario.USER)
                .build();

        repository.save(user);
    }

    @Test
    public void createWhenSenhaIsEmptyShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Por favor, verifique sua senha");

        Usuario user = Usuario.builder()
                .email("elvisdesouza10@hotmail.com")
                .nome("Elvis de Sousa")
                .senha("")
                .status(StatusUsuario.EMAIL_VALIDADO)
                .tipoUsuario(TipoUsuario.USER)
                .build();

        repository.save(user);
    }

    @Test
    public void createWhenEmailAlreadyExistsThrowDataIntegrityViolationException() {
        thrown.expect(DataIntegrityViolationException.class);

        Usuario user = Usuario.builder()
                .nome("Elvis de Sousa")
                .email("leoguardah@gmail.com")
                .senha("eloah1106")
                .status(StatusUsuario.EMAIL_VALIDADO)
                .tipoUsuario(TipoUsuario.USER)
                .build();

        repository.save(user);
    }

    @Test
    public void createWhenEmailIsEmptyShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Por favor, verifique seu Email");

        Usuario user = Usuario.builder()
                .nome("Elvis de Sousa")
                .email("")
                .senha("eloah1106")
                .status(StatusUsuario.EMAIL_VALIDADO)
                .tipoUsuario(TipoUsuario.USER)
                .build();

        repository.save(user);
    }

    @Test
    public void createWhenEmailIsNotValidShouldThrowConstraintViolationException() {
        thrown.expect(ConstraintViolationException.class);
        thrown.expectMessage("Digite um email válido!");

        Usuario usuario = Usuario.builder()
                .nome("Elvis de Sousa")
                .email("elvis")
                .senha("eloah1106")
                .status(StatusUsuario.EMAIL_VALIDADO)
                .tipoUsuario(TipoUsuario.USER)
                .build();

        repository.save(usuario);
    }
}