package br.com.ternarius.inventario.sagi.application.controller;

import br.com.ternarius.inventario.sagi.application.dto.RecuperaSenhaDto;
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
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class RecuperaSenhaControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    public void testRecuperaSenhaWhenEmailIsEmpty() throws Exception {
        mockMvc.perform(post("/esqueci-minha-senha")
                .param("email", "")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("login/recupera-senha"))
                .andExpect(model().attribute("erros", hasValue("O campo e-mail é obrigatório.")))
                .andExpect(model().attribute("dto", equalTo(RecuperaSenhaDto.builder()
                        .email("")
                        .build())))
                .andDo(print());
    }

    @Test
    public void testRecuperaSenhaWhenEmailIsInvalid() throws Exception {
        mockMvc.perform(post("/esqueci-minha-senha")
                .param("email", "ozama")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("login/recupera-senha"))
                .andExpect(model().attribute("erros", hasValue("Digite um E-mail válido.")))
                .andExpect(model().attribute("dto", equalTo(RecuperaSenhaDto.builder()
                        .email("ozama")
                        .build())))
                .andDo(print());
    }

    @Test
    public void testRecuperaSenhaWhenEmailDontExists() throws Exception {
        mockMvc.perform(post("/esqueci-minha-senha")
                .param("email", "ozama123@gmail.com")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("login/recupera-senha"))
                .andExpect(model().attribute("fail", is(true)))
                .andDo(print());
    }

    @Test
    public void testRecuperaSenhaChangeAndPersistData() throws Exception {
        mockMvc.perform(post("/esqueci-minha-senha")
                .param("email", "gguacom@gmail.com")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/esqueci-minha-senha"))
                .andExpect(flash().attribute("success", is(true)))
                .andDo(print());
    }
}
