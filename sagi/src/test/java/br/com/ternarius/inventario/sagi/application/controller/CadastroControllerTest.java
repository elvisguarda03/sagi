package br.com.ternarius.inventario.sagi.application.controller;

import br.com.ternarius.inventario.sagi.application.dto.CadastroDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CadastroControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testCadastroChangeAndPersistData() throws Exception {
        mockMvc.perform(post("/cadastro")
                .param("nome", "Elvis de Sousa")
                .param("email", "ozamabinladen@gmail.com")
                .param("senha", "CALO1106")
                .param("confirmacaoSenha", "CALO1106")
                .param("termosCondicoes", String.valueOf(true))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cadastro"))
                .andExpect(flash().attribute("msgSucesso", containsString("Cadastro efetuado, verifique a sua caixa de e-mail para ativar a sua conta.")))
                .andDo(print());
    }

    @Test
    public void testCadastroWhenSenhaDontMatchConfimacaoSenha() throws Exception {
        mockMvc.perform(post("/cadastro")
                .param("nome", "Elvis de Sousa")
                .param("email", "ozamabinladen@gmail.com")
                .param("senha", "ozama123")
                .param("confirmacaoSenha", "ozama234")
                .param("termosCondicoes", String.valueOf(true))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro/index"))
                .andExpect(model().attribute("erros", hasValue("As senhas não coincidem.")))
                .andDo(print());
    }

    @Test
    public void testCadastroWhenAllDataAreEmpty() throws Exception {
        mockMvc.perform(post("/cadastro")
                .param("nome", "")
                .param("email", "")
                .param("senha", "")
                .param("confirmacaoSenha", "")
                .param("termosCondiicoes", "")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro/index"))
                .andExpect(model().attribute("erros", aMapWithSize(5)))
                .andDo(print());
    }

    @Test
    public void testCadastroWhenEmailIsInvalid() throws Exception {
        mockMvc.perform(post("/cadastro")
                .param("nome", "Elvis de Sousa")
                .param("email", "ozama")
                .param("senha", "ozama123")
                .param("confirmacaoSenha", "ozama123")
                .param("termosCondicoes", String.valueOf(true))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro/index"))
                .andExpect(model().attribute("erros", hasValue("Email inválido.")))
                .andDo(print());
    }

    @Test
    public void testCadastroWhenEmailAlreadyExists() throws Exception {
        mockMvc.perform(post("/cadastro")
                .param("nome", "Elvis de Sousa")
                .param("email", "gguacom@gmail.com")
                .param("senha", "ozama123")
                .param("confirmacaoSenha", "ozama123")
                .param("termosCondicoes", String.valueOf(true))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro/index"))
                .andExpect(model().attribute("msg", containsString("E-mail já cadastrado.")))
                .andDo(print());
    }

    @Test
    public void testCadastroWhenTermosCondicoesIsFalse() throws Exception {
        mockMvc.perform(post("/cadastro")
                .param("nome", "Elvis de Sousa")
                .param("email", "ozama123@gmail.com")
                .param("senha", "ozama123")
                .param("confirmacaoSenha", "ozama123")
                .param("termosCondicoes", String.valueOf(false))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro/index"))
                .andExpect(model().attribute("erros", hasValue("Aceite os termos e condições.")))
                .andDo(print());
    }

    @Test
    public void testCadastroWhenSenhaIsTooLong() throws Exception {
        mockMvc.perform(post("/cadastro")
                .param("nome", "Elvis de Sousa")
                .param("email", "ozamabinladen@gmail.com")
                .param("senha", "ozama12345678987654321")
                .param("confirmacaoSenha", "ozama12345678987654321")
                .param("termosCondicoes", String.valueOf(true))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro/index"))
                .andExpect(model().attribute("erros", hasValue("Deve conter entre 8 a 15 caracteres.")))
                .andExpect(model().attribute("dto", equalTo(CadastroDto.builder()
                        .nome("Elvis de Sousa")
                        .email("ozamabinladen@gmail.com")
                        .senha("ozama12345678987654321")
                        .confirmacaoSenha("ozama12345678987654321")
                        .build())))
                .andDo(print());
    }

    @Test
    public void testCadastroWhenSenhaIsTooSmall() throws Exception {
        mockMvc.perform(post("/cadastro")
                .param("nome", "Ozama Binladen")
                .param("email", "ozama123@gmail.com")
                .param("senha", "ozama")
                .param("confirmacaoSenha", "ozama")
                .param("termosCondicoes", String.valueOf(true))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro/index"))
                .andExpect(model().attribute("erros", hasValue("Deve conter entre 8 a 15 caracteres.")))
                .andDo(print());
    }
}