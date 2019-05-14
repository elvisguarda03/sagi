//package br.com.ternarius.inventario.sagi;
//
//import br.com.ternarius.inventario.sagi.application.controller.rest.LaboratorioRestController;
//import br.com.ternarius.inventario.sagi.domain.entity.Laboratorio;
//import br.com.ternarius.inventario.sagi.domain.service.LaboratorioService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class LaboratorioRestControllerTest {
//
//    @Mock
//    private MockMvc mock;
//
//
//
//    @Test
//    public void TestLaboratorioChangeAndPersistData(){
//        var laboratorio = Laboratorio.builder()
//                .build();
//
//        LaboratorioService lab_serv = mock(LaboratorioService.class);
//
//        LaboratorioRestController api = mock(LaboratorioRestController.class);
//
//        mock.when(api.create(laboratorio)).thenReturn(ResponseEntity.badRequest().build());
//
//        verify(lab_serv.cadastrar(laboratorio), atMost(0));
//
//
//    }
//}